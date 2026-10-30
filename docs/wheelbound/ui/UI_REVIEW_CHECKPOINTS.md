# Review checkpoints

> **Current direction: [simple UI first](SIMPLE_UI_FIRST.md).** Use normal buttons and functional lists for initial testing; decorative styling and complex tree presentation are deferred. Earlier mockups are exploratory.

## Current approval — initial simple UI

The user approved the 36-screen plain-control prototype reviewed at design commit `b47919e0e2bf27cb151fd38be21562a2b1522ad1`, then instructed: “this will work for now, please create a backlog for builing this out etc and then do a pr and merege everything and document with an md file”. This explicitly approves ordinary buttons, lists/panels, initial routes/flows and a replaceable-view architecture, and authorizes merging PR #58 after checks. Prototype route navigation is not production gameplay implementation.

No additional visual approval is needed for those initial standard controls. Functional refinements remain provisional for user testing. Complex tree art, marquee Shop lights and older decorative compositions are **DEFERRED / NOT APPROVED FOR INITIAL BUILD**. Existing Master/GF4/B3 selections remain preserved. Backend prices/evidence and current-client requirements are not approved by a visual review.

## Historical / future styled concepts

The rows below retain the earlier artistic review history. They do not block ordinary implementation of the approved simple screens. See [handoff](UI_IMPLEMENTATION_HANDOFF.md) for current scope and execution order.

| Checkpoint | Artifact | Approval state | Allowed before approval |
| --- | --- | --- | --- |
| Existing Master | current WheelStyle/WheelPopup | Preserve, no redesign | Regression work only |
| Grand wheel | GF4 original/spec | Selected baseline D156 | Faithful compliant asset plumbing; deviations require approval |
| Boss wheel | simplified B3 stone/four blue gems spec | Selected baseline; original PNG absent in repo | Faithful plumbing; do not replace with ornate concept |
| First: Fate Tree | previews/fate-tree.svg + FATE_TREE_DESIGN | Awaiting review | Catalog, transform, rendering scaffolding and component tests |
| Hybrid Hub/sidebar | previews/hub-overview.svg | Awaiting review | Window/route/presenter foundations |
| Builder | previews/wheel-builder.svg | Awaiting review | Inventory projection and validation adapters |
| Offers/objective | previews/fate-offers.svg, objective-detail.svg | Awaiting review | State/pending gateway scaffolding |
| Grand flow | previews/grand-fate.svg | Awaiting review | Read-only seal/checklist adapters |
| Shops/bounties/Challenge | previews/shops-bounties.svg, challenge-ranks.svg | Awaiting review | Catalog projection, atomic gateway presenters |
| Risk/recovery and new wheels | previews/risk-recovery.svg | Awaiting review; final Taint/Punishment wheel art outstanding | Consequence models and disabled evidence states |
| Settings/help | previews/settings-help.svg | Awaiting review for main layout | Text, warnings and preference foundations |

For a later decorative redesign, review one material choice at a time. The initial plain UI can be built now; do not re-ask its approval. Record response, artifact hash/revision and scope here in the next reviewed change; do not rewrite historical gameplay decisions to call a proposal approved. If approval changes composition, revise dependent screenshots and task acceptance. Do not ask the user to approve backend evidence claims through a visual selection.
