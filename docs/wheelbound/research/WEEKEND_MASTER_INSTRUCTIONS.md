# WHEELBOUND — WEEKEND AUTONOMOUS WORK MASTER INSTRUCTIONS

> Paste this entire document into the EXISTING ChatGPT Work task, attach the existing design PDF, and provide the shared link to the original Wheelbound conversation(s). The user will be away for the weekend. This instruction supersedes conflicting earlier Work prompts only where explicitly stated; it does not supersede actual later user-approved design decisions.

## 0. Mission and operating boundaries

You are the autonomous lead game-design analyst, technical researcher, documentation editor, balance analyst, QA planner, and pre-implementation architect for **Wheelbound**, an optional RuneLite / Old School RuneScape account challenge mode.

**GitHub repository:** https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound

**Work branch:** `wheelbound-mode`

**Protected branch:** `main` — NEVER push to, merge into, or otherwise modify `main`.

**Canonical docs:** `docs/wheelbound/` on `wheelbound-mode`. Markdown in Git is the authoritative, version-controlled specification. The PDF is a derived publication, not the source of truth.

**Original conversation shared link:** [USER WILL PASTE HERE]

**Existing design PDF:** attached by user to this Work task.

**Primary outcome:** Return a technically credible, internally consistent, substantially complete Wheelbound specification with real executable simulation results, a verified RuneLite feasibility and policy audit, an implementation-ready backlog, and a specific development pipeline plan. **Do not implement production gameplay code or submit to Plugin Hub yet.** Documentation, reproducible simulation code, test harnesses, architectural sketches, CI/workflow proposals, and development pipeline setup instructions are authorized. Do not alter existing production plugin behavior.

The user is away and does not want routine prompts. Work deeply and independently within available tools and limits. Never claim you can bypass usage limits or automatically relaunch when a limit resets. Instead checkpoint often; if stopped, preserve work and make continuation deterministic. If an automation/scheduled resume feature is genuinely available and explicitly authorized, describe its limits and ask for any required setup; do not falsely promise auto-reset/restart.

## 1. Source recovery and provenance — DO THIS FIRST

1. Inspect the current repository, its branches, existing files, and recent commits. Verify `wheelbound-mode` exists; if not, report and use an appropriately authorized non-main branch rather than writing to `main`.
2. Try to read the user-supplied original conversation link and any other specifically supplied Wheelbound discussion transcripts. A shared ChatGPT conversation link may not be accessible to Work or may be a snapshot. Verify actual contents; do not infer access from a URL. If inaccessible, record that explicitly and ask for an export only at the final consolidated question checkpoint, continuing with available sources.
3. Read the attached PDF as a secondary source. It contains omissions, outdated decisions, and contradictions.
4. Reconcile the latest **explicitly approved** user decisions over older approvals, earlier assistant recommendations, PDFs, and generated summaries. Do not treat a recent assistant assertion as proof that the user approved a change. Track confidence and source provenance.
5. Record every decision in `DECISIONS.md` with unique stable ID, exact rule, source and date/link or transcript locator where available, status (`CONFIRMED`, `DELEGATED`, `PROPOSED`, `BALANCE_TBD`, `TECHNICAL_TBD`, `UNVERIFIED`, `OPEN`, `SUPERSEDED`), supersession history, rationale, affected files, and acceptance criteria. If not verifiable, explicitly mark it unverified; do not invent prior approval.
6. Treat the detailed context in this master instruction as a strong **reconstruction seed**, but distinguish historical confirmation from assistant-proposed balance values. If the transcript conflicts, latest explicit user instruction wins.
7. Before substantial new design, populate and commit a first complete, cross-referenced Markdown checkpoint. Provide the commit URL and ensure `main` unchanged. If GitHub write unavailable, create downloadable files and clearly say they are not committed.

## 2. Required file layout

Create and populate, at minimum:

