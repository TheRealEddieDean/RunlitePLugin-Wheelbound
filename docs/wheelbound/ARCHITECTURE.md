# Architecture (design only)
Separate pure domain rules from RuneLite adapters and Swing UI.
Domain: WheelValidator/weighted selection; AssignmentGenerator; FP ledger; progression prerequisites; shop pricing; Defy state machine; bounty eligibility; Grand Fate checklist.
Adapters: account identity/config persistence; XP/quest/CA/drop/container/menu data; event attribution; versioned catalogs.
Presentation: sidebar navigation, canvas wheels, tree/builder/shop/cards and confirmations. UI emits intents, never directly credits FP.

Client reads on ClientThread, Swing changes on EDT; immutable snapshots bridge them. Generation tokens discard old account callbacks. Pure deterministic simulation uses same documented formulas eventually, but no runtime implementation authorized yet.

Reuse existing renderer/icons/eligibility cautiously; existing normal suggestion wheels do not supply transactional challenge completion. No external analytics or credential handling introduced.

## Adapter/domain contracts
ObservedEvent contains account token, session generation, evidence key, source and typed payload. Reject stale callbacks and check active mode before credit. Adapters attribute sources; domain never guesses from names.

Commands produce durable mutation or typed error: insufficient FP, unknown evidence, invalid wheel, blocked activity, unresolved catalog. Random provider injectable offline; production persists complete outcomes, not just seeds. Inject client's Gson, use scoped Filepath. Python economy/report tooling stays under docs and excluded from plugin JAR. No runtime implementation authorized.
