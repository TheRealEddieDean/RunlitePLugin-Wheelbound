# Wheelbound autonomous task queue

Authoritative design: [decision register](../docs/wheelbound/DECISIONS.md). Operating rules: [protocol](PROTOCOL.md). Sizes are effort estimates, not allowance measurements: XS 2–5m, S 5–15m, M 15–30m, L >30m. Split M/L work before claiming it. Priority P0 precedes P1/P2. DONE means committed deliverable plus stated validation; BLOCKED means an actual dependency is missing. Existing experiments/research are preserved, not restarted.

| ID | Deliverable | Priority | Dependencies | Size | Status | Documentation | Completion criteria |
| --- | --- | --- | --- | --- | --- | --- | --- |
| WB-A00 | Activate protocol/checkpoint files and session ownership | P0 | Current head/main verification | S | DONE | automation/* | 1bd0e93c protocol/checkpoints committed and remote head verified; main baseline unchanged |
| WB-A01 | Reconcile first historical forced Sacrifice/audit/scope/Grand/card pool batch | P0 | Recovered S8 transcript | S | DONE | DECISIONS WB-D033/054/086–090; historical report | b1f9249e corrections committed; 90 unique decisions and preserved model hashes checked |
| WB-A02 | Review S8-M0001–0100 approvals and onboarding/progression | P0 | A00, A01 | S | DONE | research/HISTORICAL_RECONCILIATION.md; DECISIONS; relevant specs | 100 visible turns indexed, 50 user turns reviewed, named adjacent proposals read; WB-D091–094 and affected specs verified; owning checkpoint must be reachable from remote head |
| WB-A03 | Review S8-M0101–0200 progression/punishment/boss approvals | P0 | A02 | S | DONE | Historical report; PROGRESSION; PUNISHMENTS | 100 turns indexed/50 user turns reviewed; D027/035 provenance and D095–099 specs checked; no nonexistent historical catalog claimed |
| WB-A04 | Review S8-M0201–0300 card/bounty/vendor/audit approvals | P0 | A03 | S | DONE | Historical report; FATE_CARDS; BOUNTIES; ACCOUNT_ACCESS | 100 turns indexed/50 users reviewed; D100–103 and 64-node grouping validated; immutable experiment catalogs preserved |
| WB-A05 | Review S8-M0301–0436 Grand/shop/delegated safeguards | P0 | A04 | S | DONE | Historical report; GRAND_FATES; FATE_SHOP | All436 visible turns indexed/218 users reviewed; D104–106 and delegated authority distinguished; A06 cross-file pass still required |
| WB-A06 | Cross-file recovery consistency and question deduplication | P0 | A02–A05 | S | DONE | All gameplay specs; OPEN_QUESTIONS; automation/DECISIONS_PENDING | Scoped gameplay/state/UX/detector/handoff contradictions corrected;106 IDs, all436 turn coverage, links and immutable hashes checked; actual open questions retained |
| WB-A07 | Catalog authority manifest; compare new drafts with recovered rules | P1 | A06 | S | DONE | CONTENT_CATALOGS; CATALOG_ACCEPTANCE | Historical promises vs actual lists distinguished; experimental catalog hashes preserved; normative gaps recorded |
| WB-A08 | Audit 100 permanent bounty targets, grouped families and source/access evidence | P1 | A07 | M → split batches | DONE (conditional review) | BOUNTIES; catalog review records | Finite 100 reviewed targets or explicitly marked conditional entries; no untraced acquisition enabled |
| WB-A09 | Audit 50 punishment templates and consecutive severity tiers | P1 | A07 | M → split plain/restricted/escalated | DONE (conditional review) | PUNISHMENTS; template review | Feasible observable rules; approved harsh escalation retained; experimental 120m cap not normative |
| WB-A10 | Audit daily/CA/diary receipts and reward sinks | P1 | A06, A07 | S per receipt family | DONE (conditional review) | BOUNTIES; ACQUISITION_EVIDENCE; ECONOMY | Manual/idempotent claims and paused rules; real source gates; optional dailies |
| WB-A11 | Version next faithful model contracts before execution | P1 | A06–A10 | S | DONE (contract saved) | simulation version specification | Correct end-audit/random sacrifice/card pools; real content gaps explicit; H1–H3 immutable |
| WB-A12 | Execute next model in bounded trial/sensitivity batches | P1 | A11 | M → split smoke/baseline/sensitivity | DONE (bounded core) | SIMULATION_RESULTS; new version outputs | Actual seeds/config/hashes/counts/checks; no claimed full-account realism beyond modeled data |
| WB-A13 | Review architecture/backlog acceptance against recovered rules | P1 | A06 | S | DONE | STATE_CONTRACT; TRACEABILITY; DEVELOPMENT_PIPELINE | Correct RNG/audit/Grand/pool behavior in tickets and failure fixtures; no production changes |
| WB-A14 | Recheck source research gaps without repeating completed policy audit | P1 | A06 | S per gap | DONE (source scope) | TECHNICAL_SPECIFICATIONS; POLICY_AND_FEASIBILITY | Targeted current primary evidence only; distinguish candidate API from live proof |
| WB-A15 | Refresh Markdown-derived PDF and visual QA | P2 | A06, A07, A13 | S | DONE (checkpoint published) | build_pdf; output PDF | Generated from committed SHA; changed chapters rendered/checked; saved deliverable; no stale latest claim |
| WB-A16 | Final readiness/handoff and consolidated decisions | P2 | A08–A15 or explicit limitations | S | DONE (documentary scope) | MONDAY_HANDOFF; automation/* | Actual completed/blocked scope, exact next engineering milestone and commit links |
| WB-A17 | Live Coffer/method/vendor/GE/trade trace fixtures | Release gate | Explicit implementation authorization + real client | L | BLOCKED | TECHNICAL_SPECIFICATIONS; TESTING | Current-client positive/negative/paused/order/failure traces; no mocked receipt treated as live |
| WB-A18 | Existing clean project build baseline | Engineering gate | Accessible JDK/Gradle dependencies | S | DONE (remote build; reporting partial) | DEVELOPMENT_PIPELINE | Remote clean build actually compiled/tested; dependency version and XML export still incomplete |

| WB-A19 | Verify successful CI build and inspect package/test baseline | P1 | A18 source configuration; current GitHub run | S | DONE (source/CI evidence) | research/BUILD_BASELINE; DEVELOPMENT_PIPELINE | Exact run/head/job/toolchain/tasks/test limits; no production changes |

| WB-A20 | Define current-client capture packet and offline envelope validator | P1 | A14 source contracts; A17 live gaps | S | DONE (planned/offline only) | research/TRACE_CAPTURE_PROTOCOL; schemas/trace_capture |46 NOT_RUN cases, schema and origin/order fixtures; zero live evidence claimed |

| WB-A21 | Cross-process verification of saved H4 boundary cases | P1 | A12 saved outputs | S | DONE (offline replay) | simulation/h4/PROCESS_REPLAY_VALIDATION |23 cases×4 processes match CSV/ledger/snapshot; no input changes or new balance claims |
| WB-A22 | Draft inspectable CI report/dependency pipeline proposal | P1 | A19 actual CI limits | S | DONE (proposal only) | research/CI_PROPOSAL; CI_ACTION_PINS | YAML/action source pins checked; not activated or runtime-tested |

| WB-A23 | Draft exact restrictive-control review brief without sending | P1 | A14 policy/source limits; A20 route plan | S | DONE (draft only) | research/POLICY_REVIEW_PACKET | Current published sources rechecked; precise behavior/routes/agenda; no review contact or approval |

Paths without a directory prefix above are under docs/wheelbound/. Selection policy: acquire ownership, choose highest-priority TODO or PARTIAL whose dependency is DONE, work to a bounded deliverable, verify and checkpoint. Speculative numbers are delegated candidates, not historically approved values. Questions block only affected work.

| WB-A24 | Execute offline receipt/recovery acceptance fixtures | P1 | A13 state contract; A19 baseline | S | DONE (offline only) | automation/receipt_safety | 284799b1 saved44 fixtures/32 crash combinations,10 detected mutants/6 malformed packets; no production/live/disk proof |

| WB-A25 | Focused historical pause-claim timing check | P1 | A24 newly exposed timing ambiguity | XS | DONE (focused scope) | automation/receipt_safety/PAUSE_CLAIM_RECOVERY |10 keyword-matching messages plus explicit adjacent reply; no direct prior rule identified or new approval inferred |

| WB-A26 | Prepare source-pinned JUnit report reconciliation | P1 | A19 baseline declarations; A22 export proposal | S | DONE (offline only) | automation/test_reports | 4df7d830 pins70 declarations/12 files and23 synthetic XML checks; actual exported reports remain missing |

| WB-A27 | Quantify Q6 exhausted-pool odds and replacement topology | P1 | Confirmed5/3/24 and unresolvedQ6 | S | DONE (analysis only) | automation/decision_analysis |411ccde4 records6 exact wheels/32,116 compositions; no rule adopted or account trial added |

| WB-A28 | Compare Q10 Reject bounty settlement and claim timing | P1 | ExistingQ10; A24 provenance boundaries | S | DONE (analysis only) | automation/decision_analysis |e61ed27b records72 hypothetical ledger comparisons, claim-timing difference and spent-FP consequences; no policy adopted |

| WB-A29 | Specify exact build-artifact and standard-JAR acceptance packet | P1 | A19/A22/A26 source and reporting gaps | S | DONE (specification only) | automation/pipeline_acceptance | 5297ccf9/5f25897f:12 future acceptance checks, five I00 subtasks, seven source pins; byte-export/configuration gaps documented; zero new builds/artifact reviews |

| WB-A30 | Split I01 into Codex-sized implementation handoffs | P1 | A13/A24/A29 existing contracts | S | DONE (handoff only) | automation/codex_handoff | 102f319f: eight tasks, ready future prompt,44 fixture dispositions and nine source pins; no coding |

| WB-A31 | Split I02 wheel editing into Codex-sized transactions | P1 | Approved wheel/shop/Defy contracts; A30 handoff | S | DONE (handoff only) | automation/codex_handoff | 457bcaf7: eight I02 tasks/20 planned acceptance examples; preserved counter/availability gates; no new prices or code |

| WB-A32 | Record current user design answers and one-at-a-time choice protocol | P0 | Latest explicit replies2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; gameplay specs; automation | 908aeafe: WB-D111–115 and29 affected files; supersession/one-choice protocol checked; PDF explicitly marked stale |

| WB-A33 | Refresh derived PDF after latest explicit decisions | P1 | A32 committed source; PDF rendering/storage capability | S | TODO | build_pdf; automation/PDF_CHECKPOINT | Generate from new committed Markdown, render/inspect/save next version; published version9 stays marked stale until done |

| WB-A34 | Record Bounties Shop terminology and Master hilt item objective | P0 | Explicit reply2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; GRAND_FATES; affected specs | 7415922e: WB-D116/117 and17 affected specs;117 IDs verified; item-shop scope/old source supersession retained |

| WB-A35 | Confirm slice lifetime purchase classification and continuous single-choice pacing | P0 | Explicit recommendation approval2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; WHEELS; shop/ledger/handoff/protocol | 9cf01adc: WB-D118/119;13 affected files;119 IDs checked; no numeric price change |

| WB-A36 | Confirm zero extra allocation for empty-Questing retries | P0 | Explicit approval2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; wheel/assignment/store/tests/handoff | 8cb8222c: WB-D120; zero extra retry cost; eventual valid Fate one allocation;120 IDs checked |

| WB-A37 | Confirm Questing removable to storage but indestructible | P0 | Explicit user reply2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; wheel/storage/Defy/shop/tests/handoff | b0300a0d: WB-D121;13 specs updated; normal paid storage/removal allowed, destruction prohibited;121 IDs checked |

| WB-A38 | Confirm Bossing Challenge card with difficult CA or rare creature drop | P0 | Explicit user correction2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; FATE_CARDS; catalog/assignment/receipt/handoff | da1acdd4: WB-D122;19 affected specs; fourth card CA/rare drop confirmed; pool/order/catalog/evidence pending;122 IDs checked |

| WB-A39 | Confirm dedicated random Bossing Challenge boss/creature target pool | P0 | User accepts recommendation2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; card/pool/progression/catalog/store/handoff | 053cde1a: WB-D123;14 specs; dedicated random eligible boss/creature pool, normal L/S/G unchanged;123 IDs checked |

| WB-A40 | Record Bossing Challenge prerequisite tree, starter bundle and unique-drop filter | P0 | Explicit user design2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; progression/card/shop/catalog/store/handoff | fe757959: WB-D124/125;14 specs; AND prerequisite/starter bundle/individual expansion/unique-drop exclusion;125 IDs checked; eligible-target floor proposed |

| WB-A41 | Confirm three actual eligible Challenge targets and reaffirm starter bundle | P0 | Explicit user recommendation approval2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; card/tree/catalog/state/UI/tests/handoff | c4601647: WB-D126;14 specs;≥3 actual distinct targets for offers, card/bundle retained;126 IDs checked |

| WB-A42 | Confirm random Bossing Challenge objective family | P0 | Explicit user reply random2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; card/state/store/UI/tests/handoff | e30da5c0: WB-D127;10 specs; random family/target, no picker/reroll; probabilities/order not adopted;127 IDs checked |

| WB-A43 | Remove CA objective family from Bossing Challenge | P0 | Explicit user correction2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; card/catalog/state/store/UI/tests/handoff | 49ce5cc2: WB-D128;16 specs; CA family removed, item hunts only; bundle/tree/random pool/≥3 drop targets retained;128 IDs checked |

| WB-A44 | Confirm owned items remain eligible but fresh assigned drop required | P0 | Explicit approval2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; card/evidence/store/UI/tests/handoff | beb648eb: WB-D129;12 specs; owned item eligible, fresh active assigned-source drop required;129 IDs checked |

| WB-A45 | Confirm one specific Challenge item and bounded1/400–1/1500 rarity | P0 | Explicit user design2026-10-12 | S | DONE (Markdown checkpoint) | DECISIONS; card/catalog/economy/state/UI/tests/handoff | 38678af1: WB-D130/131;13 specs; one exact logical item, inclusive[1/1500,1/400];131 IDs checked; no guaranteed kill cap |

| WB-A46 | Resolve Challenge personal probability and supported-context policy | P0 | WB-D132 current delegation; verified owner | S | DONE (documentation only; owning commit) | DECISIONS; affected specs/checkpoints | WB-D132–134; consistency checks; guarded publication must verify remote before starting A47 |

| WB-A47 | Specify Challenge draw/reveal and non-consuming completion | P0 | A46 remotely verified; current owner | S | DONE (documentation only; owning commit) | CHALLENGE_ASSIGNMENT_CONTRACT; DECISIONS; affected specs | WB-D135–137; odds arithmetic/consistency/scope checks, guarded remote publication |

| WB-A48 | Review bounded Challenge starter/expansion target candidates | P1 | A46/A47; source provenance/limitations | S per candidate batch | PARTIAL — B1 DONE (source-limited) | research/CHALLENGE_CANDIDATE_REVIEW_B1 | Seven hunts/six creatures; fractions/access/continuation gaps; current primary freshness blocked; no enabled/final roster |

| WB-A49 | Record explicit approval of Challenge recommendations | P0 | User approval; guarded ownership | XS | DONE (owning checkpoint) | DECISIONS; contract; checkpoints | WB-D133–137 CONFIRMED with provenance; validation and remote verification before next work |

| WB-A50 | Resolve ordinary-empty retries and all-unavailable readiness | P0 | A49 remote verified; WB-D132 same-rubric delegation | S | DONE (documentation only; owning checkpoint) | DECISIONS; UNAVAILABLE_SELECTION_CONTRACT; affected specs/checkpoints | WB-D138/139; exact conditional-odds/consistency/scope checks; guarded remote verification |

| WB-A51 | Trace Loot Lookup drop-table retrieval and parser limits | P1 | User request; verified upstream/Hub pin; current owner | S | DONE (source analysis only; owning checkpoint) | research/LOOT_LOOKUP_SOURCE_TRACE | Ten source pins; request/parser/context/data flow; no live/Java run; guarded remote verification |

| WB-A52 | Specify lossless candidate import/export review packet | P1 | A51 source trace; existing Challenge contexts | S | DONE (format/fixtures via A53) | tools/wiki_drops/README.md; fixtures | Raw HTML/context/rate alternatives and disabled review records; no approved runtime catalog |

| WB-A53 | Build user-requested Python Wiki drop research collector | P0 | Explicit tools authorization; verified owner; A51 source study | M (bounded tool) | DONE (tool/offline validation; owning checkpoint) | tools/wiki_drops; checkpoints | Standard-library category/URL/offline modes; boss/monster evidence; raw rates/contexts; SQLite/JSON/CSV;17 offline tests and CLI sample; guarded remote verification; live all-monster coverage NOT_RUN |

| WB-A54 | Collect/review first bounded real Wiki batch | P1 | A53; guarded ownership; allowed source paths | S | DONE (three real pages; no admission) | research/WIKI_REAL_BATCH_A54 | Three network captures/revision IDs,201 rows/four target extracts, classification/layout/freshness limits; remote verification required |
| WB-A55 | Review bounded real boss classification/candidate batch | P1 | A54 remotely verified | S | DONE (three real bosses; no admission) | research/CHALLENGE_CANDIDATE_REVIEW_B2 | Three bosses/210 rows/nine reviewed extracts; category labels, source hashes/revisions and hold/exclusions; remote verification required |

| WB-A56 | Prepare Q6 postcommit recovery boundary matrix and one material choice | P1 | A55 verified; existing no-reroll/Pause/Reject contracts | S | DONE (planning; awaiting one answer) | automation/decision_analysis/POSTCOMMIT_RECOVERY_A56 |16 cases:13 existing-rule derivations/one open/two proposed; no new adopted rule; remote verification required |

| WB-A57 | Prepare user-collected data new-chat handoff and starter prompt | P0 | User request; verified A56 remote/owner | XS | DONE (handoff only; owning checkpoint) | DATA_PARSING_HANDOFF; START_DATA_PARSING; SESSION_STATE | Preserve A54–56/current decisions; attachment guidance; exact intake-first steps; remote verification |

| WB-A58 | Inspect uploaded collector output and save quality/intake report | P1 | User upload; A57 handoff; guarded ownership | S per bounded intake | DONE (intake only; owning checkpoint) | Separate data intake report/reference | Original hashes/provenance, scope/counts/classification/parser/source gaps; no full roster/fixture/live evidence claims; verify remote before next batch |

| WB-A59 | Review uploaded Abyssal demon/Kraken/Zulrah unique-drop contexts | P1 | A58 remote verification; supplied cache | S | DONE (review only; owning checkpoint) | research/CHALLENGE_UPLOAD_BATCH_A59 | Source-pinned lossless context review; no runtime admission |

| WB-A60 | Quarantine weak monster labels and ancestry-only boss classifications in review index | P1 | A59 verified; supplied cache | S per batch | DONE (overlay; owning checkpoint) | research/CHALLENGE_TAXONOMY_A60 | Preserve raw records; explicit evidence/disposition, no enabled encounter keys |

| WB-A61 | Author finite personal-rate Challenge contexts from corrected upload | P0 | A60 verified | M bounded catalog | DONE (source-reviewed design; owning checkpoint) | catalogs/challenge_hunts_v1 | Specific meaningful items, normalized contexts and exclusion coverage; no production enablement |

| WB-A62 | Complete Challenge access/receipt/FP schedule and catalog delivery | P0 | A61 verified | M bounded packet | DONE (complete authored v1; owning checkpoint) | CHALLENGE_HUNT_TABLE; contracts/catalog | Every supported hunt has concrete access, completion evidence and frozen FP; validation, manifest, remote verification |

A62 completion note: all four requested finite catalog tasks complete;25 creature keys/44 hunts/six starter keys with access/personal rates/receipt definitions/numeric FP. A60 corrected overlay32 components/1,437 monsters after source-introduction cross-check; historical A60 commit retained. Runtime engineering/client traces remain separate authorization gate, not an unfilled design table.

| WB-A63 | Four Challenge rarity cards/per-roll brackets and migration tables | P0 | Explicit2026-10-14 rule change; guarded ownership | S | DONE (rules/tables; owning checkpoint) | CHALLENGE_CARD_TIERS; challenge_tier_rules_v2 | WB-D144/145, exact no-overlap edges,44 retier mappings,22 offline tests; proposals not adopted |
| WB-A64 | Settle rank unlock topology then expand four-tier source/access/receipt/FP/offer catalog | P0 | A63 verified; one unlock answer; existing source data | M per bounded source packet | NEXT_DESIGN_ANSWER | Future challenge catalogv2 | Coherent separate rank ownership/offer/actual-diversity gates and reviewed lower-tier hunts; no use of raw lead counts as eligibility |

A63 supersedes old A62 single-range future selection. Old v1 completion remains historical; the expanded four-tier catalog is not complete. Ask one material topology question recorded in SESSION_STATE; no automatic chain or opt-in control adopted.

| WB-A65 | Update CHALLENGE_HUNT_TABLE with all-tier reviewed and source-candidate entries | P0 | Explicit file request; A63 rules; supplied output; guarded ownership | S | DONE (display/index; owning checkpoint) | CHALLENGE_HUNT_TABLE; review indexv2/CSV; archive; validation | All44 reviewed+202 candidate groups in four tiers,45 unresolved rows held, exact source evidence/status; no roster/FP/unlock adoption |

| WB-A66 | Rename/fix current monster and boss Challenge tier drop tables | P0 | Explicit user clarification; A65 source index; guarded ownership | S | DONE (two current tables; owning checkpoint) | MONSTER_CHALLENGE_HUNT_TABLE; BOSS_CHALLENGE_HUNT_TABLE |90/156 rows exactly partitioned, source_kind evidence/links/generator/validation, no obsolete active filenames or new mechanics |

## A67 — DONE: complete supplied-monster drop review

Priority user-directed; effort M; dependency A66 + supplied archive. Deliverables: full1,437-source/31,757-row/1,803-item audit and report, expanded327-row monster table with four tiers and explicit holds;309 candidates (252 new/15 removed/57 retained),18 previous contexts. Boss156 membership unchanged. Validated source/cache/SQLite, complete row conservation, reproducibility,12 manifest pins and22 tests; guarded publication and exact remote verification required. No production activation. Next safe batch: contextual access/receipt/FP admission; unsupported399 and unresolved444 entity gaps stay visible, no broad recrawl.

## A68 — DONE: bounded lower-tier context/access/receipt review

Priority P1; size M bounded packet; dependencies A67 committed evidence and guarded ownership (no topology answer required for review-only work). Seven source contexts/five Standard and two Lesser; exact source receipts/context preserved, access drafts and specific holds, ten planned receipt cases, conditional effort quantiles without throughput/FP guesses. Deliverables research/LOWER_TIER_CONTEXT_REVIEW_A68.md/.json and tools/wiki_drops/validate_lower_tier_review.py; committed-source validator and22 tests pass. All13 manifest pins checked. No catalog admission/table membership/production change. A64 topology remains NEXT_DESIGN_ANSWER. Next bounded source work is item/form/access/receipt completion for Killerwatt/Molanisk; rank offer/readiness and FP completion remain separate dependencies.

## A69 — DONE in owning checkpoint: confirmed progressive rank topology

Priority P0/P1; size S (A73 M); preceding checkpoint remotely verified. WB-D147 recorded with precise WB-D124/144 supersession; current tier/spec/UI/persistence references aligned. No prices, roster admissions or new runtime behavior. Validation:147 decision IDs,13 unchanged manifest pins and whitespace. Next: A70 owned-rank control/persistence contract under current recommendation delegation.

## A70 — DONE in owning checkpoint: owned-rank opt-in and immutable-offer controls

Priority P0/P1; size S (A73 M); preceding checkpoint remotely verified. WB-D148 DELEGATED; concrete control/persistence contract and five affected specs. Validation:148 unique decisions,13 manifest pins and whitespace. No normal-card toggle or new FP/target grant. Next: A71 preserve offer count and Challenge family exposure while integrating per-rank readiness.

## A71 — DONE in owning checkpoint: ranked offering and per-rank diversity contract

Priority P0/P1; size S (A73 M); preceding checkpoint remotely verified. WB-D149 DELEGATED; concrete family/rank/source sequencing, conditional q/R odds, per-rank≥3 floor and immutable snapshot requirements. Validation:149 unique IDs,13 manifest hashes, whitespace. No new numeric card weight or real catalog admission. Next: A72 shared target ownership, first starter bundle and clear purchase/readiness previews.

## A72 — DONE in owning checkpoint: shared targets and atomic ranked purchase contract

Priority P0/P1; size S (A73 M); preceding checkpoint remotely verified. WB-D150 DELEGATED; canonical target entitlements shared across ranks, initial bundle only on first paid Lesser, honest underfloor/content/price previews and legacy preservation. Validation:150 unique decisions,13 unchanged manifest pins, whitespace. No concrete v2 price/bundle/receipt support invented. Next: A73 executable synthetic rank/purchase/offer invariant checks and readiness handoff; release ownership afterward.
