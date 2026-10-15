# Major decisions pending

Authority: docs/wheelbound/DECISIONS.md. Latest gameplay approvals2026-10-12 extend through WB-D131; current2026-10-13 delegation is WB-D132 and adopted recommendations WB-D133–137. Ask one choice at a time, discuss its answer, and automatically present the next single choice until the user says stop. Do not repeat approved rules.

## Resolved current answers

- Q6 Questing: retain slice and spin normally; known-empty Questing landing gives brief notice and automatically respins (WB-D111). Suspension proposal rejected for this behavior. Broader activity recovery, unknown evidence, WB-D121 resolves Questing as removable to normal storage but indestructible. WB-D120 confirms no extra empty-retry cost; eventual valid Fate one spin.
- Q10: automatic independent qualifying bounty FP survives Fate failure/Reject; no manual claim, escrow or clawback (WB-D112 supersedes WB-D094 manual flow and historical forfeiture).
- Bossing: L/S/G requirements reaffirmed (WB-D113). WB-D122 confirms fourth Challenge; WB-D128 narrows it to item hunts only.
- PAUSE-CLAIM: no reward/claim processing while paused; preserve prior paid FP/active evidence; no paused earning backfill (WB-D114).
- Workflow: one design choice at a time (WB-D115).

## Still pending

| ID | Narrow unresolved boundary | Work permitted |
| --- | --- | --- |
| Separate legacy CA reward scope | Historical per-boss reward is separate from item Bounties Shop and Master Grand | Preserve evidence; no mixed-CA gate on hilt5 Grand; do not silently delete or invent a separate reward |
| Duplicate first/reacquisition / choice6 — RESOLVED | WB-D118 first-instance baseline includes starting grants; later new purchases escalate despite destruction | Same-instance Satchel return/replay does not increment |
| Q9 existence — RESOLVED / Challenge pool pending | WB-D122 confirms fourth card: significant unique item-drop hunt only (WB-D128) | WB-D123 pool and WB-D124 AND gate/starter bundle/individual expansion confirmed; exact members/gates/prices/order/catalog pending; random/no reroll remains |
| Q6 residual | WB-D138/139 resolve other verified-empty ordinary pools and all-unavailable precommit readiness; postcommit invalidation/snapshot reconciliation details remain | Persist verified-empty retry provenance; unknown is not empty; no invented cost/penalty/free edits |
| Q2 | Genuinely full/unsupported Coffer forced donation | Actual current-client boundary evidence first; no forced death or invented escape |
| Q5 conditional | Fallback only if exact restriction fails specific policy review | Preserve Trade with; route/review packet remains unsent |

Balance numbers remain delegated candidate research, not arbitrary isolated approval questions. Q1 new-account scope, Q7 random forced sacrifice and Q8 no active Fate/obligation for Grand remain resolved.

Earlier Q6/Q10 analysis artifacts and A24 manual-claim oracle inputs remain immutable historical evidence. Their options/statuses are superseded where WB-D111/112/114 apply; they are not current implementation acceptance or automatic runtime enablement. Source pins identify prior inputs, not permission to ignore latest replies.

## Latest clarification2026-10-12 — WB-D116/117
Bounties means item objectives in the Bounties Shop. Master CA Grand Fate requires obtaining Ghommal's hilt5; no plugin-maintained active-only CA tally or anti-cheese audit. Earlier mixed-CA choice is no longer a Master Grand condition. Any historical separate boss-mastery/diary reward feature is separate and its scope is not deleted/invented by this clarification. Do not ask the same conflated question again. One-choice-at-a-time pacing remains; automatically present the next single choice until stopped.

Questing voluntary edit question RESOLVED by WB-D121: removable through normal storage, never destroyable. Not mandatory active; no special storage waiver. Continue next single choice automatically until stopped.

WB-D123 resolves pool separation: Challenge randomly draws eligible bosses/selected creatures from a dedicated target pool; L/S/G keeps normal Bossing pool. Remaining next choice: WB-D124 target unlock policy resolved: Lesser AND Greater→Challenge with starter bundle→individual paid expansion. WB-D126 fixes actual eligible-count floor at3; exact starter set remains delegated catalog work.

WB-D124 resolves target unlock structure: owned Bossing Lesser AND Greater, then paid Challenge card including starter unlock bundle, then further individual target purchases. WB-D125 restricts drops to meaningful creature-specific uniques, excluding shared rare table. WB-D126 resolves anti-narrowing offering rule: require≥3 distinct actually eligible boss/creature targets; card/bundle remains owned belowfloor.

WB-D126 eligible-target diversity resolved: minimum3 distinct actually eligible bosses/creatures before new Challenge offerings, card/bundle retained belowfloor. Starter bundle reaffirmed. WB-D128 removes CA objectives and family draw; only random unique item hunts remain. CA incomplete/repeat question is obsolete. Target/item weighting/reveal remains technical work.

WB-D128: Bossing Challenge is item-drop-only; CA family and random-family decision superseded, incomplete-versus-repeat question obsolete for this card. Bundle/AND tree/dedicated random pool/≥3 supported drop targets retained. WB-D129 resolves ownership eligibility: owned item can still be assigned, but fresh active assigned-source drop during assignment required; old items never satisfy.

WB-D129 fresh-drop rule confirmed. WB-D130 resolves one specific item. WB-D131 fixes inclusive1/400–1/1500 specific-item band; no any-unique substitute or guaranteed kill cap.

