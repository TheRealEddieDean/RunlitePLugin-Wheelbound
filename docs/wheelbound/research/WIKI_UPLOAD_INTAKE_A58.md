# A58 — uploaded collector intake and quality review

Research intake only, 2026-10-14. Authoritative input: user attachment `output.zip`, Library ID `libfile_12514dfedef0819184279d6d84d380ff`; original remains unchanged. Archive SHA-256 `e00babd58e09c047f68df94625cd5b4e77e119952d52c0d0c50b53a7d2b60309`, 91,341,367 bytes. Starting remote `85c4c7f51b8a04e61f3ba30d98308cbedba76da5`, guarded ownership `8a122352004f7ee20895719bd439f85c83e99e64`. Main baseline `c8cc38ac9a7ab74d9bc10125388da7cc75af99fa`. No active lease preceded acquisition. No Wiki requests, gameplay code, roster admission or design approvals.

Machine-readable evidence, input hashes, duplicate groups, classification flags and eight cached-source checks: [WIKI_UPLOAD_INTAKE_A58.json](WIKI_UPLOAD_INTAKE_A58.json). Reproduce from the repository root:

```bash
python tools/wiki_drops/audit_intake.py /path/to/output /path/to/output.zip /tmp/A58.json
```

The supplied archive already persists as the user attachment; Git contains the compact report, not a bulk Wiki/cache/database dump. The 9,062 files total 613,150,582 uncompressed bytes. Top-level hashes and a deterministic whole-file-inventory digest identify the dataset. Full raw originals are required for later source-context review; the small report does not replace them.

## Scope and counts

| Measure | Observed |
| --- | ---: |
| Unique source URLs / parsed page JSON records | 2,929 |
| Labeled boss / monster pages | 203 / 1,754 |
| Unclassified pages retained separately | 972 |
| Rows in labeled JSON / CSV / SQLite | 40,534 each |
| Rows on unclassified pages | 3,913 |
| All retained rows | 44,447 |
| Unsupported rows, labeled / all | 599 / 742 |
| No recognized tables, labeled / all | 500 / 1,152 |
| Labeled pages lacking reported NPC IDs | 373 |
| Network cache receipt/HTML pairs | 3,063 |

These are parser labels/page records, **not** counts of distinct valid gameplay creatures. Coverage is explicitly PARTIAL, all_monsters_verified=false, pending=[]; queue exhaustion does not prove completeness. Crawl state has 4,118 done task keys: 3,938 page keys plus 180 category keys, with 1,005 page URLs visited under both ancestry kinds. It reports 3,929 processed tasks. Twelve errors affect eleven unique URLs: three unsupported category layouts, seven connection resets, and two 404 attempts for the same Big_rat_(brown) URL. Nine page errors explain 3,938 minus 3,929 processed page tasks; duplicate category ancestry explains why this is not 3,929 distinct source pages. No failure means a verified empty activity. Three category failures are Unknown_talisman, Safespot_images and Slayer_monster_images. Network failures include Frog, Ennius_Tullus, Green_Guard and four Araxxor achievement pages. These are specific gaps, not authorization to restart the crawl.

## Provenance and timestamps

All 2,929 records declare network_html and fixture=false; none are marked offline or synthetic, and every record and drop is runtime-disabled. Supplied network receipts, substantial Wiki HTML, revision fields, source hash agreement, crawl state and matching exports corroborate a real user-collected dataset. This is provenance review of supplied captures, not independent authentication of the user's HTTP session. Exact executed collector source/version is unproven: no script/commit pin was bundled; schema and user agent match committed 0.1. The audit pins the current parser SHA separately. Existing A54/A55 six-page receipts remain independent historical evidence, not counted again as new collection.

Fetched timestamps span 2026-10-14 15:02:46–16:47:51 UTC (10:02:46–11:47:51 America/Chicago). Catalog export is 16:48:14 UTC. ZIP directory timestamps are not source capture times. Each page has a reported Last-Modified header; that is not itself a Wiki revision proof. Eight sampled HTML pages have reported wgRevisionId values retained in JSON. No new current-source verification occurred.

Every cached HTML **byte** hash differs from the collector's source hash because Windows text output expands line endings. Reading with universal newline normalization and re-encoding UTF-8 matches all 2,929 source hashes. Preserve the original bytes; the report stores byte hashes for sampled HTML and top-level files plus the inventory digest. Do not call these byte-identical originals of the HTTP response or silently rewrite their hashes. Sidecar receipt keys match the corresponding capture fields; origin/fixture are additional record metadata rather than sidecar discrepancies.

## Consistency and parser checks

