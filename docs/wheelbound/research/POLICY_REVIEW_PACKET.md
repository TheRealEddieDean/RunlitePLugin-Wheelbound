# Wheelbound restrictive-control review brief

Prepared2026-10-10. Draft for a later explicitly authorized review; NOT SENT. No approval, maintainer response or new gameplay decision is claimed. The exact future implementation and captured route evidence still need review.

## Request to be reviewed

Wheelbound is an optional local challenge alongside the existing wheels. Active-run state can mark an NPC vendor Locked/Banned, the GE Locked, or player trading restricted. The requested behavior retains native options and displays a local Fate notice when the player attempts a restricted route. The proposed implementation would consume only a supported observed menu/widget action before vanilla processing, then queue a dismissible local notice. It does not invent server-side account bans.

Local state, FP purchases and random wheels remain plugin UI actions. Every actual OSRS action—including shop interaction, trade, movement and Coffer donation—is performed manually. Pause immediately disables restrictions without a label or penalty; switching accounts/shutdown clears transient notices. Dismiss closes the notice and sends no request, replay, automatic decline or unlock.

Proposed copy: “Fate has sealed player trading.” For a Banned NPC: “Fate has banned you from this vendor. Fate's Pardon restores them to Locked.” Locked vendors use different wording. There is no player-specific Pardon, attempt fine, public chat message, focus-stealing dialog or player identity upload in this proposal. Exact trading-unlock rules remain open; this review must not fill them implicitly.

## Current published boundaries and evidence limits

Rechecked the [Jagex client guidelines](https://secure.runescape.com/m=news/third-party-client-guidelines?oldschool=1) and [RuneLite rejected features](https://github.com/runelite/runelite/wiki/Rejected-or-Rolled-Back-Features) on2026-10-10. Jagex expressly excludes removing/reordering player options, including Trade with, and can treat similar features as unacceptable. Preserving text/order alone therefore does not prove consumption is accepted. RuneLite excludes global focus-manager use, fresh Gson/OkHttp instances, runtime subprocesses/reflection and unsupported action generation; its rules also distinguish sensitive APIs and grandfathered examples. Use host services and passive callbacks; obtain an exact review for controls.

The original Wheelbound PR16702's documented Gson packaging and KeyboardFocusManager blockers were corrected in its accepted revision; that acceptance is not approval of later Wheelbound restrictions. [Current baseline](BUILD_BASELINE.md) finds host Gson injection and no fresh Gson/focus-manager references in inspected production source. A successful Java build is not a Hub packaging scan or gameplay-policy ruling.

The existing [trade precedent audit](../POLICY_AND_FEASIBILITY.md) identifies connected outgoing/incoming consume-and-message code in Hub-pinned Bronzeman Unleashed. Its commented trade-window handler does not prove final-confirmation coverage; a local message precedent does not approve this exact popup. Do not copy grandfathered persistence/network patterns or infer blanket permission from another plugin.

## Behavior and route matrix for the eventual reviewer

| Proposed surface | Requested behavior | Evidence to attach later | Open limitation |
| --- | --- | --- | --- |
| Player Trade with | Preserve menu text/order; consume supported restricted attempt; local notice | Captures R01 outgoing and R05 context/repeat/plugin combinations; exact callback code | A click handler covers only its observed route |
| Incoming request | Same active-run rule, separately evaluated path | R02 request acceptance and manual alternate routes | Outgoing precedent does not establish incoming completeness |
| Already-open trade | Review first/final acceptance behavior separately; no automatic decline packet | R03/R04 screen/widget/order captures | Final Accept coverage not established; activation/open-state policy needs explicit handling |
| NPC shops | Distinguish player trading from NPC routes; active Locked/Banned state; physical vendor identity | V01–V05 identity/access plus R08 quantity/X/repeat routes | Names/proximity alone fail; alternative dialogue paths can bypass one action handler |
| GE | Review new buy/sell/price/quantity/final-confirm paths individually | R06–R09 initialized offers, controls and server-fill lifecycle | Cannot cancel existing server fills; unknown initial EMPTY events are not empty account state |
| Notice/input | Local dismissible notice, appropriate UI thread, no whole-game input capture | UI/thread code, repeated-click/dismiss/pause/account-switch demonstrations | Modal/focus capture is not approved by the local-message precedent |

A review request should provide proposed code paths, native menu before/after examples, action/opcode/widget mapping, negative controls, route omissions and exact pause/account-switch behavior. The [46-case capture packet](TRACE_CAPTURE_PROTOCOL.md) supplies named NOT_RUN cases; there is no current live evidence to attach.

## Questions for exact future review

1. Is state-dependent consumption of an otherwise retained Trade with attempt acceptable for this voluntary challenge, including its incoming acceptance route?
2. Which already-open and final-confirmation cancellations, if any, are acceptable without automatic game actions or native menu changes?
3. Are the separately documented Locked/Banned NPC-shop and GE controls acceptable, and which widget/menu routes require additional review?
4. Is the proposed nonmodal local notice acceptable without stealing focus or capturing unrelated game input?
5. What source/route demonstrations are required beyond this existing randomizer's accepted packaging fix?

These questions are a draft review agenda, not messages already sent. If a required control is rejected or unavailable, keep the feature gated and collect the actual reason. Q5 then requires a material user decision about fallback; do not silently replace locks with warnings, add attempt penalties or describe partial coverage as comprehensive enforcement.

## Runtime versus offline tooling

Wheelbound production remains Java using supported RuneLite services. Python simulations, JSON capture validators, Groovy diagnostic init scripts and YAML proposals in documentation are offline/preparation tools; they are not plugin-runtime features and must not be vendored into the Hub plugin or invoked from its runtime. No game-content practice simulator, automatic chat input, menuAction, setCanSendPackets or generated donation is proposed. Passive observation of existing events must not be confused with invoking a server-action script.

## Submission status

NOT SENT; NO RESTRICTIVE CODE IMPLEMENTED; NO LIVE CASES EXECUTED; NO NEW POLICY APPROVAL. Preserve the approved mechanics and evidence gaps in Markdown until a later authorized implementation/review session can provide the missing concrete evidence.
