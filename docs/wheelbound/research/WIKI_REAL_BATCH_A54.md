# A54 — first bounded real Wiki collector review

Starting head `04709b3e043f8b1bd29dbe07a53e0cf33992dde4`; verified guarded ownership `c69fd835532ac3f0ca6de5508293ea65b2f95316`. Captured 2026-10-14 UTC (2026-10-13 America/Chicago). [Machine-readable review receipt](WIKI_REAL_BATCH_A54.json).

## Actual result

The existing standalone collector successfully fetched robots.txt and three canonical HTTPS creature pages using its declared WheelboundDropCatalog identity, two-second minimum request spacing and existing access checks. Exit code0; manifest exhausted with no request errors or pending tasks. SQLite contains three creature records and201 drop rows, matching JSON/CSV. All records remain unreviewed/runtime-disabled. This is one finite URL manifest, not all-monster discovery.

| Captured creature | HTML revision reported | Parsed rows | Classification |
| --- | --- | --- | --- |
| [Tormented Demon](https://oldschool.runescape.wiki/w/Tormented_Demon) |15337983 |47 | monster |
| [Vyrewatch Sentinel](https://oldschool.runescape.wiki/w/Vyrewatch_Sentinel) |15367082 |93 | monster |
| [Kurask](https://oldschool.runescape.wiki/w/Kurask) |15338306 |61 | monster |

Revision identifiers were read from each already-captured canonical HTML response; no robots-disallowed oldid requests were made. Hashes, fetch timestamps, response Last-Modified and requested/resolved URLs are in the receipt. Last-Modified can reflect generated/cache output and is not an independently verified edit timestamp. Full HTML, database and full-page prose are generated transient output, not archived in Git; the committed selected extracts support this bounded review but cannot reconstruct every original byte.

## Candidate-source improvement

| Selected item/source | Captured reported fraction | Existing rarity screen |
| --- | --- | --- |
| Tormented Demon / Tormented synapse |1/500 | In band |
| Tormented Demon / one Burning claw |499/250000 | In band; preserve exact numerator |
| Vyrewatch Sentinel combat / Blood shard |1/1500 | Inclusive lower probability endpoint |
| Standard Kurask / Leaf-bladed battleaxe |1/1026 | In band |

These four specific source rows agree with B1's provisional reports. This replaces the indexed-only freshness limitation for these three captured pages at this timestamp; it does not validate the other B1 sources or future page state. Blood shard footnote1 distinguishes the reported combat rate from pickpocketing; the latter remains a different method. The Kurask battleaxe rate has a linked source footnote. The two Tormented Demon items remain two possible hunts on one creature, not two creature-diversity entries.

All selected rows were manually checked against the saved HTML. Their normal item/quantity/rarity columns parsed with no layout warnings. No whole-page zero-drop/empty-pool inference is used. Creature categories/monster infobox evidence support the three monster labels. **Boss classification remains untested on real data.** Reported NPC IDs cover only extracted displayed infobox values; switch variants and canonical item IDs are not a complete reviewed mapping. Non-target rows, shared tables, estimates and conditional footnotes remain unreviewed;201 parsed rows is not201 approved hunts.

## Remaining admission gates

No final starter membership/count is adopted. These are promising repeatable-source candidates under WB-D140, subject to complete access/context review, actual account predicates, personal receiving-probability interpretation, canonical source/item identities, source-correlated active personal drop receipts and frozen reviewed reward quotes. Source prose supports off-task Sentinel combat; this does not authorize unrelated task acquisition or waive quest/equipment requirements. No new stats, task permissions, prices or FP rewards are invented.

## Access finding and limits

The fetched rules allowed the collector's matching identity on these three canonical page paths. They also explicitly disallow several named AI/search crawlers and query/API/Special routes. Successful standalone collection is not a blanket permission for a blocked search/browser identity or alternate endpoint. No identity was changed and no access failure was bypassed. WB-B09's earlier browser/search observations remain historical facts; current limitation is route/source-specific rather than universal inability to collect any Wiki HTML.

## Verification and next bounded task

Verified three nonfixture network captures and their hashes, reported HTML revision IDs, four selected exact rational fractions, three monster classifications,201 rows in JSON/CSV/SQLite, zero errors/pending/unsupported-layout warnings in this manifest, and all runtime flags false. Existing17 synthetic collector tests plus documentation/hash/scope checks are verification of tooling/contracts, not live RuneLite tests. Historical source/model/catalog files stay untouched.

Next A55: one finite real boss-page batch to check boss classification and candidate inclusion/exclusion evidence. Save/verify A54 remotely before starting it. Do not launch an unlimited crawl, rewrite original B1 receipts, or claim all-monster/current-client coverage.
