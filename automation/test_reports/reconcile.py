"""Offline JUnit XML reconciliation; never establishes execution provenance.

Only use an inventory from the same source commit as the artifact-producing run.
Import API: reconcile(xml_bytes_list, inventory). Report parsing is not a test run.
"""
import xml.etree.ElementTree as ET
import re


class ReportError(ValueError):
    pass


def reconcile(documents, inventory):
    expected = {(x['class'], x['method']) for x in inventory['declarations']}
    if not documents:
        raise ReportError('No XML documents supplied')
    if len(expected) != inventory['declared_method_count']:
        raise ReportError('Inventory count or uniqueness mismatch')
    seen, outcomes = set(), []
    for document in documents:
        if not isinstance(document, bytes) or len(document) > 10 * 1024 * 1024:
            raise ReportError('XML input must be bytes, at most10MiB per document')
        try:
            text = document.decode('utf-8-sig')
        except UnicodeDecodeError as e:
            raise ReportError('Only UTF-8 XML is supported') from e
        if '\x00' in text:
            raise ReportError('NUL/UTF-16 XML is unsupported')
        declared_encoding = re.search(r'<\?xml[^>]*encoding=[\"\']([^\"\']+)', text, re.I)
        if declared_encoding and declared_encoding.group(1).lower() not in ('utf-8', 'utf8'):
            raise ReportError('Only UTF-8 XML is supported')
        if '<!DOCTYPE' in text.upper() or '<!ENTITY' in text.upper():
            raise ReportError('DTD/entity declarations are not accepted')
        try:
            root = ET.fromstring(text)
        except ET.ParseError as e:
            raise ReportError('Malformed XML') from e
        if root.tag not in ('testsuite', 'testsuites'):
            raise ReportError('Unsupported XML root')
        suites = [root] if root.tag == 'testsuite' else list(root)
        if not suites or any(s.tag != 'testsuite' for s in suites):
            raise ReportError('Only flat Gradle/JUnit-style suites are supported')
        aggregate = dict(tests=0, failures=0, errors=0, skipped=0)
        for suite in suites:
            cases = suite.findall('testcase')
            if suite.findall('testsuite'):
                raise ReportError('Nested suites require a separately reviewed parser')
            counts = dict(tests=len(cases), failures=0, errors=0, skipped=0)
            for case in cases:
                if set(case.attrib) - {'classname', 'name', 'time'}:
                    raise ReportError('Unsupported testcase attributes require format review')
                if any(x.tag not in ('failure', 'error', 'skipped', 'system-out', 'system-err') for x in case):
                    raise ReportError('Unsupported testcase result element')
                key = (case.get('classname'), case.get('name'))
                if not all(key):
                    raise ReportError('Testcase classname and name are required')
                if key in seen:
                    raise ReportError('Duplicate reported test identity')
                seen.add(key)
                tags = [x.tag for x in case if x.tag in ('failure', 'error', 'skipped')]
                if len(tags) > 1:
                    raise ReportError('Multiple result states on one testcase')
                status = tags[0] if tags else 'passed'
                if status != 'passed':
                    counts[dict(failure='failures', error='errors', skipped='skipped')[status]] += 1
                outcomes.append(dict(classname=key[0], name=key[1], status=status))
            for attr, count in counts.items():
                if attr in suite.attrib:
                    text = suite.attrib[attr]
                    if not text.isascii() or not text.isdecimal() or int(text) != count:
                        raise ReportError('Suite ' + attr + ' count disagrees with testcase results')
            # A suite-level failure with no failed testcase cannot masquerade as success.
            if suite.find('failure') is not None or suite.find('error') is not None:
                raise ReportError('Suite-level failure requires explicit handling')
            for attr, count in counts.items():
                aggregate[attr] += count
        if root.tag == 'testsuites':
            for attr, count in aggregate.items():
                if attr in root.attrib and (not root.attrib[attr].isascii()
                                           or not root.attrib[attr].isdecimal()
                                           or int(root.attrib[attr]) != count):
                    raise ReportError('Aggregate count mismatch')
    missing = sorted(expected - seen)
    unexpected = sorted(seen - expected)
    failed = [x for x in outcomes if x['status'] in ('failure', 'error')]
    skipped = [x for x in outcomes if x['status'] == 'skipped']
    status = 'COMPLETE_MATCH'
    if missing or unexpected:
        status = 'INVENTORY_MISMATCH'
    if skipped:
        status = 'SKIPPED_CASES'
    if failed:
        status = 'FAILED_CASES'
    return dict(status=status, declared=len(expected), reported=len(outcomes),
                passed=sum(x['status'] == 'passed' for x in outcomes),
                failures=sum(x['status'] == 'failure' for x in outcomes),
                errors=sum(x['status'] == 'error' for x in outcomes),
                skipped=len(skipped), missing=missing,
                unexpected=unexpected, execution_provenance='NOT_ESTABLISHED_BY_XML_PARSE',
                cases=sorted(outcomes, key=lambda x: (x['classname'], x['name'])))