- `docs/wheelbound/README.md` — navigation, source-of-truth policy, statuses
- `BLUEPRINT.md` — holistic product vision and core loop
- `GAME_RULES.md` — run lifecycle, rule enforcement, completion
- `DECISIONS.md` — authoritative decisions with provenance/supersession
- `PROGRESSION.md` — tiers, tree nodes, AND/OR gates, dependency graph
- `WHEELS.md` — master, boss, punishment, grand wheels; weights and probability
- `FATE_CARDS.md` — pools, offerings, targets, rewards, eligibility
- `FATE_SHOP.md` — purchase catalog and unlock interactions
- `ECONOMY.md` — FP and spin earning/spending, prices, balance targets
- `GRAND_FATES.md` — five endings, requirements, verification
- `DEFY_FATE.md` — defiance, taint, sacrifice, cleansing, blessing
- `PUNISHMENTS.md` — template library, eligibility, verification, anti-deadlock
- `BOUNTIES.md` — permanent, daily, CA, diary bounties
- `ACCOUNT_ACCESS.md` — NPC vendors, GE, bans, pardon, physical presence
- `UI_UX.md` — player journeys, components, accessibility, error states
- `PERSISTENCE.md` — save schema, migration, account separation, transactions
- `ARCHITECTURE.md` — proposed code modules and responsibilities
- `RUNELITE_INTEGRATION.md` — API events, observability, policy, uncertainty
- `EDGE_CASES.md` — systematic cross-feature failures and recovery
- `TESTING.md` — unit, integration, simulator, manual acceptance cases
- `SIMULATION_RESULTS.md` — methodology, reproducibility, data, sensitivity
- `IMPLEMENTATION_PLAN.md` — sequenced milestones and dependency plan
- `TASKS.md` — prioritized backlog, estimates/risks/dependencies
- `OPEN_QUESTIONS.md` — only genuinely consequential unresolved choices
- `WORK_STATUS.md` — checkpoint, last commit, workstream, next action
- `DEVELOPMENT_PIPELINE.md` — concrete repo/Gradle/Java/RuneLite/CI/dev-test plan
- `TRACEABILITY.md` — requirement → decision → implementation task → test mapping
- `SOURCES.md` — cited OSRS Wiki, RuneLite API, Jagex rules, dates and URLs

Add subdirectories such as `simulations/`, `research/`, `diagrams/`, `schemas/` as appropriate. Maintain useful README links, no empty placeholder documents presented as complete. Generated PDF should be derived from committed Markdown and clearly versioned with commit hash/date.

## 3. Reconstructed game design — preserve unless newer user-approved transcript says otherwise

### 3.1 Purpose, run lifecycle, and non-goals

- Wheelbound is an **optional** self-imposed challenge mode alongside existing normal Bossing and Skilling wheels in the RuneLite plugin; the published normal experience remains intact.
- States: Not Started, Active, Paused, Abandoned, Completed. Tutorial Island is safe onboarding; select a permanent Grand Fate using a one-time equal-probability five-way red/skull/spiked Grand Fate Wheel, then first ordinary Master Fate on mainland. No artificial minimum playtime, combat level, or timing for Grand Fate attempts; OSRS prerequisites still apply.
- Pausing is unrestricted and penalty-free, with no Assisted label or competitive enforcement. Paused drops, XP, quests, CA progress, bounties, etc. **do not earn retrospective Wheelbound credit**. Actual OSRS account progression remains, of course. On resume, take a fresh snapshot and avoid backfilling. Uninstall does not equal abandon; preserve state if feasible. Abandon is explicit with confirmation, never deletes actual OSRS progress.
- No competitive highscores or policing users for honesty; communicate observable vs self-attested actions and API limitations. Client-side restrictions cannot be guaranteed server-side.
- Persistence must survive logout/crash/restart and support account-specific runs, version migrations, deterministic RNG commitments and idempotent reward/transaction handling.

### 3.2 Master Wheel, slices, storage, probabilities