All per-page JSON payloads match catalog records. All labeled creature payloads and all 40,534 drop payloads match SQLite; SQLite integrity_check=ok. CSV matches its expected projection row-for-row, including annotations; it is intentionally less expressive than JSON. Standalone, embedded and database coverage agree. All corresponding normalized HTML hashes and sidecar receipt fields agree. The audit reports zero discrepancies for these checks.

Cached HTML was independently reparsed for Kurask, King kurask, Abyssal demon, Basilisk Knight, Zulrah, Vorkath, Summoned Soul and Barrows. Across all eight, names, drop arrays, footnotes, categories, infobox fields, NPC IDs, prose and source hashes match. This establishes reproducibility under the current parser for those pages, not correctness of every contextual probability. Summoned Soul becomes monster without its ancestry hint, demonstrating the boss-label limitation. Barrows remains unknown with zero recognized rows despite captured encounter prose: unsupported markup/classification is not an empty reward pool.

## Classification and duplicate hazards

- **Confirmed broad-category false positives:** Oathplate helm is labeled monster solely via Items dropped by monster; Fishy salvage is labeled through that category and Monsters that drop clues; Steel Chest (crimson) through Monsters that drop clues. The parser's broad monster regex accepts relationship categories. These cannot become creature keys. Quarantine weak labels pending page-specific evidence; lacking NPC IDs alone is a review flag, not a universal exclusion.
- Forty boss labels lack a direct boss category and use ancestry plus infobox evidence. Examples: Summoned Soul, Snakeling, encounter totems, parasites and skeletons. They may be adds or components; no automatic independent boss assignment or diversity count.
- Category discovery includes items, pets, achievement pages, containers, minigame encounters, quest-only NPCs, alternate modes and historical/alpha variants. Review actual repeatable source/mode before canonicalizing. Combat level is not a boss rule.
- No repeated source URLs, page titles or normalized HTML hashes across final records. This does not prove logical uniqueness. Thirteen NPC IDs overlap across different URLs: twelve Nightmare/Phosani totem forms and Arianwyn/Nightmare Zone. Preserve context; do not blindly collapse by ID or count forms as separate eligible targets.
- Five identical raw-row groups (two copies each) occur in crimson/red/brown/black/purple Steel Chest Black spear rows. Keep original row positions until HTML review explains duplication. Repeated item names under different headings/methods are contexts, not automatic duplicates.

## Rate preservation and review lanes

| Labeled-row rate kind | Count |
| --- | ---: |
| Fraction | 17,774 |
| Always | 1,724 |
| Unknown | 10,933 |
| Alternatives | 6,406 |
| Multiple rolls | 2,002 |
| Approximate | 1,695 |

Raw expressions, quantities, row cells, sections, annotations, footnotes and page prose remain in original JSON/SQLite/HTML. Unknown includes decimal denominators such as 1/34.1, unsupported layouts, verbal rates and formulas. The parser sometimes marks unknown needs_context_review=false; **unknown must still be held**. A decimal reported rate is not automatically an exact personal probability. Preserve semicolon alternatives and roll counts independently; never sum rolls or select the convenient alternative. King-kurask A51 warning remains controlling. Row annotations alone are insufficient: paragraph/heading qualifiers and source-linked footnotes must follow the row into a curated context.

The audit finds 1,521 syntactically exact fraction rows inside inclusive [1/1500,1/400]. This is a search lead count, not 1,521 Challenge items or approved personal probabilities; shared tables, containers, variants and conditional quantities remain mixed in it. No normal raw drop is discarded for being outside the Challenge band.

Usable now: intact raw evidence, reproducible parser output, bounded manual source/context extraction. Needs review: taxonomy/canonical encounter identity, weak labels, 742 layouts, unknown/alternative/approximate/multi-roll interpretation, unclassified reward pages, repeatability/access, exact logical item mapping, personal allocation/receipt and reward quotes. No record is runtime-ready. Approved WB-D125/129–141 remain unchanged; A56 retirement proposal remains unanswered and NOT_ADOPTED.

## Validation and exact next batch

Seventeen existing offline collector tests pass (synthetic/mocked; not live evidence). Audit checks above ran on the uploaded archive; eight cached-source reparses passed their listed equality checks. No production build or gameplay acceptance test ran.

Publish and verify this A58 report/checkpoint before A59. Next bounded batch: inspect three previously unreviewed supplied sources, Abyssal demon, Kraken and Zulrah, for specific unique-drop context leads. Preserve row positions, reported revision/hash, exact raw rate alternatives and adjacent prose; screen generic drops and ambiguity; keep all candidates disabled. Do not repeat A54/A55/Kurask research or recrawl. Review task/boost/encounter context and logical creature identity before giving any item an eligible status. Separate further batches for weak taxonomy and unsupported reward tables.
