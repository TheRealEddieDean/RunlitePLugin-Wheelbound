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
| Active bounty notifications and independent rarity/reward | WB-D094 | I05/I09 | WB-D112/114: automatic once-only active payout; pause blocks processing; prior evidence retained |

| Recovered peer gates/clusters/64 boss nodes and pool commitment | WB-D027/095/099/100 | I03/I04/I09 | 5/9 and 4/6 unique unlocks; mode roster covers all67 source categories; no ordinary boss removal |
| Visible grouped bounties, vendor transparency, Wilderness opt-in | WB-D101/102/103 | I03/I04/I05/I06/I09 | Family acquisition/claim provenance; current/next class quotes; no implicit access permission |
| Grand checklists and delegated lifecycle/economy | WB-D104/105/106 | I01/I04/I07/I08/I09 | Failed attempt preserves checks; no offline credit, timer or passive FP; WB-D112 independent bounty settlement preserved after failure/Reject |

## Offline receipt acceptance supplement

WB-A24 executes the existing I01/I04/I05/I06/I07/I08 receipt boundaries in [44 expected traces and32 systematic crash combinations](../../automation/receipt_safety/README.md). The mapping includes exact case IDs, source hashes and limitations. Passing this in-memory oracle does not mark those implementation tickets complete. Unparsed mock tails are preserved, duplicate mock settlements pay once, and Q10/CA-MIXED/audit-order/Q6 remain gated.

Latest requirements: WB-D111→I03/I09 known-empty Questing retry with retained wheel and persisted outcome; WB-D120 confirms zero extra empty-retry allocation and one eventual valid Fate allocation. WB-D112→I04/I05/I09 automatic independent bounty payout; failure/Reject never revokes it. WB-D113→I03 reaffirms L/S/G without adding a fourth card. WB-D114→I01/I05 paused reward processing refused. WB-D115→workflow one choice at a time. Historic A24 cases/manual-claim sources remain pinned prior-contract evidence, not current acceptance of superseded behavior.

WB-D116→item Bounties Shop terminology and I04/I05/I09 item reward flow. WB-D117→I08/I05 supported hilt5 item detector; Master active-only CA threshold/possession-insufficient rules superseded. Do not read historical CA-MIXED fixtures as a requirement for this Grand goal. Any separate CA aggregate payout remains its own scope/receipt contract.

WB-D118→I02 slice quote/counter transactions; first-instance baseline includes starting grants; later new purchased instances use activity lifetime ladder after destruction; Satchel return and retries add no count. WB-D119→workflow: automatically offer next single choice until stopped.

WB-D121→I02/I07/I09: Questing may store/remove under normal rules but cannot destroy; forced displacement respects this type exception; all other active minimum/capacity/price/history rules preserved.

WB-D122→I03/I05/I09: fourth Bossing Challenge confirmed with difficult-CA or significant rare-drop hunt; separate unlock/context/receipt; pool/generation/catalog/live gates remain. Earlier Q9 existence uncertainty superseded, not the evidence gates.

WB-D123→I03/I05/I09: dedicated eligible random Challenge boss/creature target pool distinct from ordinary Bossing and card types; unlock mapping/order still pending. No normal-roster expansion or player reroll.

WB-D124→I03/I04/I09 prerequisite AND tree, atomic included starter bundle and paid expansion; WB-D125→I03/I05 catalog unique/shared-table classification and reviewed rate/source. Exact bundle/gates/prices/time balance pending; eligibility-diversity minimum not approved.

WB-D126→I03/I05/I09: minimum3 distinct actually eligible Challenge boss/creature targets for new offerings; permanent card/bundle retained belowfloor; knownness/grouping/current evidence count; committed selections not rerolled.

WB-D127→I03/I05/I09 random Challenge family/target, no picker/reroll, durable typed result and reviewed valid eligibility; probabilities/order not numerically approved.
