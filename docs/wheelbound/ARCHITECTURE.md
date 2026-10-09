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

## Policy boundary and observation-first adapters
Original PR blockers require injected Gson and no KeyboardFocusManager. Keep local spin input separate from OSRS commands. Every new restrictive adapter needs an explicit policy/coverage status; unavailable evidence must not become successful completion or speculative penalty. Trade-menu removal is prohibited, while alternate trade cancellation and shop/GE cancellation remain unapproved. Warning/log fallback is proposed and must not silently overwrite established gameplay intent.

Use getResourceAsStream for packaged images; use scoped Filepath for persisted files. Do not interpret the file-API rule as a ban on all java.io types used for resource streams. Current official standard build/example target Java 11. Never add native code, process launching, runtime executable downloads, credentials or server-action scripts. No mode implementation was performed in this audit.