WB-D133/134 resolve rate policy under current delegation: personal receiving probability in explicit supported contexts; entire reviewed probability range must lie inside the band. Whole-party chance and unknown allocation cannot qualify. Actual formulas/source/current-client proof remain gated; do not re-ask the policy. WB-D135/136 now resolve generation/reveal and creature/item weighting; WB-D137 confirms player keeps the drop. Next bounded work: separate reviewed starter/expansion catalog candidate research, with real primary source data and no runtime enablement.

Current workflow WB-D132 supersedes ask-before-adoption for context-supported delegated recommendations. Remaining questions require one-at-a-time discussion only where approved intent/evidence cannot resolve them. No implementation authorized.

Challenge generation/reveal/weights RESOLVED under WB-D135/136 delegation. Exact starter set, concrete target/source/rate/reward/price/tier tables remain research/balance work; no arbitrary isolated approval question. Broader all-unavailable/postcommit recovery, evidence-dependent Coffer/policy and separate legacy CA reward scope remain independent unresolved boundaries.

User explicitly confirmed all five WB-D133–137 policies2026-10-13 after in-game explanation (“Yep this is all good”). Hypothetical probabilities were illustrative, not source evidence. Next requested work: two context-supported remaining decisions under WB-D132 delegation; no implementation.

Q6 precommit ordinary-empty/all-unavailable policy RESOLVED under WB-D138/139 delegation. Retain full wheel; verified-empty ordinary retries are free; unknown/unsupported positive-weight outcomes block readiness; all-empty ordinary wheel stops without failure/debit. Do not skip mandatory special outcomes. Remaining Q6: postcommit invalidation and specific changed-snapshot reconciliation details, no automatic reroll. See UNAVAILABLE_SELECTION_CONTRACT in gameplay specs. Concrete target catalog/rewards and Q2/Q5 evidence gates remain separate.

A48 batch1 complete (bounded/source-limited): WB-D140 task-independent starter authoring and WB-D141 no dynamic GE-price floor adopted. Exact starter members/count remain candidates, not enabled roster. Task-dependent expansion continuation remains unresolved and held. Next independent deliverable: another bounded candidate/source revalidation batch; direct current Wiki reading blocked, indexed excerpts not current revision proof.

## Next single question — Q6 permanent ordinary invalidation (A56)

[Recovery boundary/matrix](decision_analysis/POSTCOMMIT_RECOVERY_A56.md) is planning only. Recommend no-fault retirement of an unresolved selected ordinary Fate only when a reviewed external game change eliminates every qualifying completion route: no completion reward, FP penalty, Punishment or consumed ordinary spin; release actual reservation idempotently, never blanket +1. Temporary/unknown/self-caused changes and mandatory specials excluded. **PROPOSED, NOT ADOPTED; await user answer.** Existing Pause/Reject/no-reroll applies until answered. Committed-but-unselected offer reconciliation remains separately open.

## Catalog v1 authoring complete — WB-D142/143,2026-10-14

Supersedes earlier pending **concrete v1 members/rates/access/receipt definitions/reward table** status: CHALLENGE_HUNT_TABLE and catalogs/challenge_hunts_v1 now hold25 creature keys/44 specific-item hunts/six starter members and numeric FP. Taxonomy/full-corpus screen/source/identity/conditions reviewed. No broader task/group/stateful contexts silently adopted: excluded from v1. Task-dependent expansion policy, purchase prices, wider empirical balance and actual runtime/current-client validation remain separate. Do not repeat completed intake or ask for approval of routine delegated table authoring. A56 retirement proposal remains PROPOSED/NOT_ADOPTED; no answer inferred.

## Current next choice — Challenge rank unlock topology (A63)

WB-D144 explicitly replaces single future Challenge rarity with four unlockable ranks and unmultiplied per-roll classification. WB-D145 resolves edge overlaps as delegated convention. CHALLENGE_CARD_TIERS/current rulesv2 supersedes older band paragraphs above; v1 roster/FP remains historical. The exact question is progressive Lesser→Standard→Greater→Superior versus independent rank purchases after a common entry gate. Recommend progressive, retain lower ranks. **PROPOSED, NOT_ADOPTED** until answered because this changes WB-D124's old monolithic prerequisite contract. Optional enabled-rank selector/offer integration/prices and per-rank actual eligibility need coherent follow-on, no invented four extra slots or free Lesser. Expanded v2 roster is not yet complete. A56 retirement remains unrelated/unanswered.

## A68 review-only continuation

Current one-at-a-time topology question is preserved pending an answer; the2026-10-14 request for context-supported recommendations is used for safe bounded catalog review, not inferred approval of the explicitly pending prerequisite change. See research/LOWER_TIER_CONTEXT_REVIEW_A68.md for seven lower-tier contexts and conservative holds. No new gameplay decision, source admission, reward or per-rank readiness rule adopted. Progression recommendation still Lesser→Standard→Greater→Superior, lower ownership retained; exact enable controls and offer integration are separate.

## A69 current supersession — topology resolved

WB-D147 CONFIRMED2026-10-15: progressive paid Lesser→Standard→Greater→Superior, lower ranks retained. Earlier “current next choice” and A68 topology-pending paragraphs are historical and no longer a question. First Lesser preserves WB-D124 ordinary Bossing Lesser AND Greater entry, without a second generic charge. Next delegated integration is owned-rank controls and immutable offers; catalog/FP/source holds remain.

## A70 — owned-rank control resolved under delegation

WB-D148 adopts the future-offer owned-rank selector and safe edit boundary, including explicit first Lesser enable and higher-rank opt-in defaults. No more approval question for these controls. Next integration: family offering/rank choice and per-rank readiness; content/quotes and current-client proof remain unfinished.
