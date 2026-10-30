# Current direction: simple UI first

User instruction, 2026-10-29 America/Chicago: “lets just go super simple for the UI for now just normal buttons until i get a hold of it”; “things may change after i test it some”.

This direction supersedes the decorative and visual-polish requirements in this packet for the initial usable version. The first UI is provisional and should be easy to adjust after hands-on testing. Existing gameplay decisions, service/evidence gates and Master/GF4/B3 selections remain intact.

## Initial presentation

- Standard RuneLite/Swing buttons, labels, panels, tabs and lists. Inherit the normal host fonts, colors and focus behavior; no custom skin or decorative window chrome.
- Ordinary text Shop button. No marquee, flashing bulbs or chasing lights.
- Bounties: simple rows with item icon, name/description and Fate Points. Missing reviewed rewards remain unavailable; automatic reward semantics are unchanged.
- Progression: start with a readable upgrade list or basic hierarchy, prerequisite explanations, cost/status and an ordinary Unlock button. A custom Path of Exile-style graph, elaborate clusters, pan/zoom polish and path animations are deferred until the basic flow is usable. Keep the production graph as rule authority so a richer view can be added later.
- Sidebar contains essentials; use a plain shared window where extra space helps. Prioritize functioning navigation, real-state display and clear actions over the mockups' exact composition.
- Use actual services when available. Show unsupported actions disabled with explanations; do not introduce placeholder balances, fabricated prices or pretend working gameplay.

## Backlog interpretation

The 43 IDs remain traceable; do not delete task history or treat deferred appearance as completed. Deliver ordinary controls and real-state integration first. WB-UI-10 means minimal reusable host styling, not a custom theme. WB-UI-18–20 provide graph/catalog rules; WB-UI-29 can supply the first simple progression list. Its prerequisite explanations may be factored out of WB-UI-26 without waiting for the custom canvas. WB-UI-21–27/30/51 graphical presentation and performance work is deferred where unnecessary for that list. WB-UI-35/36 preserve approved wheel directions but do not block ordinary navigation/status screens. WB-UI-46 final special-wheel art is deferred. Do not let visual-only dependencies block independent standard-control screens.

Existing major mockups and the later interactive tree/Shop/bounty exploration are reference ideas, not the initial implementation target. Final aesthetic choices remain open. No additional visual-approval ceremony is needed for routine standard buttons, labels and functional scaffolding within the already authorized scope. Do not interpret this as approval of the prior elaborate Category B designs or permission to merge the planning PR itself.

## Build for the later UI upgrade

User clarification: build everything with the intention of upgrading the UI later. Plain controls are the first presentation, not throwaway application architecture.

- Keep gameplay rules, persistence, purchases and evidence processing outside Swing views; expose immutable presentation snapshots and explicit action callbacks.
- Implement each system as a replaceable section panel behind shared navigation. Keep routes and stable entity IDs independent of layout, labels, icons and control type.
- Centralize the small set of spacing/font/color defaults and common button, resource, objective and confirmation components. Initially these use ordinary host styling; later skins should replace presentation without duplicating behavior. Do not build a large speculative theming framework first.
- Make the initial progression list and future graphical tree consume the same catalog, prerequisite explanations, ownership and command gateway. Replacing the list with a canvas must not change unlock rules or saved state.
- Keep bounty row data independent of row rendering, and shop product/action data independent of the plain Shop button. Art, animation and richer detail layouts can be added later without rewriting rewards or transactions.
- Preserve accessibility, resizing, lifecycle cleanup and reduced-motion support as shared behavior. Prefer focused presenter/service tests that survive visual changes.
- Do not bake screen coordinates, widget references or appearance choices into gameplay saves. View preferences are separate and optional.

Acceptance for initial screens: they work with standard controls, and a later visual replacement can reuse the same state/actions without a gameplay rewrite or save migration. Avoid temporary UI-only implementations that bypass the real service boundaries.

## Iterate after testing

Separate presenters/game rules from Swing views. Use shared ordinary components and stable route/entity IDs so layouts and labels can change without save migrations or changing gameplay. Build the smallest useful connected screen, let the user test it, collect concrete feedback and revise in small increments. Avoid hardening speculative layouts before that feedback.

Validation still covers lifecycle, account isolation, authoritative state, safe transactions and legacy wheel regressions. Styling can change; correctness and supported evidence remain required. No supervisor scripts, schedule or active task ownership changes are requested.
