# Wiki drop-table research collector

Python3.10+; standard library only, no package installation. Research tooling explicitly requested by the user; does not run inside RuneLite or change gameplay. This collects all drop rows it can recognize, not only Wheelbound's1/400–1/1500 candidates.

## What it writes

The output folder contains:

- `catalog.json`: boss/monster records, classification evidence, source hash/timestamps, reported NPC IDs, categories/infobox/access prose, tables and footnotes. Unknown classifications are retained separately.
- `drops.sqlite`: `creatures`, `drops`, and `metadata` tables. The full record JSON is retained alongside queryable columns.
- `drops.csv`: a simple creature/kind/item/section/quantity/rate review table.
- `html/`: original imported/downloaded HTML and network capture receipts when applicable.
- `pages/`: independently checkpointed parsed page records.
- `coverage.json`: collected/unclassified/unsupported counts, pending URLs, errors and scope.
- `crawl_state.json`: resumable category/page queue, completed work and failed tasks.
- `robots_receipt.json`: current fetched access rules/user agent/hash for network mode.

Every record is UNREVIEWED and runtime-disabled. Item IDs, personal group allocation, exact condition mapping, actual access and Wheelbound reward review remain separate work. Missing tables or HTTP/parser errors never prove a game activity is empty. JSON/SQLite outputs are references, not an automatically approved plugin catalog.

## Network collection — run on your computer

From the repository root:

```bash
python tools/wiki_drops/scrape.py --discover-all
```

On Windows use `py -3` in place of `python` if that is your installed launcher. The default invocation attempts up to250 page/category tasks. Run the same command again to resume the pending queue. For an uncapped category traversal:

```bash
python tools/wiki_drops/scrape.py --discover-all --max-pages 0
```

This follows the Monsters and Bosses category trees and allowed pagination. It does **not** promise complete coverage of every monster: categories may be incomplete, pages can have unsupported layouts, and the Wiki's general robots rules can forbid pagination query URLs. The coverage report always keeps `all_monsters_verified` false. A blocked or incomplete run exits with code2 and keeps the successfully gathered reference data. An exhausted category queue is labeled discovery-exhausted/unverified, not all-monsters-complete. Unknown pages and unsupported rows remain visible for review.

The collector identifies itself truthfully as WheelboundDropCatalog, honors matching robots rules and redirects, uses at least two seconds between uncached requests, checks crawl-delay/request-rate where present, and stops the run on HTTP403/429 or an access challenge. It has no browser/RuneLite impersonation, CAPTCHA bypass, special API bypass, parallel flood or ignoring-robots flag. This script's existence does not establish this AI agent's permission to bypass a blocked route. The [A54 review](../../docs/wheelbound/research/WIKI_REAL_BATCH_A54.md) records a later successful three-page live manifest. Full category/all-monster traversal remains NOT_RUN; named-client restrictions remain distinct.

### Smaller explicit batch

Edit/copy `example_urls.txt`, then run:

```bash
python tools/wiki_drops/scrape.py --urls tools/wiki_drops/example_urls.txt --output tools/wiki_drops/output-batch
```

Input URLs and redirects must remain on HTTPS oldschool.runescape.wiki. No `/api.php` or `Special:Lookup` alternative is used to evade a restriction. If an input file changes, use a new output directory so datasets cannot silently mix scopes.

### Resume, retry, freshness

Repeated commands resume saved pending work. Cached captures retain their original fetch time; they are not silently advertised fresh. Explicit failed-task retry, still under current robots rules:

```bash
python tools/wiki_drops/scrape.py --discover-all --retry-errors
```

To rewalk the same scope and recapture allowed URLs:

```bash
python tools/wiki_drops/scrape.py --discover-all --refresh
```

Refresh is subject to the same request budget. Continue pending refresh work with the ordinary command; avoid repeatedly passing refresh, which deliberately restarts root discovery. Old successful captures remain stored until replacement; inspect each record's timestamp, not just the latest export date. Deleted/renamed/disappeared sources require review before final catalog publication. Raw HTML/local DB/cache are ignored by Git. They are not packaged with the plugin. Respect the Wiki's content license/attribution when publishing collected data; no bulk Wiki text is committed by this tool.

