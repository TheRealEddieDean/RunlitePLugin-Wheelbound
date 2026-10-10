"""Synthetic XML tests only; no declared Java test is executed here."""
import copy
import hashlib
import json
import sys
import xml.etree.ElementTree as ET
from pathlib import Path
from reconcile import reconcile, ReportError

HERE = Path(__file__).resolve().parent


def xml(rows, status=None):
    suite = ET.Element('testsuite', name='SYNTHETIC_REPORT', tests=str(len(rows)),
                       failures='1' if status == 'failure' else '0',
                       errors='1' if status == 'error' else '0',
                       skipped='1' if status == 'skipped' else '0')
    for i, row in enumerate(rows):
        case = ET.SubElement(suite, 'testcase', classname=row['class'], name=row['method'])
        if i == 0 and status:
            ET.SubElement(case, status)
    return ET.tostring(suite, encoding='utf-8')


def main():
    inventory = json.loads((HERE / 'DECLARATIONS.json').read_text())
    rows = inventory['declarations']
    checks = []
    scenarios = [('complete', rows, None, 'COMPLETE_MATCH'),
                 ('missing', rows[:-1], None, 'INVENTORY_MISMATCH'),
                 ('extra', rows + [{'class': 'fixture.Extra', 'method': 'extra'}], None, 'INVENTORY_MISMATCH'),
                 ('failure', rows, 'failure', 'FAILED_CASES'),
                 ('error', rows, 'error', 'FAILED_CASES'),
                 ('skip', rows, 'skipped', 'SKIPPED_CASES')]
    for name, subset, status, expected in scenarios:
        report = reconcile([xml(subset, status)], inventory)
        assert report['status'] == expected, name
        if name == 'complete':
            assert report['reported'] == report['passed'] == 70
        if name == 'skip':
            assert report['reported'] == 70 and report['passed'] == 69 and report['skipped'] == 1
        if name == 'error':
            assert report['errors'] == 1 and report['failures'] == 0
        if name == 'missing':
            assert report['reported'] == 69 and len(report['missing']) == 1 and not report['unexpected']
        if name == 'extra':
            assert report['reported'] == 71 and len(report['unexpected']) == 1 and not report['missing']
        if name == 'failure':
            assert report['failures'] == 1 and report['errors'] == 0 and report['passed'] == 69
        checks.append(dict(id=name, result=expected))
    # Multiple exported suite files need exactly one identity each.
    split = reconcile([xml(rows[:20]), xml(rows[20:])], inventory)
    assert split['status'] == 'COMPLETE_MATCH'
    checks.append(dict(id='split_files', result='COMPLETE_MATCH'))
    wrapper = ET.Element('testsuites', tests='70')
    wrapper.append(ET.fromstring(xml(rows)))
    assert reconcile([ET.tostring(wrapper)], inventory)['status'] == 'COMPLETE_MATCH'
    checks.append(dict(id='aggregate_wrapper', result='COMPLETE_MATCH'))
    bad = [('duplicate_identity', [xml(rows + [rows[0]])]),
           ('duplicated_report_file', [xml(rows), xml(rows)]),
           ('wrong_suite_count', [xml(rows).replace(b'tests="70"', b'tests="71"')]),
           ('wrong_aggregate_count', [ET.tostring(wrapper).replace(b'tests="70"', b'tests="71"', 1)]),
           ('malformed_xml', [b'<testsuite>']),
           ('unsupported_root', [b'<report/>']),
           ('missing_classname', [b'<testsuite><testcase name="x"/></testsuite>']),
           ('dtd_entity', [b'<!DOCTYPE testsuite [<!ENTITY x "a">]><testsuite/>']),
           ('utf16_dtd', ['<!DOCTYPE testsuite><testsuite/>'.encode('utf-16')]),
           ('multiple_outcomes', [b'<testsuite><testcase classname="x" name="y"><error/><skipped/></testcase></testsuite>']),
           ('suite_level_failure', [b'<testsuite><failure/></testsuite>']),
           ('alternative_status_attribute', [b'<testsuite><testcase classname="x" name="y" status="failed"/></testsuite>']),
           ('alternative_result_element', [b'<testsuite><testcase classname="x" name="y"><flakyFailure/></testcase></testsuite>']),
           ('oversize_document', [b' ' * (10 * 1024 * 1024 + 1)]),
           ('no_reports', [])]
    for name, docs in bad:
        try:
            reconcile(docs, inventory)
        except ReportError:
            checks.append(dict(id=name, result='REJECTED'))
        else:
            raise AssertionError('Invalid report accepted: ' + name)
    hashes = {p.name: hashlib.sha256(p.read_bytes()).hexdigest()
              for p in [HERE / 'DECLARATIONS.json', HERE / 'reconcile.py', HERE / 'checks.py']}
    output = dict(status='PASS', input_origin='SYNTHETIC_XML_ONLY', source_commit=inventory['source_commit'],
                  declared_methods=70, actual_java_tests_executed=0, actual_exported_reports_available=False,
                  check_count=len(checks), checks=checks, input_sha256=hashes)
    text = json.dumps(output, indent=2) + '\n'
    if '--verify' in sys.argv:
        assert (HERE / 'RESULTS.json').read_text() == text
    else:
        (HERE / 'RESULTS.json').write_text(text)
    print('PASS:', len(checks), 'synthetic XML checks; zero Java execution or real exported reports')


if __name__ == '__main__':
    main()
