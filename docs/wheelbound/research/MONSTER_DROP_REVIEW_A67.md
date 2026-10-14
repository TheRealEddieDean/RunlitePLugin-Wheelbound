# Full supplied-monster drop review — A67

User requested every monster in the supplied collector output be reviewed after Killerwatt → Air diamond was missing. Starting branch head `bf213a6bc4f2c34a6693fc42bbaebcae3ffdc12a`; guarded owner claim `3f849d6d458afff67e2d3f12734cdf69bc320bc0` (resolve exact SHA from branch history). This is a documentation/source review, not production gameplay or final catalog admission.

## Result

Reviewed **1,437 corrected monster captures / 31,757 captured rows / 1,803 distinct item names**. The previous monster retrieval whitelist is replaced by complete per-item judgements and a per-page ledger assigning every row exactly one disposition. The boss candidate scan is unchanged.

[Monster hunt table](../MONSTER_CHALLENGE_HUNT_TABLE.md) now lists **327 tiered rows**:18 prior source-reviewed contexts plus309 candidate source/item/rate groups across 156 candidate source pages. This replaces90 listed rows. Relative to the prior72 monster candidates: **252 new groups**, **15 removed groups**, **57 retained groups**. Candidate status is essential: this is not327 eligible creatures or runtime-ready hunts. Same-item extra variants/rates remain separate contexts, not extra random creature weight.

| Tier | Total monster rows |
| --- | ---: |
| Lesser | 22 |
| Standard | 34 |
| Greater | 164 |
| Superior | 107 |

Killerwatt → Air diamond, Molanisk → Earth emerald, Infernal Mage → Fire ruby and Sea Snake Hatchling/Young → Water sapphire are all captured at1/64, Standard. Other additions include narrow-source Slayer boots/light-dark mystic pieces, defenders, obsidian gear, skeletal/rock-shell/spined components, and upgrade items. Low GP value is not an exclusion. New rows still need exact-context access, identity, personal acquisition and FP authoring before admission.

## Review rules and exclusions

WB-D144/145 apply: Lesser25≤D≤50; Standard50<D≤125; Greater125<D≤750; Superior750<D≤5000 for exact D=1/p. Ignore roll multipliers for placement only. Keep original rows, quantities, sections, annotations, repeated rolls, alternatives and conditions. No per-roll-to-per-kill shortcut or favorable alternative selection.

The complete supplied item inventory was inspected for equipment/component/function leads. Every item receives an explicit persisted judgement, including generic supplies/equipment/resources and quest/activity loot that are excluded from the unique-hunt catalog. This is a bounded judgement over this archive, not a universal heuristic or future silent default: the tool pins the input catalog hash and requires a new inventory review if it changes. Merely appearing on one captured page does not prove in-game exclusivity. Cosmetics, pets, keys and special-activity materials that could matter are separately held for significance/context review, rather than silently adopted.401 such rows are visible in the monster table appendix;171 signature rows with unresolved/approximate/conditional rates are held separately.

Source categories and introductions reveal League/discontinued/Deadman captures even when the creature name lacks that mode. These rows are audited, but not current ordinary hunts. Shared Superior Slayer tertiary Dust/Mist battlestaves are excluded from specific-creature uniques, although source-specific ordinary-table staff drops remain candidates. Imbued heart/eternal gem, shared RDT, clue/ensouled/fossil outcomes and generic supplies do not become specific-source uniques. Impling loot, pickpocketing and dialogue/Hunter methods are not monster-kill receipts. Normal superior Slayer creatures remain source candidates with their task/spawn constraints unresolved; they do not gain task-independent access.

Encounter-dependent minions/variants are source records, not approved independent creature unlocks. Original source URL plus item/tier/exact rate forms a display group; repeated identical-rate rows retain every original row number/section. Distinct rates are not collapsed. Existing A60 wrong-entity/encounter-component overlay is unchanged. No new unlock topology, reward schedule, task-continuation waiver, cosmetic eligibility or paid boss/monster card family is adopted.

## Row conservation

| Disposition | Captured rows |
| --- | ---: |
| OUTSIDE_BRACKETS_GUARANTEED | 1,186 |
| EXCLUDED_NON_SIGNATURE_LOOT | 19,003 |
| OUTSIDE_BRACKETS_SCALAR | 3,992 |
| CANDIDATE_FIXED_TIER | 318 |
| EXCLUDED_SHARED_SUPERIOR_TABLE | 152 |
| HELD_SIGNIFICANCE_OR_ACTIVITY_CONTEXT | 401 |
| HELD_NON_KILL_ACQUISITION_METHOD | 391 |
| HELD_RATE_CONTEXT | 171 |
| EXCLUDED_ALTERNATE_MODE | 5,925 |
| PRIOR_REVIEWED_ROW | 18 |
| HELD_DISCONTINUED_OR_EVENT_SOURCE | 200 |
| **Total** | **31,757** |

## Provenance, cached-source and parser checks

Original supplied archive SHA256:`e00babd58e09c047f68df94625cd5b4e77e119952d52c0d0c50b53a7d2b60309`. All1,437 monster capture receipts identify `network_html`, `fixture=false`. Captures span **2026-10-14T15:02:46.545609+00:00 through 2026-10-14T16:47:51.811345+00:00**; per-source server Last-Modified values and fetch timestamps are in the audit. These are capture dates, not a claim that every source remains current today. No new scrape occurred and no synthetic fixture was promoted.

Every monster's cached HTML exists and its newline-normalized text hash matches the catalog source hash (the original Windows CRLF byte/hash difference was already documented in A58). Every captured drop payload equals its SQLite record, with row counts/indices checked: **36,068 source assertions**. This corroborates capture/serialization consistency; it is not independent proof that the Wiki itself has no errors or every condition was parsed correctly. Raw rates and context remain authoritative evidence. The new index preserves source introductions (first3 paragraphs), access infobox fields, all captured footnotes, reported NPC IDs, warnings and capture receipts for displayed monster sources. Full source/cache remains in the supplied archive.

**1,038 monster captures have parsed rows;399 have `NO_SUPPORTED_TABLES` and zero captured drops.** Zero captured rows is not proof that the creature has no drops. No capture warnings were populated. The supplied crawl remains PARTIAL with the previously recorded12 collection errors/11 URLs; A58 covers those errors, not a new network attempt. A60 still separates161 bosses,855 non-creatures,32 encounter components and444 unresolved pages: unresolved classifications are not silently counted as reviewed monsters. This review covers every corrected monster record we have, not every monster in OSRS or missing/unparsed pages.

The boss table retains its156 groups (26 prior/130 candidates); only explanatory scope text changes. Historical v1 JSON/CSV/FP are unchanged. V2 FP remains TBD. All runtime flags remain false.

## Per-monster coverage ledger

The [machine audit](MONSTER_DROP_AUDIT_A67.json) contains every item judgement, every monster URL/hash/timestamp, and complete row-index groups by disposition. The table below makes empty/held/mode-excluded pages visible. “Audited” means reviewed captured data; it does not admit the source.

