# Current-client evidence capture packet

Status: planned technical acceptance work,2026-10-10.46 named cases are NOT_RUN; live_trace_count is zero. This packet makes WB-A17 concrete without implementing a plugin adapter, using an account, sending review messages or changing gameplay rules. [Case ledger](TRACE_CASES_V1.json) contains14 Coffer,6 vendor,10 trade/shop/GE route,8 method and8 earning cases.

## Scope and prerequisites

Current source contracts describe plausible observation surfaces, not actual current-client receipts. A later authorized engineering session must supply a supported passive capture adapter, resolved RuneLite client version, game revision, adapter hash and a user-controlled legitimate test account. Real donation/trade attempts require separate appropriate authorization in that session. Do not collect login/session credentials or create automatic game input. No game client was started here.

Use unmodified normal-client routes first to establish observations. Restrictive cancellation/notice implementation remains disabled pending the exact policy gate. Successful event observation cannot authorize hiding Trade with or claim a popup intercepts all paths. The packet separates technical proof, run-state credit policy and reviewer acceptance; each has its own unresolved gates.

## Minimal capture envelope

[Schema](../schemas/trace_capture.schema.json) is versioned with this packet. Each envelope has case ID, origin LIVE_CAPTURE or SYNTHETIC, UTC capture time, source commit, resolved client version, game revision, capture adapter SHA-256, random account alias, account mode, policy gate, ordered events, limitations and assessment. No account username, login token, external upload endpoint or bank dump belongs in the envelope.

Each callback records a contiguous sequence number, monotonic observation time, optional game tick, session epoch, run state, event name, evidence kind and allowlisted source-specific payload. Keep raw numeric widget/menu/source IDs and actual initialized before/after values. Do not substitute display names for IDs or infer zero from unavailable data. Client callback order describes local observation order, not a signed server transaction timeline. Equal-time callbacks retain sequence order. Restart slices should be separate envelopes with linked session notes; do not invent timestamps to force one monotonic sequence.

Payload permits source IDs, item/quantity, XP/animation, menu/widget parameters, variable before/after, mapped location, actual game quote/balance, offer slot/state, task/boss/board IDs, initialized/known flags and a small target-item list. Optional redacted messages need manual review; raw chat/player names are excluded. Generic message text is not a server-authenticated receipt. Capture only the demanded/observed target items, not the full bank. Record knownness separately; an omitted snapshot never means empty ownership.

## Operator sequence

1. Select a NOT_RUN case, record the authored target/route and applicable source contract. Establish a negative control and initialized snapshot before any earning action.
2. Pin client/game/adapter/source versions. Write OBSERVE_ONLY policy gate for passive baseline work. Set a fresh random account alias; do not derive it from username or account hash.
3. Start a bounded local capture window, include explicit run-state/session changes, then perform the listed action manually if authorized. Preserve both callback order and independently observed game outcome; no game input automation.
4. End capture after the relevant UI/delivery window. State missing callbacks, unavailable state, other interactions, client/plugin configuration and any ambiguous order. A timeout never proves a missing receipt completed or an unknown GE/bank state empty.
5. Run the offline envelope validator. It checks structure, ordering and explicit origin; it cannot decide donation/kill ownership or declare detector acceptance. A valid envelope always returns ENVELOPE_VALID_REVIEW_REQUIRED with runtime_enabled false.
6. Review against the case's required observations and negative controls. Attach a redacted screenshot/video only when necessary to independently interpret game UI. Confirm versioned detector contract, paused/earning boundaries and duplicate-key behavior separately.
7. Write the actual result and hash of evidence into a new result ledger. Keep the planned case definition immutable. No live PASS may be entered without real capture and review; synthetic schema fixtures do not change live_trace_count.

## Priority capture groups and failure semantics

