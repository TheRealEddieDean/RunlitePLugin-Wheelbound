# Requirement traceability

| Requirement | Decisions | Ticket | Acceptance evidence |
| --- | --- | --- | --- |
| Optional mode, existing wheels preserved | WB-D065/076 | I00/I01/I09 | Existing build tests + normal-wheel regression |
| Five ordinary, three activities, 24 total | WB-D005/006 | I02 | Offline diversity/capacity checks; future edit/crash fixtures |
| Enhancement tier prices, lifetime activity duplicates | WB-D009/010 | I02/I04 | Ledger/price sequence and destroy/repurchase tests |
| Bossing cards then random boss | WB-D049 | I03 | Sequence test, no choices/rerolls, card scaling |
| Two distinct Standard targets; third upgrade | WB-D050 | I03 | Distinct draws and eligible-pool tests |
| Unlimited fixed-price Pardon Banned→Locked | WB-D051 | I04/I06 | Repeat purchases, normal unlock still required |
| Free pause, no paused credit | WB-D002/073 | I01/I05 | Baseline and reconnect fixtures; no Assisted flags |
| Minimum 10k eligible item acquisition | WB-D057/074 | I07 | Current server quote, frozen ownership exception |
| Taint and Sacrifice one spin | WB-D058 | I07 | Atomic debit/replay; punishment adds no debit |
| Preserve Trade with; attempt notice | WB-D066/067/073 | I06 | Menu retained; route-specific cancellation and policy gate |
| Supported method/tool attribution | WB-D069 | I05 | Positive/negative traces for every catalog template |
| Quest access route compatibility | WB-D070 | I03/I05 | Journal prerequisites plus required shop/access route |
| Grand Fate fresh evidence | WB-D064/071 | I08 | Each detector contract; retroactive update negative tests |
| Physical vendor visit | WB-D072 | I06 | Interaction + actual location + shop identity |
| Coffer receipt, not disappearance | WB-D068/074 | I07 | Confirm + item delta + credit; ambiguous receipt pending |
| Single writable session, durable receipts | WB-D075 | I01 | Conflict, crash, duplicate receipt and migration fixtures |
| No main or production changes | WB-D076 | All | GitHub compare restricted to docs/design tooling |

| New content/daily drafts | WB-D079/080/084 | I03/I05/I07 | Source identity, runtime gating, smaller pools, expiry/owned-receipt cases |
| Paused-acquired recovery item | WB-D002/057/085 | I07 | No acquisition credit, active verified donation once, empty-set exception only |


The register remains authoritative for exact established IDs. This mapping is a handoff index, not evidence that future implementation tests have already passed.

## Catalog drafting checkpoint

See [CONTENT_CATALOGS](CONTENT_CATALOGS.md) and [CATALOG_ACCEPTANCE](CATALOG_ACCEPTANCE.md) for newly authored drafts (WB-D079/080), distinct from missing historical catalogs. Machine-readable 100 bounty candidates, 50 punishments, 67 repository boss categories and 213 quest enum records are in catalogs/. Source metadata is not runtime eligibility. All unvalidated bounty/punishment entries remain disabled. H3 exercises named bounty/reward and plain-XP punishment drafts in synthetic trials only; exact quest/boss access remains unmodeled.

## Restored historical contracts

| Requirement | Decisions | Ticket | Acceptance evidence |
| --- | --- | --- | --- |
| Random forced Death’s Wheel, voluntary choice | WB-D033 | I02/I07 | Frozen candidates/outcome; crash restores demand; no item choice/reroll |
| Completion-time objective-specific audit and clean streak reset | WB-D087 | I01/I05/I07 | No immediate tolerance charge; supported by-products; Penance escalation/reset fixtures |
| Fresh-account scope and no-active-Fate Grand activation | WB-D088/089 | I01/I08 | Valid onboarding; pending obligations block activation; no artificial time/level gate |
| Separate card purchase pools | WB-D090 | I03/I04 | Skilling purchase does not unlock Bossing namesake; Challenge evidence gates |
| Skill modifiers, clean XP targets | WB-D091 | I03 | Bracket/modifier/increment bounds and distinct Standard offers; provisional values explicit |
| Protected tutorial and qualified starting quests | WB-D092 | I01/I03/I09 | Mainland-first ordinary assignment; no tutorial violation/credit backfill |
| Introductory child and meaningful AND/OR nodes | WB-D093 | I04/I09 | FP cannot bypass prerequisites; node benefit survives alternate route |
| Active bounty notifications and independent rarity/reward | WB-D094 | I05/I09 | Unclaimed evidence retained; manual idempotent claims; paused negative fixtures |

| Recovered peer gates/clusters/64 boss nodes and pool commitment | WB-D027/095/099/100 | I03/I04/I09 | 5/9 and 4/6 unique unlocks; mode roster covers all67 source categories; no ordinary boss removal |
| Visible grouped bounties, vendor transparency, Wilderness opt-in | WB-D101/102/103 | I03/I04/I05/I06/I09 | Family acquisition/claim provenance; current/next class quotes; no implicit access permission |
| Grand checklists and delegated lifecycle/economy | WB-D104/105/106 | I01/I04/I07/I08/I09 | Failed attempt preserves checks; no offline credit, timer or passive FP; Q10 settlement held |

## Offline receipt acceptance supplement

WB-A24 executes the existing I01/I04/I05/I06/I07/I08 receipt boundaries in [44 expected traces and32 systematic crash combinations](../../automation/receipt_safety/README.md). The mapping includes exact case IDs, source hashes and limitations. Passing this in-memory oracle does not mark those implementation tickets complete. Unparsed mock tails are preserved, duplicate mock settlements pay once, and Q10/CA-MIXED/audit-order/Q6 remain gated.
