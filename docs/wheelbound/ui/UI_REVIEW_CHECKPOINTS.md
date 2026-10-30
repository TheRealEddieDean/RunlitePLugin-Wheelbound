# Review checkpoints

> **Current direction: [simple UI first](SIMPLE_UI_FIRST.md).** Use normal buttons and functional lists for initial testing; decorative styling and complex tree presentation are deferred. Earlier mockups are exploratory.

All new layout concepts are **PROPOSED / AWAITING VISUAL APPROVAL**, not approved runtime UI. Approval of this planning PR permits scheduling and Category A foundations; it does not approve every pictured layout. No final Category B merge without user approval of the reviewed artifact/revision. Keep unrelated backend work moving.

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

Review one material choice at a time. Start with Fate Tree composition, then Hub. Record response, artifact hash/revision and scope here in the next reviewed change; do not rewrite historical gameplay decisions to call a proposal approved. If approval changes composition, revise dependent screenshots and task acceptance. Do not ask the user to approve backend evidence claims through a visual selection.
