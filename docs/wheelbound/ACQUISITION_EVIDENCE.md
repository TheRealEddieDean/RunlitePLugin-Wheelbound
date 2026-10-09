# Acquisition receipts and daily-board design

Status: DELEGATED technical/content specification, 2026-10-09. No live detector tests performed.

## Owned acquisition, not inventory movement
Normalize observations into a receipt containing account/run/session, source adapter, source NPC/chest/object identity, action/tick context, canonical item ID/quantity and event key. Raw server loot, reward-container refresh and later inventory delivery can describe one acquisition; merge rather than pay separately. Ground-item spawn or nearby NPC death cannot establish ownership. A menu click is intent, not success. A full-inventory drop can still be legitimate owned loot if its source receipt proves it; require a source-specific adapter rather than rejecting merely because inventory did not increase.

The active/paused and permitted-assignment flags are evaluated at the earning event. Paused acquisition never backfills on resume, even if the item remains available for OSRS use. Existing items, bank withdrawals, notes, charging, equipping, cosmetic conversion and purchases do not create another permanent acquisition. Normal canonicalization can unify valid note/charged variants for display, but cannot manufacture an eligible drop from an unrelated item transformation.

Reward exchange is a distinct adapter. RuneLite LootTrackerPlugin blob 586694b07c1b3445eabbf44d04956d3d60629a9a contains Font of Consumption region 12106 and a server-chat-triggered inventory collection for Unsired rewards; it is concrete precedent beyond NPC loot. Source confirmation still does not prove Wheelbound timing/parent provenance. An Abyssal dagger can need direct-owned-loot or reviewed Unsired reward exchange routes. Do not assume one NPC event covers both.

Proposed exchange provenance: require an active qualifying parent-item receipt, consume its quantity credit once at the verified exchange, then issue the output receipt. If indistinguishable stacks mix old/new inputs, track conservative eligible-unit credits rather than claiming physical item serial identity. Unknown stack histories stay unverified and never trigger an FP punishment. This is a local voluntary trust boundary, not server enforcement. Do not automatically declare every crafted/processed bounty to require such provenance without its authored definition; the rare-exchange rule is scoped to parent-drop transformations.

## Separate daily pool
Machine-readable catalogs/daily_bounties.json contains 18 NEW draft repeatable targets. Item names/IDs are source-checked; real method/access and receipt readiness remain gates. Quantity and FP are BALANCE_TBD. All runtime_enabled flags are false until validation.

| Family | Draft targets | Minimum check |
| --- | --- | --- |
| Woodcutting | Logs, oak, willow, maple | Real level, trees/access, positive gathering receipt |
| Mining | Copper, tin, iron, coal | Real level, tool/access, positive mining receipt |
| Fishing | Shrimp, anchovy, trout, salmon | Real level, correct equipment/bait/access, positive catch receipt |
| Combat | Raw chicken, raw beef, bones, cowhide | Reviewed owned loot and available permitted encounter |
| Smithing | Bronze bars | Smelting action/resource/access receipt; bank bars do not count |
| Crafting | Bow strings | Processing receipt and legal flax/access; purchased output does not count |

Draft quantities are 10–30 units and rewards 10–20 FP. These are repeatable item-bounty tasks, not replacements for the XP-based ordinary Fate system. They are optional and may require waiting for a matching permitted Fate; daily completion grants no off-task permission. The historical 100-item pool remains unavailable; this new daily list is not claimed recovered.

## Rolling schedule and claims
Retain the previously proposed rolling 24-hour epoch anchored at first board creation; no silent UTC-midnight switch. Seed each board from run/epoch/catalog version and persist its three distinct offers before display. Compute current epoch on login without creating an offline backlog. If fewer than three distinct legal targets exist, report board unavailable rather than invent target access or block ordinary play. A missing source/identity snapshot is unavailable data, not zero eligibility.

Each daily claim key includes immutable board ID and bounty ID. Active qualifying quantity receipts accumulated before expiry remain claimable afterward; no new receipts enter an expired board. One acquisition can meet permanent and active daily definitions only if each has its own once-only key. Manual claims remain manual. No dailies are required by progression gates, GE or Grand Fate readiness.

Clock rollback must not restore an older board or double-pay. Persist the greatest observed epoch for that run and explain an unavailable time state if current clock becomes inconsistent. This does not police the OSRS account or penalize pausing. Changing timezone has no effect on a rolling epoch; changing catalog version affects the next board, not current frozen offers.

## Fixture matrix
1. Three distinct eligible targets; same family can occur, same target cannot.
2. Clock rollback, exact boundary, forward jump and long offline gap; no board accumulation.
3. Paused loot/gather and pre-owned items generate zero eligible credit; resume does not backfill.
4. Last-before-expiry quantity remains claimable; first-after-expiry belongs only to a valid new board.
5. Duplicate loot/chat/container receipts merge once; duplicate claim cannot pay twice.
6. Reward delivered to bank, full inventory ground delivery and partial chest claim are adapter-specific cases.
7. Old input exchanged while active cannot manufacture qualifying rare-drop provenance under the proposed exchange rule.
8. Unknown account/metadata/detector yields unavailable state, not a false empty board or player penalty.
9. Daily absence and zero daily income never block normal progression (H3 minimal-bounty policy already exercises zero permanent bounty income, not daily implementation).

Daily schedules, quantities and clock policy are delegated proposals, not historically confirmed behavior. No daily income is included in the current H3 result totals.