| Group | First positive/negative pair | Critical unresolved boundary | Failure interpretation |
| --- | --- | --- | --- |
| Coffer | C01 accepted transaction / C02 cancel / C03 unrelated disappearance | Actual quote/item/credit ordering; C07 headroom, C08 account modes, C09-C10 close/restart | Preserve mandatory obligation and evidence; no automatic reroll, waiver, double donation or extra Master debit |
| Vendor | V01 actual correlated visit / V02 wrong shop / V03 walk-by | Alternate dialogue routes, location/instance identity, stale callback | Proximity or unknown identity grants no unlock; visit does not itself pay an unlock cost |
| Trade/shop/GE | R01 outgoing route / R02 incoming / R03-R04 already-open/final screen | All actual routes plus specific policy acceptance | Preserve Trade with; never label partial interception complete or peer precedent approval |
| Method | M01 bronze mining / M02 wrong tool/swap | Effective tool and action/XP attribution, not mere inventory presence | Unsupported evidence grants no method credit and makes no violation accusation |
| Acquisition | E01 personal loot / unrelated ground item; E02 full inventory | Earning versus delivery, shared container/source and parent exchange | No possession/backfill or duplicated permanent/daily payout |
| CA/diary/Grand | E04 initialized CA then fresh task; E06 completion versus collection | Noncontiguous word IDs, bit31, current threshold, mixed active/paused aggregate | Baseline is not earned credit; mixed aggregate policy remains unverified |

## Coffer-specific interpretation worksheet

Keep each isolated selection transaction explicit: account/run/obligation, ownership knownness, committed demanded item/stack, Blessed exclusion, actual selected quantity, game acceptance and quote, initialized before balance, manual confirmation intent, item decrease, refreshed credit, closure/reconnect and concurrent action context. Use the game-displayed quote and actual credited delta; a market lookup cannot establish eligibility or current headroom. The minimum applies per unit, not total stack value.

Intent without both supported item and credit evidence remains attempted/pending. Item loss without Coffer context may be drop, bank, consume or trade. A credit increase without the matched demand could be another donation. Unknown ordering cannot prove a transaction happened while active. If positive earning is proven active and delivery arrives later, do not silently classify the earning by display time; otherwise preserve ambiguity pending contract review. Never ask a player to die to create headroom. No fixed cap or unsupported-account rule is guessed here.

Capture C05/C14 quantities and rounding independently, including allowed noted variants only if the current game accepts them. Repeated callbacks and two identical successive donations need distinct reviewed transaction boundaries; a hash of item+quantity alone conflates them. No dedicated server donation transaction ID has been recovered, so collision/dedup proof remains a live requirement rather than a fabricated globally unique key.

## Offline checks and dependencies

The validator uses jsonschema; this session installed jsonschema4.25.1 only in a scratch dependency directory. Future sessions may use an equivalent compatible environment. In this workspace: `PYTHONPATH=tmp/trace-validation-deps python3 docs/wheelbound/research/trace_schema_checks.py`. The meaningful fixtures prove a synthetic envelope cannot pass the default live-origin gate, unapproved identity fields are rejected, missing live versions fail, and sequence/time rules reject malformed captures. They prove no actual receipt semantics.

To inspect a future real file: `python3 docs/wheelbound/research/validate_trace.py <capture.json>`. `--allow-synthetic` is only for explicitly synthetic examples and never release acceptance. TRACE_SCHEMA_CHECKS.json records one positive synthetic and six negative envelope checks; zero live traces. No runtime flags or detector enablement are changed by this tool.

## Outstanding choices preserved

Q2 Coffer escape, Q6 exhausted ordinary pool, Q9 additional Bossing Challenge, Q10 rejected-Fate bounty disposition and conditional Q5 exact-policy fallback remain in the authoritative question register. CA-MIXED needs explicit aggregate credit semantics before payout. This capture plan adds no punishment, readiness gate, pause cost, card, price or automatic action. Actual failed/unavailable observations are limitations, not player misconduct.
