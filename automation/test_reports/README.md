# Future JUnit report reconciliation supplement

WB-A26, offline tooling only. `DECLARATIONS.json` names the70 existing plain JUnit4 test methods in12 test-source files at e6d445c8c27847050abb07f843cfe9c9d4ab476b. Every source file's Git blob matched the previously verified build manifest. The manual RuneLite launcher has zero methods. This archive describes declarations, not executed tests or final Wheelbound-mode coverage.

`reconcile.py` parses supplied UTF-8 Gradle/JUnit XML and compares class/method identities to that inventory. It distinguishes reported pass/failure/error/skipped cases, missing declarations, extra cases and duplicates. Suite and aggregate counters must agree with individual results when present. Duplicate case identities/files, missing identifiers, malformed XML, multiple result states, suite-level failures and DTD/entity declarations are rejected. Nested suites, parameterized name conventions and other report formats require an explicitly reviewed extension; do not silently normalize names to make missing tests disappear. Only UTF-8 input up to10MiB per file is accepted.

Run the synthetic checks:

```bash
python3 automation/test_reports/checks.py
python3 automation/test_reports/checks.py --verify
```

Saved results:23 synthetic report checks (eight valid/coverage/result scenarios and15 rejection cases). Alternative testcase status attributes/result tags and oversized documents are rejected rather than silently interpreted as passes. No Java test ran, no real XML artifact became available, and the active workflow was not changed. Synthetic “70 passed” is deliberately an input fixture; it is not new CI evidence. Test XML text does not prove source/run provenance even if its identities match.

## Future export workflow

1. In an authorized pipeline session export `build/test-results/test/TEST-*.xml` from the exact successful source commit/run, together with resolved dependency and JAR metadata. Use the existing unactivated CI proposal for review; this supplement does not activate it.
2. Verify the actual producing run/job/commit and checksums. The archived declaration inventory is appropriate only when these test sources match that source commit. After intentional new tests, create a separately versioned inventory; preserve this baseline.
3. Parse all exported suite files together. Missing/extra/failed/skipped cases must be explained rather than replaced with the source declaration count. Failures and errors remain separate counters; a skipped testcase was reported but did not pass.
4. Record the supplied artifact origin separately from parsing results. The parser always reports `NOT_ESTABLISHED_BY_XML_PARSE` for execution provenance. Do not use filenames, timestamps or a fabricated suite title as authentication.

Example once actual files exist, run from repository root:

```python
import json
from pathlib import Path
from automation.test_reports.reconcile import reconcile

inventory = json.loads(Path('automation/test_reports/DECLARATIONS.json').read_text())
documents = [p.read_bytes() for p in sorted(Path('build/test-results/test').glob('TEST-*.xml'))]
parsed = reconcile(documents, inventory)  # Empty export is an error, never success.
```

This supplements, rather than closes, the existing build-report blocker. Exact RuneLite/dependency versions, actual test counts, package inventory, legitimate live traces and specific policy review remain independent gates. These Python tools stay outside the plugin runtime/JAR. The gameplay PDF source remains284799b1; this later automation-only supplement changes no gameplay Markdown or approved decisions.
