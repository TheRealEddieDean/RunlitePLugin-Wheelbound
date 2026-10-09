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