| Monster capture | Captured rows | Source scope / parser status |
| --- | ---: | --- |
| [A corpse ](https://oldschool.runescape.wiki/w/A_corpse) | 1 | PARSED_UNREVIEWED |
| [A Doubt ](https://oldschool.runescape.wiki/w/A_Doubt) | 1 | PARSED_UNREVIEWED |
| [Aberrant spectre ](https://oldschool.runescape.wiki/w/Aberrant_spectre) | 50 | PARSED_UNREVIEWED |
| [Abhorrent spectre ](https://oldschool.runescape.wiki/w/Abhorrent_spectre) | 51 | PARSED_UNREVIEWED |
| [Abomination ](https://oldschool.runescape.wiki/w/Abomination) | 0 | NO_SUPPORTED_TABLES |
| [Abyssal antibody ](https://oldschool.runescape.wiki/w/Abyssal_antibody) | 0 | NO_SUPPORTED_TABLES |
| [Abyssal demon ](https://oldschool.runescape.wiki/w/Abyssal_demon) | 157 | PARSED_UNREVIEWED |
| [Abyssal guardian ](https://oldschool.runescape.wiki/w/Abyssal_guardian) | 22 | PARSED_UNREVIEWED |
| [Abyssal leech ](https://oldschool.runescape.wiki/w/Abyssal_leech) | 22 | PARSED_UNREVIEWED |
| [Abyssal leech (The Scar) ](https://oldschool.runescape.wiki/w/Abyssal_leech_(The_Scar)) | 0 | NO_SUPPORTED_TABLES |
| [Abyssal portal ](https://oldschool.runescape.wiki/w/Abyssal_portal) | 0 | NO_SUPPORTED_TABLES |
| [Abyssal walker ](https://oldschool.runescape.wiki/w/Abyssal_walker) | 23 | PARSED_UNREVIEWED |
| [Abyssal walker (The Scar) ](https://oldschool.runescape.wiki/w/Abyssal_walker_(The_Scar)) | 0 | NO_SUPPORTED_TABLES |
| [Acidic Bloodveld ](https://oldschool.runescape.wiki/w/Acidic_Bloodveld) | 0 | NO_SUPPORTED_TABLES |
| [Adala ](https://oldschool.runescape.wiki/w/Adala) | 1 | PARSED_UNREVIEWED |
| [Adamant dragon ](https://oldschool.runescape.wiki/w/Adamant_dragon) | 56 | PARSED_UNREVIEWED |
| [Afflicted ](https://oldschool.runescape.wiki/w/Afflicted) | 33 | PARSED_UNREVIEWED |
| [Agile Scarab ](https://oldschool.runescape.wiki/w/Agile_Scarab) | 0 | NO_SUPPORTED_TABLES |
| [Agile Warrior ](https://oldschool.runescape.wiki/w/Agile_Warrior) | 0 | NO_SUPPORTED_TABLES |
| [Agrith Naar (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Agrith_Naar_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Agrith-Na-Na (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Agrith-Na-Na_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Air elemental ](https://oldschool.runescape.wiki/w/Air_elemental) | 34 | PARSED_UNREVIEWED |
| [Air wizard ](https://oldschool.runescape.wiki/w/Air_wizard) | 3 | PARSED_UNREVIEWED |
| [Akkha's Shadow ](https://oldschool.runescape.wiki/w/Akkha%27s_Shadow) | 0 | NO_SUPPORTED_TABLES |
| [Al Kharid warrior ](https://oldschool.runescape.wiki/w/Al_Kharid_warrior) | 36 | PARSED_UNREVIEWED |
| [Albatross ](https://oldschool.runescape.wiki/w/Albatross) | 22 | PARSED_UNREVIEWED |
| [Albino bat ](https://oldschool.runescape.wiki/w/Albino_bat) | 2 | PARSED_UNREVIEWED |
| [Alexis ](https://oldschool.runescape.wiki/w/Alexis) | 55 | PARSED_UNREVIEWED |
| [Alomone ](https://oldschool.runescape.wiki/w/Alomone) | 1 | PARSED_UNREVIEWED |
| [Alrik ](https://oldschool.runescape.wiki/w/Alrik) | 0 | NO_SUPPORTED_TABLES |
| [Ammonite Crab ](https://oldschool.runescape.wiki/w/Ammonite_Crab) | 43 | PARSED_UNREVIEWED |
| [Amoxliatl (Echo) ](https://oldschool.runescape.wiki/w/Amoxliatl_(Echo)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Ancient Custodian ](https://oldschool.runescape.wiki/w/Ancient_Custodian) | 48 | PARSED_UNREVIEWED |
| [Ancient feral vyre ](https://oldschool.runescape.wiki/w/Ancient_feral_vyre) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Ancient Guardian ](https://oldschool.runescape.wiki/w/Ancient_Guardian) | 0 | NO_SUPPORTED_TABLES |
| [Ancient Guardian (Desert Treasure II) ](https://oldschool.runescape.wiki/w/Ancient_Guardian_(Desert_Treasure_II)) | 0 | NO_SUPPORTED_TABLES |
| [Ancient Wizard ](https://oldschool.runescape.wiki/w/Ancient_Wizard) | 31 | PARSED_UNREVIEWED |
| [Ancient Wyvern ](https://oldschool.runescape.wiki/w/Ancient_Wyvern) | 91 | PARSED_UNREVIEWED |
| [Ancient Zygomite ](https://oldschool.runescape.wiki/w/Ancient_Zygomite) | 52 | PARSED_UNREVIEWED |
| [Angry barbarian spirit ](https://oldschool.runescape.wiki/w/Angry_barbarian_spirit) | 59 | PARSED_UNREVIEWED |
| [Angry bear ](https://oldschool.runescape.wiki/w/Angry_bear) | 0 | NO_SUPPORTED_TABLES |
| [Angry giant rat ](https://oldschool.runescape.wiki/w/Angry_giant_rat) | 1 | PARSED_UNREVIEWED |
| [Angry goblin ](https://oldschool.runescape.wiki/w/Angry_goblin) | 0 | NO_SUPPORTED_TABLES |
| [Angry tortugan ](https://oldschool.runescape.wiki/w/Angry_tortugan) | 1 | PARSED_UNREVIEWED |
| [Angry unicorn ](https://oldschool.runescape.wiki/w/Angry_unicorn) | 0 | NO_SUPPORTED_TABLES |
| [Animated Adamant Armour ](https://oldschool.runescape.wiki/w/Animated_Adamant_Armour) | 4 | PARSED_UNREVIEWED |
| [Animated Black Armour ](https://oldschool.runescape.wiki/w/Animated_Black_Armour) | 4 | PARSED_UNREVIEWED |
| [Animated Bronze Armour ](https://oldschool.runescape.wiki/w/Animated_Bronze_Armour) | 4 | PARSED_UNREVIEWED |
| [Animated Iron Armour ](https://oldschool.runescape.wiki/w/Animated_Iron_Armour) | 4 | PARSED_UNREVIEWED |
| [Animated Mithril Armour ](https://oldschool.runescape.wiki/w/Animated_Mithril_Armour) | 4 | PARSED_UNREVIEWED |
| [Animated Rune Armour ](https://oldschool.runescape.wiki/w/Animated_Rune_Armour) | 4 | PARSED_UNREVIEWED |
| [Animated spade ](https://oldschool.runescape.wiki/w/Animated_spade) | 1 | PARSED_UNREVIEWED |
| [Animated Steel Armour ](https://oldschool.runescape.wiki/w/Animated_Steel_Armour) | 4 | PARSED_UNREVIEWED |
| [Animated steel armour (Tarn's Lair) ](https://oldschool.runescape.wiki/w/Animated_steel_armour_(Tarn%27s_Lair)) | 1 | PARSED_UNREVIEWED |
| [Anja ](https://oldschool.runescape.wiki/w/Anja) | 34 | PARSED_UNREVIEWED |
| [Ankou ](https://oldschool.runescape.wiki/w/Ankou) | 183 | PARSED_UNREVIEWED |
| [Apparition ](https://oldschool.runescape.wiki/w/Apparition) | 1 | PARSED_UNREVIEWED |
| [Aquanite ](https://oldschool.runescape.wiki/w/Aquanite) | 25 | PARSED_UNREVIEWED |
| [Araxyte ](https://oldschool.runescape.wiki/w/Araxyte) | 116 | PARSED_UNREVIEWED |
| [Arcane Scarab ](https://oldschool.runescape.wiki/w/Arcane_Scarab) | 0 | NO_SUPPORTED_TABLES |
| [Archer (Ardougne) ](https://oldschool.runescape.wiki/w/Archer_(Ardougne)) | 1 | PARSED_UNREVIEWED |
| [Archer (Burthorpe) ](https://oldschool.runescape.wiki/w/Archer_(Burthorpe)) | 23 | PARSED_UNREVIEWED |
| [Arianwyn ](https://oldschool.runescape.wiki/w/Arianwyn) | 0 | NO_SUPPORTED_TABLES |
| [Arianwyn (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Arianwyn_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Armadylean guard ](https://oldschool.runescape.wiki/w/Armadylean_guard) | 41 | PARSED_UNREVIEWED |
| [Armoured Foe ](https://oldschool.runescape.wiki/w/Armoured_Foe) | 1 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Armoured kraken ](https://oldschool.runescape.wiki/w/Armoured_kraken) | 76 | PARSED_UNREVIEWED |
| [Armoured zombie (Defender of Varrock) ](https://oldschool.runescape.wiki/w/Armoured_zombie_(Defender_of_Varrock)) | 3 | PARSED_UNREVIEWED |
| [Armoured zombie (The Curse of Arrav) ](https://oldschool.runescape.wiki/w/Armoured_zombie_(The_Curse_of_Arrav)) | 0 | NO_SUPPORTED_TABLES |
| [Armoured zombie (Zemouregal's Base) ](https://oldschool.runescape.wiki/w/Armoured_zombie_(Zemouregal%27s_Base)) | 44 | PARSED_UNREVIEWED |
| [Armoured zombie (Zemouregal's Fort) ](https://oldschool.runescape.wiki/w/Armoured_zombie_(Zemouregal%27s_Fort)) | 45 | PARSED_UNREVIEWED |
| [Arrav ](https://oldschool.runescape.wiki/w/Arrav) | 0 | NO_SUPPORTED_TABLES |
| [Arrg (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Arrg_(Nightmare_Zone)) | 3 | EXCLUDED_ALTERNATE_MODE |
| [Assassin ](https://oldschool.runescape.wiki/w/Assassin) | 0 | NO_SUPPORTED_TABLES |
| [Assassin (A Kingdom Divided) ](https://oldschool.runescape.wiki/w/Assassin_(A_Kingdom_Divided)) | 0 | NO_SUPPORTED_TABLES |
| [Assassin (While Guthix Sleeps) ](https://oldschool.runescape.wiki/w/Assassin_(While_Guthix_Sleeps)) | 0 | NO_SUPPORTED_TABLES |
| [Asyn Shade ](https://oldschool.runescape.wiki/w/Asyn_Shade) | 1 | PARSED_UNREVIEWED |
| [Asyn shadow (Temple Trekking) ](https://oldschool.runescape.wiki/w/Asyn_shadow_(Temple_Trekking)) | 1 | PARSED_UNREVIEWED |
| [Avatar of Creation ](https://oldschool.runescape.wiki/w/Avatar_of_Creation) | 0 | NO_SUPPORTED_TABLES |
| [Avatar of Destruction ](https://oldschool.runescape.wiki/w/Avatar_of_Destruction) | 0 | NO_SUPPORTED_TABLES |
| [Aviansie ](https://oldschool.runescape.wiki/w/Aviansie) | 48 | PARSED_UNREVIEWED |
| [Baboon ](https://oldschool.runescape.wiki/w/Baboon) | 0 | NO_SUPPORTED_TABLES |
| [Baboon Brawler ](https://oldschool.runescape.wiki/w/Baboon_Brawler) | 0 | NO_SUPPORTED_TABLES |
| [Baboon Mage ](https://oldschool.runescape.wiki/w/Baboon_Mage) | 0 | NO_SUPPORTED_TABLES |
| [Baboon Shaman ](https://oldschool.runescape.wiki/w/Baboon_Shaman) | 0 | NO_SUPPORTED_TABLES |
| [Baboon Thrall ](https://oldschool.runescape.wiki/w/Baboon_Thrall) | 0 | NO_SUPPORTED_TABLES |
| [Baboon Thrower ](https://oldschool.runescape.wiki/w/Baboon_Thrower) | 0 | NO_SUPPORTED_TABLES |
| [Baby black dragon ](https://oldschool.runescape.wiki/w/Baby_black_dragon) | 2 | PARSED_UNREVIEWED |
| [Baby blue dragon ](https://oldschool.runescape.wiki/w/Baby_blue_dragon) | 6 | PARSED_UNREVIEWED |
| [Baby blue dragon (Realm of Memories) ](https://oldschool.runescape.wiki/w/Baby_blue_dragon_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Baby dragon (unused) ](https://oldschool.runescape.wiki/w/Baby_dragon_(unused)) | 0 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Baby green dragon ](https://oldschool.runescape.wiki/w/Baby_green_dragon) | 1 | PARSED_UNREVIEWED |
| [Baby impling ](https://oldschool.runescape.wiki/w/Baby_impling) | 21 | PARSED_UNREVIEWED |
| [Baby leviathan ](https://oldschool.runescape.wiki/w/Baby_leviathan) | 0 | NO_SUPPORTED_TABLES |
| [Baby red dragon ](https://oldschool.runescape.wiki/w/Baby_red_dragon) | 6 | PARSED_UNREVIEWED |
| [Baby red dragon (Construction) ](https://oldschool.runescape.wiki/w/Baby_red_dragon_(Construction)) | 0 | NO_SUPPORTED_TABLES |
| [Baby Roc ](https://oldschool.runescape.wiki/w/Baby_Roc) | 1 | PARSED_UNREVIEWED |
| [Baby sea kraken ](https://oldschool.runescape.wiki/w/Baby_sea_kraken) | 44 | PARSED_UNREVIEWED |
| [Baby tanglefoot ](https://oldschool.runescape.wiki/w/Baby_tanglefoot) | 1 | PARSED_UNREVIEWED |
| [Balance Elemental ](https://oldschool.runescape.wiki/w/Balance_Elemental) | 0 | NO_SUPPORTED_TABLES |
| [Balfrug Kreeyath ](https://oldschool.runescape.wiki/w/Balfrug_Kreeyath) | 19 | PARSED_UNREVIEWED |
| [Bandit ](https://oldschool.runescape.wiki/w/Bandit) | 96 | PARSED_UNREVIEWED |
| [Bandit (Bandit Camp) ](https://oldschool.runescape.wiki/w/Bandit_(Bandit_Camp)) | 29 | PARSED_UNREVIEWED |
| [Bandit (Pollnivneach) ](https://oldschool.runescape.wiki/w/Bandit_(Pollnivneach)) | 5 | PARSED_UNREVIEWED |
| [Bandit (Shayzien) ](https://oldschool.runescape.wiki/w/Bandit_(Shayzien)) | 38 | PARSED_UNREVIEWED |
| [Bandit (Varlamore) ](https://oldschool.runescape.wiki/w/Bandit_(Varlamore)) | 38 | PARSED_UNREVIEWED |
| [Bandit champion ](https://oldschool.runescape.wiki/w/Bandit_champion) | 3 | PARSED_UNREVIEWED |
| [Bandosian guard ](https://oldschool.runescape.wiki/w/Bandosian_guard) | 41 | PARSED_UNREVIEWED |
| [Banshee ](https://oldschool.runescape.wiki/w/Banshee) | 43 | PARSED_UNREVIEWED |
| [Barbarian ](https://oldschool.runescape.wiki/w/Barbarian) | 73 | PARSED_UNREVIEWED |
| [Barbarian (Barbarian Camp) ](https://oldschool.runescape.wiki/w/Barbarian_(Barbarian_Camp)) | 72 | PARSED_UNREVIEWED |
| [Barbarian (historical) ](https://oldschool.runescape.wiki/w/Barbarian_(historical)) | 0 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Barbarian Warlord ](https://oldschool.runescape.wiki/w/Barbarian_Warlord) | 0 | NO_SUPPORTED_TABLES |
| [Barbarian woman (historical) ](https://oldschool.runescape.wiki/w/Barbarian_woman_(historical)) | 0 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Bark Blamish Snail ](https://oldschool.runescape.wiki/w/Bark_Blamish_Snail) | 2 | PARSED_UNREVIEWED |
| [Barrelchest (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Barrelchest_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Basilisk ](https://oldschool.runescape.wiki/w/Basilisk) | 46 | PARSED_UNREVIEWED |
| [Basilisk (The Fremennik Exiles) ](https://oldschool.runescape.wiki/w/Basilisk_(The_Fremennik_Exiles)) | 0 | NO_SUPPORTED_TABLES |
| [Basilisk (unused) ](https://oldschool.runescape.wiki/w/Basilisk_(unused)) | 0 | NO_SUPPORTED_TABLES |
| [Basilisk Knight ](https://oldschool.runescape.wiki/w/Basilisk_Knight) | 42 | PARSED_UNREVIEWED |
| [Basilisk Sentinel ](https://oldschool.runescape.wiki/w/Basilisk_Sentinel) | 44 | PARSED_UNREVIEWED |
| [Basilisk Youngling ](https://oldschool.runescape.wiki/w/Basilisk_Youngling) | 2 | PARSED_UNREVIEWED |
| [Bat ](https://oldschool.runescape.wiki/w/Bat) | 2 | PARSED_UNREVIEWED |
| [Battle mage ](https://oldschool.runescape.wiki/w/Battle_mage) | 2 | PARSED_UNREVIEWED |
| [Bear Cub ](https://oldschool.runescape.wiki/w/Bear_Cub) | 6 | PARSED_UNREVIEWED |
| [Bedabin Nomad Fighter ](https://oldschool.runescape.wiki/w/Bedabin_Nomad_Fighter) | 1 | PARSED_UNREVIEWED |
| [Bee Swarm (Deadman) ](https://oldschool.runescape.wiki/w/Bee_Swarm_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Berry ](https://oldschool.runescape.wiki/w/Berry) | 74 | PARSED_UNREVIEWED |
| [Berserk barbarian spirit ](https://oldschool.runescape.wiki/w/Berserk_barbarian_spirit) | 59 | PARSED_UNREVIEWED |
| [Big Evil Chicken ](https://oldschool.runescape.wiki/w/Big_Evil_Chicken) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Big frog ](https://oldschool.runescape.wiki/w/Big_frog) | 13 | PARSED_UNREVIEWED |
| [Big Snake ](https://oldschool.runescape.wiki/w/Big_Snake) | 2 | PARSED_UNREVIEWED |
| [Big Wolf ](https://oldschool.runescape.wiki/w/Big_Wolf) | 2 | PARSED_UNREVIEWED |
| [Billy Goat ](https://oldschool.runescape.wiki/w/Billy_Goat) | 2 | PARSED_UNREVIEWED |
| [Bird ](https://oldschool.runescape.wiki/w/Bird) | 1 | PARSED_UNREVIEWED |
| [Black bear ](https://oldschool.runescape.wiki/w/Black_bear) | 6 | PARSED_UNREVIEWED |
| [Black chinchompa (Hunter) ](https://oldschool.runescape.wiki/w/Black_chinchompa_(Hunter)) | 2 | PARSED_UNREVIEWED |
| [Black demon (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Black_demon_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Black demon (Realm of Memories) ](https://oldschool.runescape.wiki/w/Black_demon_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Black demon (The Grand Tree) ](https://oldschool.runescape.wiki/w/Black_demon_(The_Grand_Tree)) | 0 | NO_SUPPORTED_TABLES |
| [Black demon (The Scar) ](https://oldschool.runescape.wiki/w/Black_demon_(The_Scar)) | 0 | NO_SUPPORTED_TABLES |
| [Black dragon ](https://oldschool.runescape.wiki/w/Black_dragon) | 138 | PARSED_UNREVIEWED |
| [Black dragon (Echo) ](https://oldschool.runescape.wiki/w/Black_dragon_(Echo)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Black dragon (Realm of Memories) ](https://oldschool.runescape.wiki/w/Black_dragon_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Black Eye Bethel ](https://oldschool.runescape.wiki/w/Black_Eye_Bethel) | 0 | NO_SUPPORTED_TABLES |
| [Black Guard ](https://oldschool.runescape.wiki/w/Black_Guard) | 75 | PARSED_UNREVIEWED |
| [Black Guard Berserker ](https://oldschool.runescape.wiki/w/Black_Guard_Berserker) | 41 | PARSED_UNREVIEWED |
| [Black Heather ](https://oldschool.runescape.wiki/w/Black_Heather) | 46 | PARSED_UNREVIEWED |
| [Black jaguar ](https://oldschool.runescape.wiki/w/Black_jaguar) | 3 | PARSED_UNREVIEWED |
| [Black jaguar (Scrambled!) ](https://oldschool.runescape.wiki/w/Black_jaguar_(Scrambled!)) | 0 | NO_SUPPORTED_TABLES |
| [Black Knight ](https://oldschool.runescape.wiki/w/Black_Knight) | 60 | PARSED_UNREVIEWED |
| [Black knight (Port Sarim jail) ](https://oldschool.runescape.wiki/w/Black_knight_(Port_Sarim_jail)) | 1 | PARSED_UNREVIEWED |
| [Black Knight (Wanted!) ](https://oldschool.runescape.wiki/w/Black_Knight_(Wanted!)) | 0 | NO_SUPPORTED_TABLES |
| [Black Knight Titan (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Black_Knight_Titan_(Nightmare_Zone)) | 3 | EXCLUDED_ALTERNATE_MODE |
| [Black unicorn ](https://oldschool.runescape.wiki/w/Black_unicorn) | 5 | PARSED_UNREVIEWED |
| [Black unicorn Foal ](https://oldschool.runescape.wiki/w/Black_unicorn_Foal) | 1 | PARSED_UNREVIEWED |
| [Blessed giant rat ](https://oldschool.runescape.wiki/w/Blessed_giant_rat) | 2 | PARSED_UNREVIEWED |
| [Blessed spider ](https://oldschool.runescape.wiki/w/Blessed_spider) | 0 | NO_SUPPORTED_TABLES |
| [Blink ](https://oldschool.runescape.wiki/w/Blink) | 0 | NO_SUPPORTED_TABLES |
| [Blood Blamish Snail ](https://oldschool.runescape.wiki/w/Blood_Blamish_Snail) | 4 | PARSED_UNREVIEWED |
| [Blood jaguar ](https://oldschool.runescape.wiki/w/Blood_jaguar) | 0 | NO_SUPPORTED_TABLES |
| [Blood Reaver ](https://oldschool.runescape.wiki/w/Blood_Reaver) | 43 | PARSED_UNREVIEWED |
| [Blood spawn ](https://oldschool.runescape.wiki/w/Blood_spawn) | 0 | NO_SUPPORTED_TABLES |
| [Blood-starved venator ](https://oldschool.runescape.wiki/w/Blood-starved_venator) | 46 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirst rockslug ](https://oldschool.runescape.wiki/w/Bloodthirst_rockslug) | 56 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty abomination ](https://oldschool.runescape.wiki/w/Bloodthirsty_abomination) | 78 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty abyssal demon ](https://oldschool.runescape.wiki/w/Bloodthirsty_abyssal_demon) | 97 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty banshee ](https://oldschool.runescape.wiki/w/Bloodthirsty_banshee) | 50 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty basilisk ](https://oldschool.runescape.wiki/w/Bloodthirsty_basilisk) | 56 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Basilisk Sentinel ](https://oldschool.runescape.wiki/w/Bloodthirsty_Basilisk_Sentinel) | 58 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Bloodveld ](https://oldschool.runescape.wiki/w/Bloodthirsty_Bloodveld) | 63 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Bloodthirsty choke devil ](https://oldschool.runescape.wiki/w/Bloodthirsty_choke_devil) | 69 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Cockathrice ](https://oldschool.runescape.wiki/w/Bloodthirsty_Cockathrice) | 81 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Crawler ](https://oldschool.runescape.wiki/w/Bloodthirsty_Crawler) | 56 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Drake ](https://oldschool.runescape.wiki/w/Bloodthirsty_Drake) | 89 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty gargoyle ](https://oldschool.runescape.wiki/w/Bloodthirsty_gargoyle) | 56 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty hand ](https://oldschool.runescape.wiki/w/Bloodthirsty_hand) | 33 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Hydra ](https://oldschool.runescape.wiki/w/Bloodthirsty_Hydra) | 99 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Infernal pyrelord ](https://oldschool.runescape.wiki/w/Bloodthirsty_Infernal_pyrelord) | 40 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Jelly ](https://oldschool.runescape.wiki/w/Bloodthirsty_Jelly) | 43 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty kurask ](https://oldschool.runescape.wiki/w/Bloodthirsty_kurask) | 78 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Mage ](https://oldschool.runescape.wiki/w/Bloodthirsty_Mage) | 37 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty mutated Bloodveld ](https://oldschool.runescape.wiki/w/Bloodthirsty_mutated_Bloodveld) | 76 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Nechryarch ](https://oldschool.runescape.wiki/w/Bloodthirsty_Nechryarch) | 169 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Night beast ](https://oldschool.runescape.wiki/w/Bloodthirsty_Night_beast) | 103 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty pyrelord ](https://oldschool.runescape.wiki/w/Bloodthirsty_pyrelord) | 42 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Repugnant spectre ](https://oldschool.runescape.wiki/w/Bloodthirsty_Repugnant_spectre) | 75 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Shadow Wyrm ](https://oldschool.runescape.wiki/w/Bloodthirsty_Shadow_Wyrm) | 80 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty smoke devil ](https://oldschool.runescape.wiki/w/Bloodthirsty_smoke_devil) | 94 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty spectre ](https://oldschool.runescape.wiki/w/Bloodthirsty_spectre) | 67 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Turoth ](https://oldschool.runescape.wiki/w/Bloodthirsty_Turoth) | 66 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty twisted banshee ](https://oldschool.runescape.wiki/w/Bloodthirsty_twisted_banshee) | 71 | EXCLUDED_ALTERNATE_MODE |
| [Bloodthirsty Warped Jelly ](https://oldschool.runescape.wiki/w/Bloodthirsty_Warped_Jelly) | 49 | EXCLUDED_ALTERNATE_MODE |
| [Bloodveld ](https://oldschool.runescape.wiki/w/Bloodveld) | 55 | PARSED_UNREVIEWED |
| [Bloody Jack ](https://oldschool.runescape.wiki/w/Bloody_Jack) | 0 | NO_SUPPORTED_TABLES |
| [Blue dagannoth ](https://oldschool.runescape.wiki/w/Blue_dagannoth) | 2 | PARSED_UNREVIEWED |
| [Blue dragon ](https://oldschool.runescape.wiki/w/Blue_dragon) | 50 | PARSED_UNREVIEWED |
| [Blue dragon (Realm of Memories) ](https://oldschool.runescape.wiki/w/Blue_dragon_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Boar ](https://oldschool.runescape.wiki/w/Boar) | 3 | PARSED_UNREVIEWED |
| [Boatswain Bill Teak ](https://oldschool.runescape.wiki/w/Boatswain_Bill_Teak) | 0 | NO_SUPPORTED_TABLES |
| [Body Golem ](https://oldschool.runescape.wiki/w/Body_Golem) | 15 | PARSED_UNREVIEWED |
| [Boris ](https://oldschool.runescape.wiki/w/Boris) | 55 | PARSED_UNREVIEWED |
| [Borrokar ](https://oldschool.runescape.wiki/w/Borrokar) | 51 | PARSED_UNREVIEWED |
| [Bouncer (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Bouncer_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Brassican Mage ](https://oldschool.runescape.wiki/w/Brassican_Mage) | 31 | PARSED_UNREVIEWED |
| [Brawler ](https://oldschool.runescape.wiki/w/Brawler) | 0 | NO_SUPPORTED_TABLES |
| [Bree ](https://oldschool.runescape.wiki/w/Bree) | 18 | PARSED_UNREVIEWED |
| [Breoca ](https://oldschool.runescape.wiki/w/Breoca) | 32 | PARSED_UNREVIEWED |
| [Brine rat ](https://oldschool.runescape.wiki/w/Brine_rat) | 27 | PARSED_UNREVIEWED |
| [Broddi ](https://oldschool.runescape.wiki/w/Broddi) | 1 | PARSED_UNREVIEWED |
| [Bronze dragon ](https://oldschool.runescape.wiki/w/Bronze_dragon) | 58 | PARSED_UNREVIEWED |
| [Broodoo victim ](https://oldschool.runescape.wiki/w/Broodoo_victim) | 3 | PARSED_UNREVIEWED |
| [Bruise Blamish Snail ](https://oldschool.runescape.wiki/w/Bruise_Blamish_Snail) | 4 | PARSED_UNREVIEWED |
| [Brutal black dragon ](https://oldschool.runescape.wiki/w/Brutal_black_dragon) | 71 | PARSED_UNREVIEWED |
| [Brutal blue dragon ](https://oldschool.runescape.wiki/w/Brutal_blue_dragon) | 70 | PARSED_UNREVIEWED |
| [Brutal green dragon ](https://oldschool.runescape.wiki/w/Brutal_green_dragon) | 67 | PARSED_UNREVIEWED |
| [Brutal red dragon ](https://oldschool.runescape.wiki/w/Brutal_red_dragon) | 67 | PARSED_UNREVIEWED |
| [Buffalo ](https://oldschool.runescape.wiki/w/Buffalo) | 5 | PARSED_UNREVIEWED |
| [Bull shark ](https://oldschool.runescape.wiki/w/Bull_shark) | 33 | PARSED_UNREVIEWED |
| [Bunny ](https://oldschool.runescape.wiki/w/Bunny) | 3 | PARSED_UNREVIEWED |
| [Bush snake ](https://oldschool.runescape.wiki/w/Bush_snake) | 2 | PARSED_UNREVIEWED |
| [Butterfly ray ](https://oldschool.runescape.wiki/w/Butterfly_ray) | 55 | PARSED_UNREVIEWED |
| [Callisto (PvM Arena) ](https://oldschool.runescape.wiki/w/Callisto_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Camp dweller ](https://oldschool.runescape.wiki/w/Camp_dweller) | 1 | PARSED_UNREVIEWED |
| [Captain Cotus ](https://oldschool.runescape.wiki/w/Captain_Cotus) | 0 | NO_SUPPORTED_TABLES |
| [Captain Darvald ](https://oldschool.runescape.wiki/w/Captain_Darvald) | 0 | NO_SUPPORTED_TABLES |
| [Captain Jack ](https://oldschool.runescape.wiki/w/Captain_Jack) | 0 | NO_SUPPORTED_TABLES |
| [Captain Ruban Acer ](https://oldschool.runescape.wiki/w/Captain_Ruban_Acer) | 0 | NO_SUPPORTED_TABLES |
| [Carnivorous chinchompa ](https://oldschool.runescape.wiki/w/Carnivorous_chinchompa) | 3 | PARSED_UNREVIEWED |
| [Catablepon ](https://oldschool.runescape.wiki/w/Catablepon) | 51 | PARSED_UNREVIEWED |
| [Catherby monodon ](https://oldschool.runescape.wiki/w/Catherby_monodon) | 59 | PARSED_UNREVIEWED |
| [Cave abomination ](https://oldschool.runescape.wiki/w/Cave_abomination) | 68 | PARSED_UNREVIEWED |
| [Cave abomination (Deadman) ](https://oldschool.runescape.wiki/w/Cave_abomination_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Cave bug ](https://oldschool.runescape.wiki/w/Cave_bug) | 61 | PARSED_UNREVIEWED |
| [Cave Bug (Realm of Memories) ](https://oldschool.runescape.wiki/w/Cave_Bug_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Cave crawler ](https://oldschool.runescape.wiki/w/Cave_crawler) | 52 | PARSED_UNREVIEWED |
| [Cave goblin (monster) ](https://oldschool.runescape.wiki/w/Cave_goblin_(monster)) | 19 | PARSED_UNREVIEWED |
| [Cave goblin guard ](https://oldschool.runescape.wiki/w/Cave_goblin_guard) | 10 | PARSED_UNREVIEWED |
| [Cave goblin miner ](https://oldschool.runescape.wiki/w/Cave_goblin_miner) | 9 | PARSED_UNREVIEWED |
| [Cave horror ](https://oldschool.runescape.wiki/w/Cave_horror) | 64 | PARSED_UNREVIEWED |
| [Cave kraken ](https://oldschool.runescape.wiki/w/Cave_kraken) | 71 | PARSED_UNREVIEWED |
| [Cave lizard ](https://oldschool.runescape.wiki/w/Cave_lizard) | 1 | PARSED_UNREVIEWED |
| [Cave slime ](https://oldschool.runescape.wiki/w/Cave_slime) | 30 | PARSED_UNREVIEWED |
| [Ceolburg ](https://oldschool.runescape.wiki/w/Ceolburg) | 32 | PARSED_UNREVIEWED |
| [Cerberus (Deadman) ](https://oldschool.runescape.wiki/w/Cerberus_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Cerberus (Echo) ](https://oldschool.runescape.wiki/w/Cerberus_(Echo)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Champion of Scabaras ](https://oldschool.runescape.wiki/w/Champion_of_Scabaras) | 0 | NO_SUPPORTED_TABLES |
| [Chaos druid ](https://oldschool.runescape.wiki/w/Chaos_druid) | 45 | PARSED_UNREVIEWED |
| [Chaos druid warrior ](https://oldschool.runescape.wiki/w/Chaos_druid_warrior) | 44 | PARSED_UNREVIEWED |
| [Chaos dwarf ](https://oldschool.runescape.wiki/w/Chaos_dwarf) | 38 | PARSED_UNREVIEWED |
| [Chaos Elemental (PvM Arena) ](https://oldschool.runescape.wiki/w/Chaos_Elemental_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Chaos Golem ](https://oldschool.runescape.wiki/w/Chaos_Golem) | 15 | PARSED_UNREVIEWED |
| [Chaotic death spawn ](https://oldschool.runescape.wiki/w/Chaotic_death_spawn) | 1 | PARSED_UNREVIEWED |
| [Chasm Crawler ](https://oldschool.runescape.wiki/w/Chasm_Crawler) | 54 | PARSED_UNREVIEWED |
| [Chicken ](https://oldschool.runescape.wiki/w/Chicken) | 7 | PARSED_UNREVIEWED |
| [Chicken (Tutorial Island) ](https://oldschool.runescape.wiki/w/Chicken_(Tutorial_Island)) | 1 | PARSED_UNREVIEWED |
| [Chilled jelly ](https://oldschool.runescape.wiki/w/Chilled_jelly) | 33 | PARSED_UNREVIEWED |
| [Chimalli ](https://oldschool.runescape.wiki/w/Chimalli) | 0 | NO_SUPPORTED_TABLES |
| [Chinchompa (Hunter) ](https://oldschool.runescape.wiki/w/Chinchompa_(Hunter)) | 3 | PARSED_UNREVIEWED |
| [Choke devil ](https://oldschool.runescape.wiki/w/Choke_devil) | 58 | PARSED_UNREVIEWED |
| [Chompy bird ](https://oldschool.runescape.wiki/w/Chompy_bird) | 4 | PARSED_UNREVIEWED |
| [Chronozon (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Chronozon_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Chuck up ](https://oldschool.runescape.wiki/w/Chuck_up) | 2 | PARSED_UNREVIEWED |
| [City guard ](https://oldschool.runescape.wiki/w/City_guard) | 30 | PARSED_UNREVIEWED |
| [Clivet ](https://oldschool.runescape.wiki/w/Clivet) | 1 | PARSED_UNREVIEWED |
| [Cockathrice ](https://oldschool.runescape.wiki/w/Cockathrice) | 78 | PARSED_UNREVIEWED |
| [Cockatrice ](https://oldschool.runescape.wiki/w/Cockatrice) | 56 | PARSED_UNREVIEWED |
| [Cockatrice (unused) ](https://oldschool.runescape.wiki/w/Cockatrice_(unused)) | 0 | NO_SUPPORTED_TABLES |
| [Colonel Radick ](https://oldschool.runescape.wiki/w/Colonel_Radick) | 22 | PARSED_UNREVIEWED |
| [Colossal Chocco Chicken ](https://oldschool.runescape.wiki/w/Colossal_Chocco_Chicken) | 4 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Colossal Hydra ](https://oldschool.runescape.wiki/w/Colossal_Hydra) | 80 | PARSED_UNREVIEWED |
| [Commander Zilyana (Deadman) ](https://oldschool.runescape.wiki/w/Commander_Zilyana_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Commander Zilyana (PvM Arena) ](https://oldschool.runescape.wiki/w/Commander_Zilyana_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Confused barbarian ](https://oldschool.runescape.wiki/w/Confused_barbarian) | 1 | PARSED_UNREVIEWED |
| [Confusion beast ](https://oldschool.runescape.wiki/w/Confusion_beast) | 0 | NO_SUPPORTED_TABLES |
| [Corrupt Lizardman ](https://oldschool.runescape.wiki/w/Corrupt_Lizardman) | 2 | PARSED_UNREVIEWED |
| [Corrupt Lizardman (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Corrupt_Lizardman_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Corrupted Bat ](https://oldschool.runescape.wiki/w/Corrupted_Bat) | 7 | PARSED_UNREVIEWED |
| [Corrupted Bear ](https://oldschool.runescape.wiki/w/Corrupted_Bear) | 10 | PARSED_UNREVIEWED |
| [Corrupted Dark Beast ](https://oldschool.runescape.wiki/w/Corrupted_Dark_Beast) | 10 | PARSED_UNREVIEWED |
| [Corrupted Dragon ](https://oldschool.runescape.wiki/w/Corrupted_Dragon) | 10 | PARSED_UNREVIEWED |
| [Corrupted Hunllef (Echo) ](https://oldschool.runescape.wiki/w/Corrupted_Hunllef_(Echo)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Corrupted Rat ](https://oldschool.runescape.wiki/w/Corrupted_Rat) | 7 | PARSED_UNREVIEWED |
| [Corrupted Scorpion ](https://oldschool.runescape.wiki/w/Corrupted_Scorpion) | 7 | PARSED_UNREVIEWED |
| [Corrupted Spider ](https://oldschool.runescape.wiki/w/Corrupted_Spider) | 7 | PARSED_UNREVIEWED |
| [Corrupted Unicorn ](https://oldschool.runescape.wiki/w/Corrupted_Unicorn) | 7 | PARSED_UNREVIEWED |
| [Corrupted Wolf ](https://oldschool.runescape.wiki/w/Corrupted_Wolf) | 7 | PARSED_UNREVIEWED |
| [Corsair Traitor ](https://oldschool.runescape.wiki/w/Corsair_Traitor) | 1 | PARSED_UNREVIEWED |
| [Count Draynor (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Count_Draynor_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Cow ](https://oldschool.runescape.wiki/w/Cow) | 4 | PARSED_UNREVIEWED |
| [Cow (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Cow_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Cow calf ](https://oldschool.runescape.wiki/w/Cow_calf) | 4 | PARSED_UNREVIEWED |
| [Crab ](https://oldschool.runescape.wiki/w/Crab) | 8 | PARSED_UNREVIEWED |
| [Crandor shark ](https://oldschool.runescape.wiki/w/Crandor_shark) | 33 | PARSED_UNREVIEWED |
| [Crawling Hand ](https://oldschool.runescape.wiki/w/Crawling_Hand) | 27 | PARSED_UNREVIEWED |
| [Crimson sanguisphera ](https://oldschool.runescape.wiki/w/Crimson_sanguisphera) | 1 | PARSED_UNREVIEWED |
| [Crocodile ](https://oldschool.runescape.wiki/w/Crocodile) | 14 | PARSED_UNREVIEWED |
| [Crocodile (Tombs of Amascut) ](https://oldschool.runescape.wiki/w/Crocodile_(Tombs_of_Amascut)) | 0 | NO_SUPPORTED_TABLES |
| [Cruor ](https://oldschool.runescape.wiki/w/Cruor) | 5 | PARSED_UNREVIEWED |
| [Crushing hand ](https://oldschool.runescape.wiki/w/Crushing_hand) | 30 | PARSED_UNREVIEWED |
| [Crystal impling ](https://oldschool.runescape.wiki/w/Crystal_impling) | 20 | PARSED_UNREVIEWED |
| [Crystalline Bat ](https://oldschool.runescape.wiki/w/Crystalline_Bat) | 5 | PARSED_UNREVIEWED |
| [Crystalline Bear ](https://oldschool.runescape.wiki/w/Crystalline_Bear) | 7 | PARSED_UNREVIEWED |
| [Crystalline Dark Beast ](https://oldschool.runescape.wiki/w/Crystalline_Dark_Beast) | 7 | PARSED_UNREVIEWED |
| [Crystalline Dragon ](https://oldschool.runescape.wiki/w/Crystalline_Dragon) | 7 | PARSED_UNREVIEWED |
| [Crystalline Rat ](https://oldschool.runescape.wiki/w/Crystalline_Rat) | 5 | PARSED_UNREVIEWED |
| [Crystalline Scorpion ](https://oldschool.runescape.wiki/w/Crystalline_Scorpion) | 4 | PARSED_UNREVIEWED |
| [Crystalline Spider ](https://oldschool.runescape.wiki/w/Crystalline_Spider) | 5 | PARSED_UNREVIEWED |
| [Crystalline Unicorn ](https://oldschool.runescape.wiki/w/Crystalline_Unicorn) | 4 | PARSED_UNREVIEWED |
| [Crystalline Wolf ](https://oldschool.runescape.wiki/w/Crystalline_Wolf) | 4 | PARSED_UNREVIEWED |
| [Cuffs ](https://oldschool.runescape.wiki/w/Cuffs) | 34 | PARSED_UNREVIEWED |
| [Culinaromancer (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Culinaromancer_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Cultist ](https://oldschool.runescape.wiki/w/Cultist) | 1 | PARSED_UNREVIEWED |
| [Cursed Baboon ](https://oldschool.runescape.wiki/w/Cursed_Baboon) | 0 | NO_SUPPORTED_TABLES |
| [Cuthbert ](https://oldschool.runescape.wiki/w/Cuthbert) | 0 | NO_SUPPORTED_TABLES |
| [Cyclops ](https://oldschool.runescape.wiki/w/Cyclops) | 166 | PARSED_UNREVIEWED |
| [Cyclops (Ardougne Zoo) ](https://oldschool.runescape.wiki/w/Cyclops_(Ardougne_Zoo)) | 68 | PARSED_UNREVIEWED |
| [Cyclops (Catacombs of Kourend) ](https://oldschool.runescape.wiki/w/Cyclops_(Catacombs_of_Kourend)) | 77 | PARSED_UNREVIEWED |
| [Cyclops (God Wars Dungeon) ](https://oldschool.runescape.wiki/w/Cyclops_(God_Wars_Dungeon)) | 73 | PARSED_UNREVIEWED |
| [Dad (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Dad_(Nightmare_Zone)) | 3 | EXCLUDED_ALTERNATE_MODE |
| [Daddy's special water ](https://oldschool.runescape.wiki/w/Daddy%27s_special_water) | 2 | PARSED_UNREVIEWED |
| [Dagannoth ](https://oldschool.runescape.wiki/w/Dagannoth) | 59 | PARSED_UNREVIEWED |
| [Dagannoth (Construction) ](https://oldschool.runescape.wiki/w/Dagannoth_(Construction)) | 0 | NO_SUPPORTED_TABLES |
| [Dagannoth (Horror from the Deep) ](https://oldschool.runescape.wiki/w/Dagannoth_(Horror_from_the_Deep)) | 0 | NO_SUPPORTED_TABLES |
| [Dagannoth (Waterbirth Island) ](https://oldschool.runescape.wiki/w/Dagannoth_(Waterbirth_Island)) | 154 | PARSED_UNREVIEWED |
| [Dagannoth fledgeling ](https://oldschool.runescape.wiki/w/Dagannoth_fledgeling) | 3 | PARSED_UNREVIEWED |
| [Dagannoth mother (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Dagannoth_mother_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Dagannoth Prime (Deadman) ](https://oldschool.runescape.wiki/w/Dagannoth_Prime_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Dagannoth Prime (Echo) ](https://oldschool.runescape.wiki/w/Dagannoth_Prime_(Echo)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Dagannoth Prime (PvM Arena) ](https://oldschool.runescape.wiki/w/Dagannoth_Prime_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Dagannoth Rex (Deadman) ](https://oldschool.runescape.wiki/w/Dagannoth_Rex_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Dagannoth Rex (Echo) ](https://oldschool.runescape.wiki/w/Dagannoth_Rex_(Echo)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Dagannoth Rex (PvM Arena) ](https://oldschool.runescape.wiki/w/Dagannoth_Rex_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Dagannoth spawn ](https://oldschool.runescape.wiki/w/Dagannoth_spawn) | 27 | PARSED_UNREVIEWED |
| [Dagannoth Supreme (Deadman) ](https://oldschool.runescape.wiki/w/Dagannoth_Supreme_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Dagannoth Supreme (Echo) ](https://oldschool.runescape.wiki/w/Dagannoth_Supreme_(Echo)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Dagannoth Supreme (PvM Arena) ](https://oldschool.runescape.wiki/w/Dagannoth_Supreme_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Damien Leucurte ](https://oldschool.runescape.wiki/w/Damien_Leucurte) | 0 | NO_SUPPORTED_TABLES |
| [Damis (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Damis_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Dark Ankou ](https://oldschool.runescape.wiki/w/Dark_Ankou) | 0 | NO_SUPPORTED_TABLES |
| [Dark beast ](https://oldschool.runescape.wiki/w/Dark_beast) | 91 | PARSED_UNREVIEWED |
| [Dark warrior ](https://oldschool.runescape.wiki/w/Dark_warrior) | 102 | PARSED_UNREVIEWED |
| [Dark wizard ](https://oldschool.runescape.wiki/w/Dark_wizard) | 69 | PARSED_UNREVIEWED |
| [Dawn (Echo) ](https://oldschool.runescape.wiki/w/Dawn_(Echo)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Deadly red spider ](https://oldschool.runescape.wiki/w/Deadly_red_spider) | 4 | PARSED_UNREVIEWED |
| [Death spawn ](https://oldschool.runescape.wiki/w/Death_spawn) | 1 | PARSED_UNREVIEWED |
| [Death wing ](https://oldschool.runescape.wiki/w/Death_wing) | 1 | PARSED_UNREVIEWED |
| [Deathly mage ](https://oldschool.runescape.wiki/w/Deathly_mage) | 1 | PARSED_UNREVIEWED |
| [Deathly ranger ](https://oldschool.runescape.wiki/w/Deathly_ranger) | 1 | PARSED_UNREVIEWED |
| [Deep sea kraken ](https://oldschool.runescape.wiki/w/Deep_sea_kraken) | 42 | PARSED_UNREVIEWED |
| [Defiler ](https://oldschool.runescape.wiki/w/Defiler) | 0 | NO_SUPPORTED_TABLES |
| [Definitely Not Dusk ](https://oldschool.runescape.wiki/w/Definitely_Not_Dusk) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Demon (Construction) ](https://oldschool.runescape.wiki/w/Demon_(Construction)) | 0 | NO_SUPPORTED_TABLES |
| [Demon of Balance ](https://oldschool.runescape.wiki/w/Demon_of_Balance) | 2 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Demon of Darkness ](https://oldschool.runescape.wiki/w/Demon_of_Darkness) | 2 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Demon of Light ](https://oldschool.runescape.wiki/w/Demon_of_Light) | 2 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Demonic gorilla ](https://oldschool.runescape.wiki/w/Demonic_gorilla) | 73 | PARSED_UNREVIEWED |
| [Derwen ](https://oldschool.runescape.wiki/w/Derwen) | 2 | PARSED_UNREVIEWED |
| [Derwen (Deadman) ](https://oldschool.runescape.wiki/w/Derwen_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Desert Lizard ](https://oldschool.runescape.wiki/w/Desert_Lizard) | 47 | PARSED_UNREVIEWED |
| [Desert Wolf ](https://oldschool.runescape.wiki/w/Desert_Wolf) | 2 | PARSED_UNREVIEWED |
| [Dessourt (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Dessourt_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Dessous (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Dessous_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Deviant spectre ](https://oldschool.runescape.wiki/w/Deviant_spectre) | 54 | PARSED_UNREVIEWED |
| [Dharok the Wretched (Deadman) ](https://oldschool.runescape.wiki/w/Dharok_the_Wretched_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Dinky the drink troll ](https://oldschool.runescape.wiki/w/Dinky_the_drink_troll) | 11 | PARSED_UNREVIEWED |
| [Dire bat ](https://oldschool.runescape.wiki/w/Dire_bat) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Dire gryphon ](https://oldschool.runescape.wiki/w/Dire_gryphon) | 56 | PARSED_UNREVIEWED |
| [Dire Wolf ](https://oldschool.runescape.wiki/w/Dire_Wolf) | 2 | PARSED_UNREVIEWED |
| [Dire Wolf Alpha ](https://oldschool.runescape.wiki/w/Dire_Wolf_Alpha) | 1 | PARSED_UNREVIEWED |
| [Disciple of Iban ](https://oldschool.runescape.wiki/w/Disciple_of_Iban) | 3 | PARSED_UNREVIEWED |
| [Dolphin ](https://oldschool.runescape.wiki/w/Dolphin) | 12 | PARSED_UNREVIEWED |
| [Don't Know What ](https://oldschool.runescape.wiki/w/Don%27t_Know_What) | 0 | NO_SUPPORTED_TABLES |
| [Donny the lad ](https://oldschool.runescape.wiki/w/Donny_the_lad) | 46 | PARSED_UNREVIEWED |
| [Doomion ](https://oldschool.runescape.wiki/w/Doomion) | 2 | PARSED_UNREVIEWED |
| [Double agent ](https://oldschool.runescape.wiki/w/Double_agent) | 2 | PARSED_UNREVIEWED |
| [Dragon impling ](https://oldschool.runescape.wiki/w/Dragon_impling) | 20 | PARSED_UNREVIEWED |
| [Drake ](https://oldschool.runescape.wiki/w/Drake) | 69 | PARSED_UNREVIEWED |
| [Dreadborn Araxyte ](https://oldschool.runescape.wiki/w/Dreadborn_Araxyte) | 62 | PARSED_UNREVIEWED |
| [Drink troll ](https://oldschool.runescape.wiki/w/Drink_troll) | 11 | PARSED_UNREVIEWED |
| [Drink troll queen ](https://oldschool.runescape.wiki/w/Drink_troll_queen) | 2 | PARSED_UNREVIEWED |
| [Druid ](https://oldschool.runescape.wiki/w/Druid) | 30 | PARSED_UNREVIEWED |
| [Drunken man ](https://oldschool.runescape.wiki/w/Drunken_man) | 35 | PARSED_UNREVIEWED |
| [Duck ](https://oldschool.runescape.wiki/w/Duck) | 0 | NO_SUPPORTED_TABLES |
| [Duckling ](https://oldschool.runescape.wiki/w/Duckling) | 0 | NO_SUPPORTED_TABLES |
| [Duke (monkey) ](https://oldschool.runescape.wiki/w/Duke_(monkey)) | 1 | PARSED_UNREVIEWED |
| [Dungeon rat ](https://oldschool.runescape.wiki/w/Dungeon_rat) | 6 | PARSED_UNREVIEWED |
| [Durial321 ](https://oldschool.runescape.wiki/w/Durial321) | 1 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Durial321 (Deadman) ](https://oldschool.runescape.wiki/w/Durial321_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Dusk (Echo) ](https://oldschool.runescape.wiki/w/Dusk_(Echo)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Dust devil ](https://oldschool.runescape.wiki/w/Dust_devil) | 126 | PARSED_UNREVIEWED |
| [Dwarf ](https://oldschool.runescape.wiki/w/Dwarf) | 33 | PARSED_UNREVIEWED |
| [Dwarf gang member ](https://oldschool.runescape.wiki/w/Dwarf_gang_member) | 1 | PARSED_UNREVIEWED |
| [Eadburg ](https://oldschool.runescape.wiki/w/Eadburg) | 1 | PARSED_UNREVIEWED |
| [Eagle ray ](https://oldschool.runescape.wiki/w/Eagle_ray) | 52 | PARSED_UNREVIEWED |
| [Earth elemental ](https://oldschool.runescape.wiki/w/Earth_elemental) | 36 | PARSED_UNREVIEWED |
| [Earth impling ](https://oldschool.runescape.wiki/w/Earth_impling) | 20 | PARSED_UNREVIEWED |
| [Earth warrior ](https://oldschool.runescape.wiki/w/Earth_warrior) | 63 | PARSED_UNREVIEWED |
| [Earth warrior (RuneScape 2 Beta) ](https://oldschool.runescape.wiki/w/Earth_warrior_(RuneScape_2_Beta)) | 0 | NO_SUPPORTED_TABLES |
| [Earth Warrior Champion ](https://oldschool.runescape.wiki/w/Earth_Warrior_Champion) | 0 | NO_SUPPORTED_TABLES |
| [Earth wizard ](https://oldschool.runescape.wiki/w/Earth_wizard) | 3 | PARSED_UNREVIEWED |
| [Earthen Nagua ](https://oldschool.runescape.wiki/w/Earthen_Nagua) | 31 | PARSED_UNREVIEWED |
| [Eclectic impling ](https://oldschool.runescape.wiki/w/Eclectic_impling) | 20 | PARSED_UNREVIEWED |
| [Eduard ](https://oldschool.runescape.wiki/w/Eduard) | 55 | PARSED_UNREVIEWED |
| [Egg (Tombs of Amascut) ](https://oldschool.runescape.wiki/w/Egg_(Tombs_of_Amascut)) | 0 | NO_SUPPORTED_TABLES |
| [Einar ](https://oldschool.runescape.wiki/w/Einar) | 1 | PARSED_UNREVIEWED |
| [Elder aquanite ](https://oldschool.runescape.wiki/w/Elder_aquanite) | 29 | PARSED_UNREVIEWED |
| [Elder Chaos druid ](https://oldschool.runescape.wiki/w/Elder_Chaos_druid) | 64 | PARSED_UNREVIEWED |
| [Elder custodian stalker ](https://oldschool.runescape.wiki/w/Elder_custodian_stalker) | 44 | PARSED_UNREVIEWED |
| [Elf Archer ](https://oldschool.runescape.wiki/w/Elf_Archer) | 44 | PARSED_UNREVIEWED |
| [Elf Warrior ](https://oldschool.runescape.wiki/w/Elf_Warrior) | 44 | PARSED_UNREVIEWED |
| [Elidinis' Warden (Deadman) ](https://oldschool.runescape.wiki/w/Elidinis%27_Warden_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Elite Black Knight ](https://oldschool.runescape.wiki/w/Elite_Black_Knight) | 34 | PARSED_UNREVIEWED |
| [Elite Dark Mage ](https://oldschool.runescape.wiki/w/Elite_Dark_Mage) | 11 | PARSED_UNREVIEWED |
| [Elite Dark Ranger ](https://oldschool.runescape.wiki/w/Elite_Dark_Ranger) | 11 | PARSED_UNREVIEWED |
| [Elite Dark Warrior ](https://oldschool.runescape.wiki/w/Elite_Dark_Warrior) | 11 | PARSED_UNREVIEWED |
| [Elite Khazard Guard ](https://oldschool.runescape.wiki/w/Elite_Khazard_Guard) | 0 | NO_SUPPORTED_TABLES |
| [Elvarg (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Elvarg_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Embertailed jerboa ](https://oldschool.runescape.wiki/w/Embertailed_jerboa) | 2 | PARSED_UNREVIEWED |
| [Emissary Acolyte ](https://oldschool.runescape.wiki/w/Emissary_Acolyte) | 38 | PARSED_UNREVIEWED |
| [Emissary Archer ](https://oldschool.runescape.wiki/w/Emissary_Archer) | 0 | NO_SUPPORTED_TABLES |
| [Emissary Ascended ](https://oldschool.runescape.wiki/w/Emissary_Ascended) | 39 | PARSED_UNREVIEWED |
| [Emissary Brawler ](https://oldschool.runescape.wiki/w/Emissary_Brawler) | 0 | NO_SUPPORTED_TABLES |
| [Emissary Chosen ](https://oldschool.runescape.wiki/w/Emissary_Chosen) | 38 | PARSED_UNREVIEWED |
| [Emissary Conjurer ](https://oldschool.runescape.wiki/w/Emissary_Conjurer) | 0 | NO_SUPPORTED_TABLES |
| [Emissary Enforcer ](https://oldschool.runescape.wiki/w/Emissary_Enforcer) | 1 | PARSED_UNREVIEWED |
| [Emissary Forebearer (unused) ](https://oldschool.runescape.wiki/w/Emissary_Forebearer_(unused)) | 0 | NO_SUPPORTED_TABLES |
| [Enclave guard ](https://oldschool.runescape.wiki/w/Enclave_guard) | 31 | PARSED_UNREVIEWED |
| [Energy sprite ](https://oldschool.runescape.wiki/w/Energy_sprite) | 0 | NO_SUPPORTED_TABLES |
| [Enormous Tentacle ](https://oldschool.runescape.wiki/w/Enormous_Tentacle) | 0 | NO_SUPPORTED_TABLES |
| [Enraged barbarian spirit ](https://oldschool.runescape.wiki/w/Enraged_barbarian_spirit) | 59 | PARSED_UNREVIEWED |
| [Enraged Boar ](https://oldschool.runescape.wiki/w/Enraged_Boar) | 3 | PARSED_UNREVIEWED |
| [Ent ](https://oldschool.runescape.wiki/w/Ent) | 10 | PARSED_UNREVIEWED |
| [Entrana firebird ](https://oldschool.runescape.wiki/w/Entrana_firebird) | 2 | PARSED_UNREVIEWED |
| [Essence impling ](https://oldschool.runescape.wiki/w/Essence_impling) | 20 | PARSED_UNREVIEWED |
| [Essyllt ](https://oldschool.runescape.wiki/w/Essyllt) | 0 | NO_SUPPORTED_TABLES |
| [Essyllt (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Essyllt_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Evelot ](https://oldschool.runescape.wiki/w/Evelot) | 0 | NO_SUPPORTED_TABLES |
| [Evil Chicken ](https://oldschool.runescape.wiki/w/Evil_Chicken) | 3 | PARSED_UNREVIEWED |
| [Evil Chicken (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Evil_Chicken_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Evil Chicken (Recipe for Disaster) ](https://oldschool.runescape.wiki/w/Evil_Chicken_(Recipe_for_Disaster)) | 4 | PARSED_UNREVIEWED |
| [Evil Creature ](https://oldschool.runescape.wiki/w/Evil_Creature) | 1 | PARSED_UNREVIEWED |
| [Experiment ](https://oldschool.runescape.wiki/w/Experiment) | 3 | PARSED_UNREVIEWED |
| [Experiment No.2 ](https://oldschool.runescape.wiki/w/Experiment_No.2) | 36 | PARSED_UNREVIEWED |
| [Famished shark ](https://oldschool.runescape.wiki/w/Famished_shark) | 33 | PARSED_UNREVIEWED |
| [Fareed (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Fareed_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Farmer ](https://oldschool.runescape.wiki/w/Farmer) | 60 | PARSED_UNREVIEWED |
| [Fear reaper ](https://oldschool.runescape.wiki/w/Fear_reaper) | 0 | NO_SUPPORTED_TABLES |
| [Feral Vampyre ](https://oldschool.runescape.wiki/w/Feral_Vampyre) | 62 | PARSED_UNREVIEWED |
| [Ferocious barbarian spirit ](https://oldschool.runescape.wiki/w/Ferocious_barbarian_spirit) | 54 | PARSED_UNREVIEWED |
| [Fever spider ](https://oldschool.runescape.wiki/w/Fever_spider) | 22 | PARSED_UNREVIEWED |
| [Fire elemental ](https://oldschool.runescape.wiki/w/Fire_elemental) | 35 | PARSED_UNREVIEWED |
| [Fire giant ](https://oldschool.runescape.wiki/w/Fire_giant) | 71 | PARSED_UNREVIEWED |
| [Fire giant (Realm of Memories) ](https://oldschool.runescape.wiki/w/Fire_giant_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Fire Warrior of Lesarkus ](https://oldschool.runescape.wiki/w/Fire_Warrior_of_Lesarkus) | 0 | NO_SUPPORTED_TABLES |
| [Fire wizard ](https://oldschool.runescape.wiki/w/Fire_wizard) | 3 | PARSED_UNREVIEWED |
| [Fiyr Shade ](https://oldschool.runescape.wiki/w/Fiyr_Shade) | 1 | PARSED_UNREVIEWED |
| [Flambeed (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Flambeed_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Flaming pyrelord ](https://oldschool.runescape.wiki/w/Flaming_pyrelord) | 37 | PARSED_UNREVIEWED |
| [Flaming pyrelord (Deadman) ](https://oldschool.runescape.wiki/w/Flaming_pyrelord_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Flawed Golem ](https://oldschool.runescape.wiki/w/Flawed_Golem) | 12 | PARSED_UNREVIEWED |
| [Flesh Crawler ](https://oldschool.runescape.wiki/w/Flesh_Crawler) | 46 | PARSED_UNREVIEWED |
| [Flight Kilisa ](https://oldschool.runescape.wiki/w/Flight_Kilisa) | 21 | PARSED_UNREVIEWED |
| [Flockleader Geerin ](https://oldschool.runescape.wiki/w/Flockleader_Geerin) | 21 | PARSED_UNREVIEWED |
| [Flower ](https://oldschool.runescape.wiki/w/Flower) | 0 | NO_SUPPORTED_TABLES |
| [Flower (A Night at the Theatre) ](https://oldschool.runescape.wiki/w/Flower_(A_Night_at_the_Theatre)) | 0 | NO_SUPPORTED_TABLES |
| [Foreman ](https://oldschool.runescape.wiki/w/Foreman) | 1 | PARSED_UNREVIEWED |
| [Forester (Woodcutting Guild) ](https://oldschool.runescape.wiki/w/Forester_(Woodcutting_Guild)) | 1 | PARSED_UNREVIEWED |
| [Forgotten Soul ](https://oldschool.runescape.wiki/w/Forgotten_Soul) | 46 | PARSED_UNREVIEWED |
| [Forgotten Soul (Soul Wars) ](https://oldschool.runescape.wiki/w/Forgotten_Soul_(Soul_Wars)) | 1 | PARSED_UNREVIEWED |
| [Fortress Guard ](https://oldschool.runescape.wiki/w/Fortress_Guard) | 1 | PARSED_UNREVIEWED |
| [Fox ](https://oldschool.runescape.wiki/w/Fox) | 2 | PARSED_UNREVIEWED |
| [Fragment of Seren ](https://oldschool.runescape.wiki/w/Fragment_of_Seren) | 0 | NO_SUPPORTED_TABLES |
| [Freidir ](https://oldschool.runescape.wiki/w/Freidir) | 51 | PARSED_UNREVIEWED |
| [Fremennik warband archer ](https://oldschool.runescape.wiki/w/Fremennik_warband_archer) | 0 | NO_SUPPORTED_TABLES |
| [Fremennik warband berserker ](https://oldschool.runescape.wiki/w/Fremennik_warband_berserker) | 0 | NO_SUPPORTED_TABLES |
| [Fremennik warband seer ](https://oldschool.runescape.wiki/w/Fremennik_warband_seer) | 0 | NO_SUPPORTED_TABLES |
| [Fremennik warrior ](https://oldschool.runescape.wiki/w/Fremennik_warrior) | 0 | NO_SUPPORTED_TABLES |
| [Frenzied ice troll female ](https://oldschool.runescape.wiki/w/Frenzied_ice_troll_female) | 3 | PARSED_UNREVIEWED |
| [Frenzied ice troll grunt ](https://oldschool.runescape.wiki/w/Frenzied_ice_troll_grunt) | 3 | PARSED_UNREVIEWED |
| [Frenzied ice troll male ](https://oldschool.runescape.wiki/w/Frenzied_ice_troll_male) | 3 | PARSED_UNREVIEWED |
| [Frenzied ice troll runt ](https://oldschool.runescape.wiki/w/Frenzied_ice_troll_runt) | 3 | PARSED_UNREVIEWED |
| [Frigatebird ](https://oldschool.runescape.wiki/w/Frigatebird) | 21 | PARSED_UNREVIEWED |
| [Frogeel ](https://oldschool.runescape.wiki/w/Frogeel) | 4 | PARSED_UNREVIEWED |
| [Frost Crab ](https://oldschool.runescape.wiki/w/Frost_Crab) | 39 | PARSED_UNREVIEWED |
| [Frost dragon ](https://oldschool.runescape.wiki/w/Frost_dragon) | 56 | PARSED_UNREVIEWED |
| [Frost Nagua ](https://oldschool.runescape.wiki/w/Frost_Nagua) | 35 | PARSED_UNREVIEWED |
| [Fumus ](https://oldschool.runescape.wiki/w/Fumus) | 5 | PARSED_UNREVIEWED |
| [Galina ](https://oldschool.runescape.wiki/w/Galina) | 55 | PARSED_UNREVIEWED |
| [Gang boss ](https://oldschool.runescape.wiki/w/Gang_boss) | 31 | PARSED_UNREVIEWED |
| [Gangster ](https://oldschool.runescape.wiki/w/Gangster) | 31 | PARSED_UNREVIEWED |
| [Gardener ](https://oldschool.runescape.wiki/w/Gardener) | 1 | PARSED_UNREVIEWED |
| [Gargoyle ](https://oldschool.runescape.wiki/w/Gargoyle) | 37 | PARSED_UNREVIEWED |
| [Gelatinnoth Mother (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Gelatinnoth_Mother_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [General Danknuck ](https://oldschool.runescape.wiki/w/General_Danknuck) | 10 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [General Graardor (Deadman) ](https://oldschool.runescape.wiki/w/General_Graardor_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [General Graardor (PvM Arena) ](https://oldschool.runescape.wiki/w/General_Graardor_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Georgy ](https://oldschool.runescape.wiki/w/Georgy) | 55 | PARSED_UNREVIEWED |
| [Ghast ](https://oldschool.runescape.wiki/w/Ghast) | 43 | PARSED_UNREVIEWED |
| [Ghost ](https://oldschool.runescape.wiki/w/Ghost) | 2 | PARSED_UNREVIEWED |
| [Ghost (Grave of Scorpius) ](https://oldschool.runescape.wiki/w/Ghost_(Grave_of_Scorpius)) | 1 | PARSED_UNREVIEWED |
| [Ghost (Lassar Undercity) ](https://oldschool.runescape.wiki/w/Ghost_(Lassar_Undercity)) | 0 | NO_SUPPORTED_TABLES |
| [Ghost (Melzar's Maze) ](https://oldschool.runescape.wiki/w/Ghost_(Melzar%27s_Maze)) | 1 | PARSED_UNREVIEWED |
| [Ghoul ](https://oldschool.runescape.wiki/w/Ghoul) | 3 | PARSED_UNREVIEWED |
| [Ghoul Champion ](https://oldschool.runescape.wiki/w/Ghoul_Champion) | 0 | NO_SUPPORTED_TABLES |
| [Giant ant ](https://oldschool.runescape.wiki/w/Giant_ant) | 0 | NO_SUPPORTED_TABLES |
| [Giant bat ](https://oldschool.runescape.wiki/w/Giant_bat) | 3 | PARSED_UNREVIEWED |
| [Giant bat (Brine Rat Cavern) ](https://oldschool.runescape.wiki/w/Giant_bat_(Brine_Rat_Cavern)) | 1 | PARSED_UNREVIEWED |
| [Giant Champion ](https://oldschool.runescape.wiki/w/Giant_Champion) | 2 | PARSED_UNREVIEWED |
| [Giant frog ](https://oldschool.runescape.wiki/w/Giant_frog) | 21 | PARSED_UNREVIEWED |
| [Giant goblin ](https://oldschool.runescape.wiki/w/Giant_goblin) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Giant lobster ](https://oldschool.runescape.wiki/w/Giant_lobster) | 24 | PARSED_UNREVIEWED |
| [Giant lobster (Ghosts Ahoy) ](https://oldschool.runescape.wiki/w/Giant_lobster_(Ghosts_Ahoy)) | 3 | PARSED_UNREVIEWED |
| [Giant lobster (The Red Reef) ](https://oldschool.runescape.wiki/w/Giant_lobster_(The_Red_Reef)) | 0 | NO_SUPPORTED_TABLES |
| [Giant Mole (LANScape) ](https://oldschool.runescape.wiki/w/Giant_Mole_(LANScape)) | 0 | NO_SUPPORTED_TABLES |
| [Giant Mole (PvM Arena) ](https://oldschool.runescape.wiki/w/Giant_Mole_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Giant mosquito ](https://oldschool.runescape.wiki/w/Giant_mosquito) | 0 | NO_SUPPORTED_TABLES |
| [Giant rat ](https://oldschool.runescape.wiki/w/Giant_rat) | 9 | PARSED_UNREVIEWED |
| [Giant rat (Tutorial Island) ](https://oldschool.runescape.wiki/w/Giant_rat_(Tutorial_Island)) | 1 | PARSED_UNREVIEWED |
| [Giant Roc (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Giant_Roc_(Nightmare_Zone)) | 3 | EXCLUDED_ALTERNATE_MODE |
| [Giant Rock Crab ](https://oldschool.runescape.wiki/w/Giant_Rock_Crab) | 66 | EXCLUDED_ALTERNATE_MODE |
| [Giant rockslug ](https://oldschool.runescape.wiki/w/Giant_rockslug) | 56 | PARSED_UNREVIEWED |
| [Giant Scarab (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Giant_Scarab_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Giant skeleton (Shadow Dungeon) ](https://oldschool.runescape.wiki/w/Giant_skeleton_(Shadow_Dungeon)) | 45 | PARSED_UNREVIEWED |
| [Giant skeleton (Tarn's Lair) ](https://oldschool.runescape.wiki/w/Giant_skeleton_(Tarn%27s_Lair)) | 66 | PARSED_UNREVIEWED |
| [Giant snail ](https://oldschool.runescape.wiki/w/Giant_snail) | 2 | PARSED_UNREVIEWED |
| [Giant spider ](https://oldschool.runescape.wiki/w/Giant_spider) | 4 | PARSED_UNREVIEWED |
| [Giant wasp ](https://oldschool.runescape.wiki/w/Giant_wasp) | 0 | NO_SUPPORTED_TABLES |
| [Glacies ](https://oldschool.runescape.wiki/w/Glacies) | 5 | PARSED_UNREVIEWED |
| [Glod (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Glod_(Nightmare_Zone)) | 3 | EXCLUDED_ALTERNATE_MODE |
| [Glowing crystal ](https://oldschool.runescape.wiki/w/Glowing_crystal) | 0 | NO_SUPPORTED_TABLES |
| [Gnome ](https://oldschool.runescape.wiki/w/Gnome) | 10 | PARSED_UNREVIEWED |
| [Gnome Archer ](https://oldschool.runescape.wiki/w/Gnome_Archer) | 1 | PARSED_UNREVIEWED |
| [Gnome child ](https://oldschool.runescape.wiki/w/Gnome_child) | 9 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Gnome Driver ](https://oldschool.runescape.wiki/w/Gnome_Driver) | 1 | PARSED_UNREVIEWED |
| [Gnome guard ](https://oldschool.runescape.wiki/w/Gnome_guard) | 1 | PARSED_UNREVIEWED |
| [Gnome Mage ](https://oldschool.runescape.wiki/w/Gnome_Mage) | 1 | PARSED_UNREVIEWED |
| [Gnome troop ](https://oldschool.runescape.wiki/w/Gnome_troop) | 1 | PARSED_UNREVIEWED |
| [Gnome woman ](https://oldschool.runescape.wiki/w/Gnome_woman) | 9 | PARSED_UNREVIEWED |
| [Goat ](https://oldschool.runescape.wiki/w/Goat) | 2 | PARSED_UNREVIEWED |
| [Goblin ](https://oldschool.runescape.wiki/w/Goblin) | 71 | PARSED_UNREVIEWED |
| [Goblin (Goblin Village) ](https://oldschool.runescape.wiki/w/Goblin_(Goblin_Village)) | 40 | PARSED_UNREVIEWED |
| [Goblin (God Wars Dungeon) ](https://oldschool.runescape.wiki/w/Goblin_(God_Wars_Dungeon)) | 40 | PARSED_UNREVIEWED |
| [Goblin (historical) ](https://oldschool.runescape.wiki/w/Goblin_(historical)) | 0 | NO_SUPPORTED_TABLES |
| [Goblin (Realm of Memories) ](https://oldschool.runescape.wiki/w/Goblin_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Goblin (Vault of War) ](https://oldschool.runescape.wiki/w/Goblin_(Vault_of_War)) | 39 | PARSED_UNREVIEWED |
| [Goblin Champion ](https://oldschool.runescape.wiki/w/Goblin_Champion) | 0 | NO_SUPPORTED_TABLES |
| [Goblin guard ](https://oldschool.runescape.wiki/w/Goblin_guard) | 25 | PARSED_UNREVIEWED |
| [Goblin lieutenant ](https://oldschool.runescape.wiki/w/Goblin_lieutenant) | 4 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Goblin priest ](https://oldschool.runescape.wiki/w/Goblin_priest) | 0 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Goblin raider ](https://oldschool.runescape.wiki/w/Goblin_raider) | 41 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Golem (unused NPC) ](https://oldschool.runescape.wiki/w/Golem_(unused_NPC)) | 0 | NO_SUPPORTED_TABLES |
| [Golem guard ](https://oldschool.runescape.wiki/w/Golem_guard) | 0 | NO_SUPPORTED_TABLES |
| [Gorad ](https://oldschool.runescape.wiki/w/Gorad) | 31 | PARSED_UNREVIEWED |
| [Gorak ](https://oldschool.runescape.wiki/w/Gorak) | 66 | PARSED_UNREVIEWED |
| [Gourmet impling ](https://oldschool.runescape.wiki/w/Gourmet_impling) | 20 | PARSED_UNREVIEWED |
| [Grave scorpion ](https://oldschool.runescape.wiki/w/Grave_scorpion) | 2 | PARSED_UNREVIEWED |
| [Great white shark ](https://oldschool.runescape.wiki/w/Great_white_shark) | 37 | PARSED_UNREVIEWED |
| [Greater abyssal demon ](https://oldschool.runescape.wiki/w/Greater_abyssal_demon) | 77 | PARSED_UNREVIEWED |
| [Greater abyssal demon (Deadman) ](https://oldschool.runescape.wiki/w/Greater_abyssal_demon_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Greater demon ](https://oldschool.runescape.wiki/w/Greater_demon) | 102 | PARSED_UNREVIEWED |
| [Greater demon (Realm of Memories) ](https://oldschool.runescape.wiki/w/Greater_demon_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Greater demon (The Scar) ](https://oldschool.runescape.wiki/w/Greater_demon_(The_Scar)) | 0 | NO_SUPPORTED_TABLES |
| [Greater Nechryael ](https://oldschool.runescape.wiki/w/Greater_Nechryael) | 146 | PARSED_UNREVIEWED |
| [Greater Skeleton Hellhound (Calvar'ion) ](https://oldschool.runescape.wiki/w/Greater_Skeleton_Hellhound_(Calvar%27ion)) | 5 | PARSED_UNREVIEWED |
| [Greater Skeleton Hellhound (Vet'ion) ](https://oldschool.runescape.wiki/w/Greater_Skeleton_Hellhound_(Vet%27ion)) | 5 | PARSED_UNREVIEWED |
| [Green dragon ](https://oldschool.runescape.wiki/w/Green_dragon) | 123 | PARSED_UNREVIEWED |
| [Grimy Lizard ](https://oldschool.runescape.wiki/w/Grimy_Lizard) | 68 | PARSED_UNREVIEWED |
| [Grip ](https://oldschool.runescape.wiki/w/Grip) | 0 | NO_SUPPORTED_TABLES |
| [Grizzly bear ](https://oldschool.runescape.wiki/w/Grizzly_bear) | 9 | PARSED_UNREVIEWED |
| [Grizzly bear (Escape Caves) ](https://oldschool.runescape.wiki/w/Grizzly_bear_(Escape_Caves)) | 0 | NO_SUPPORTED_TABLES |
| [Grizzly bear cub ](https://oldschool.runescape.wiki/w/Grizzly_bear_cub) | 6 | PARSED_UNREVIEWED |
| [Growler ](https://oldschool.runescape.wiki/w/Growler) | 18 | PARSED_UNREVIEWED |
| [Growthling ](https://oldschool.runescape.wiki/w/Growthling) | 0 | NO_SUPPORTED_TABLES |
| [Gryphon ](https://oldschool.runescape.wiki/w/Gryphon) | 52 | PARSED_UNREVIEWED |
| [Gryphon (Troubled Tortugans) ](https://oldschool.runescape.wiki/w/Gryphon_(Troubled_Tortugans)) | 0 | NO_SUPPORTED_TABLES |
| [Guard ](https://oldschool.runescape.wiki/w/Guard) | 37 | PARSED_UNREVIEWED |
| [Guard (Aldarin) ](https://oldschool.runescape.wiki/w/Guard_(Aldarin)) | 33 | PARSED_UNREVIEWED |
| [Guard (Another Slice of H.A.M.) ](https://oldschool.runescape.wiki/w/Guard_(Another_Slice_of_H.A.M.)) | 0 | NO_SUPPORTED_TABLES |
| [Guard (Auburnvale) ](https://oldschool.runescape.wiki/w/Guard_(Auburnvale)) | 34 | PARSED_UNREVIEWED |
| [Guard (Burthorpe) ](https://oldschool.runescape.wiki/w/Guard_(Burthorpe)) | 24 | PARSED_UNREVIEWED |
| [Guard (Cave goblin) ](https://oldschool.runescape.wiki/w/Guard_(Cave_goblin)) | 10 | PARSED_UNREVIEWED |
| [Guard (Deadman Mode) ](https://oldschool.runescape.wiki/w/Guard_(Deadman_Mode)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Guard (Death to the Dorgeshuun) ](https://oldschool.runescape.wiki/w/Guard_(Death_to_the_Dorgeshuun)) | 0 | NO_SUPPORTED_TABLES |
| [Guard (Desert Mining Camp) ](https://oldschool.runescape.wiki/w/Guard_(Desert_Mining_Camp)) | 1 | PARSED_UNREVIEWED |
| [Guard (Doric's hut) ](https://oldschool.runescape.wiki/w/Guard_(Doric%27s_hut)) | 36 | PARSED_UNREVIEWED |
| [Guard (dwarf) ](https://oldschool.runescape.wiki/w/Guard_(dwarf)) | 1 | PARSED_UNREVIEWED |
| [Guard (H.A.M. Storerooms) ](https://oldschool.runescape.wiki/w/Guard_(H.A.M._Storerooms)) | 44 | PARSED_UNREVIEWED |
| [Guard (Hosidius) ](https://oldschool.runescape.wiki/w/Guard_(Hosidius)) | 33 | PARSED_UNREVIEWED |
| [Guard (Port Roberts) ](https://oldschool.runescape.wiki/w/Guard_(Port_Roberts)) | 36 | PARSED_UNREVIEWED |
| [Guard (Port Sarim jail) ](https://oldschool.runescape.wiki/w/Guard_(Port_Sarim_jail)) | 36 | PARSED_UNREVIEWED |
| [Guard (Prifddinas) ](https://oldschool.runescape.wiki/w/Guard_(Prifddinas)) | 46 | PARSED_UNREVIEWED |
| [Guard (Ranging Guild) ](https://oldschool.runescape.wiki/w/Guard_(Ranging_Guild)) | 1 | PARSED_UNREVIEWED |
| [Guard (Shayzien) ](https://oldschool.runescape.wiki/w/Guard_(Shayzien)) | 34 | PARSED_UNREVIEWED |
| [Guard (Tlati Rainforest) ](https://oldschool.runescape.wiki/w/Guard_(Tlati_Rainforest)) | 36 | PARSED_UNREVIEWED |
| [Guard (unused) ](https://oldschool.runescape.wiki/w/Guard_(unused)) | 0 | NO_SUPPORTED_TABLES |
| [Guard (Varlamore) ](https://oldschool.runescape.wiki/w/Guard_(Varlamore)) | 34 | PARSED_UNREVIEWED |
| [Guard Bandit ](https://oldschool.runescape.wiki/w/Guard_Bandit) | 4 | PARSED_UNREVIEWED |
| [Guard dog ](https://oldschool.runescape.wiki/w/Guard_dog) | 3 | PARSED_UNREVIEWED |
| [Guard dog (Construction) ](https://oldschool.runescape.wiki/w/Guard_dog_(Construction)) | 0 | NO_SUPPORTED_TABLES |
| [Guardian (Chambers of Xeric) ](https://oldschool.runescape.wiki/w/Guardian_(Chambers_of_Xeric)) | 3 | PARSED_UNREVIEWED |
| [Guardian Drake ](https://oldschool.runescape.wiki/w/Guardian_Drake) | 73 | PARSED_UNREVIEWED |
| [Guardian of Armadyl ](https://oldschool.runescape.wiki/w/Guardian_of_Armadyl) | 2 | PARSED_UNREVIEWED |
| [Gunthor the brave ](https://oldschool.runescape.wiki/w/Gunthor_the_brave) | 37 | PARSED_UNREVIEWED |
| [H.A.M. Archer ](https://oldschool.runescape.wiki/w/H.A.M._Archer) | 0 | NO_SUPPORTED_TABLES |
| [H.A.M. Guard ](https://oldschool.runescape.wiki/w/H.A.M._Guard) | 49 | PARSED_UNREVIEWED |
| [H.A.M. Mage ](https://oldschool.runescape.wiki/w/H.A.M._Mage) | 0 | NO_SUPPORTED_TABLES |
| [Halla (Miscellania) ](https://oldschool.runescape.wiki/w/Halla_(Miscellania)) | 1 | PARSED_UNREVIEWED |
| [Hammerhead shark ](https://oldschool.runescape.wiki/w/Hammerhead_shark) | 33 | PARSED_UNREVIEWED |
| [Harpie Bug Swarm ](https://oldschool.runescape.wiki/w/Harpie_Bug_Swarm) | 30 | PARSED_UNREVIEWED |
| [Haze ](https://oldschool.runescape.wiki/w/Haze) | 0 | NO_SUPPORTED_TABLES |
| [Head (Temple Trekking) ](https://oldschool.runescape.wiki/w/Head_(Temple_Trekking)) | 0 | NO_SUPPORTED_TABLES |
| [Head Guard ](https://oldschool.runescape.wiki/w/Head_Guard) | 46 | PARSED_UNREVIEWED |
| [Head Menaphite Guard ](https://oldschool.runescape.wiki/w/Head_Menaphite_Guard) | 0 | NO_SUPPORTED_TABLES |
| [Head Thief ](https://oldschool.runescape.wiki/w/Head_Thief) | 1 | PARSED_UNREVIEWED |
| [Headless Beast ](https://oldschool.runescape.wiki/w/Headless_Beast) | 3 | PARSED_UNREVIEWED |
| [Headless Beast (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Headless_Beast_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Healing totem ](https://oldschool.runescape.wiki/w/Healing_totem) | 0 | NO_SUPPORTED_TABLES |
| [Hellhound ](https://oldschool.runescape.wiki/w/Hellhound) | 53 | PARSED_UNREVIEWED |
| [Hellhound (Construction) ](https://oldschool.runescape.wiki/w/Hellhound_(Construction)) | 0 | NO_SUPPORTED_TABLES |
| [Hengel ](https://oldschool.runescape.wiki/w/Hengel) | 34 | PARSED_UNREVIEWED |
| [Hermit crab ](https://oldschool.runescape.wiki/w/Hermit_crab) | 39 | PARSED_UNREVIEWED |
| [Hero ](https://oldschool.runescape.wiki/w/Hero) | 11 | PARSED_UNREVIEWED |
| [Hespori (A Night at the Theatre) ](https://oldschool.runescape.wiki/w/Hespori_(A_Night_at_the_Theatre)) | 0 | NO_SUPPORTED_TABLES |
| [Hespori (Echo) ](https://oldschool.runescape.wiki/w/Hespori_(Echo)) | 2 | EXCLUDED_ALTERNATE_MODE |
| [High Priest (2013 Goblin Invasion) ](https://oldschool.runescape.wiki/w/High_Priest_(2013_Goblin_Invasion)) | 1 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Highwayman ](https://oldschool.runescape.wiki/w/Highwayman) | 4 | PARSED_UNREVIEWED |
| [Hill Giant ](https://oldschool.runescape.wiki/w/Hill_Giant) | 74 | PARSED_UNREVIEWED |
| [Hill Giant (Realm of Memories) ](https://oldschool.runescape.wiki/w/Hill_Giant_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Hobgoblin ](https://oldschool.runescape.wiki/w/Hobgoblin) | 139 | PARSED_UNREVIEWED |
| [Hobgoblin (The Slug Menace) ](https://oldschool.runescape.wiki/w/Hobgoblin_(The_Slug_Menace)) | 0 | NO_SUPPORTED_TABLES |
| [Hobgoblin Champion ](https://oldschool.runescape.wiki/w/Hobgoblin_Champion) | 0 | NO_SUPPORTED_TABLES |
| [Hobgoblin guard ](https://oldschool.runescape.wiki/w/Hobgoblin_guard) | 0 | NO_SUPPORTED_TABLES |
| [Holthion ](https://oldschool.runescape.wiki/w/Holthion) | 2 | PARSED_UNREVIEWED |
| [Honour guard ](https://oldschool.runescape.wiki/w/Honour_guard) | 0 | NO_SUPPORTED_TABLES |
| [Hoop Snake ](https://oldschool.runescape.wiki/w/Hoop_Snake) | 7 | PARSED_UNREVIEWED |
| [Hopeless creature ](https://oldschool.runescape.wiki/w/Hopeless_creature) | 0 | NO_SUPPORTED_TABLES |
| [Huge spider ](https://oldschool.runescape.wiki/w/Huge_spider) | 0 | NO_SUPPORTED_TABLES |
| [Hydra ](https://oldschool.runescape.wiki/w/Hydra) | 76 | PARSED_UNREVIEWED |
| [Hygd ](https://oldschool.runescape.wiki/w/Hygd) | 32 | PARSED_UNREVIEWED |
| [I DSCIM YOU ](https://oldschool.runescape.wiki/w/I_DSCIM_YOU) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Ice giant ](https://oldschool.runescape.wiki/w/Ice_giant) | 163 | PARSED_UNREVIEWED |
| [Ice Queen ](https://oldschool.runescape.wiki/w/Ice_Queen) | 1 | PARSED_UNREVIEWED |
| [Ice spider ](https://oldschool.runescape.wiki/w/Ice_spider) | 6 | PARSED_UNREVIEWED |
| [Ice troll ](https://oldschool.runescape.wiki/w/Ice_troll) | 45 | PARSED_UNREVIEWED |
| [Ice troll female ](https://oldschool.runescape.wiki/w/Ice_troll_female) | 72 | PARSED_UNREVIEWED |
| [Ice troll grunt ](https://oldschool.runescape.wiki/w/Ice_troll_grunt) | 69 | PARSED_UNREVIEWED |
| [Ice Troll King (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Ice_Troll_King_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Ice troll male ](https://oldschool.runescape.wiki/w/Ice_troll_male) | 139 | PARSED_UNREVIEWED |
| [Ice troll runt ](https://oldschool.runescape.wiki/w/Ice_troll_runt) | 139 | PARSED_UNREVIEWED |
| [Ice warrior ](https://oldschool.runescape.wiki/w/Ice_warrior) | 69 | PARSED_UNREVIEWED |
| [Ice wolf ](https://oldschool.runescape.wiki/w/Ice_wolf) | 2 | PARSED_UNREVIEWED |
| [Icefiend ](https://oldschool.runescape.wiki/w/Icefiend) | 23 | PARSED_UNREVIEWED |
| [Icelord ](https://oldschool.runescape.wiki/w/Icelord) | 3 | PARSED_UNREVIEWED |
| [Imp ](https://oldschool.runescape.wiki/w/Imp) | 37 | PARSED_UNREVIEWED |
| [Imp (unused) ](https://oldschool.runescape.wiki/w/Imp_(unused)) | 0 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Imp Champion ](https://oldschool.runescape.wiki/w/Imp_Champion) | 0 | NO_SUPPORTED_TABLES |
| [Impaler deer ](https://oldschool.runescape.wiki/w/Impaler_deer) | 2 | EXCLUDED_ALTERNATE_MODE |
| [Imre ](https://oldschool.runescape.wiki/w/Imre) | 55 | PARSED_UNREVIEWED |
| [Infernal Mage ](https://oldschool.runescape.wiki/w/Infernal_Mage) | 25 | PARSED_UNREVIEWED |
| [Infernal pyrelord ](https://oldschool.runescape.wiki/w/Infernal_pyrelord) | 35 | PARSED_UNREVIEWED |
| [Insatiable Bloodveld ](https://oldschool.runescape.wiki/w/Insatiable_Bloodveld) | 53 | PARSED_UNREVIEWED |
| [Insatiable mutated Bloodveld ](https://oldschool.runescape.wiki/w/Insatiable_mutated_Bloodveld) | 65 | PARSED_UNREVIEWED |
| [Invrigar the Necromancer ](https://oldschool.runescape.wiki/w/Invrigar_the_Necromancer) | 1 | PARSED_UNREVIEWED |
| [Iorwerth Archer ](https://oldschool.runescape.wiki/w/Iorwerth_Archer) | 58 | PARSED_UNREVIEWED |
| [Iorwerth Warrior ](https://oldschool.runescape.wiki/w/Iorwerth_Warrior) | 104 | PARSED_UNREVIEWED |
| [Irina ](https://oldschool.runescape.wiki/w/Irina) | 55 | PARSED_UNREVIEWED |
| [Iron dragon ](https://oldschool.runescape.wiki/w/Iron_dragon) | 57 | PARSED_UNREVIEWED |
| [Irvig Senay ](https://oldschool.runescape.wiki/w/Irvig_Senay) | 1 | PARSED_UNREVIEWED |
| [Ithoi the Navigator ](https://oldschool.runescape.wiki/w/Ithoi_the_Navigator) | 0 | NO_SUPPORTED_TABLES |
| [Jackal ](https://oldschool.runescape.wiki/w/Jackal) | 2 | PARSED_UNREVIEWED |
| [Jaguar ](https://oldschool.runescape.wiki/w/Jaguar) | 3 | PARSED_UNREVIEWED |
| [Jaguar cub ](https://oldschool.runescape.wiki/w/Jaguar_cub) | 2 | PARSED_UNREVIEWED |
| [Jaguar warrior ](https://oldschool.runescape.wiki/w/Jaguar_warrior) | 0 | NO_SUPPORTED_TABLES |
| [Jaguar warrior (Deadman) ](https://oldschool.runescape.wiki/w/Jaguar_warrior_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Jail guard ](https://oldschool.runescape.wiki/w/Jail_guard) | 1 | PARSED_UNREVIEWED |
| [Jailer ](https://oldschool.runescape.wiki/w/Jailer) | 2 | PARSED_UNREVIEWED |
| [Jake ](https://oldschool.runescape.wiki/w/Jake) | 1 | PARSED_UNREVIEWED |
| [Jal-Ak ](https://oldschool.runescape.wiki/w/Jal-Ak) | 0 | NO_SUPPORTED_TABLES |
| [Jal-AkRek-Ket ](https://oldschool.runescape.wiki/w/Jal-AkRek-Ket) | 0 | NO_SUPPORTED_TABLES |
| [Jal-AkRek-Mej ](https://oldschool.runescape.wiki/w/Jal-AkRek-Mej) | 0 | NO_SUPPORTED_TABLES |
| [Jal-AkRek-Xil ](https://oldschool.runescape.wiki/w/Jal-AkRek-Xil) | 0 | NO_SUPPORTED_TABLES |
| [Jal-ImKot ](https://oldschool.runescape.wiki/w/Jal-ImKot) | 0 | NO_SUPPORTED_TABLES |
| [Jal-ImKot (Deadman) ](https://oldschool.runescape.wiki/w/Jal-ImKot_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Jal-MejJak ](https://oldschool.runescape.wiki/w/Jal-MejJak) | 0 | NO_SUPPORTED_TABLES |
| [Jal-MejRah ](https://oldschool.runescape.wiki/w/Jal-MejRah) | 0 | NO_SUPPORTED_TABLES |
| [Jal-Nib ](https://oldschool.runescape.wiki/w/Jal-Nib) | 0 | NO_SUPPORTED_TABLES |
| [Jal-Xil ](https://oldschool.runescape.wiki/w/Jal-Xil) | 0 | NO_SUPPORTED_TABLES |
| [Jal-Zek ](https://oldschool.runescape.wiki/w/Jal-Zek) | 0 | NO_SUPPORTED_TABLES |
| [JalTok-Jad ](https://oldschool.runescape.wiki/w/JalTok-Jad) | 0 | NO_SUPPORTED_TABLES |
| [Javelin Colossus ](https://oldschool.runescape.wiki/w/Javelin_Colossus) | 0 | NO_SUPPORTED_TABLES |
| [Jeff ](https://oldschool.runescape.wiki/w/Jeff) | 34 | PARSED_UNREVIEWED |
| [Jelly ](https://oldschool.runescape.wiki/w/Jelly) | 80 | PARSED_UNREVIEWED |
| [Jennella ](https://oldschool.runescape.wiki/w/Jennella) | 51 | PARSED_UNREVIEWED |
| [Jhallan ](https://oldschool.runescape.wiki/w/Jhallan) | 0 | NO_SUPPORTED_TABLES |
| [Jim ](https://oldschool.runescape.wiki/w/Jim) | 0 | NO_SUPPORTED_TABLES |
| [Jogre ](https://oldschool.runescape.wiki/w/Jogre) | 69 | PARSED_UNREVIEWED |
| [Jogre Champion ](https://oldschool.runescape.wiki/w/Jogre_Champion) | 0 | NO_SUPPORTED_TABLES |
| [Jonny the Beard ](https://oldschool.runescape.wiki/w/Jonny_the_Beard) | 2 | PARSED_UNREVIEWED |
| [Joseph ](https://oldschool.runescape.wiki/w/Joseph) | 55 | PARSED_UNREVIEWED |
| [Jubbly bird ](https://oldschool.runescape.wiki/w/Jubbly_bird) | 3 | PARSED_UNREVIEWED |
| [Jubster ](https://oldschool.runescape.wiki/w/Jubster) | 4 | PARSED_UNREVIEWED |
| [Judge of Yama ](https://oldschool.runescape.wiki/w/Judge_of_Yama) | 0 | NO_SUPPORTED_TABLES |
| [Jungle Demon (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Jungle_Demon_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Jungle horror ](https://oldschool.runescape.wiki/w/Jungle_horror) | 50 | PARSED_UNREVIEWED |
| [Jungle spider ](https://oldschool.runescape.wiki/w/Jungle_spider) | 1 | PARSED_UNREVIEWED |
| [Jungle spider (Ape Atoll) ](https://oldschool.runescape.wiki/w/Jungle_spider_(Ape_Atoll)) | 1 | PARSED_UNREVIEWED |
| [Jungle Wolf ](https://oldschool.runescape.wiki/w/Jungle_Wolf) | 2 | PARSED_UNREVIEWED |
| [Justiciar Zachariah ](https://oldschool.runescape.wiki/w/Justiciar_Zachariah) | 2 | PARSED_UNREVIEWED |
| [Justiciar Zachariah (Deadman) ](https://oldschool.runescape.wiki/w/Justiciar_Zachariah_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Juvenile custodian stalker ](https://oldschool.runescape.wiki/w/Juvenile_custodian_stalker) | 41 | PARSED_UNREVIEWED |
| [K'ril Tsutsaroth (Deadman) ](https://oldschool.runescape.wiki/w/K%27ril_Tsutsaroth_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [K'ril Tsutsaroth (PvM Arena) ](https://oldschool.runescape.wiki/w/K%27ril_Tsutsaroth_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Kalphite Guardian ](https://oldschool.runescape.wiki/w/Kalphite_Guardian) | 60 | PARSED_UNREVIEWED |
| [Kalphite Queen (Echo) ](https://oldschool.runescape.wiki/w/Kalphite_Queen_(Echo)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Kalphite Queen (PvM Arena) ](https://oldschool.runescape.wiki/w/Kalphite_Queen_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Kalphite Queen (Realm of Memories) ](https://oldschool.runescape.wiki/w/Kalphite_Queen_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Kalphite Soldier ](https://oldschool.runescape.wiki/w/Kalphite_Soldier) | 43 | PARSED_UNREVIEWED |
| [Kalphite soldier (Construction) ](https://oldschool.runescape.wiki/w/Kalphite_soldier_(Construction)) | 0 | NO_SUPPORTED_TABLES |
| [Kalphite Worker ](https://oldschool.runescape.wiki/w/Kalphite_Worker) | 45 | PARSED_UNREVIEWED |
| [Kalrag ](https://oldschool.runescape.wiki/w/Kalrag) | 0 | NO_SUPPORTED_TABLES |
| [Kamil (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Kamil_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Karamel (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Karamel_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Karamjan tiger shark ](https://oldschool.runescape.wiki/w/Karamjan_tiger_shark) | 34 | PARSED_UNREVIEWED |
| [Kasonde ](https://oldschool.runescape.wiki/w/Kasonde) | 0 | NO_SUPPORTED_TABLES |
| [Kasonde the Craven ](https://oldschool.runescape.wiki/w/Kasonde_the_Craven) | 0 | NO_SUPPORTED_TABLES |
| [Kebbit (Eagles' Peak) ](https://oldschool.runescape.wiki/w/Kebbit_(Eagles%27_Peak)) | 2 | PARSED_UNREVIEWED |
| [Keef ](https://oldschool.runescape.wiki/w/Keef) | 0 | NO_SUPPORTED_TABLES |
| [Ket-Zek ](https://oldschool.runescape.wiki/w/Ket-Zek) | 0 | NO_SUPPORTED_TABLES |
| [Ketla the Unworthy ](https://oldschool.runescape.wiki/w/Ketla_the_Unworthy) | 0 | NO_SUPPORTED_TABLES |
| [Kharazi monodon ](https://oldschool.runescape.wiki/w/Kharazi_monodon) | 58 | PARSED_UNREVIEWED |
| [Khazard commander ](https://oldschool.runescape.wiki/w/Khazard_commander) | 2 | PARSED_UNREVIEWED |
| [Khazard Guard ](https://oldschool.runescape.wiki/w/Khazard_Guard) | 1 | PARSED_UNREVIEWED |
| [Khazard Ogre ](https://oldschool.runescape.wiki/w/Khazard_Ogre) | 2 | PARSED_UNREVIEWED |
| [Khazard Scorpion ](https://oldschool.runescape.wiki/w/Khazard_Scorpion) | 0 | NO_SUPPORTED_TABLES |
| [Khazard trooper ](https://oldschool.runescape.wiki/w/Khazard_trooper) | 1 | PARSED_UNREVIEWED |
| [Khazard warlord ](https://oldschool.runescape.wiki/w/Khazard_warlord) | 2 | PARSED_UNREVIEWED |
| [Khazard warlord (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Khazard_warlord_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Killerwatt ](https://oldschool.runescape.wiki/w/Killerwatt) | 49 | PARSED_UNREVIEWED |
| [King Black Dragon (Deadman) ](https://oldschool.runescape.wiki/w/King_Black_Dragon_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [King Black Dragon (Echo) ](https://oldschool.runescape.wiki/w/King_Black_Dragon_(Echo)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [King Black Dragon (PvM Arena) ](https://oldschool.runescape.wiki/w/King_Black_Dragon_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [King black dragon (Realm of Memories) ](https://oldschool.runescape.wiki/w/King_black_dragon_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [King kurask ](https://oldschool.runescape.wiki/w/King_kurask) | 65 | PARSED_UNREVIEWED |
| [King Roald ](https://oldschool.runescape.wiki/w/King_Roald) | 0 | NO_SUPPORTED_TABLES |
| [King Roald (Nightmare Zone) ](https://oldschool.runescape.wiki/w/King_Roald_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [King Sand Crab ](https://oldschool.runescape.wiki/w/King_Sand_Crab) | 70 | PARSED_UNREVIEWED |
| [King Scorpion ](https://oldschool.runescape.wiki/w/King_Scorpion) | 5 | PARSED_UNREVIEWED |
| [King Scorpion (Realm of Memories) ](https://oldschool.runescape.wiki/w/King_Scorpion_(Realm_of_Memories)) | 2 | EXCLUDED_ALTERNATE_MODE |
| [King Snagwort ](https://oldschool.runescape.wiki/w/King_Snagwort) | 2 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Knight of Ardougne ](https://oldschool.runescape.wiki/w/Knight_of_Ardougne) | 3 | PARSED_UNREVIEWED |
| [Knight of Ardougne (Song of the Elves) ](https://oldschool.runescape.wiki/w/Knight_of_Ardougne_(Song_of_the_Elves)) | 0 | NO_SUPPORTED_TABLES |
| [Knight of Ardougne (unused) ](https://oldschool.runescape.wiki/w/Knight_of_Ardougne_(unused)) | 0 | NO_SUPPORTED_TABLES |
| [Knight of Saradomin ](https://oldschool.runescape.wiki/w/Knight_of_Saradomin) | 4 | PARSED_UNREVIEWED |
| [Knight of Varlamore ](https://oldschool.runescape.wiki/w/Knight_of_Varlamore) | 17 | PARSED_UNREVIEWED |
| [Knight of Varlamore (Twilight's Promise) ](https://oldschool.runescape.wiki/w/Knight_of_Varlamore_(Twilight%27s_Promise)) | 0 | NO_SUPPORTED_TABLES |
| [Kob ](https://oldschool.runescape.wiki/w/Kob) | 0 | NO_SUPPORTED_TABLES |
| [Kolodion ](https://oldschool.runescape.wiki/w/Kolodion) | 0 | NO_SUPPORTED_TABLES |
| [Kraka ](https://oldschool.runescape.wiki/w/Kraka) | 49 | PARSED_UNREVIEWED |
| [Kree'arra (Deadman) ](https://oldschool.runescape.wiki/w/Kree%27arra_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Kree'arra (PvM Arena) ](https://oldschool.runescape.wiki/w/Kree%27arra_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Kroy ](https://oldschool.runescape.wiki/w/Kroy) | 0 | NO_SUPPORTED_TABLES |
| [Kruk ](https://oldschool.runescape.wiki/w/Kruk) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Ksenia ](https://oldschool.runescape.wiki/w/Ksenia) | 55 | PARSED_UNREVIEWED |
| [Kurask ](https://oldschool.runescape.wiki/w/Kurask) | 61 | PARSED_UNREVIEWED |
| [Lanzig ](https://oldschool.runescape.wiki/w/Lanzig) | 51 | PARSED_UNREVIEWED |
| [Large chicken ](https://oldschool.runescape.wiki/w/Large_chicken) | 0 | NO_SUPPORTED_TABLES |
| [Large mosquito ](https://oldschool.runescape.wiki/w/Large_mosquito) | 1 | PARSED_UNREVIEWED |
| [Large reef shark ](https://oldschool.runescape.wiki/w/Large_reef_shark) | 0 | NO_SUPPORTED_TABLES |
| [Lava beast ](https://oldschool.runescape.wiki/w/Lava_beast) | 0 | NO_SUPPORTED_TABLES |
| [Lava dragon ](https://oldschool.runescape.wiki/w/Lava_dragon) | 74 | PARSED_UNREVIEWED |
| [Lava Strykewyrm ](https://oldschool.runescape.wiki/w/Lava_Strykewyrm) | 50 | PARSED_UNREVIEWED |
| [Leech ](https://oldschool.runescape.wiki/w/Leech) | 0 | NO_SUPPORTED_TABLES |
| [Lensa ](https://oldschool.runescape.wiki/w/Lensa) | 51 | PARSED_UNREVIEWED |
| [Leon d'Cour ](https://oldschool.runescape.wiki/w/Leon_d%27Cour) | 0 | NO_SUPPORTED_TABLES |
| [Lesser demon ](https://oldschool.runescape.wiki/w/Lesser_demon) | 122 | PARSED_UNREVIEWED |
| [Lesser demon (Melzar's Maze) ](https://oldschool.runescape.wiki/w/Lesser_demon_(Melzar%27s_Maze)) | 2 | PARSED_UNREVIEWED |
| [Lesser demon (Realm of Memories) ](https://oldschool.runescape.wiki/w/Lesser_demon_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Lesser demon (The Scar) ](https://oldschool.runescape.wiki/w/Lesser_demon_(The_Scar)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Lesser Demon Champion ](https://oldschool.runescape.wiki/w/Lesser_Demon_Champion) | 0 | NO_SUPPORTED_TABLES |
| [Lev ](https://oldschool.runescape.wiki/w/Lev) | 55 | PARSED_UNREVIEWED |
| [Liliya ](https://oldschool.runescape.wiki/w/Liliya) | 55 | PARSED_UNREVIEWED |
| [Little monster ](https://oldschool.runescape.wiki/w/Little_monster) | 0 | NO_SUPPORTED_TABLES |
| [Lizard ](https://oldschool.runescape.wiki/w/Lizard) | 50 | PARSED_UNREVIEWED |
| [Lizard man (Realm of Memories) ](https://oldschool.runescape.wiki/w/Lizard_man_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Lizardman ](https://oldschool.runescape.wiki/w/Lizardman) | 60 | PARSED_UNREVIEWED |
| [Lizardman brute ](https://oldschool.runescape.wiki/w/Lizardman_brute) | 30 | PARSED_UNREVIEWED |
| [Lizardman shaman ](https://oldschool.runescape.wiki/w/Lizardman_shaman) | 72 | PARSED_UNREVIEWED |
| [Lizardman shaman (Chambers of Xeric) ](https://oldschool.runescape.wiki/w/Lizardman_shaman_(Chambers_of_Xeric)) | 6 | PARSED_UNREVIEWED |
| [Loar Shade ](https://oldschool.runescape.wiki/w/Loar_Shade) | 1 | PARSED_UNREVIEWED |
| [Lobstrosity ](https://oldschool.runescape.wiki/w/Lobstrosity) | 85 | PARSED_UNREVIEWED |
| [Locust ](https://oldschool.runescape.wiki/w/Locust) | 1 | PARSED_UNREVIEWED |
| [Locust rider ](https://oldschool.runescape.wiki/w/Locust_rider) | 77 | PARSED_UNREVIEWED |
| [Long-tailed Wyvern ](https://oldschool.runescape.wiki/w/Long-tailed_Wyvern) | 60 | PARSED_UNREVIEWED |
| [Lost barbarian ](https://oldschool.runescape.wiki/w/Lost_barbarian) | 1 | PARSED_UNREVIEWED |
| [Lucien ](https://oldschool.runescape.wiki/w/Lucien) | 0 | NO_SUPPORTED_TABLES |
| [Lucius ](https://oldschool.runescape.wiki/w/Lucius) | 0 | NO_SUPPORTED_TABLES |
| [Lucky impling ](https://oldschool.runescape.wiki/w/Lucky_impling) | 5 | PARSED_UNREVIEWED |
| [Lynx ](https://oldschool.runescape.wiki/w/Lynx) | 2 | PARSED_UNREVIEWED |
| [Lynx Tamer ](https://oldschool.runescape.wiki/w/Lynx_Tamer) | 32 | PARSED_UNREVIEWED |
| [Magic axe ](https://oldschool.runescape.wiki/w/Magic_axe) | 14 | PARSED_UNREVIEWED |
| [Magic Mark ](https://oldschool.runescape.wiki/w/Magic_Mark) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Magma strykewyrm ](https://oldschool.runescape.wiki/w/Magma_strykewyrm) | 54 | PARSED_UNREVIEWED |
| [Magpie impling ](https://oldschool.runescape.wiki/w/Magpie_impling) | 21 | PARSED_UNREVIEWED |
| [Malevolent Mage ](https://oldschool.runescape.wiki/w/Malevolent_Mage) | 29 | PARSED_UNREVIEWED |
| [Malevolent Mage (Deadman) ](https://oldschool.runescape.wiki/w/Malevolent_Mage_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Mammoth ](https://oldschool.runescape.wiki/w/Mammoth) | 79 | PARSED_UNREVIEWED |
| [Mammoth (unused NPC) ](https://oldschool.runescape.wiki/w/Mammoth_(unused_NPC)) | 0 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Man ](https://oldschool.runescape.wiki/w/Man) | 37 | PARSED_UNREVIEWED |
| [Man (Canifis) ](https://oldschool.runescape.wiki/w/Man_(Canifis)) | 0 | NO_SUPPORTED_TABLES |
| [Man (East Ardougne) ](https://oldschool.runescape.wiki/w/Man_(East_Ardougne)) | 2 | PARSED_UNREVIEWED |
| [Man (level 4) ](https://oldschool.runescape.wiki/w/Man_(level_4)) | 1 | PARSED_UNREVIEWED |
| [Maniacal monkey ](https://oldschool.runescape.wiki/w/Maniacal_monkey) | 11 | PARSED_UNREVIEWED |
| [Maniacal Monkey Archer ](https://oldschool.runescape.wiki/w/Maniacal_Monkey_Archer) | 11 | PARSED_UNREVIEWED |
| [Manta ray (monster) ](https://oldschool.runescape.wiki/w/Manta_ray_(monster)) | 81 | PARSED_UNREVIEWED |
| [Manticore ](https://oldschool.runescape.wiki/w/Manticore) | 0 | NO_SUPPORTED_TABLES |
| [Marble gargoyle ](https://oldschool.runescape.wiki/w/Marble_gargoyle) | 40 | PARSED_UNREVIEWED |
| [Market Guard ](https://oldschool.runescape.wiki/w/Market_Guard) | 47 | PARSED_UNREVIEWED |
| [Market Guard (Draynor) ](https://oldschool.runescape.wiki/w/Market_Guard_(Draynor)) | 1 | PARSED_UNREVIEWED |
| [Mature custodian stalker ](https://oldschool.runescape.wiki/w/Mature_custodian_stalker) | 43 | PARSED_UNREVIEWED |
| [Maxilla beast ](https://oldschool.runescape.wiki/w/Maxilla_beast) | 0 | NO_SUPPORTED_TABLES |
| [Me (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Me_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Menaphite Akh ](https://oldschool.runescape.wiki/w/Menaphite_Akh) | 0 | NO_SUPPORTED_TABLES |
| [Menaphite Shadow ](https://oldschool.runescape.wiki/w/Menaphite_Shadow) | 0 | NO_SUPPORTED_TABLES |
| [Menaphite Thug ](https://oldschool.runescape.wiki/w/Menaphite_Thug) | 3 | PARSED_UNREVIEWED |
| [Mercenary ](https://oldschool.runescape.wiki/w/Mercenary) | 0 | NO_SUPPORTED_TABLES |
| [Mercenary axeman ](https://oldschool.runescape.wiki/w/Mercenary_axeman) | 0 | NO_SUPPORTED_TABLES |
| [Mercenary Captain ](https://oldschool.runescape.wiki/w/Mercenary_Captain) | 2 | PARSED_UNREVIEWED |
| [Mercenary mage ](https://oldschool.runescape.wiki/w/Mercenary_mage) | 0 | NO_SUPPORTED_TABLES |
| [Metzli, Teokan of Ranul ](https://oldschool.runescape.wiki/w/Metzli,_Teokan_of_Ranul) | 0 | NO_SUPPORTED_TABLES |
| [Milla ](https://oldschool.runescape.wiki/w/Milla) | 55 | PARSED_UNREVIEWED |
| [Mind Golem ](https://oldschool.runescape.wiki/w/Mind_Golem) | 15 | PARSED_UNREVIEWED |
| [Minotaur ](https://oldschool.runescape.wiki/w/Minotaur) | 31 | PARSED_UNREVIEWED |
| [Minotaur (Fortis Colosseum) ](https://oldschool.runescape.wiki/w/Minotaur_(Fortis_Colosseum)) | 0 | NO_SUPPORTED_TABLES |
| [Minotaur (Meat and Greet) ](https://oldschool.runescape.wiki/w/Minotaur_(Meat_and_Greet)) | 0 | NO_SUPPORTED_TABLES |
| [Mister U. ](https://oldschool.runescape.wiki/w/Mister_U.) | 0 | NO_SUPPORTED_TABLES |
| [Mithril dragon ](https://oldschool.runescape.wiki/w/Mithril_dragon) | 58 | PARSED_UNREVIEWED |
| [Mogre ](https://oldschool.runescape.wiki/w/Mogre) | 47 | PARSED_UNREVIEWED |
| [Mogre (sea) ](https://oldschool.runescape.wiki/w/Mogre_(sea)) | 44 | PARSED_UNREVIEWED |
| [Molanisk ](https://oldschool.runescape.wiki/w/Molanisk) | 36 | PARSED_UNREVIEWED |
| [Monk ](https://oldschool.runescape.wiki/w/Monk) | 2 | PARSED_UNREVIEWED |
| [Monk of Zamorak ](https://oldschool.runescape.wiki/w/Monk_of_Zamorak) | 5 | PARSED_UNREVIEWED |
| [Monk of Zamorak (Paterdomus) ](https://oldschool.runescape.wiki/w/Monk_of_Zamorak_(Paterdomus)) | 5 | EXCLUDED_ALTERNATE_MODE |
| [Monkey (monster) ](https://oldschool.runescape.wiki/w/Monkey_(monster)) | 4 | PARSED_UNREVIEWED |
| [Monkey Archer ](https://oldschool.runescape.wiki/w/Monkey_Archer) | 2 | PARSED_UNREVIEWED |
| [Monkey Guard ](https://oldschool.runescape.wiki/w/Monkey_Guard) | 3 | PARSED_UNREVIEWED |
| [Monkey Guard (ninja) ](https://oldschool.runescape.wiki/w/Monkey_Guard_(ninja)) | 0 | NO_SUPPORTED_TABLES |
| [Monkey Zombie ](https://oldschool.runescape.wiki/w/Monkey_Zombie) | 4 | PARSED_UNREVIEWED |
| [Monstrous basilisk ](https://oldschool.runescape.wiki/w/Monstrous_basilisk) | 48 | PARSED_UNREVIEWED |
| [Monstrous Basilisk (The Fremennik Exiles) ](https://oldschool.runescape.wiki/w/Monstrous_Basilisk_(The_Fremennik_Exiles)) | 0 | NO_SUPPORTED_TABLES |
| [Moonlight Cockatrice ](https://oldschool.runescape.wiki/w/Moonlight_Cockatrice) | 61 | PARSED_UNREVIEWED |
| [Mosquito swarm ](https://oldschool.runescape.wiki/w/Mosquito_swarm) | 2 | PARSED_UNREVIEWED |
| [Moss giant ](https://oldschool.runescape.wiki/w/Moss_giant) | 89 | PARSED_UNREVIEWED |
| [Moss Giant (Iorwerth Dungeon) ](https://oldschool.runescape.wiki/w/Moss_Giant_(Iorwerth_Dungeon)) | 80 | PARSED_UNREVIEWED |
| [Moss Guardian (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Moss_Guardian_(Nightmare_Zone)) | 3 | EXCLUDED_ALTERNATE_MODE |
| [Mosschin ](https://oldschool.runescape.wiki/w/Mosschin) | 1 | PARSED_UNREVIEWED |
| [Mother ](https://oldschool.runescape.wiki/w/Mother) | 0 | NO_SUPPORTED_TABLES |
| [Mountain troll ](https://oldschool.runescape.wiki/w/Mountain_troll) | 73 | PARSED_UNREVIEWED |
| [Mounted terrorbird gnome ](https://oldschool.runescape.wiki/w/Mounted_terrorbird_gnome) | 1 | PARSED_UNREVIEWED |
| [Mourner ](https://oldschool.runescape.wiki/w/Mourner) | 59 | PARSED_UNREVIEWED |
| [Mourner (boss) ](https://oldschool.runescape.wiki/w/Mourner_(boss)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Mourner (Song of the Elves) ](https://oldschool.runescape.wiki/w/Mourner_(Song_of_the_Elves)) | 0 | NO_SUPPORTED_TABLES |
| [Mouse ](https://oldschool.runescape.wiki/w/Mouse) | 3 | PARSED_UNREVIEWED |
| [Mudskipper ](https://oldschool.runescape.wiki/w/Mudskipper) | 2 | PARSED_UNREVIEWED |
| [Mugger ](https://oldschool.runescape.wiki/w/Mugger) | 30 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Mummy (Ancient Pyramid sarcophagus) ](https://oldschool.runescape.wiki/w/Mummy_(Ancient_Pyramid_sarcophagus)) | 1 | PARSED_UNREVIEWED |
| [Mummy (Ancient Pyramid) ](https://oldschool.runescape.wiki/w/Mummy_(Ancient_Pyramid)) | 50 | PARSED_UNREVIEWED |
| [Mummy (historical) ](https://oldschool.runescape.wiki/w/Mummy_(historical)) | 0 | NO_SUPPORTED_TABLES |
| [Mummy (Klenter's Pyramid) ](https://oldschool.runescape.wiki/w/Mummy_(Klenter%27s_Pyramid)) | 1 | PARSED_UNREVIEWED |
| [Mummy (Pyramid Plunder) ](https://oldschool.runescape.wiki/w/Mummy_(Pyramid_Plunder)) | 1 | PARSED_UNREVIEWED |
| [Mutated Bloodveld ](https://oldschool.runescape.wiki/w/Mutated_Bloodveld) | 64 | PARSED_UNREVIEWED |
| [Mutated Terrorbird ](https://oldschool.runescape.wiki/w/Mutated_Terrorbird) | 39 | PARSED_UNREVIEWED |
| [Mutated Tortoise ](https://oldschool.runescape.wiki/w/Mutated_Tortoise) | 40 | PARSED_UNREVIEWED |
| [Mute Jack ](https://oldschool.runescape.wiki/w/Mute_Jack) | 0 | NO_SUPPORTED_TABLES |
| [Myre Blamish Snail ](https://oldschool.runescape.wiki/w/Myre_Blamish_Snail) | 4 | PARSED_UNREVIEWED |
| [Mysterious Figure ](https://oldschool.runescape.wiki/w/Mysterious_Figure) | 0 | NO_SUPPORTED_TABLES |
| [Nail beast ](https://oldschool.runescape.wiki/w/Nail_beast) | 4 | PARSED_UNREVIEWED |
| [Narf ](https://oldschool.runescape.wiki/w/Narf) | 34 | PARSED_UNREVIEWED |
| [Narwhal ](https://oldschool.runescape.wiki/w/Narwhal) | 36 | PARSED_UNREVIEWED |
| [Nature impling ](https://oldschool.runescape.wiki/w/Nature_impling) | 20 | PARSED_UNREVIEWED |
| [Nazastarool ](https://oldschool.runescape.wiki/w/Nazastarool) | 4 | PARSED_UNREVIEWED |
| [Nazastarool (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Nazastarool_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Nechryael ](https://oldschool.runescape.wiki/w/Nechryael) | 63 | PARSED_UNREVIEWED |
| [Nechryarch ](https://oldschool.runescape.wiki/w/Nechryarch) | 137 | PARSED_UNREVIEWED |
| [Necromancer ](https://oldschool.runescape.wiki/w/Necromancer) | 28 | PARSED_UNREVIEWED |
| [Necromancer (Great Kourend) ](https://oldschool.runescape.wiki/w/Necromancer_(Great_Kourend)) | 21 | PARSED_UNREVIEWED |
| [Newtroost ](https://oldschool.runescape.wiki/w/Newtroost) | 4 | PARSED_UNREVIEWED |
| [Nezikchened (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Nezikchened_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Night beast ](https://oldschool.runescape.wiki/w/Night_beast) | 84 | PARSED_UNREVIEWED |
| [Night beast (Deadman) ](https://oldschool.runescape.wiki/w/Night_beast_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Nikita ](https://oldschool.runescape.wiki/w/Nikita) | 55 | PARSED_UNREVIEWED |
| [Nikolai ](https://oldschool.runescape.wiki/w/Nikolai) | 55 | PARSED_UNREVIEWED |
| [Ninja impling ](https://oldschool.runescape.wiki/w/Ninja_impling) | 20 | PARSED_UNREVIEWED |
| [Nuclear smoke devil ](https://oldschool.runescape.wiki/w/Nuclear_smoke_devil) | 75 | PARSED_UNREVIEWED |
| [Nylocas Athanatos ](https://oldschool.runescape.wiki/w/Nylocas_Athanatos) | 0 | NO_SUPPORTED_TABLES |
| [Nylocas Hagios ](https://oldschool.runescape.wiki/w/Nylocas_Hagios) | 0 | NO_SUPPORTED_TABLES |
| [Nylocas Ischyros ](https://oldschool.runescape.wiki/w/Nylocas_Ischyros) | 0 | NO_SUPPORTED_TABLES |
| [Nylocas Matomenos ](https://oldschool.runescape.wiki/w/Nylocas_Matomenos) | 0 | NO_SUPPORTED_TABLES |
| [Nylocas Prinkipas ](https://oldschool.runescape.wiki/w/Nylocas_Prinkipas) | 0 | NO_SUPPORTED_TABLES |
| [Nylocas Toxobolos ](https://oldschool.runescape.wiki/w/Nylocas_Toxobolos) | 0 | NO_SUPPORTED_TABLES |
| [Oaky Doak ](https://oldschool.runescape.wiki/w/Oaky_Doak) | 0 | NO_SUPPORTED_TABLES |
| [Obelisk (Tombs of Amascut) ](https://oldschool.runescape.wiki/w/Obelisk_(Tombs_of_Amascut)) | 0 | NO_SUPPORTED_TABLES |
| [Ocelot ](https://oldschool.runescape.wiki/w/Ocelot) | 1 | PARSED_UNREVIEWED |
| [Ocga ](https://oldschool.runescape.wiki/w/Ocga) | 32 | PARSED_UNREVIEWED |
| [Ochre Blamish Snail ](https://oldschool.runescape.wiki/w/Ochre_Blamish_Snail) | 4 | PARSED_UNREVIEWED |
| [Ogre ](https://oldschool.runescape.wiki/w/Ogre) | 33 | PARSED_UNREVIEWED |
| [Ogre chieftain ](https://oldschool.runescape.wiki/w/Ogre_chieftain) | 31 | PARSED_UNREVIEWED |
| [Ogre monodon ](https://oldschool.runescape.wiki/w/Ogre_monodon) | 59 | PARSED_UNREVIEWED |
| [Ogre shaman ](https://oldschool.runescape.wiki/w/Ogre_shaman) | 0 | NO_SUPPORTED_TABLES |
| [Ogre shaman (Swan Song) ](https://oldschool.runescape.wiki/w/Ogre_shaman_(Swan_Song)) | 0 | NO_SUPPORTED_TABLES |
| [Ogress Shaman ](https://oldschool.runescape.wiki/w/Ogress_Shaman) | 56 | PARSED_UNREVIEWED |
| [Ogress Warrior ](https://oldschool.runescape.wiki/w/Ogress_Warrior) | 56 | PARSED_UNREVIEWED |
| [Oipuis ](https://oldschool.runescape.wiki/w/Oipuis) | 1 | PARSED_UNREVIEWED |
| [Old Jack ](https://oldschool.runescape.wiki/w/Old_Jack) | 0 | NO_SUPPORTED_TABLES |
| [Oomlie bird ](https://oldschool.runescape.wiki/w/Oomlie_bird) | 2 | PARSED_UNREVIEWED |
| [Orc (historical) ](https://oldschool.runescape.wiki/w/Orc_(historical)) | 0 | NO_SUPPORTED_TABLES |
| [Orc (Realm of Memories) ](https://oldschool.runescape.wiki/w/Orc_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Orca ](https://oldschool.runescape.wiki/w/Orca) | 41 | PARSED_UNREVIEWED |
| [Ork ](https://oldschool.runescape.wiki/w/Ork) | 31 | PARSED_UNREVIEWED |
| [Oryx ](https://oldschool.runescape.wiki/w/Oryx) | 1 | PARSED_UNREVIEWED |
| [Osprey ](https://oldschool.runescape.wiki/w/Osprey) | 21 | PARSED_UNREVIEWED |
| [Othainian ](https://oldschool.runescape.wiki/w/Othainian) | 2 | PARSED_UNREVIEWED |
| [Otherworldly being ](https://oldschool.runescape.wiki/w/Otherworldly_being) | 34 | PARSED_UNREVIEWED |
| [Ouhai ](https://oldschool.runescape.wiki/w/Ouhai) | 1 | PARSED_UNREVIEWED |
| [Outlaw ](https://oldschool.runescape.wiki/w/Outlaw) | 26 | PARSED_UNREVIEWED |
| [Outlaw (A Ruff Situation) ](https://oldschool.runescape.wiki/w/Outlaw_(A_Ruff_Situation)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Padulah ](https://oldschool.runescape.wiki/w/Padulah) | 1 | PARSED_UNREVIEWED |
| [Painted Goblin ](https://oldschool.runescape.wiki/w/Painted_Goblin) | 10 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Paladin ](https://oldschool.runescape.wiki/w/Paladin) | 43 | PARSED_UNREVIEWED |
| [Paladin (Song of the Elves) ](https://oldschool.runescape.wiki/w/Paladin_(Song_of_the_Elves)) | 0 | NO_SUPPORTED_TABLES |
| [Palmer ](https://oldschool.runescape.wiki/w/Palmer) | 1 | PARSED_UNREVIEWED |
| [Passionate Supporter ](https://oldschool.runescape.wiki/w/Passionate_Supporter) | 1 | PARSED_UNREVIEWED |
| [Pee Hat ](https://oldschool.runescape.wiki/w/Pee_Hat) | 49 | PARSED_UNREVIEWED |
| [Penance Fighter ](https://oldschool.runescape.wiki/w/Penance_Fighter) | 3 | PARSED_UNREVIEWED |
| [Penance Healer ](https://oldschool.runescape.wiki/w/Penance_Healer) | 3 | PARSED_UNREVIEWED |
| [Penance Ranger ](https://oldschool.runescape.wiki/w/Penance_Ranger) | 3 | PARSED_UNREVIEWED |
| [Penance Runner ](https://oldschool.runescape.wiki/w/Penance_Runner) | 3 | PARSED_UNREVIEWED |
| [Penda ](https://oldschool.runescape.wiki/w/Penda) | 33 | PARSED_UNREVIEWED |
| [Penguin (monster) ](https://oldschool.runescape.wiki/w/Penguin_(monster)) | 1 | PARSED_UNREVIEWED |
| [Persten the Deceitful ](https://oldschool.runescape.wiki/w/Persten_the_Deceitful) | 0 | NO_SUPPORTED_TABLES |
| [Pestilent Bloat (Deadman) ](https://oldschool.runescape.wiki/w/Pestilent_Bloat_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Pete Kayer ](https://oldschool.runescape.wiki/w/Pete_Kayer) | 10 | EXCLUDED_ALTERNATE_MODE |
| [Phantom Hand ](https://oldschool.runescape.wiki/w/Phantom_Hand) | 0 | NO_SUPPORTED_TABLES |
| [Phantom Muspah (Deadman) ](https://oldschool.runescape.wiki/w/Phantom_Muspah_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Pheasant ](https://oldschool.runescape.wiki/w/Pheasant) | 1 | PARSED_UNREVIEWED |
| [Phrin Shade ](https://oldschool.runescape.wiki/w/Phrin_Shade) | 1 | PARSED_UNREVIEWED |
| [Pirate ](https://oldschool.runescape.wiki/w/Pirate) | 41 | PARSED_UNREVIEWED |
| [Pirate (Feldip Gulf) ](https://oldschool.runescape.wiki/w/Pirate_(Feldip_Gulf)) | 0 | NO_SUPPORTED_TABLES |
| [Pirate (The Red Reef) ](https://oldschool.runescape.wiki/w/Pirate_(The_Red_Reef)) | 1 | PARSED_UNREVIEWED |
| [Pirate (Thieving) ](https://oldschool.runescape.wiki/w/Pirate_(Thieving)) | 44 | PARSED_UNREVIEWED |
| [Pirate captain (Feldip Gulf) ](https://oldschool.runescape.wiki/w/Pirate_captain_(Feldip_Gulf)) | 0 | NO_SUPPORTED_TABLES |
| [Pirate Guard ](https://oldschool.runescape.wiki/w/Pirate_Guard) | 1 | PARSED_UNREVIEWED |
| [Pit Scorpion ](https://oldschool.runescape.wiki/w/Pit_Scorpion) | 2 | PARSED_UNREVIEWED |
| [Plague frog ](https://oldschool.runescape.wiki/w/Plague_frog) | 0 | NO_SUPPORTED_TABLES |
| [Poison Scorpion ](https://oldschool.runescape.wiki/w/Poison_Scorpion) | 2 | PARSED_UNREVIEWED |
| [Poison spider ](https://oldschool.runescape.wiki/w/Poison_spider) | 4 | PARSED_UNREVIEWED |
| [Poison spider (Escape Caves) ](https://oldschool.runescape.wiki/w/Poison_spider_(Escape_Caves)) | 0 | NO_SUPPORTED_TABLES |
| [Poltenip ](https://oldschool.runescape.wiki/w/Poltenip) | 1 | PARSED_UNREVIEWED |
| [Porazdir ](https://oldschool.runescape.wiki/w/Porazdir) | 4 | PARSED_UNREVIEWED |
| [Porazdir (Deadman) ](https://oldschool.runescape.wiki/w/Porazdir_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Porcupine ](https://oldschool.runescape.wiki/w/Porcupine) | 2 | PARSED_UNREVIEWED |
| [Portal (Pest Control) ](https://oldschool.runescape.wiki/w/Portal_(Pest_Control)) | 0 | NO_SUPPORTED_TABLES |
| [Portal (wighted leech) ](https://oldschool.runescape.wiki/w/Portal_(wighted_leech)) | 0 | NO_SUPPORTED_TABLES |
| [Possessed pickaxe ](https://oldschool.runescape.wiki/w/Possessed_pickaxe) | 11 | PARSED_UNREVIEWED |
| [Possessed Priest ](https://oldschool.runescape.wiki/w/Possessed_Priest) | 6 | PARSED_UNREVIEWED |
| [Praying mantis ](https://oldschool.runescape.wiki/w/Praying_mantis) | 0 | NO_SUPPORTED_TABLES |
| [Prince Itzla Arkan ](https://oldschool.runescape.wiki/w/Prince_Itzla_Arkan) | 0 | NO_SUPPORTED_TABLES |
| [Pygmy kraken ](https://oldschool.runescape.wiki/w/Pygmy_kraken) | 69 | PARSED_UNREVIEWED |
| [Pyrefiend ](https://oldschool.runescape.wiki/w/Pyrefiend) | 34 | PARSED_UNREVIEWED |
| [Pyrelord ](https://oldschool.runescape.wiki/w/Pyrelord) | 31 | PARSED_UNREVIEWED |
| [Queen spawn ](https://oldschool.runescape.wiki/w/Queen_spawn) | 0 | NO_SUPPORTED_TABLES |
| [Rabbit ](https://oldschool.runescape.wiki/w/Rabbit) | 3 | PARSED_UNREVIEWED |
| [Rabbit (Prifddinas) ](https://oldschool.runescape.wiki/w/Rabbit_(Prifddinas)) | 2 | PARSED_UNREVIEWED |
| [Radat ](https://oldschool.runescape.wiki/w/Radat) | 1 | PARSED_UNREVIEWED |
| [Radiant sanguisphera ](https://oldschool.runescape.wiki/w/Radiant_sanguisphera) | 1 | PARSED_UNREVIEWED |
| [Ragnar (Miscellania) ](https://oldschool.runescape.wiki/w/Ragnar_(Miscellania)) | 0 | NO_SUPPORTED_TABLES |
| [Ragnvald ](https://oldschool.runescape.wiki/w/Ragnvald) | 1 | PARSED_UNREVIEWED |
| [Ram ](https://oldschool.runescape.wiki/w/Ram) | 2 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Ranalph Devere ](https://oldschool.runescape.wiki/w/Ranalph_Devere) | 1 | PARSED_UNREVIEWED |
| [Ranging Ro ](https://oldschool.runescape.wiki/w/Ranging_Ro) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Ranis Drakan ](https://oldschool.runescape.wiki/w/Ranis_Drakan) | 0 | NO_SUPPORTED_TABLES |
| [Rannveig ](https://oldschool.runescape.wiki/w/Rannveig) | 1 | PARSED_UNREVIEWED |
| [Rat ](https://oldschool.runescape.wiki/w/Rat) | 6 | PARSED_UNREVIEWED |
| [Rat Boss Test NPC ](https://oldschool.runescape.wiki/w/Rat_Boss_Test_NPC) | 0 | NO_SUPPORTED_TABLES |
| [Ravager ](https://oldschool.runescape.wiki/w/Ravager) | 0 | NO_SUPPORTED_TABLES |
| [Reanimated abyssal ](https://oldschool.runescape.wiki/w/Reanimated_abyssal) | 1 | PARSED_UNREVIEWED |
| [Reanimated aviansie ](https://oldschool.runescape.wiki/w/Reanimated_aviansie) | 1 | PARSED_UNREVIEWED |
| [Reanimated bear ](https://oldschool.runescape.wiki/w/Reanimated_bear) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Reanimated bloodveld ](https://oldschool.runescape.wiki/w/Reanimated_bloodveld) | 1 | PARSED_UNREVIEWED |
| [Reanimated chaos druid ](https://oldschool.runescape.wiki/w/Reanimated_chaos_druid) | 0 | NO_SUPPORTED_TABLES |
| [Reanimated dagannoth ](https://oldschool.runescape.wiki/w/Reanimated_dagannoth) | 2 | EXCLUDED_ALTERNATE_MODE |
| [Reanimated demon ](https://oldschool.runescape.wiki/w/Reanimated_demon) | 0 | NO_SUPPORTED_TABLES |
| [Reanimated demon spawn ](https://oldschool.runescape.wiki/w/Reanimated_demon_spawn) | 0 | NO_SUPPORTED_TABLES |
| [Reanimated dog ](https://oldschool.runescape.wiki/w/Reanimated_dog) | 0 | NO_SUPPORTED_TABLES |
| [Reanimated dragon ](https://oldschool.runescape.wiki/w/Reanimated_dragon) | 0 | NO_SUPPORTED_TABLES |
| [Reanimated elf ](https://oldschool.runescape.wiki/w/Reanimated_elf) | 0 | NO_SUPPORTED_TABLES |
| [Reanimated giant ](https://oldschool.runescape.wiki/w/Reanimated_giant) | 1 | PARSED_UNREVIEWED |
| [Reanimated goblin ](https://oldschool.runescape.wiki/w/Reanimated_goblin) | 2 | PARSED_UNREVIEWED |
| [Reanimated hellhound ](https://oldschool.runescape.wiki/w/Reanimated_hellhound) | 1 | PARSED_UNREVIEWED |
| [Reanimated horror ](https://oldschool.runescape.wiki/w/Reanimated_horror) | 0 | NO_SUPPORTED_TABLES |
| [Reanimated imp ](https://oldschool.runescape.wiki/w/Reanimated_imp) | 1 | PARSED_UNREVIEWED |
| [Reanimated kalphite ](https://oldschool.runescape.wiki/w/Reanimated_kalphite) | 1 | PARSED_UNREVIEWED |
| [Reanimated minotaur ](https://oldschool.runescape.wiki/w/Reanimated_minotaur) | 0 | NO_SUPPORTED_TABLES |
| [Reanimated monkey ](https://oldschool.runescape.wiki/w/Reanimated_monkey) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Reanimated ogre ](https://oldschool.runescape.wiki/w/Reanimated_ogre) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Reanimated scorpion ](https://oldschool.runescape.wiki/w/Reanimated_scorpion) | 0 | NO_SUPPORTED_TABLES |
| [Reanimated troll ](https://oldschool.runescape.wiki/w/Reanimated_troll) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Reanimated TzHaar ](https://oldschool.runescape.wiki/w/Reanimated_TzHaar) | 0 | NO_SUPPORTED_TABLES |
| [Reanimated unicorn ](https://oldschool.runescape.wiki/w/Reanimated_unicorn) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Red dragon ](https://oldschool.runescape.wiki/w/Red_dragon) | 52 | PARSED_UNREVIEWED |
| [Red dragon (Realm of Memories) ](https://oldschool.runescape.wiki/w/Red_dragon_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Red dragon (Scrambled!) ](https://oldschool.runescape.wiki/w/Red_dragon_(Scrambled!)) | 0 | NO_SUPPORTED_TABLES |
| [Redeyes ](https://oldschool.runescape.wiki/w/Redeyes) | 1 | PARSED_UNREVIEWED |
| [Reef snake ](https://oldschool.runescape.wiki/w/Reef_snake) | 0 | NO_SUPPORTED_TABLES |
| [Renegade knight ](https://oldschool.runescape.wiki/w/Renegade_knight) | 1 | PARSED_UNREVIEWED |
| [Repugnant spectre ](https://oldschool.runescape.wiki/w/Repugnant_spectre) | 56 | PARSED_UNREVIEWED |
| [Revenant cyclops ](https://oldschool.runescape.wiki/w/Revenant_cyclops) | 60 | PARSED_UNREVIEWED |
| [Revenant dark beast ](https://oldschool.runescape.wiki/w/Revenant_dark_beast) | 60 | PARSED_UNREVIEWED |
| [Revenant demon ](https://oldschool.runescape.wiki/w/Revenant_demon) | 60 | PARSED_UNREVIEWED |
| [Revenant dragon ](https://oldschool.runescape.wiki/w/Revenant_dragon) | 59 | PARSED_UNREVIEWED |
| [Revenant goblin ](https://oldschool.runescape.wiki/w/Revenant_goblin) | 61 | PARSED_UNREVIEWED |
| [Revenant hellhound ](https://oldschool.runescape.wiki/w/Revenant_hellhound) | 60 | PARSED_UNREVIEWED |
| [Revenant hobgoblin ](https://oldschool.runescape.wiki/w/Revenant_hobgoblin) | 61 | PARSED_UNREVIEWED |
| [Revenant imp ](https://oldschool.runescape.wiki/w/Revenant_imp) | 61 | PARSED_UNREVIEWED |
| [Revenant knight ](https://oldschool.runescape.wiki/w/Revenant_knight) | 60 | PARSED_UNREVIEWED |
| [Revenant ork ](https://oldschool.runescape.wiki/w/Revenant_ork) | 60 | PARSED_UNREVIEWED |
| [Revenant pyrefiend ](https://oldschool.runescape.wiki/w/Revenant_pyrefiend) | 60 | PARSED_UNREVIEWED |
| [River troll ](https://oldschool.runescape.wiki/w/River_troll) | 17 | PARSED_UNREVIEWED |
| [River troll (random event) ](https://oldschool.runescape.wiki/w/River_troll_(random_event)) | 0 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Riyl Shade ](https://oldschool.runescape.wiki/w/Riyl_Shade) | 1 | PARSED_UNREVIEWED |
| [Riyl shadow (Temple Trekking) ](https://oldschool.runescape.wiki/w/Riyl_shadow_(Temple_Trekking)) | 1 | PARSED_UNREVIEWED |
| [Robert the Strong ](https://oldschool.runescape.wiki/w/Robert_the_Strong) | 0 | NO_SUPPORTED_TABLES |
| [Rock (Troll) ](https://oldschool.runescape.wiki/w/Rock_(Troll)) | 49 | PARSED_UNREVIEWED |
| [Rock Crab ](https://oldschool.runescape.wiki/w/Rock_Crab) | 38 | PARSED_UNREVIEWED |
| [Rock Golem (monster) ](https://oldschool.runescape.wiki/w/Rock_Golem_(monster)) | 38 | PARSED_UNREVIEWED |
| [Rock lobster ](https://oldschool.runescape.wiki/w/Rock_lobster) | 53 | PARSED_UNREVIEWED |
| [Rockslug ](https://oldschool.runescape.wiki/w/Rockslug) | 34 | PARSED_UNREVIEWED |
| [Rocnar ](https://oldschool.runescape.wiki/w/Rocnar) | 0 | NO_SUPPORTED_TABLES |
| [Rogue ](https://oldschool.runescape.wiki/w/Rogue) | 46 | PARSED_UNREVIEWED |
| [Rooster ](https://oldschool.runescape.wiki/w/Rooster) | 6 | PARSED_UNREVIEWED |
| [Rowdy Guard ](https://oldschool.runescape.wiki/w/Rowdy_Guard) | 0 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Rowdy slave ](https://oldschool.runescape.wiki/w/Rowdy_slave) | 4 | PARSED_UNREVIEWED |
| [Rune dragon ](https://oldschool.runescape.wiki/w/Rune_dragon) | 58 | PARSED_UNREVIEWED |
| [Rune dragon (Construction) ](https://oldschool.runescape.wiki/w/Rune_dragon_(Construction)) | 0 | NO_SUPPORTED_TABLES |
| [Runite Golem ](https://oldschool.runescape.wiki/w/Runite_Golem) | 0 | NO_SUPPORTED_TABLES |
| [Rusty ](https://oldschool.runescape.wiki/w/Rusty) | 34 | PARSED_UNREVIEWED |
| [San Tojalon ](https://oldschool.runescape.wiki/w/San_Tojalon) | 1 | PARSED_UNREVIEWED |
| [Sand Crab ](https://oldschool.runescape.wiki/w/Sand_Crab) | 38 | PARSED_UNREVIEWED |
| [Sand Snake ](https://oldschool.runescape.wiki/w/Sand_Snake) | 0 | NO_SUPPORTED_TABLES |
| [Sand Snake (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Sand_Snake_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Sanguidae ](https://oldschool.runescape.wiki/w/Sanguidae) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Saradomin priest ](https://oldschool.runescape.wiki/w/Saradomin_priest) | 2 | PARSED_UNREVIEWED |
| [Saradomin wizard ](https://oldschool.runescape.wiki/w/Saradomin_wizard) | 24 | PARSED_UNREVIEWED |
| [SAS Member ](https://oldschool.runescape.wiki/w/SAS_Member) | 0 | NO_SUPPORTED_TABLES |
| [Scarab (Tombs of Amascut) ](https://oldschool.runescape.wiki/w/Scarab_(Tombs_of_Amascut)) | 0 | NO_SUPPORTED_TABLES |
| [Scarab Mage ](https://oldschool.runescape.wiki/w/Scarab_Mage) | 76 | PARSED_UNREVIEWED |
| [Scarab Swarm ](https://oldschool.runescape.wiki/w/Scarab_Swarm) | 1 | PARSED_UNREVIEWED |
| [Scarab Swarm (Beneath Cursed Sands) ](https://oldschool.runescape.wiki/w/Scarab_Swarm_(Beneath_Cursed_Sands)) | 0 | NO_SUPPORTED_TABLES |
| [Scarab Swarm (Tombs of Amascut) ](https://oldschool.runescape.wiki/w/Scarab_Swarm_(Tombs_of_Amascut)) | 0 | NO_SUPPORTED_TABLES |
| [Scarabs ](https://oldschool.runescape.wiki/w/Scarabs) | 0 | NO_SUPPORTED_TABLES |
| [Scarred black demon ](https://oldschool.runescape.wiki/w/Scarred_black_demon) | 1 | PARSED_UNREVIEWED |
| [Scarred greater demon ](https://oldschool.runescape.wiki/w/Scarred_greater_demon) | 1 | PARSED_UNREVIEWED |
| [Scarred hellhound ](https://oldschool.runescape.wiki/w/Scarred_hellhound) | 1 | PARSED_UNREVIEWED |
| [Scarred imp ](https://oldschool.runescape.wiki/w/Scarred_imp) | 1 | PARSED_UNREVIEWED |
| [Scarred lesser demon ](https://oldschool.runescape.wiki/w/Scarred_lesser_demon) | 1 | PARSED_UNREVIEWED |
| [Scavenger beast ](https://oldschool.runescape.wiki/w/Scavenger_beast) | 13 | PARSED_UNREVIEWED |
| [Scavenger runt ](https://oldschool.runescape.wiki/w/Scavenger_runt) | 12 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Scorpia's guardian ](https://oldschool.runescape.wiki/w/Scorpia%27s_guardian) | 5 | PARSED_UNREVIEWED |
| [Scorpia's offspring (monster) ](https://oldschool.runescape.wiki/w/Scorpia%27s_offspring_(monster)) | 5 | PARSED_UNREVIEWED |
| [Scorpion ](https://oldschool.runescape.wiki/w/Scorpion) | 6 | PARSED_UNREVIEWED |
| [Scorpion (Realm of Memories) ](https://oldschool.runescape.wiki/w/Scorpion_(Realm_of_Memories)) | 2 | EXCLUDED_ALTERNATE_MODE |
| [Screaming banshee ](https://oldschool.runescape.wiki/w/Screaming_banshee) | 47 | PARSED_UNREVIEWED |
| [Screaming twisted banshee ](https://oldschool.runescape.wiki/w/Screaming_twisted_banshee) | 66 | PARSED_UNREVIEWED |
| [Scurrius (Deadman) ](https://oldschool.runescape.wiki/w/Scurrius_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Sea hawk ](https://oldschool.runescape.wiki/w/Sea_hawk) | 42 | PARSED_UNREVIEWED |
| [Sea kraken (Air) ](https://oldschool.runescape.wiki/w/Sea_kraken_(Air)) | 0 | NO_SUPPORTED_TABLES |
| [Sea kraken (Earth) ](https://oldschool.runescape.wiki/w/Sea_kraken_(Earth)) | 0 | NO_SUPPORTED_TABLES |
| [Sea kraken (Fire) ](https://oldschool.runescape.wiki/w/Sea_kraken_(Fire)) | 0 | NO_SUPPORTED_TABLES |
| [Sea kraken (Sailing alpha) ](https://oldschool.runescape.wiki/w/Sea_kraken_(Sailing_alpha)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Sea kraken (Water) ](https://oldschool.runescape.wiki/w/Sea_kraken_(Water)) | 0 | NO_SUPPORTED_TABLES |
| [Sea monodon ](https://oldschool.runescape.wiki/w/Sea_monodon) | 58 | PARSED_UNREVIEWED |
| [Sea Snake Hatchling ](https://oldschool.runescape.wiki/w/Sea_Snake_Hatchling) | 33 | PARSED_UNREVIEWED |
| [Sea Snake Young ](https://oldschool.runescape.wiki/w/Sea_Snake_Young) | 71 | PARSED_UNREVIEWED |
| [Sea troll ](https://oldschool.runescape.wiki/w/Sea_troll) | 4 | PARSED_UNREVIEWED |
| [Seagull ](https://oldschool.runescape.wiki/w/Seagull) | 2 | PARSED_UNREVIEWED |
| [Seagull (Icyene Graveyard) ](https://oldschool.runescape.wiki/w/Seagull_(Icyene_Graveyard)) | 2 | PARSED_UNREVIEWED |
| [Seal (Sailing alpha) ](https://oldschool.runescape.wiki/w/Seal_(Sailing_alpha)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Sergeant (Shayzien) ](https://oldschool.runescape.wiki/w/Sergeant_(Shayzien)) | 46 | PARSED_UNREVIEWED |
| [Sergeant Grimspike ](https://oldschool.runescape.wiki/w/Sergeant_Grimspike) | 23 | PARSED_UNREVIEWED |
| [Sergeant Steelwill ](https://oldschool.runescape.wiki/w/Sergeant_Steelwill) | 23 | PARSED_UNREVIEWED |
| [Sergeant Strongstack ](https://oldschool.runescape.wiki/w/Sergeant_Strongstack) | 23 | PARSED_UNREVIEWED |
| [Serpent shaman ](https://oldschool.runescape.wiki/w/Serpent_shaman) | 0 | NO_SUPPORTED_TABLES |
| [Servant (Burthorpe) ](https://oldschool.runescape.wiki/w/Servant_(Burthorpe)) | 1 | PARSED_UNREVIEWED |
| [Shade ](https://oldschool.runescape.wiki/w/Shade) | 7 | PARSED_UNREVIEWED |
| [Shade (Temple Trekking) ](https://oldschool.runescape.wiki/w/Shade_(Temple_Trekking)) | 1 | PARSED_UNREVIEWED |
| [Shadow (Temple of Light) ](https://oldschool.runescape.wiki/w/Shadow_(Temple_of_Light)) | 0 | NO_SUPPORTED_TABLES |
| [Shadow Hound ](https://oldschool.runescape.wiki/w/Shadow_Hound) | 1 | PARSED_UNREVIEWED |
| [Shadow Keeper ](https://oldschool.runescape.wiki/w/Shadow_Keeper) | 0 | NO_SUPPORTED_TABLES |
| [Shadow Rift ](https://oldschool.runescape.wiki/w/Shadow_Rift) | 0 | NO_SUPPORTED_TABLES |
| [Shadow spider ](https://oldschool.runescape.wiki/w/Shadow_spider) | 4 | PARSED_UNREVIEWED |
| [Shadow warrior ](https://oldschool.runescape.wiki/w/Shadow_warrior) | 36 | PARSED_UNREVIEWED |
| [Shadow warrior (RuneScape 2 Beta) ](https://oldschool.runescape.wiki/w/Shadow_warrior_(RuneScape_2_Beta)) | 0 | NO_SUPPORTED_TABLES |
| [Shadow Wyrm ](https://oldschool.runescape.wiki/w/Shadow_Wyrm) | 66 | PARSED_UNREVIEWED |
| [Shaeded Beast ](https://oldschool.runescape.wiki/w/Shaeded_Beast) | 1 | PARSED_UNREVIEWED |
| [Shantay Guard ](https://oldschool.runescape.wiki/w/Shantay_Guard) | 1 | PARSED_UNREVIEWED |
| [Shark (Sailing alpha) ](https://oldschool.runescape.wiki/w/Shark_(Sailing_alpha)) | 13 | EXCLUDED_ALTERNATE_MODE |
| [Shellbane gryphon (Troubled Tortugans) ](https://oldschool.runescape.wiki/w/Shellbane_gryphon_(Troubled_Tortugans)) | 0 | NO_SUPPORTED_TABLES |
| [Shifter ](https://oldschool.runescape.wiki/w/Shifter) | 0 | NO_SUPPORTED_TABLES |
| [Shipyard worker ](https://oldschool.runescape.wiki/w/Shipyard_worker) | 1 | PARSED_UNREVIEWED |
| [Shockwave Colossus ](https://oldschool.runescape.wiki/w/Shockwave_Colossus) | 0 | NO_SUPPORTED_TABLES |
| [Sir Bedivere ](https://oldschool.runescape.wiki/w/Sir_Bedivere) | 0 | NO_SUPPORTED_TABLES |
| [Sir Carl ](https://oldschool.runescape.wiki/w/Sir_Carl) | 39 | PARSED_UNREVIEWED |
| [Sir Gawain ](https://oldschool.runescape.wiki/w/Sir_Gawain) | 0 | NO_SUPPORTED_TABLES |
| [Sir Harry ](https://oldschool.runescape.wiki/w/Sir_Harry) | 39 | PARSED_UNREVIEWED |
| [Sir Jerro ](https://oldschool.runescape.wiki/w/Sir_Jerro) | 39 | PARSED_UNREVIEWED |
| [Sir Kay ](https://oldschool.runescape.wiki/w/Sir_Kay) | 0 | NO_SUPPORTED_TABLES |
| [Sir Lancelot ](https://oldschool.runescape.wiki/w/Sir_Lancelot) | 0 | NO_SUPPORTED_TABLES |
| [Sir Lucan ](https://oldschool.runescape.wiki/w/Sir_Lucan) | 0 | NO_SUPPORTED_TABLES |
| [Sir Palomedes ](https://oldschool.runescape.wiki/w/Sir_Palomedes) | 0 | NO_SUPPORTED_TABLES |
| [Sir Pelleas ](https://oldschool.runescape.wiki/w/Sir_Pelleas) | 0 | NO_SUPPORTED_TABLES |
| [Sir Tristram ](https://oldschool.runescape.wiki/w/Sir_Tristram) | 0 | NO_SUPPORTED_TABLES |
| [Siren (Sailing alpha) ](https://oldschool.runescape.wiki/w/Siren_(Sailing_alpha)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Skeletal miner ](https://oldschool.runescape.wiki/w/Skeletal_miner) | 44 | PARSED_UNREVIEWED |
| [Skeletal Mystic ](https://oldschool.runescape.wiki/w/Skeletal_Mystic) | 6 | PARSED_UNREVIEWED |
| [Skeletal Wyvern ](https://oldschool.runescape.wiki/w/Skeletal_Wyvern) | 75 | PARSED_UNREVIEWED |
| [Skeleton ](https://oldschool.runescape.wiki/w/Skeleton) | 105 | PARSED_UNREVIEWED |
| [Skeleton (Ape Atoll) ](https://oldschool.runescape.wiki/w/Skeleton_(Ape_Atoll)) | 2 | PARSED_UNREVIEWED |
| [Skeleton (Catacombs of Kourend) ](https://oldschool.runescape.wiki/w/Skeleton_(Catacombs_of_Kourend)) | 48 | PARSED_UNREVIEWED |
| [Skeleton (Draynor Manor) ](https://oldschool.runescape.wiki/w/Skeleton_(Draynor_Manor)) | 0 | NO_SUPPORTED_TABLES |
| [Skeleton (Forthos Ruin) ](https://oldschool.runescape.wiki/w/Skeleton_(Forthos_Ruin)) | 2 | PARSED_UNREVIEWED |
| [Skeleton (Lucien's camp) ](https://oldschool.runescape.wiki/w/Skeleton_(Lucien%27s_camp)) | 112 | PARSED_UNREVIEWED |
| [Skeleton (mage) ](https://oldschool.runescape.wiki/w/Skeleton_(mage)) | 66 | PARSED_UNREVIEWED |
| [Skeleton (Melzar's Maze) ](https://oldschool.runescape.wiki/w/Skeleton_(Melzar%27s_Maze)) | 6 | PARSED_UNREVIEWED |
| [Skeleton (Shayzien Crypts) ](https://oldschool.runescape.wiki/w/Skeleton_(Shayzien_Crypts)) | 112 | PARSED_UNREVIEWED |
| [Skeleton (Stronghold of Security) ](https://oldschool.runescape.wiki/w/Skeleton_(Stronghold_of_Security)) | 2 | PARSED_UNREVIEWED |
| [Skeleton (Tarn's Lair) ](https://oldschool.runescape.wiki/w/Skeleton_(Tarn%27s_Lair)) | 195 | PARSED_UNREVIEWED |
| [Skeleton (The Restless Ghost) ](https://oldschool.runescape.wiki/w/Skeleton_(The_Restless_Ghost)) | 2 | PARSED_UNREVIEWED |
| [Skeleton (Wilderness Agility Course) ](https://oldschool.runescape.wiki/w/Skeleton_(Wilderness_Agility_Course)) | 5 | PARSED_UNREVIEWED |
| [Skeleton (Wrath Altar) ](https://oldschool.runescape.wiki/w/Skeleton_(Wrath_Altar)) | 0 | NO_SUPPORTED_TABLES |
| [Skeleton brute ](https://oldschool.runescape.wiki/w/Skeleton_brute) | 61 | PARSED_UNREVIEWED |
| [Skeleton Champion ](https://oldschool.runescape.wiki/w/Skeleton_Champion) | 0 | NO_SUPPORTED_TABLES |
| [Skeleton fremennik ](https://oldschool.runescape.wiki/w/Skeleton_fremennik) | 56 | PARSED_UNREVIEWED |
| [Skeleton guard ](https://oldschool.runescape.wiki/w/Skeleton_guard) | 0 | NO_SUPPORTED_TABLES |
| [Skeleton heavy ](https://oldschool.runescape.wiki/w/Skeleton_heavy) | 61 | PARSED_UNREVIEWED |
| [Skeleton Hellhound ](https://oldschool.runescape.wiki/w/Skeleton_Hellhound) | 0 | NO_SUPPORTED_TABLES |
| [Skeleton Hellhound (Calvar'ion) ](https://oldschool.runescape.wiki/w/Skeleton_Hellhound_(Calvar%27ion)) | 5 | PARSED_UNREVIEWED |
| [Skeleton Hellhound (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Skeleton_Hellhound_(Nightmare_Zone)) | 3 | EXCLUDED_ALTERNATE_MODE |
| [Skeleton Hellhound (Vet'ion) ](https://oldschool.runescape.wiki/w/Skeleton_Hellhound_(Vet%27ion)) | 6 | PARSED_UNREVIEWED |
| [Skeleton hero ](https://oldschool.runescape.wiki/w/Skeleton_hero) | 61 | PARSED_UNREVIEWED |
| [Skeleton Mage ](https://oldschool.runescape.wiki/w/Skeleton_Mage) | 37 | PARSED_UNREVIEWED |
| [Skeleton thug ](https://oldschool.runescape.wiki/w/Skeleton_thug) | 61 | PARSED_UNREVIEWED |
| [Skeleton warlord ](https://oldschool.runescape.wiki/w/Skeleton_warlord) | 61 | PARSED_UNREVIEWED |
| [Skoblin ](https://oldschool.runescape.wiki/w/Skoblin) | 0 | NO_SUPPORTED_TABLES |
| [Skogre ](https://oldschool.runescape.wiki/w/Skogre) | 5 | PARSED_UNREVIEWED |
| [Skraeling (Etceteria) ](https://oldschool.runescape.wiki/w/Skraeling_(Etceteria)) | 1 | PARSED_UNREVIEWED |
| [Skraeling (Miscellania) ](https://oldschool.runescape.wiki/w/Skraeling_(Miscellania)) | 1 | PARSED_UNREVIEWED |
| [Slagilith (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Slagilith_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Small Lizard ](https://oldschool.runescape.wiki/w/Small_Lizard) | 47 | PARSED_UNREVIEWED |
| [Small scarab ](https://oldschool.runescape.wiki/w/Small_scarab) | 0 | NO_SUPPORTED_TABLES |
| [Smoke devil ](https://oldschool.runescape.wiki/w/Smoke_devil) | 71 | PARSED_UNREVIEWED |
| [Smokedevil ](https://oldschool.runescape.wiki/w/Smokedevil) | 0 | NO_SUPPORTED_TABLES |
| [Snailfeet ](https://oldschool.runescape.wiki/w/Snailfeet) | 1 | PARSED_UNREVIEWED |
| [Snake ](https://oldschool.runescape.wiki/w/Snake) | 2 | PARSED_UNREVIEWED |
| [Snake (Ape Atoll) ](https://oldschool.runescape.wiki/w/Snake_(Ape_Atoll)) | 2 | PARSED_UNREVIEWED |
| [Snake (desert) ](https://oldschool.runescape.wiki/w/Snake_(desert)) | 2 | PARSED_UNREVIEWED |
| [Snake (Mos Le'Harmless) ](https://oldschool.runescape.wiki/w/Snake_(Mos_Le%27Harmless)) | 1 | PARSED_UNREVIEWED |
| [Snakeling (Sea charting) ](https://oldschool.runescape.wiki/w/Snakeling_(Sea_charting)) | 0 | NO_SUPPORTED_TABLES |
| [Snothead ](https://oldschool.runescape.wiki/w/Snothead) | 1 | PARSED_UNREVIEWED |
| [Sofiya ](https://oldschool.runescape.wiki/w/Sofiya) | 55 | PARSED_UNREVIEWED |
| [Sol Heredit (Deadman) ](https://oldschool.runescape.wiki/w/Sol_Heredit_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Sol Heredit (Echo) ](https://oldschool.runescape.wiki/w/Sol_Heredit_(Echo)) | 2 | EXCLUDED_ALTERNATE_MODE |
| [Soldier (Burthorpe) ](https://oldschool.runescape.wiki/w/Soldier_(Burthorpe)) | 37 | PARSED_UNREVIEWED |
| [Soldier (Shayzien) ](https://oldschool.runescape.wiki/w/Soldier_(Shayzien)) | 32 | PARSED_UNREVIEWED |
| [Soldier (tier 1) ](https://oldschool.runescape.wiki/w/Soldier_(tier_1)) | 5 | PARSED_UNREVIEWED |
| [Soldier (tier 2) ](https://oldschool.runescape.wiki/w/Soldier_(tier_2)) | 5 | PARSED_UNREVIEWED |
| [Soldier (tier 3) ](https://oldschool.runescape.wiki/w/Soldier_(tier_3)) | 5 | PARSED_UNREVIEWED |
| [Soldier (tier 4) ](https://oldschool.runescape.wiki/w/Soldier_(tier_4)) | 5 | PARSED_UNREVIEWED |
| [Soldier (tier 5) ](https://oldschool.runescape.wiki/w/Soldier_(tier_5)) | 5 | PARSED_UNREVIEWED |
| [Soldier (Yanille) ](https://oldschool.runescape.wiki/w/Soldier_(Yanille)) | 22 | PARSED_UNREVIEWED |
| [Soldier Scarab ](https://oldschool.runescape.wiki/w/Soldier_Scarab) | 0 | NO_SUPPORTED_TABLES |
| [Solus Dellagar ](https://oldschool.runescape.wiki/w/Solus_Dellagar) | 0 | NO_SUPPORTED_TABLES |
| [Sorebones ](https://oldschool.runescape.wiki/w/Sorebones) | 49 | PARSED_UNREVIEWED |
| [Soulless ](https://oldschool.runescape.wiki/w/Soulless) | 1 | PARSED_UNREVIEWED |
| [Sourhog ](https://oldschool.runescape.wiki/w/Sourhog) | 88 | PARSED_UNREVIEWED |
| [Sourhog (A Porcine of Interest) ](https://oldschool.runescape.wiki/w/Sourhog_(A_Porcine_of_Interest)) | 0 | NO_SUPPORTED_TABLES |
| [Sparring partner ](https://oldschool.runescape.wiki/w/Sparring_partner) | 0 | NO_SUPPORTED_TABLES |
| [Spawn (Dragon Slayer II) ](https://oldschool.runescape.wiki/w/Spawn_(Dragon_Slayer_II)) | 0 | NO_SUPPORTED_TABLES |
| [Spawn of Sarachnis ](https://oldschool.runescape.wiki/w/Spawn_of_Sarachnis) | 0 | NO_SUPPORTED_TABLES |
| [Spawn sharhai ](https://oldschool.runescape.wiki/w/Spawn_sharhai) | 0 | NO_SUPPORTED_TABLES |
| [Speedy Keith ](https://oldschool.runescape.wiki/w/Speedy_Keith) | 46 | PARSED_UNREVIEWED |
| [Spider ](https://oldschool.runescape.wiki/w/Spider) | 4 | PARSED_UNREVIEWED |
| [Spider (Ape Atoll) ](https://oldschool.runescape.wiki/w/Spider_(Ape_Atoll)) | 1 | PARSED_UNREVIEWED |
| [Spider (Ungael) ](https://oldschool.runescape.wiki/w/Spider_(Ungael)) | 1 | PARSED_UNREVIEWED |
| [Spidine ](https://oldschool.runescape.wiki/w/Spidine) | 4 | PARSED_UNREVIEWED |
| [Spiked Turoth ](https://oldschool.runescape.wiki/w/Spiked_Turoth) | 59 | PARSED_UNREVIEWED |
| [Spindel's Spiderling ](https://oldschool.runescape.wiki/w/Spindel%27s_Spiderling) | 0 | NO_SUPPORTED_TABLES |
| [Spined kraken ](https://oldschool.runescape.wiki/w/Spined_kraken) | 59 | PARSED_UNREVIEWED |
| [Spinner ](https://oldschool.runescape.wiki/w/Spinner) | 0 | NO_SUPPORTED_TABLES |
| [Spinolyp ](https://oldschool.runescape.wiki/w/Spinolyp) | 1 | PARSED_UNREVIEWED |
| [Spiritual mage ](https://oldschool.runescape.wiki/w/Spiritual_mage) | 86 | PARSED_UNREVIEWED |
| [Spiritual ranger ](https://oldschool.runescape.wiki/w/Spiritual_ranger) | 68 | PARSED_UNREVIEWED |
| [Spiritual warrior ](https://oldschool.runescape.wiki/w/Spiritual_warrior) | 80 | PARSED_UNREVIEWED |
| [Spitting Scarab ](https://oldschool.runescape.wiki/w/Spitting_Scarab) | 0 | NO_SUPPORTED_TABLES |
| [Spitting Wyvern ](https://oldschool.runescape.wiki/w/Spitting_Wyvern) | 60 | PARSED_UNREVIEWED |
| [Splatter ](https://oldschool.runescape.wiki/w/Splatter) | 0 | NO_SUPPORTED_TABLES |
| [Splatter (Deadman) ](https://oldschool.runescape.wiki/w/Splatter_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Stag ](https://oldschool.runescape.wiki/w/Stag) | 1 | PARSED_UNREVIEWED |
| [Starlight ](https://oldschool.runescape.wiki/w/Starlight) | 18 | PARSED_UNREVIEWED |
| [Starved horror ](https://oldschool.runescape.wiki/w/Starved_horror) | 0 | NO_SUPPORTED_TABLES |
| [Steel dragon ](https://oldschool.runescape.wiki/w/Steel_dragon) | 58 | PARSED_UNREVIEWED |
| [Steel dragon (Construction) ](https://oldschool.runescape.wiki/w/Steel_dragon_(Construction)) | 0 | NO_SUPPORTED_TABLES |
| [Stick ](https://oldschool.runescape.wiki/w/Stick) | 49 | PARSED_UNREVIEWED |
| [Stingray ](https://oldschool.runescape.wiki/w/Stingray) | 57 | PARSED_UNREVIEWED |
| [Stone Guardian ](https://oldschool.runescape.wiki/w/Stone_Guardian) | 0 | NO_SUPPORTED_TABLES |
| [Strange Creature ](https://oldschool.runescape.wiki/w/Strange_Creature) | 0 | NO_SUPPORTED_TABLES |
| [Strange creature (Shadows of Custodia) ](https://oldschool.runescape.wiki/w/Strange_creature_(Shadows_of_Custodia)) | 41 | PARSED_UNREVIEWED |
| [Stranger ](https://oldschool.runescape.wiki/w/Stranger) | 0 | NO_SUPPORTED_TABLES |
| [Strangled ](https://oldschool.runescape.wiki/w/Strangled) | 1 | PARSED_UNREVIEWED |
| [Strangled Bear ](https://oldschool.runescape.wiki/w/Strangled_Bear) | 0 | NO_SUPPORTED_TABLES |
| [Strangled Boar ](https://oldschool.runescape.wiki/w/Strangled_Boar) | 0 | NO_SUPPORTED_TABLES |
| [Strangled Lynx ](https://oldschool.runescape.wiki/w/Strangled_Lynx) | 0 | NO_SUPPORTED_TABLES |
| [Strong Warrior ](https://oldschool.runescape.wiki/w/Strong_Warrior) | 0 | NO_SUPPORTED_TABLES |
| [Strongbones ](https://oldschool.runescape.wiki/w/Strongbones) | 1 | PARSED_UNREVIEWED |
| [Stunted demonic gorilla ](https://oldschool.runescape.wiki/w/Stunted_demonic_gorilla) | 0 | NO_SUPPORTED_TABLES |
| [Suit of armour ](https://oldschool.runescape.wiki/w/Suit_of_armour) | 0 | NO_SUPPORTED_TABLES |
| [Sulphur Lizard ](https://oldschool.runescape.wiki/w/Sulphur_Lizard) | 54 | PARSED_UNREVIEWED |
| [Sulphur Lizard (Deadman) ](https://oldschool.runescape.wiki/w/Sulphur_Lizard_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Sulphur Nagua ](https://oldschool.runescape.wiki/w/Sulphur_Nagua) | 28 | PARSED_UNREVIEWED |
| [Sulphur Nagua (Perilous Moons) ](https://oldschool.runescape.wiki/w/Sulphur_Nagua_(Perilous_Moons)) | 0 | NO_SUPPORTED_TABLES |
| [Summoned Zombie ](https://oldschool.runescape.wiki/w/Summoned_Zombie) | 1 | PARSED_UNREVIEWED |
| [Suqah ](https://oldschool.runescape.wiki/w/Suqah) | 21 | PARSED_UNREVIEWED |
| [Surok Magis ](https://oldschool.runescape.wiki/w/Surok_Magis) | 0 | NO_SUPPORTED_TABLES |
| [Svetlana ](https://oldschool.runescape.wiki/w/Svetlana) | 55 | PARSED_UNREVIEWED |
| [Swamp Crab ](https://oldschool.runescape.wiki/w/Swamp_Crab) | 37 | PARSED_UNREVIEWED |
| [Swamp frog ](https://oldschool.runescape.wiki/w/Swamp_frog) | 1 | PARSED_UNREVIEWED |
| [Swamp snake ](https://oldschool.runescape.wiki/w/Swamp_snake) | 2 | PARSED_UNREVIEWED |
| [Swordchick ](https://oldschool.runescape.wiki/w/Swordchick) | 4 | PARSED_UNREVIEWED |
| [Taloned Wyvern ](https://oldschool.runescape.wiki/w/Taloned_Wyvern) | 60 | PARSED_UNREVIEWED |
| [Tanglefoot ](https://oldschool.runescape.wiki/w/Tanglefoot) | 1 | PARSED_UNREVIEWED |
| [Tanglefoot (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Tanglefoot_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Tar Monster ](https://oldschool.runescape.wiki/w/Tar_Monster) | 7 | PARSED_UNREVIEWED |
| [Target ](https://oldschool.runescape.wiki/w/Target) | 0 | NO_SUPPORTED_TABLES |
| [Tarik (Nardah) ](https://oldschool.runescape.wiki/w/Tarik_(Nardah)) | 1 | PARSED_UNREVIEWED |
| [Temple Guardian ](https://oldschool.runescape.wiki/w/Temple_Guardian) | 0 | NO_SUPPORTED_TABLES |
| [Temple Spider ](https://oldschool.runescape.wiki/w/Temple_Spider) | 71 | PARSED_UNREVIEWED |
| [Tentacle (Temple Trekking) ](https://oldschool.runescape.wiki/w/Tentacle_(Temple_Trekking)) | 0 | NO_SUPPORTED_TABLES |
| [Tern ](https://oldschool.runescape.wiki/w/Tern) | 21 | PARSED_UNREVIEWED |
| [Terror dog ](https://oldschool.runescape.wiki/w/Terror_dog) | 90 | PARSED_UNREVIEWED |
| [Terrorbird ](https://oldschool.runescape.wiki/w/Terrorbird) | 2 | PARSED_UNREVIEWED |
| [The Everlasting (Nightmare Zone) ](https://oldschool.runescape.wiki/w/The_Everlasting_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [The Forsaken Assassin ](https://oldschool.runescape.wiki/w/The_Forsaken_Assassin) | 0 | NO_SUPPORTED_TABLES |
| [The Inadequacy (Nightmare Zone) ](https://oldschool.runescape.wiki/w/The_Inadequacy_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [The Jormungand ](https://oldschool.runescape.wiki/w/The_Jormungand) | 0 | NO_SUPPORTED_TABLES |
| [The Kendal ](https://oldschool.runescape.wiki/w/The_Kendal) | 2 | PARSED_UNREVIEWED |
| [The Kendal (Nightmare Zone) ](https://oldschool.runescape.wiki/w/The_Kendal_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [The rocks ](https://oldschool.runescape.wiki/w/The_rocks) | 2 | PARSED_UNREVIEWED |
| [The Shaikahan ](https://oldschool.runescape.wiki/w/The_Shaikahan) | 1 | PARSED_UNREVIEWED |
| [The Untouchable (Nightmare Zone) ](https://oldschool.runescape.wiki/w/The_Untouchable_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Thermonuclear smoke devil (Deadman) ](https://oldschool.runescape.wiki/w/Thermonuclear_smoke_devil_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Thermonuclear smoke devil (Echo) ](https://oldschool.runescape.wiki/w/Thermonuclear_smoke_devil_(Echo)) | 2 | EXCLUDED_ALTERNATE_MODE |
| [Thief ](https://oldschool.runescape.wiki/w/Thief) | 34 | PARSED_UNREVIEWED |
| [Thief (Varlamore) ](https://oldschool.runescape.wiki/w/Thief_(Varlamore)) | 31 | PARSED_UNREVIEWED |
| [Thing under the bed ](https://oldschool.runescape.wiki/w/Thing_under_the_bed) | 1 | PARSED_UNREVIEWED |
| [Third Age Mage ](https://oldschool.runescape.wiki/w/Third_Age_Mage) | 0 | NO_SUPPORTED_TABLES |
| [Third Age Ranger ](https://oldschool.runescape.wiki/w/Third_Age_Ranger) | 0 | NO_SUPPORTED_TABLES |
| [Third Age Warrior ](https://oldschool.runescape.wiki/w/Third_Age_Warrior) | 0 | NO_SUPPORTED_TABLES |
| [Thora (Miscellania) ](https://oldschool.runescape.wiki/w/Thora_(Miscellania)) | 1 | PARSED_UNREVIEWED |
| [Thorhild ](https://oldschool.runescape.wiki/w/Thorhild) | 1 | PARSED_UNREVIEWED |
| [Thrantax the Mighty ](https://oldschool.runescape.wiki/w/Thrantax_the_Mighty) | 0 | NO_SUPPORTED_TABLES |
| [Thrower Troll ](https://oldschool.runescape.wiki/w/Thrower_Troll) | 72 | PARSED_UNREVIEWED |
| [Thrower troll (Trollheim) ](https://oldschool.runescape.wiki/w/Thrower_troll_(Trollheim)) | 3 | PARSED_UNREVIEWED |
| [Thug ](https://oldschool.runescape.wiki/w/Thug) | 30 | PARSED_UNREVIEWED |
| [Tiger shark ](https://oldschool.runescape.wiki/w/Tiger_shark) | 32 | PARSED_UNREVIEWED |
| [Tok-Xil ](https://oldschool.runescape.wiki/w/Tok-Xil) | 0 | NO_SUPPORTED_TABLES |
| [Tok-Xil (Construction) ](https://oldschool.runescape.wiki/w/Tok-Xil_(Construction)) | 0 | NO_SUPPORTED_TABLES |
| [Torcher ](https://oldschool.runescape.wiki/w/Torcher) | 0 | NO_SUPPORTED_TABLES |
| [Tormented Demon ](https://oldschool.runescape.wiki/w/Tormented_Demon) | 47 | PARSED_UNREVIEWED |
| [Tormented Soul ](https://oldschool.runescape.wiki/w/Tormented_Soul) | 0 | NO_SUPPORTED_TABLES |
| [Tormented Warrior ](https://oldschool.runescape.wiki/w/Tormented_Warrior) | 1 | PARSED_UNREVIEWED |
| [Tortoise ](https://oldschool.runescape.wiki/w/Tortoise) | 103 | PARSED_UNREVIEWED |
| [Tortured gorilla ](https://oldschool.runescape.wiki/w/Tortured_gorilla) | 72 | PARSED_UNREVIEWED |
| [Tortured soul ](https://oldschool.runescape.wiki/w/Tortured_soul) | 42 | PARSED_UNREVIEWED |
| [Tough Guy ](https://oldschool.runescape.wiki/w/Tough_Guy) | 2 | PARSED_UNREVIEWED |
| [Tower Archer ](https://oldschool.runescape.wiki/w/Tower_Archer) | 4 | PARSED_UNREVIEWED |
| [Tower guard ](https://oldschool.runescape.wiki/w/Tower_guard) | 22 | PARSED_UNREVIEWED |
| [Trapped Soul ](https://oldschool.runescape.wiki/w/Trapped_Soul) | 1 | PARSED_UNREVIEWED |
| [Trapped Soul (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Trapped_Soul_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Tree spirit ](https://oldschool.runescape.wiki/w/Tree_spirit) | 48 | PARSED_UNREVIEWED |
| [Tree spirit (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Tree_spirit_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Treus Dayth (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Treus_Dayth_(Nightmare_Zone)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Tribesman ](https://oldschool.runescape.wiki/w/Tribesman) | 47 | PARSED_UNREVIEWED |
| [Tribeswoman ](https://oldschool.runescape.wiki/w/Tribeswoman) | 47 | PARSED_UNREVIEWED |
| [Troll (Construction) ](https://oldschool.runescape.wiki/w/Troll_(Construction)) | 0 | NO_SUPPORTED_TABLES |
| [Troll (historical) ](https://oldschool.runescape.wiki/w/Troll_(historical)) | 0 | NO_SUPPORTED_TABLES |
| [Troll (Realm of Memories) ](https://oldschool.runescape.wiki/w/Troll_(Realm_of_Memories)) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Troll general ](https://oldschool.runescape.wiki/w/Troll_general) | 50 | PARSED_UNREVIEWED |
| [Troll spectator ](https://oldschool.runescape.wiki/w/Troll_spectator) | 70 | PARSED_UNREVIEWED |
| [Tstanon Karlak ](https://oldschool.runescape.wiki/w/Tstanon_Karlak) | 19 | PARSED_UNREVIEWED |
| [Tumeken's Warden (Deadman) ](https://oldschool.runescape.wiki/w/Tumeken%27s_Warden_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Turoth ](https://oldschool.runescape.wiki/w/Turoth) | 55 | PARSED_UNREVIEWED |
| [Twig ](https://oldschool.runescape.wiki/w/Twig) | 74 | PARSED_UNREVIEWED |
| [Twisted Banshee ](https://oldschool.runescape.wiki/w/Twisted_Banshee) | 62 | PARSED_UNREVIEWED |
| [Typhor ](https://oldschool.runescape.wiki/w/Typhor) | 0 | NO_SUPPORTED_TABLES |
| [Tyras guard ](https://oldschool.runescape.wiki/w/Tyras_guard) | 31 | PARSED_UNREVIEWED |
| [Tz-Kek ](https://oldschool.runescape.wiki/w/Tz-Kek) | 0 | NO_SUPPORTED_TABLES |
| [Tz-Kih ](https://oldschool.runescape.wiki/w/Tz-Kih) | 0 | NO_SUPPORTED_TABLES |
| [TzHaar-Hur ](https://oldschool.runescape.wiki/w/TzHaar-Hur) | 17 | PARSED_UNREVIEWED |
| [TzHaar-Ket ](https://oldschool.runescape.wiki/w/TzHaar-Ket) | 24 | PARSED_UNREVIEWED |
| [TzHaar-Mej ](https://oldschool.runescape.wiki/w/TzHaar-Mej) | 15 | PARSED_UNREVIEWED |
| [TzHaar-Xil ](https://oldschool.runescape.wiki/w/TzHaar-Xil) | 10 | PARSED_UNREVIEWED |
| [TzTok-Jad (Deadman) ](https://oldschool.runescape.wiki/w/TzTok-Jad_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [TzTok-Jad (PvM Arena) ](https://oldschool.runescape.wiki/w/TzTok-Jad_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [TzTok-Jad-Rek ](https://oldschool.runescape.wiki/w/TzTok-Jad-Rek) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Ugthanki ](https://oldschool.runescape.wiki/w/Ugthanki) | 2 | PARSED_UNREVIEWED |
| [Umbra ](https://oldschool.runescape.wiki/w/Umbra) | 5 | PARSED_UNREVIEWED |
| [Unbound JalTok-Jad ](https://oldschool.runescape.wiki/w/Unbound_JalTok-Jad) | 0 | NO_SUPPORTED_TABLES |
| [Undead chicken ](https://oldschool.runescape.wiki/w/Undead_chicken) | 5 | PARSED_UNREVIEWED |
| [Undead cow ](https://oldschool.runescape.wiki/w/Undead_cow) | 4 | PARSED_UNREVIEWED |
| [Undead Druid ](https://oldschool.runescape.wiki/w/Undead_Druid) | 89 | PARSED_UNREVIEWED |
| [Undead Lumberjack ](https://oldschool.runescape.wiki/w/Undead_Lumberjack) | 7 | PARSED_UNREVIEWED |
| [Undead one ](https://oldschool.runescape.wiki/w/Undead_one) | 3 | PARSED_UNREVIEWED |
| [Undead Zealot ](https://oldschool.runescape.wiki/w/Undead_Zealot) | 2 | PARSED_UNREVIEWED |
| [Underwater horror ](https://oldschool.runescape.wiki/w/Underwater_horror) | 0 | NO_SUPPORTED_TABLES |
| [Ungadulu ](https://oldschool.runescape.wiki/w/Ungadulu) | 0 | NO_SUPPORTED_TABLES |
| [Unicorn ](https://oldschool.runescape.wiki/w/Unicorn) | 4 | PARSED_UNREVIEWED |
| [Unicorn Foal ](https://oldschool.runescape.wiki/w/Unicorn_Foal) | 1 | PARSED_UNREVIEWED |
| [Unicow ](https://oldschool.runescape.wiki/w/Unicow) | 4 | PARSED_UNREVIEWED |
| [Unusual Chicken ](https://oldschool.runescape.wiki/w/Unusual_Chicken) | 3 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Uodai ](https://oldschool.runescape.wiki/w/Uodai) | 1 | PARSED_UNREVIEWED |
| [Urium Shade ](https://oldschool.runescape.wiki/w/Urium_Shade) | 1 | PARSED_UNREVIEWED |
| [Uyoro ](https://oldschool.runescape.wiki/w/Uyoro) | 1 | PARSED_UNREVIEWED |
| [Valgerd ](https://oldschool.runescape.wiki/w/Valgerd) | 1 | PARSED_UNREVIEWED |
| [Vampyre Juvenile ](https://oldschool.runescape.wiki/w/Vampyre_Juvenile) | 2 | PARSED_UNREVIEWED |
| [Vampyre Juvinate ](https://oldschool.runescape.wiki/w/Vampyre_Juvinate) | 38 | PARSED_UNREVIEWED |
| [Vampyre kraken ](https://oldschool.runescape.wiki/w/Vampyre_kraken) | 65 | PARSED_UNREVIEWED |
| [Vampyre Snail ](https://oldschool.runescape.wiki/w/Vampyre_Snail) | 1 | PARSED_UNREVIEWED |
| [Vanstrom Klause ](https://oldschool.runescape.wiki/w/Vanstrom_Klause) | 0 | NO_SUPPORTED_TABLES |
| [Vardorvis (Deadman) ](https://oldschool.runescape.wiki/w/Vardorvis_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Veiled kraken ](https://oldschool.runescape.wiki/w/Veiled_kraken) | 47 | PARSED_UNREVIEWED |
| [Venator ](https://oldschool.runescape.wiki/w/Venator) | 42 | EXCLUDED_ALTERNATE_MODE |
| [Venator (The Blood Moon Rises) ](https://oldschool.runescape.wiki/w/Venator_(The_Blood_Moon_Rises)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Venenatis (PvM Arena) ](https://oldschool.runescape.wiki/w/Venenatis_(PvM_Arena)) | 0 | NO_SUPPORTED_TABLES |
| [Venenatis' Spiderling (monster) ](https://oldschool.runescape.wiki/w/Venenatis%27_Spiderling_(monster)) | 0 | NO_SUPPORTED_TABLES |
| [Vera ](https://oldschool.runescape.wiki/w/Vera) | 55 | PARSED_UNREVIEWED |
| [Vespine soldier ](https://oldschool.runescape.wiki/w/Vespine_soldier) | 0 | NO_SUPPORTED_TABLES |
| [Vitreous Chilled Jelly ](https://oldschool.runescape.wiki/w/Vitreous_Chilled_Jelly) | 37 | PARSED_UNREVIEWED |
| [Vitreous Jelly ](https://oldschool.runescape.wiki/w/Vitreous_Jelly) | 35 | PARSED_UNREVIEWED |
| [Vitreous Warped Jelly ](https://oldschool.runescape.wiki/w/Vitreous_Warped_Jelly) | 39 | PARSED_UNREVIEWED |
| [Vitreous warped Jelly (Deadman) ](https://oldschool.runescape.wiki/w/Vitreous_warped_Jelly_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Viyeldi ](https://oldschool.runescape.wiki/w/Viyeldi) | 0 | NO_SUPPORTED_TABLES |
| [Void Knight (Pest Control) ](https://oldschool.runescape.wiki/w/Void_Knight_(Pest_Control)) | 0 | NO_SUPPORTED_TABLES |
| [Volatile Baboon ](https://oldschool.runescape.wiki/w/Volatile_Baboon) | 0 | NO_SUPPORTED_TABLES |
| [Vulture ](https://oldschool.runescape.wiki/w/Vulture) | 2 | PARSED_UNREVIEWED |
| [Vyrewatch ](https://oldschool.runescape.wiki/w/Vyrewatch) | 113 | PARSED_UNREVIEWED |
| [Vyrewatch Sentinel ](https://oldschool.runescape.wiki/w/Vyrewatch_Sentinel) | 93 | PARSED_UNREVIEWED |
| [Wall beast ](https://oldschool.runescape.wiki/w/Wall_beast) | 21 | PARSED_UNREVIEWED |
| [Wallasalki ](https://oldschool.runescape.wiki/w/Wallasalki) | 65 | PARSED_UNREVIEWED |
| [Warped Jelly ](https://oldschool.runescape.wiki/w/Warped_Jelly) | 35 | PARSED_UNREVIEWED |
| [Warped Terrorbird ](https://oldschool.runescape.wiki/w/Warped_Terrorbird) | 35 | PARSED_UNREVIEWED |
| [Warped Tortoise ](https://oldschool.runescape.wiki/w/Warped_Tortoise) | 36 | PARSED_UNREVIEWED |
| [Warrior (Rellekka) ](https://oldschool.runescape.wiki/w/Warrior_(Rellekka)) | 46 | PARSED_UNREVIEWED |
| [Warrior (Thieving) ](https://oldschool.runescape.wiki/w/Warrior_(Thieving)) | 3 | PARSED_UNREVIEWED |
| [Warrior of Murahs ](https://oldschool.runescape.wiki/w/Warrior_of_Murahs) | 0 | EXCLUDED_ALTERNATE_MODE |
| [Watchman ](https://oldschool.runescape.wiki/w/Watchman) | 4 | PARSED_UNREVIEWED |
| [Watchman (random event) ](https://oldschool.runescape.wiki/w/Watchman_(random_event)) | 0 | NO_SUPPORTED_TABLES |
| [Water elemental ](https://oldschool.runescape.wiki/w/Water_elemental) | 34 | PARSED_UNREVIEWED |
| [Water wizard ](https://oldschool.runescape.wiki/w/Water_wizard) | 3 | PARSED_UNREVIEWED |
| [Waterfiend ](https://oldschool.runescape.wiki/w/Waterfiend) | 79 | PARSED_UNREVIEWED |
| [Weaponsmaster ](https://oldschool.runescape.wiki/w/Weaponsmaster) | 1 | PARSED_UNREVIEWED |
| [Webbed-winged Crow ](https://oldschool.runescape.wiki/w/Webbed-winged_Crow) | 0 | NO_SUPPORTED_TABLES |
| [Well-armed Foe ](https://oldschool.runescape.wiki/w/Well-armed_Foe) | 1 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Werewolf ](https://oldschool.runescape.wiki/w/Werewolf) | 48 | PARSED_UNREVIEWED |
| [Whale ](https://oldschool.runescape.wiki/w/Whale) | 0 | NO_SUPPORTED_TABLES |
| [White jaguar ](https://oldschool.runescape.wiki/w/White_jaguar) | 3 | PARSED_UNREVIEWED |
| [White Knight ](https://oldschool.runescape.wiki/w/White_Knight) | 236 | PARSED_UNREVIEWED |
| [White Knight (Burthorpe) ](https://oldschool.runescape.wiki/w/White_Knight_(Burthorpe)) | 1 | PARSED_UNREVIEWED |
| [White wolf ](https://oldschool.runescape.wiki/w/White_wolf) | 3 | PARSED_UNREVIEWED |
| [Wighted Leech ](https://oldschool.runescape.wiki/w/Wighted_Leech) | 0 | NO_SUPPORTED_TABLES |
| [Wild dog ](https://oldschool.runescape.wiki/w/Wild_dog) | 2 | PARSED_UNREVIEWED |
| [Wild dog (Last Man Standing) ](https://oldschool.runescape.wiki/w/Wild_dog_(Last_Man_Standing)) | 3 | EXCLUDED_ALTERNATE_MODE |
| [Wilson ](https://oldschool.runescape.wiki/w/Wilson) | 1 | PARSED_UNREVIEWED |
| [Wingman Skree ](https://oldschool.runescape.wiki/w/Wingman_Skree) | 21 | PARSED_UNREVIEWED |
| [Witch (monster) ](https://oldschool.runescape.wiki/w/Witch_(monster)) | 0 | HELD_DISCONTINUED_OR_EVENT_SOURCE |
| [Witch's experiment ](https://oldschool.runescape.wiki/w/Witch%27s_experiment) | 0 | NO_SUPPORTED_TABLES |
| [Witch's experiment (Nightmare Zone) ](https://oldschool.runescape.wiki/w/Witch%27s_experiment_(Nightmare_Zone)) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Wizard ](https://oldschool.runescape.wiki/w/Wizard) | 28 | PARSED_UNREVIEWED |
| [Wolf ](https://oldschool.runescape.wiki/w/Wolf) | 4 | PARSED_UNREVIEWED |
| [Wolf (Soul Wars) ](https://oldschool.runescape.wiki/w/Wolf_(Soul_Wars)) | 1 | PARSED_UNREVIEWED |
| [Wolfman ](https://oldschool.runescape.wiki/w/Wolfman) | 0 | NO_SUPPORTED_TABLES |
| [Wolfwoman ](https://oldschool.runescape.wiki/w/Wolfwoman) | 0 | NO_SUPPORTED_TABLES |
| [Woman ](https://oldschool.runescape.wiki/w/Woman) | 35 | PARSED_UNREVIEWED |
| [Woman (Canifis) ](https://oldschool.runescape.wiki/w/Woman_(Canifis)) | 0 | NO_SUPPORTED_TABLES |
| [Woman (East Ardougne) ](https://oldschool.runescape.wiki/w/Woman_(East_Ardougne)) | 2 | PARSED_UNREVIEWED |
| [Woman (historical) ](https://oldschool.runescape.wiki/w/Woman_(historical)) | 0 | NO_SUPPORTED_TABLES |
| [Woman (West Ardougne) ](https://oldschool.runescape.wiki/w/Woman_(West_Ardougne)) | 1 | PARSED_UNREVIEWED |
| [Wormbrain ](https://oldschool.runescape.wiki/w/Wormbrain) | 1 | PARSED_UNREVIEWED |
| [Wuss ](https://oldschool.runescape.wiki/w/Wuss) | 0 | NO_SUPPORTED_TABLES |
| [Wyrd ](https://oldschool.runescape.wiki/w/Wyrd) | 0 | NO_SUPPORTED_TABLES |
| [Wyrm ](https://oldschool.runescape.wiki/w/Wyrm) | 62 | PARSED_UNREVIEWED |
| [Wyrmling ](https://oldschool.runescape.wiki/w/Wyrmling) | 2 | PARSED_UNREVIEWED |
| [Yadviga ](https://oldschool.runescape.wiki/w/Yadviga) | 55 | PARSED_UNREVIEWED |
| [Yak ](https://oldschool.runescape.wiki/w/Yak) | 4 | PARSED_UNREVIEWED |
| [Yama (Deadman) ](https://oldschool.runescape.wiki/w/Yama_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Yeti ](https://oldschool.runescape.wiki/w/Yeti) | 0 | NO_SUPPORTED_TABLES |
| [Young impling ](https://oldschool.runescape.wiki/w/Young_impling) | 21 | PARSED_UNREVIEWED |
| [Yrsa (Miscellania) ](https://oldschool.runescape.wiki/w/Yrsa_(Miscellania)) | 0 | NO_SUPPORTED_TABLES |
| [Yt-HurKot ](https://oldschool.runescape.wiki/w/Yt-HurKot) | 0 | NO_SUPPORTED_TABLES |
| [Yt-MejKot ](https://oldschool.runescape.wiki/w/Yt-MejKot) | 0 | NO_SUPPORTED_TABLES |
| [Yuri ](https://oldschool.runescape.wiki/w/Yuri) | 55 | PARSED_UNREVIEWED |
| [Zakl'n Gritch ](https://oldschool.runescape.wiki/w/Zakl%27n_Gritch) | 20 | PARSED_UNREVIEWED |
| [Zamorak crafter ](https://oldschool.runescape.wiki/w/Zamorak_crafter) | 14 | PARSED_UNREVIEWED |
| [Zamorak mage ](https://oldschool.runescape.wiki/w/Zamorak_mage) | 8 | PARSED_UNREVIEWED |
| [Zamorak ranger ](https://oldschool.runescape.wiki/w/Zamorak_ranger) | 8 | PARSED_UNREVIEWED |
| [Zamorak warrior ](https://oldschool.runescape.wiki/w/Zamorak_warrior) | 8 | PARSED_UNREVIEWED |
| [Zamorak wizard ](https://oldschool.runescape.wiki/w/Zamorak_wizard) | 25 | PARSED_UNREVIEWED |
| [Zemouregal (Deadman) ](https://oldschool.runescape.wiki/w/Zemouregal_(Deadman)) | 90 | EXCLUDED_ALTERNATE_MODE |
| [Zemouregal Summon ](https://oldschool.runescape.wiki/w/Zemouregal_Summon) | 1 | EXCLUDED_ALTERNATE_MODE |
| [Zogre ](https://oldschool.runescape.wiki/w/Zogre) | 4 | PARSED_UNREVIEWED |
| [Zoja ](https://oldschool.runescape.wiki/w/Zoja) | 55 | PARSED_UNREVIEWED |
| [Zombie ](https://oldschool.runescape.wiki/w/Zombie) | 81 | PARSED_UNREVIEWED |
| [Zombie (Entrana Dungeon) ](https://oldschool.runescape.wiki/w/Zombie_(Entrana_Dungeon)) | 25 | PARSED_UNREVIEWED |
| [Zombie (Kourend) ](https://oldschool.runescape.wiki/w/Zombie_(Kourend)) | 45 | PARSED_UNREVIEWED |
| [Zombie (Melzar's Maze) ](https://oldschool.runescape.wiki/w/Zombie_(Melzar%27s_Maze)) | 4 | PARSED_UNREVIEWED |
| [Zombie (Sepulchre of Death) ](https://oldschool.runescape.wiki/w/Zombie_(Sepulchre_of_Death)) | 32 | PARSED_UNREVIEWED |
| [Zombie (Shayzien Crypts) ](https://oldschool.runescape.wiki/w/Zombie_(Shayzien_Crypts)) | 112 | PARSED_UNREVIEWED |
| [Zombie (Stronghold of Security) ](https://oldschool.runescape.wiki/w/Zombie_(Stronghold_of_Security)) | 32 | PARSED_UNREVIEWED |
| [Zombie (Tarn's Lair) ](https://oldschool.runescape.wiki/w/Zombie_(Tarn%27s_Lair)) | 190 | PARSED_UNREVIEWED |
| [Zombie (Temple Trekking) ](https://oldschool.runescape.wiki/w/Zombie_(Temple_Trekking)) | 3 | PARSED_UNREVIEWED |
| [Zombie (Wilderness) ](https://oldschool.runescape.wiki/w/Zombie_(Wilderness)) | 50 | PARSED_UNREVIEWED |
| [Zombie (Zogre Flesh Eaters) ](https://oldschool.runescape.wiki/w/Zombie_(Zogre_Flesh_Eaters)) | 3 | PARSED_UNREVIEWED |
| [Zombie pirate ](https://oldschool.runescape.wiki/w/Zombie_pirate) | 66 | PARSED_UNREVIEWED |
| [Zombie pirate (Braindeath Island) ](https://oldschool.runescape.wiki/w/Zombie_pirate_(Braindeath_Island)) | 45 | PARSED_UNREVIEWED |
| [Zombie pirate (Harmony Island) ](https://oldschool.runescape.wiki/w/Zombie_pirate_(Harmony_Island)) | 45 | PARSED_UNREVIEWED |
| [Zombie rat ](https://oldschool.runescape.wiki/w/Zombie_rat) | 3 | PARSED_UNREVIEWED |
| [Zombie swab ](https://oldschool.runescape.wiki/w/Zombie_swab) | 45 | PARSED_UNREVIEWED |
| [Zombies Champion ](https://oldschool.runescape.wiki/w/Zombies_Champion) | 0 | NO_SUPPORTED_TABLES |
| [Zygomite ](https://oldschool.runescape.wiki/w/Zygomite) | 66 | PARSED_UNREVIEWED |

## Validation and continuation

Run the all-tier builder against the original extracted output, then `validate_hunt_review_index.py` and22 existing offline tests. Validation covers raw SQLite equality, exact brackets, all31,757 row dispositions, complete item inventory, selected/held index equality, source-kind split, known four-gem regression cases, source-mode/shared-table exclusions, unchanged44 historical hunt IDs and output reproducibility. `CHALLENGE_TABLE_VALIDATION_A67.json` stores the measured assertions. The owning checkpoint must be published with an expected-head guard and remote head/tree/main verification.

Next content work is contextual admission/access/receipt/FP review of these visible candidates, prioritizing ordinary task-independent sources.399 unsupported/empty captures and444 unresolved entity classifications are explicit gaps, not completed research or permission to re-scrape the whole supply. Unanswered unlock topology/A56 recovery proposals remain pending.