- At most **24 active slices TOTAL**, including all ordinary, Taint, and Sacrifice slices. No hidden special capacity.
- At least **five ordinary active slices** spanning **at least three distinct activities**. Starting five distinct slices: Mining, Fishing, Woodcutting, Combat bundle, Questing. No individually protected starting slice; replacement/removal allowed if invariant holds. Taint and Sacrifice do not count toward ordinary minimum.
- Activity must first be unlocked in Progression Tree, then bought/added to wheel for a separate inexpensive FP price. Duplicates unlimited within 24 total slots. Each slice has unique ID and independent enhancements. Duplicate cost escalates **per activity by lifetime duplicates purchased**; destruction does not reset the counter. Provisional Combat duplicate ladder: 100, 250, 500, 900, 1500, 2300, 3300 FP; balance TBD.
- Slice weight progression: 1.0x → 1.25x → 1.5x → 2.0x → 3.0x, with sequential per-slice enhancement tiers. **Each tier's cost is fixed and independent of past purchases on other slices.** Provisional prices 75/150/300/600 FP, subject to simulation. Enhancement prerequisites unlock via progression; enhancements persist when stored, vanish if destroyed.
- Selection probability is each active slice weight divided by sum of all active weights, including Taint/Sacrifice. Display true odds and before/after previews. Visual rearrangement is free and does not change odds.
- Fate Satchel stores inactive ordinary slices, initial 3 slots, then upgrades to 6, 10, 15 at increasing FP. Deactivation expensive but preserves individual upgrades; reactivation inexpensive; destruction free, permanent, no refunds. Never allow purchases that violate minimum active slice/diversity invariants.
- If forced Taint arrives at 24 slots, player chooses an ordinary slice to displace, storing it for normal FP if space permits or destroying it free; Satchel expansion may be offered. Do not create deadlocks. Immutable Taint and Sacrifice cannot be stored/destroyed except Cleansing removing Taint.

### 3.3 Normal Fates, XP, cards, bossing, questing

- Master Wheel spins produce assignments. Standard Skilling XP tasks scale by skill-level brackets and randomized within tier. Provisional XP bands: 1–29: 1k–3k; 30–49: 4k–10k; 50–69: 12k–30k; 70–79: 35k–65k; 80–89: 75k–130k; 90–99: 140k–250k; validate through simulation. Level brackets must avoid impossible/absurd objectives.
- Combat bundle allows player to choose Attack/Strength/Defence/Ranged/Magic **before XP target generation**; HP XP may count as permitted by-product. Define legitimate other by-product XP and attribution limitations. Initial unauthorized XP tolerance: 1,000 combined XP per Fate; crossing threshold causes FP penalties (may go negative) and Fate Audit. Explain what counts and what cannot reliably be detected.
- Fate Cards are permanent one-time shop unlocks with pools appropriate to activities. Lesser, Standard, Greater, Challenge and Wilderness where eligible. Initially **two card offerings**, later upgrade to three. Standard available by default. A **proposed resolution** from a prior assistant, not independently verified as user-approved: when only Standard is available, generate two independently randomized Standard offers with different objective targets; same card type can appear twice. Mark as `PROPOSED`/`DELEGATED` until provenance or user approval is verified. Never reduce initial offer count to one merely because pool is small.
- Bossing **does use Lesser/Standard/Greater Fate Cards**, according to a prior assistant's reconstruction of an October 1 approval, but verify against original transcript. Candidate sequence: Master Wheel selects Bossing → generate/show baseline kill count → offer eligible Bossing cards → player selects card → spin separate unlocked Bossing Wheel → complete random boss assignment. Lesser reduces KC while retaining same base FP; Standard normal KC/FP; Greater increases KC and FP. No player choice of boss, no boss rerolls. Earlier PDF 'no Bossing cards' language is contradictory and should not silently override a verified later approval. Validate card-order, boss feasibility and reward math.
- Questing wheel selection yields choices among distinct eligible quests (initial 1–3, upgrades 4/5; reconcile exact initial count from transcript). No impossible prerequisites; eligibility toggle only quests currently qualified for. Quest tier gates Low→Medium→Hard→Master→Grandmaster, gated by X quest Fate completions and FP, not instant FP bypass.
- Boss tiers and per-boss FP purchase: Easy 9, Medium 13, Hard 22, Elite 11, Master 6, Grandmaster/Raids 3 (ToA, CoX, ToB; variants not separate nodes). Confirm actual roster and classifications. X boss unlocks to advance tiers. CA-specific boss masteries are bounties, not mandatory randomized boss-CA Fate that could hardlock.
- Fate Audit monitors progress outside the selected Fate. Be explicit about unavoidable client telemetry gaps, handling of shared XP, quest rewards, bank actions, and penalty escalation.

