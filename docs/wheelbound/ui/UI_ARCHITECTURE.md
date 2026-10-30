# Sidebar and main-window architecture

Status: proposed implementation design; major layouts await Category B approval.

## Surfaces and navigation

Retain legacy normal mode as-is. New mode sidebar is a separate panel/card, not a rewrite of the existing normal selector. On supported opt-in it shows, in order: run state and Pause/Resume; FP/debt and spins; active obligation with progress and restrictions; primary permitted action; collapsible Grand checklist; Open Wheel and Open Wheelbound. Compact navigation links open the same Hub route. Unknown values read “Unavailable”, never zero. Paused progress is frozen and labeled. Warnings displace secondary decoration, not the active obligation.

One modeless, owner-bound, resizable JDialog contains header resources, navigation and a CardLayout body: Overview, Fate Tree, Wheel Builder, Shops, Bounties, Grand Fate, Settings/Help. Back retains section selection/search/pan and bounded scroll state, not a second window. Shops has Fate upgrades, vendors and Challenge targets as sections; Builder is the slice economy destination. Objective, offer and risky-action details use a shared inspector or in-window confirmation layer. Do not open one independent native window per item/card/node.

The main content window is an ordinary companion Swing window, not inserted into the OSRS game canvas. Existing Master wheel remains in its canvas popup with current rendering/interaction. Mode wheels later reuse the popup snapshot boundary; GF4 and B3 remain distinct themes. Hub navigation never redraws or re-randomizes committed outcomes. Clicking Open Wheel may hide the Hub's transient inspector but does not cancel the run or force client focus.

Initial window recommendation: 1040×720 logical units, minimum 760×540 bounded by available screen work area. At narrow widths use compact navigation and a detail drawer; Tree controls remain fixed while its world pans. At short heights all detail/forms scroll. These are engineering defaults, not RuneLite size limits. Sidebar follows host PluginPanel width and wraps without a horizontal scrollbar. No always-on-top, forced window focus, global KeyboardFocusManager, fullscreen or viewport replacement.

## Ownership and lifecycle

`WheelboundWindowController.open(Route)` runs on EDT, gets `SwingUtilities.getWindowAncestor(panel)` from a showing host panel, creates one modeless owned dialog if absent, selects the route and calls setVisible only for a user request. If no owner is available, retain sidebar error/retry rather than inventing an unrelated owner. Repeated open routes reuse the instance without toFront/forceFocus. Use normal native decorations initially for resize/close affordances. On close dispose dialog and release child subscriptions, timers, images and transient drafts; retain only bounded view preferences. No System.exit.

On logout/profile/account switch: advance UI generation, cancel pending view requests, clear old account data immediately, dispose confirmation and invalidate draft identity. Do not write draft edits to a new account. On shutdown dispose the controller and detach every listener; reopen builds a fresh panel. Closing UI never resolves an obligation or rolls back a committed action. During an in-flight transaction close is allowed; the service completes/reconciles durable outcome and the next open reads current state.

## Proposed contracts (not existing APIs)

| Component | Proposed API / responsibility |
| --- | --- |
| Window controller | open(Route), close(), dispose(); unique owned shell, route state, teardown |
| Snapshot presenter | publish(UiSnapshot); immutable identity/generation/revision, availability, lifecycle, optional FP/spins/objectives |
| Capability model | per action supported/disabledReason, independent from ownership and affordability |
| Action gateway | submit(CommandEnvelope) → asynchronous typed result; validated identity, expected revision, command ID and authoritative quote |
| Route controller | navigate(Route, optional stable entity ID); back stack capped at 20, no native-window stack |
| View subscription | AutoCloseable registration; callbacks on EDT, stale generation rejected |
| Catalog adapter | stable node IDs, read-only prerequisite expression + metadata; same data drives evaluator and renderer |

ClientThread reads only RuneLite state and produces immutable observations. Worker executor loads/commits storage and builds expensive layout/search indexes. EDT alone mutates Swing state. Never block EDT/client thread with Future.get, disk I/O, asset decoding or graph layout. Coalesce redundant snapshots; reject late results by identity/generation/revision. Reuse session safety concepts in AsyncRunSession/RunSession; those are not a complete UI subscription API. Do not expose mutable RunDocument or Client to painters.

A click creates one intent and disables that action while pending. Only successful durable acknowledgment updates purchase/FP/ownership feedback. Stale quote/revision yields “State changed; review again”, preserving user intent without auto-purchase. Lost acknowledgment yields “Checking saved result” using the same command ID, not a new debit. Unsupported services show disabled explanations. A technical preview must be labeled “Preview — gameplay unavailable”; no synthetic FP/spins masquerade as live state.

## Sources and verification

Repository WheelNameDialog, WheelPopup and WheelboundPlugin establish actual local integration anatomy. Official references checked for this proposal: [Java 11 JDialog](https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/javax/swing/JDialog.html), [Java 11 Graphics2D](https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/java/awt/Graphics2D.html), [RuneLite PluginPanel](https://static.runelite.net/runelite-client/apidocs/net/runelite/client/ui/PluginPanel.html). These support owned dialogs, Swing containers and transform-based rendering; they are not Plugin Hub approval of this proposed Hub. Verify against resolved project dependency before implementation.

Required lifecycle tests: open twice/one dialog; close/reopen; sidebar deactivate; 50 open/close cycles with no increasing subscriptions; account A late callback after B login; close during commit; shutdown with worker pending; owner minimized and removed; small screen and mixed-DPI monitors. Headless presenter/component tests plus real-window manual checks are both needed; headless success cannot verify desktop focus.
