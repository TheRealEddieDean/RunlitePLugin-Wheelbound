# Complete screen inventory and interaction plan

> **APPROVED initial simple UI.** [Implementation handoff, delivery milestones and approval record](UI_IMPLEMENTATION_HANDOFF.md). Plain-control implementations may proceed after checks; optional visual polish is deferred.

> [36-screen simple UI coverage and delivery order](SIMPLE_UI_SCREEN_COVERAGE.md) · [Navigable plain-control preview](previews/simple-screens.html). These expand existing task acceptance; they are not implemented plugin screens.

Audit status is source-inspected, not live-client verification. Numeric task IDs below mean WB-UI-XX. All new previews are proposed layouts with illustrative state; approved Master/GF4/B3 are preserved. Rejected/superseded manual bounty claiming, Challenge CA objectives, arbitrary rerolls and invented Slayer-final-tier UI are excluded.

| Screen/system | Implementation status | Surface | Expected interaction / components | Tasks | Preview |
| --- | --- | --- | --- | --- | --- |
| Normal/Master wheel | Existing legacy UI; mode integration missing | Canvas popup + sidebar | Same current appearance, filters and result. Open/skip/close preserves selection semantics. | WB-WHL-01; 35–36 do not touch Master | Existing source; no redesign |
| Mode sidebar / Overview | Missing | Sidebar + Hub Overview | State/Pause, FP/debt/spins, obligation, restrictions, Grand checklist and navigation; unsupported values unavailable. | 17,31 | hub-overview.svg |
| Run initialization | Missing UI; partial Grand staging model | Hub Grand Fate | Protected tutorial → supported mainland eligibility → sealed five-goal draw → allowed one reroll → active status. Unknown account predicate blocks. | 34 | grand-fate.svg |
| Grand progress / attempts | Partial models only | Hub Grand Fate + GF4 popup | Immutable seal/checklist; show unfinished component and attempt eligibility, retained Awakened components, hilt5; evidence not inferred from item display. | 34,35 | grand-fate.svg; GF4 original preserved |
| Fate Tree | Evaluator only; no catalog/renderer | Hub Tree + inspector | Pan/zoom/search/fit, square nodes in circular peer clusters, ALL/ANY/count, quote/confirm/ack. | 18–30 | fate-tree.svg |
| Wheel Builder | Partial inventory/odds proposals | Hub Builder | 24 slots/Satchel, UUID and activity odds; preview draft → legal boundary validate → quote → one atomic transaction; cancel no mutation. | 39–41 | wheel-builder.svg |
| Fate/upgrade shop | Missing | Hub Shops / Upgrades | Product list/search, own/locked/price pending, benefit and exact atomic confirmation. Tree purchase routes to same command. | 42,43 | shops-bounties.svg |
| Vendor unlocks | Missing | Hub Shops / Vendors | Permanent entitlement vs actual access/evidence; disabled unsupported enforcement; trade warning retains Trade with. | 42,43,49 | shops-bounties.svg layout family |
| Challenge targets/rank controls | Missing; catalog drafts only | Hub Shops / Challenge + Tree inspector | Lesser→Standard→Greater→Superior retained; first paid bundle; shared targets; owned/enabled/ready and per-rank three floor; no target selection. | 38,42,43 | challenge-ranks.svg |
| Bossing cards and B3 wheel | Legacy normal boss wheel only | Hub offer panel then canvas B3 popup | Standard KC → two/three frozen offerings → select → random eligible individual boss. Challenge pre-offer rank/rules, target only after selection. | 32,36 | fate-offers.svg; B3 selected text spec |
| Skilling cards / XP | Legacy XP wheel; mode missing | Offer panel + objective inspector | Approved Lesser/Standard/Greater as supplied by backend, actual XP restrictions/tolerance; no new card types. | 32,33 | fate-offers.svg layout family |
| Quest choices / eligibility | Legacy support; mode missing | Hub objective detail | Committed quest choices and qualification reasons; known-empty notice/free retry; unknown blocks. Choice count comes from resolved catalog not conflicting prose. | 37,33 | objective-detail.svg |
| Combat / skilling / Slayer objectives | Missing mode UI | Sidebar + shared objective detail | Actual quantity/unit, tolerance restrictions and supported progress; combat no invented style restriction. Slayer placement follows current committed Tier II. | 33 | objective-detail.svg |
| Activation / completion / failure / Reject | Missing | Sidebar actions + Hub confirmation | Capability gates, exact consequences → durable acknowledgment → feedback; outstanding settlement stays visible across reopen. | 16,33 | objective-detail.svg; risk-recovery.svg |
| Challenge fresh-item hunt | Missing | Sidebar + objective detail | One exact item/source/context/rank/reward, fresh active assigned-source drop; owned/withdrawn item not completion, no pity kill cap/reroll. | 33,38 | objective-detail.svg; challenge-ranks.svg |
| Zero spins / Defy Fate | Missing | Hub consequence sheet | Grand or Defy when legal; exact refill, odds, full-wheel displacement consequences together; Taint distinct special slice. | 45 | risk-recovery.svg |
| Taint / Punishment wheel | Missing, no final style selection | Existing popup pattern + Hub obligation | No ordinary skill recolored as tainted; one-spin Taint task, no additional punishment spin; style needs review. | 46,47 | risk-recovery.svg wireframe; final wheel art outstanding |
| Sacrifice / Coffer | Missing, actual evidence blocked | Hub obligation inspector | Known no eligible item: acquire eligible item ≥10,000 GP; unknown bank requires refresh. No invented receipt, forced death or waived obligation. | 47 | risk-recovery.svg |
| Fate’s Pardon | Missing | Shop detail + confirmation | Fixed reviewed price, unlimited purchases, taint debt only Banned→Locked; separate unlock/gamble remains; no use counter. | 48 | risk-recovery.svg layout family |
| Bounties Shop / rewards | Missing | Hub Bounties | Item sprites/name/rarity/FP sort, entitlements/availability; qualifying active receipt pays automatically once; no Claim button/paused backfill/clawback. | 44 | shops-bounties.svg |
| Settings / notifications / help | Missing mode UI | Hub Settings/Help; sidebar concise alerts | Reduced motion/legal filters, evidence reasons and local warning on trading; notice queue capped 50, no stack traces/account hashes. | 49 | settings-help.svg |
| History / recovery | Some receipts/storage; no general run history | Hub Overview recent receipts + warning panel | Read-only actual persisted receipts, bounded recent list; no invented prior-run browser/reset/refund. Corrupt/unknown/newer save blocks actions with safe retry/export guidance only if supported. | 13,49,50 | risk-recovery.svg; settings-help.svg |