### 3.4 Progression tree

- Polished RuneLite-themed Path-of-Exile-inspired top-down pan/zoom tree with lit root, free first node/tutorial, square icon nodes, hover details, AND/OR prerequisites, four branches: Skilling, Questing, Combat/Bossing, Fate Manipulation. Aim ~25–50 meaningful nodes; not filler.
- Skilling Tier I: Cooking, Firemaking, Smithing, Crafting, Fletching, Agility, Thieving, Runecraft, Prayer; unlock 5 of 9 for Tier II.
- Skilling Tier II: Slayer, Herblore, Farming, Construction, Sailing, Hunter; unlock 4 of 6 for Mastery. Validate current RuneLite/OSRS availability of Sailing and API.
- Mastery unlocks duplicate slices and weight upgrades; **no Favored Skill system**.
- Boss nodes purchasable independently within tiers; no forced boss ordering. Quest branch unlocks tier/choice count; Fate Manipulation unlocks card offers, wheel modifications and other approved mechanics.

### 3.5 Grand Fates

Five equally likely one-time permanent endings; players do not reroll their initial destiny:

1. **Inferno**: earn Infernal Cape.
2. **Fortis Colosseum**: complete Colosseum and obtain Dizana's Quiver.
3. **Radiant Oathplate**: finish five Yama challenge contracts awarding pieces of a purifying sigil, then cosmetically upgrade complete Oathplate with sigil/aether runes. **Verify actual current OSRS content, nomenclature, acquisition, and feasibility; don't fabricate mechanics.**
4. **Awakened DT2 quartet**: defeat Awakened Leviathan, Duke Sucellus, Vardorvis, and Whisperer; track four independent permanent checks, choose unfinished boss as active attempt.
5. **Master Combat Achievement tier**: verify correct CA tier, thresholds, and corresponding Ghommal's Hilt reward. Prior PDF's 'Hilt 4' may be wrong; likely Hilt 5 but **verify from authoritative sources**. Track legitimately active earned CA points; no need for a separate Grand Fate attempt for every CA.

Grand Fate attempts only between normal Fates, do not consume Master spins; failure ends individual attempt without resetting already credited checklist. No Grand Fate grants free normal unlocks. Grand Fate is the endgame win condition; never force a target number of hours.

### 3.6 FP, shop, access and economy

- FP earned from legitimate completed Fates, Bounties, punishments and approved actions. Negative FP allowed for penalties; discretionary purchases require sufficient FP; no cap, no automatic refunds, no forced spending order, and no mandatory daily bounty grind. All costs provisional pending executed simulation and playtesting.
- Initial illustrative FP rewards: Standard Fate ~100 FP reference; low skilling 30–60, mid 60–110, high 100–180; Combat 50–150; Easy Boss 80–150, Medium 120–220, Hard 200–350, Elite/Master 350–650; low quest 100–200, high quest 400–900. Scale for real time, difficulty, resource cost, and unlock access. These are not locked numbers.
- Illustrative prices: early skill 100–200, Tier II 250–450, milestone 500–1000, easy boss 150–250, high boss 750–1500, Lesser/Greater cards 150/350, Challenge 500, Wilderness 750, third offer 1000, Satchel expansion 300/750/1500, GE ~2500 plus Fate completion prerequisite. Model and adjust empirically; document changes.
- Shop tabs: Wheel Manipulation, Fate Manipulation, Defiance & Protection, Account Access, progressively surfaced. Fate Shop handles modifications; Progression Tree handles underlying unlocks. Show purchase odds/impacts and costs before confirmation.
- NPC shop states LOCKED, UNLOCKED, BANNED while Wheelbound active. **Physical visit and verifiable interaction** required to unlock or gamble; Account Access sidebar is browse-only. Price brackets escalate per shop category (General, Rune/Magic, Food/Drink, Weapon/Armour/Tools, Other); possibly first category shop free — verify original decision. 'Tempt Fate' 50/50 immediate unlock or BANNED until Pardon. Pardon returns BANNED→LOCKED, not directly unlocked, requiring subsequent pay or gamble. Prior assistant reports **unlimited very expensive fixed-price Pardon**, with no escalation or 3-use limit; verify original conversation before calling historically confirmed.
- GE is special permanent unlock after X completed Fates plus large FP; no Tempt Fate, no bans, no bypassing quest requirements. Determine policy-compliant enforcement and graceful fallback if menu intercepts are disallowed or unreliable.

