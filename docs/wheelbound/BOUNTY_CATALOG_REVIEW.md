# Permanent bounty review: Barrows batch and grouped targets

WB-A08 documentary review now covers all100 as conditional entries, plus the separate grouped-family candidate. This first historical batch covered24 of the100 entries. No entry has passed live validation. The [review ledger](research/BOUNTY_REVIEW_A08_BARROWS.json) pins the untouched input hash and retains unresolved drop-table/access/reward status individually.

## Concrete primary-source route

RuneLite LootTrackerPlugin at commit 42a6f17a6a2e8e478aa763890ecd0181a59dad38 handles WidgetLoaded for InterfaceID.BARROWS_REWARD, reads InventoryID.TRAIL_REWARDINV, and returns without recording when the container is null. This establishes a reward-container route rather than attributing chest rewards to a brother NPC death. Source: [pinned plugin](https://github.com/runelite/runelite/blob/42a6f17a6a2e8e478aa763890ecd0181a59dad38/runelite-client/src/main/java/net/runelite/client/plugins/loottracker/LootTrackerPlugin.java).

A reward widget/container snapshot is candidate evidence. It does not prove a distinct newly earned reward, account permission, or deduplication across reopening/relogin. Do not assume the Loot Tracker plugin must be enabled or that its emitted event alone provides Wheelbound's persistent receipt key. Reuse reviewed supported API patterns in a later authorized adapter; no production implementation is made here.

Required fixtures: active fresh chest reward; empty reward; widget before initialized container; repeated widget/container notifications; reopened chest; partial inventory delivery; item banked/withdrawn later; full inventory behavior; pause before earning versus pause before collection; relog/account switch with stale container; old identical item already owned; broken/noted/transformed variants; duplicate manual claim. Trace actual earning/collection semantics before choosing a receipt boundary. Ambiguous observations remain unverified and cannot penalize the player.

The 24 named intact item identities are source-checked at the earlier ItemID pin. This checkpoint does not independently prove current drop rates or legal accessibility. Their draft 100 FP quotes remain unapproved modeling values. Barrows completion-count evidence is not equivalent to an item acquisition receipt.

## Grouped family contract required by WB-D101

The next normative catalog must reserve one finite target for any third-age and one for any gilded, counting each family once. Freeze an explicit reviewed member-ID set with the catalog version; item naming substring matches are insufficient. Member eligibility must preserve the authored acquisition routes and canonical variants. One eligible fresh family receipt makes that target claimable once; later family members cannot produce a second permanent payout. Existing possession, GE/trade purchase and cosmetic conversion do not establish the qualifying acquisition.

Do not invent the full family membership or payout from memory. Review current item identities and reward routes in a separate batch. Keep the current H3 input unchanged and replace/review two candidates only in a new 100-target version after the whole list is reconciled. The historical user-approved examples establish family intent, not every item ID or numeric reward.

## Exact continuation

Review WB-B025–WB-B040 God Wars source/owned-loot routes next; then raid reward families, followed by mixed Slayer/Wilderness/exchange candidates. Track identity, source/access, acquisition boundary, variant canonicalization, duplicate key and FP status separately. Finish all 100 and grouped-family membership before marking A08 DONE.

## All-target conditional review checkpoint

All 100 experimental entries now have individual source-route/access/variant/duplicate/reward status in [the complete conditional ledger](research/BOUNTY_REVIEW_A08_ALL.json). WB-A08's documentary review is complete with explicit conditional entries; live enablement remains blocked. Source pin HEAD was rechecked and is unchanged. Do not describe these as 100 proven drops or feasible fresh-account tasks.

RuneLite LootManager reads LOOTTRACKER_ADD_LOOT script arguments (NPC, event, item, quantity), groups server script items and posts ServerNpcLoot. ServerNpcLoot itself exposes composition/items, not the internal event ID. Legacy NpcLootReceived is also constructed from coincident ground items; it is insufficient ownership evidence by itself. The later adapter needs reviewed source-specific event context and a durable key, not reflection into private fields. PluginLootReceived supplied by another plugin is not inherently an authoritative server receipt. Loot Tracker's pickpocket suppression also shows that NPC loot does not universally mean a kill.

Raids use separate reward widgets/containers and session-local chestLooted guards. Wheelbound requires persistent deduplication across restart, account swap, reopening and bank delivery. Gauntlet's exact adapter remains unresolved: no explicit branch was located in the inspected source, so the catalog's generic reward_chest label is not accepted as implementation evidence. Wilderness targets have alternate-source/access and permitted-risk gates; the display family is not permission to enter Wilderness. Abyssal dagger's Unsired exchange requires a separate provenance-aware route.

[Normative candidate v1](catalogs/item_bounties_normative_v1.json) keeps exactly 100 targets: 98 experimental single-item candidates plus two confirmed-intent families. WB-B093 and WB-B100 were replaced as a NEW delegated composition proposal (WB-D107), not a claim that those removals were approved historically. Families contain 23 third-age and 20 gilded candidate identities, source-checked from explicit symbols. Third-age transformed/duplicate numeric variants are excluded pending canonicalization; gilded furniture, quest objects, armour sets and collection/display items are excluded. No prefix matching runs in gameplay. Each member's actual qualifying reward route still needs review; family FP remains unset.

Clue rewards share TRAIL_REWARDINV with Barrows. LootTrackerPlugin distinguishes clue tiers through completion-message context before collecting that inventory. The container ID alone cannot tell Barrows from a clue, identify a fresh casket, or establish earning while active. Freeze tier/source context and candidate member set before recording the receipt. A later transformation must not create a second family claim.