## Offline import — no network

Create a manifest beside saved HTML files:

```json
[
  {
    "file": "Kurask.html",
    "url": "https://oldschool.runescape.wiki/w/Kurask",
    "fetched_at": "2026-10-14T03:00:00Z"
  }
]
```

```bash
python tools/wiki_drops/scrape.py --offline-manifest path/to/manifest.json --output tools/wiki_drops/output-import
```

The supplied timestamp is source provenance, not independently verified by the importer. Offline imports are explicitly marked as such. Optional `kind` is a category-ancestry hint, not a forced classification of an item/guide page; optional `fixture:true` marks synthetic test data. Keep real and fixture output folders separate. The original HTML is copied into the output cache.

For a reproducible synthetic example (these are invented monsters/items):

```bash
python tools/wiki_drops/scrape.py --offline-manifest tools/wiki_drops/tests/fixtures/manifest.json --output tools/wiki_drops/output-demo
```

## Rates and classifications

Boss labels require explicit boss category evidence or a boss category ancestry hint with recognizable monster infobox evidence. Ordinary monsters use monster category/infobox evidence. Combat level alone is not a boss cutoff. Pages without enough evidence are `unknown`; category descendants can include pets or unrelated pages, so these are not silently counted as bosses. Exact live Wiki layout/taxonomy still needs validation.

Rate records preserve the source expression:

| Source expression | Representation |
| --- | --- |
| `499/250,000` | Exact rational fraction; raw string retained |
| `1/5,000; 1/1,000` | Both alternatives; no automatically selected universal rate |
| `~1/500` | Approximate reported value; no exact probability asserted |
| `3 × 1/1,026` | Roll count and per-roll fraction; no assumed additive/independent total |
| `Rare`, complex formulas, unrecognized layouts | Unknown or unsupported, raw data retained |

Table headings distinguish combat drops, pickpocketing and variants; page prose and footnotes are retained for contextual review. Rowspan/complex unsupported column layouts are recorded with warnings rather than inventing item/rate mappings. Multi-roll/conditional data and Wiki approximations must be reviewed before applying the Challenge probability band. Boss reward-chest/raid layouts are not guaranteed to use the recognized item-drops markup. No raw reference record is gameplay eligibility.

## Offline verification

```bash
python -m unittest discover -s tools/wiki_drops/tests -v
```

Tests use synthetic local HTML/mocked fetching. They cover rate semantics, colspan layouts, boss/monster distinction, footnotes/methods, unknown data, robots denial, redirect host restrictions, resume, access-stop behavior, scope isolation and SQLite/JSON exports. No real source freshness, live coverage, client behavior or production build is established by these tests.

## Current four-rank Challenge classification (A63)

`challenge_tier_rules_v2.py INPUT_OUTPUT_DIRECTORY` builds source-preserving tier rules/migration tables; `tests/test_challenge_tiers.py` validates edges and ignored roll counts. See docs/wheelbound/CHALLENGE_CARD_TIERS.md. The old `curated_hunts_v1.py`, `finalize_challenge_catalog.py` and `validate_challenge_catalog.py` reproduce the historical old-band v1 packet; they are not current four-tier eligibility code and must not overwrite the current supersession banner as a routine update. V2 is rule/migration documentation, not an expanded ready roster or gameplay adapter. No generic raw data admission.

## All-tier review table (A65)

`python tools/wiki_drops/build_hunt_table_by_tier.py PATH_TO_EXTRACTED_OUTPUT` renders CHALLENGE_HUNT_TABLE and a JSON/CSV review index from the unchanged44-hunt v1 evidence, current tier rules and an explicit named-signature item candidate scope. `python tools/wiki_drops/validate_hunt_review_index.py PATH_TO_EXTRACTED_OUTPUT` validates source/SQLite payloads, counts, bracket placement and status preservation. Renderer never approves new candidates/rewards/unlock topology; v2 quotes remain TBD. Historical human table is CHALLENGE_HUNT_TABLE_V1_ARCHIVE.md. Do not use this review index as an enabled roster.