### 3.7 Defy Fate, Sacrifice, Taint, Punishments

- Each completed ordinary Fate consumes one allotted Master spin. At zero, attempting spin displays 'Your allotted Fate has run dry' with options for Defy Fate or eligible Grand Fate; no automatic run failure.
- First Defy grants substantial spin refill, introduces one Taint slice and immutable Sacrifice slice at weight 1.0x, unlocks voluntary sacrifice, first free Blessed Item slot, and tutorial/cinematic. Subsequent Defies increase Sacrifice weight by +0.25x each.
- Taint added at odd Defies #1, #3, #5 up to three active. At later odd Defies, if three active, each existing Taint weight increases by +0.25x; if cleansed below three, add missing Taint before escalating. Taint removable only by Fate's Cleansing, infinitely purchasable at very expensive **fixed** FP price; no rollback of Defy count or Sacrifice weight.
- Sacrifice slice triggers forced Death's Coffer donation. Snapshot bank/inventory/equipment, offer ~12 diverse eligible candidate items, prioritize valuable eligible stacks (~100k+ total) but avoid impossible requirements. Non-stackable typically 1; stackable entire snapshotted stack. Protected Blessed Item types excluded. No automatic high-value item shielding except purchased/assigned Blessings. Blessed Item slots: first free on first Defy, two costly escalating upgrades (maximum three protected item types); blessing edits locked during forced sacrifice.
- **Death's Coffer item per-unit minimum 10,000 GP** (user correction); verify current game requirements and value semantics. GP→spins conversion is BALANCE_TBD. Voluntary sacrifice after first Defy allows eligible item/quantity, preview conversion, no wheel.
- **Critical technical integrity question:** merely observing item disappear from inventory does NOT prove a donation to Death's Coffer. Investigate observable widget/menu/dialog/container/chat/varbit/other signals and whether reliable verification is possible. Do not claim it is proven without a demonstrable supported signal. If impossible, design an honest, user-confirmed self-attestation fallback consistent with voluntary self-challenge, not fake automated detection.
- Taint landing opens a separate Punishment Wheel of ~12 eligible outcomes sampled from ~50 curated punishment templates. No rerolls. Punishments should be miserable but feasible, measurable where possible, dynamically scaled, persistent through relog, and pay small FP. Repeated violation escalates until legitimate completion resets streak. Avoid hardlocks and unsafe/plugin-policy-violating restrictions.
- **Reject Fate** is distinct from Defy: reject an active ordinary Fate, pay substantial FP penalty (negative allowed), consume the ONE spin allocated to that Fate (no extra spin), forfeit reward, mandatory Punishment Wheel, cannot reject punishment, no Grand Fate rejection. At zero after punishment, show zero-spin choices. Persist rejection atomically.

### 3.8 Bounties

- ~100 curated permanent item bounties by rarity; 3 daily item bounties with 24h refresh in separate pool; Combat Masteries for all CAs for each boss; achievement diary regional/tier bounties. Manual claims only for legitimately active credited events, not paused progress. Dailies never required for progression. Clarify offline detection, duplicate claims, dropped items, and quest-locked access.

### 3.9 UX

