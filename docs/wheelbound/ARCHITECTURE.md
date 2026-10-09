# Architecture (design only)
Separate pure domain rules from RuneLite adapters and Swing UI.
Domain: WheelValidator/weighted selection; AssignmentGenerator; FP ledger; progression prerequisites; shop pricing; Defy state machine; bounty eligibility; Grand Fate checklist.
Adapters: account identity/config persistence; XP/quest/CA/drop/container/menu data; event attribution; versioned catalogs.
Presentation: sidebar navigation, canvas wheels, tree/builder/shop/cards and confirmations. UI emits intents, never directly credits FP.

Client reads on ClientThread, Swing changes on EDT; immutable snapshots bridge them. Generation tokens discard old account callbacks. Pure deterministic simulation uses same documented formulas eventually, but no runtime implementation authorized yet.

Reuse existing renderer/icons/eligibility cautiously; existing normal suggestion wheels do not supply transactional challenge completion. No external analytics or credential handling introduced.