## Shared screen anatomy and implementation sequence

All major pages use ResourceStrip → route heading/search → primary content → one shared inspector/confirmation. Tree uses custom canvas, Builder slot grid, shops/bounties list/detail, offers card row, Grand checklist, risk a consequence sheet. Every page inherits loading/absent/unsupported/paused/pending/error/empty states; an empty list is not proof of gameplay eligibility.

Preview boards provide composition, not working controls. Their component breakdown follows this inventory and the design system; task details specify services and testing. Missing backend data displays an explanation and disabled action. A paid list must never offer placeholder prices or “free” unknown costs.

First useful connected milestone: WB-UI-10/12/13/14/17 plus existing EXT-IDENTITY prerequisites: an actual saved-run status sidebar with FP/debt/lifecycle and sealed Grand checklist. It is read-only until command/evidence services exist; do not claim an active Fate tracker from just lifecycle data. WB-UI-11/15 then opens the same data in one Hub. The user approved the plain 17/31 layouts; implement them under the current handoff. In parallel, 18/19/20/21/22/23 establishes the actual-data Tree renderer; 28 waits for atomic progression purchases.

No general reset/respec, unapproved history retention, new reward family or emergency retirement flow is introduced. A56 stays deferred. Genuine remaining gameplay ambiguity is documented as a service/content hold, not resolved by choosing a convenient UI.