- Preserve normal RuneLite sidebar look and polished custom RuneLite-native UI; don't require a third-party UI toolkit. Boss icons on wheel, central spin button actually works, consistent dialogs and responsive layouts.
- Distinct Master, Boss, Grand Fate, Punishment wheels; Grand Fate red/skull/spikes. Fate Card selection animation; Progression Tree gold unlocked paths; Fate Shop, Account Access, Satchel, Bounties, Fate Audit, status and progress.
- Accessible contrast, keyboard interactions where feasible, clear odds and transaction confirmation, error recovery, onboarding, tooltips, persistent action states.

## 4. Research and policy gate — REQUIRED

Research current official RuneLite API/source and Jagex/RuneLite Plugin Hub policies. Cite dated sources and distinguish supported API from guesswork. Build a feature-by-feature matrix with: required signal, RuneLite events/APIs, evidence, limitations, confidence, Plugin Hub compliance risk, prototype/test procedure, and fallback.

Prior research suggested:
- Local UI, wheel, FP, shops, and progression logic feasible.
- XP, quests, and CA signals available but attribution requires care.
- Awakened DT2 and Master CA tracking promising, verify exact APIs/varbits and current rules.
- Vendor physical visits require correlated NPC/shop interaction, not proximity alone.
- Shop/GE menu interception might be technically possible but needs **specific policy review** and coverage testing; don't implement prohibited manipulation.
- Removing 'Trade with' options was flagged as **explicitly prohibited** under published Jagex guidance. Verify the actual current wording and design a compliant alternative, such as advisory UI and self-imposed restrictions.
- Death's Coffer donation verification remains unresolved.

Do not ship or propose prohibited menu manipulation. Clearly separate UX warnings, passive detection, and actual blocking. If a design mechanic depends on an unverifiable event, redesign honestly and mark user-approval questions only if it materially changes the game identity.

## 5. Deep simulation and balancing — ACTUALLY EXECUTE

Write reproducible simulation code, preferably in a standalone documented `simulations/` directory. Run **at least 10,000 full account progression trials** across diverse player strategies, RNG seeds, progression paths and risk tolerances, if compute/time permits. If fewer run, disclose exact number and why; never invent runs. Include deterministic seeds, configs, runnable commands, and machine-readable outputs.

Model: finite spins, time/XP target distributions, level-dependent FP rewards, shop purchases, unlock tree gates, duplicates and slice weight upgrades, Satchel storage, minimum 5/3 wheel invariant, probability of getting desired boss/quest/skilling Fates, quest/boss eligibility, Defy cadence, Taint/Sacrifice frequency, cleansing, Pardon, GP→spins assumptions, bounties, and Grand Fate access. Treat unknown player skill/time and unavailable OSRS APIs as explicit assumptions, not measured facts.

Simulate strategies such as balanced progression, combat rush, quest rush, resource-constrained, risk-averse, unlucky RNG, exploit-seeking and minimal-bounty. Report median/p10/p90, distributions, deadlock frequency, FP debt, time estimates as **assumption-dependent**, failure modes, sensitivity analysis and before/after tuning. Do not tune to a fabricated universal target playtime. Use Monte Carlo and analytical cross-checks. Report where full progression simulation is not credibly possible due to missing assumptions.

## 6. Architecture, QA and implementation-ready handoff

Study the actual repository code, build scripts, plugin package structure, current Java/Gradle/RuneLite dependencies, CI, test framework, persistence and any existing Wheelbound mode implementation. Preserve existing Bossing/Skilling behavior. Propose modular architecture and interfaces for state machine, RNG, wheels, Fate generation, rewards, telemetry, account storage, shop, progression, UI and events. Include explicit client trust boundaries, transaction IDs, migration, data loss recovery, and replay tests.

Create `DEVELOPMENT_PIPELINE.md` for immediate next-session implementation: branch policy (`main` stable, `wheelbound-mode` feature branch), development environments (IntelliJ, Java version dictated by actual repo/RuneLite toolchain, Gradle wrapper rather than arbitrary versions), local build/test commands validated against repo, lint/checkstyle, unit tests, deterministic simulation tests, CI via GitHub Actions with minimal permissions, artifact strategy, safe secret handling, and staged Codex tickets with exact acceptance tests. **Do not assume Java 17/Gradle 9.6 is compatible without checking actual project and RuneLite requirements.** Avoid credentials, launcher tokens or any insecure Jagex account access. No production feature code until explicit approval.

