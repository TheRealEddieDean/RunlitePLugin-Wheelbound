# First Codex task after user authorization

This is a handoff prompt, not authorization to implement production code now.

“Work in TheRealEddieDean/RunlitePLugin-Wheelbound on wheelbound-mode or an isolated feature branch based on it. Never modify main. Read docs/wheelbound/DECISIONS.md, DEVELOPMENT_PIPELINE.md, STATE_CONTRACT.md, TECHNICAL_SPECIFICATIONS.md and OPEN_QUESTIONS.md first. Do not promote a PROPOSED/UNVERIFIED value to a confirmed rule.

Start with WB-I00 environment/build baseline. Use the existing Gradle8.10 wrapper and recommended Temurin11. Record resolved RuneLite client version, execute clean build, inspect the existing test reports and preserve all normal wheels. The earlier work environment failed wrapper download before compilation; do not mistake that for a source error or a successful build. Do not handle Jagex credentials or launcher tokens.

Once the user authorizes WB-I01, implement only pure account/run aggregate types, revision-checked commands, idempotent receipts, deterministic committed weighted outcomes and local preview wiring behind an optional mode. Preserve normal-wheel behavior. Cover five ordinary/three activities/24-total invariants, fixed enhancement tiers, per-activity lifetime duplicate counters, pause/no-backfill, account switch/stale callback rejection, crash/replay and saved outcome restoration. Use injected RuneLite Gson and supported scoped persistence; no fresh Gson, KeyboardFocusManager, native hooks or generated game inputs.

Do not implement shop/GE/player-trade restriction, Coffer settlement or unvalidated Grand Fate activation predicates until their gates are resolved. Leave unsupported catalog entries disabled. Report affected files, actual checks executed, failures and remaining limitations. Commit coherent changes only to the authorized feature branch; no merge or Plugin Hub submission.”