Produce an ordered implementation plan with milestones: repo/build health, persistence/state machine, deterministic wheel engine, fate/card generation, progression/economy, RuneLite event integration, shop/access policy-safe behavior, defiance/sacrifice, UI, testing/telemetry, packaging/Plugin Hub review. Every ticket should identify dependencies, affected modules, done criteria, and tests. Provide a minimal vertical slice to implement first.

## 7. Independent execution, approvals, and checkpoints

- Work continuously **while the Work session is active and within available quotas**. Do not ask 'continue' after each section.
- Do not ask routine design questions. Decide reasonable implementation-neutral details under delegated authority, label `DELEGATED`, document reasoning, and test assumptions.
- Do not override explicit locked user decisions. If a consequential unresolved question remains, record it in `OPEN_QUESTIONS.md` with options, recommended answer, impact and what work can proceed independently. Batch questions for user's return.
- If conflicting sources cannot be resolved, prefer `UNVERIFIED` and conditional specs, not fabricated certainty. Preserve a decision supersession log.
- Commit coherent batches frequently to `wheelbound-mode` and record commit SHA and links in `WORK_STATUS.md`. Before every commit, check branch and diff; never touch `main`. If unable to commit, write downloadable files and report clearly.
- Keep `WORK_STATUS.md` updated with last completed step, exact current status, last successful commit, blocked questions, next executable tasks, tools needed, and a copy/paste **resume prompt**. Prefer small atomic commits so session interruption loses little.
- If interrupted by quota, session end, authorization or tool limits, stop honestly; do not claim background execution or auto-restart. Save best possible checkpoint before interruption where possible.
- Periodically run consistency checks: invariant tests, link validation, requirements-to-tests coverage, cross-document contradiction scan, citations and proof of actual simulation execution.

## 8. Final acceptance criteria and weekend return briefing

Before declaring completion, deliver:

1. GitHub links to committed populated `docs/wheelbound/` files and commit SHAs on `wheelbound-mode`, with `main` confirmed untouched.
2. A decision register with source provenance, supersessions, and no silently invented confirmations.
3. Detailed gameplay rules, economic parameters and justification, interactions and edge cases.
4. Reproducible simulation source, exact executed counts, seed/config and summarized results.
5. Dated RuneLite/Jagex/OSRS technical and compliance research, with unsupported features and fallback designs highlighted.
6. Architecture, data schema, state machine, test plan, acceptance criteria and Codex-ready ticket backlog.
7. `DEVELOPMENT_PIPELINE.md` with verified environment/build commands and recommended first vertical slice.
8. Updated polished PDF generated from current Markdown, identified by commit SHA.
9. A **single consolidated, prioritized list** of major questions requiring the user's approval, ideally only those that block safe implementation.
10. A concise 'Monday handoff': what is ready to code, what must be verified first, and the exact first Codex task.

Do not falsely claim full completion, GitHub commits, PDF generation, simulations, transcript access, API verification, or an auto-resume capability without evidence. If time or quotas limit scope, prioritize durable committed docs, contradiction resolution, technical policy feasibility, and the development-ready backlog over decorative formatting.

## 9. Immediate next actions

1. Verify GitHub repo, branch and access; read existing files.
2. Attempt original conversation link; document what was actually accessible.
3. Reconcile the PDF and latest explicit decisions; create authoritative `DECISIONS.md` and populated specs.
4. Commit initial recovery checkpoint **before** deep simulation.
5. Audit policy/API feasibility; flag impossible mechanics and design alternatives.
6. Execute reproducible simulations, balance provisional numbers, commit code and results.
7. Finish architecture, tests, Codex backlog and development pipeline.
8. Generate PDF from committed Markdown, provide final return briefing.

**Remember: the user expects 'lock that in' to result in a real Markdown update and Git commit, not merely a conversational acknowledgment.**
