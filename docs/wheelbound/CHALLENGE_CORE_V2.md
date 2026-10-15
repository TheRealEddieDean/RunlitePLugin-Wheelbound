# Finite four-rank Challenge design core — A89

Compiled from three reviewed content fragments; **14 canonical keys/16 hunts**. This is a finite authored core, not all483 display groups, the full future catalog, current-account eligibility or a playable implementation. Every runtime flag is false. [Core JSON](catalogs/challenge_core_v2_draft.json) pins source fragment hashes and frozen quote versions.

| Rank | Canonical keys before account filtering | Hunts |
| --- | ---: | ---: |
| lesser | 4 | 4 |
| standard | 4 | 4 |
| greater | 3 | 5 |
| superior | 3 | 3 |

## Lesser

| Source | Exact dropped item | Base rate | Draft FP | Authored mean hours |
| --- | --- | --- | ---: | ---: |
| Minotaur | Right skull half (9007) | 3/101 | 125 | 0.187 |
| Flesh Crawler | Bottom of sceptre (9011) | 3/100 | 125 | 0.222 |
| Catablepon | Top of sceptre (9010) | 3/101 | 150 | 0.281 |
| Ankou | Left skull half (9008) | 3/100 | 150 | 0.370 |

## Standard

| Source | Exact dropped item | Base rate | Draft FP | Authored mean hours |
| --- | --- | --- | ---: | ---: |
| Killerwatt | Air diamond (34404) | 1/64 | 175 | 0.711 |
| Molanisk | Earth emerald (34416) | 1/64 | 175 | 0.533 |
| Infernal Mage | Fire ruby (34422) | 1/64 | 150 | 0.427 |
| Sea Snake Hatchling | Water sapphire (34410) | 1/64 | 175 | 0.533 |

## Greater

| Source | Exact dropped item | Base rate | Draft FP | Authored mean hours |
| --- | --- | --- | ---: | ---: |
| Commander Zilyana | Armadyl crossbow (11785) | 1/508 | 2650 | 25.400 |
| Commander Zilyana | Saradomin hilt (11814) | 1/508 | 2650 | 25.400 |
| K'ril Tsutsaroth | Staff of the Dead (11791) | 1/508 | 2650 | 25.400 |
| K'ril Tsutsaroth | Zamorak hilt (11816) | 1/508 | 2650 | 25.400 |
| Spitting Wyvern | Granite longsword (21646) | 1/512 | 975 | 8.533 |

## Superior

| Source | Exact dropped item | Base rate | Draft FP | Authored mean hours |
| --- | --- | --- | ---: | ---: |
| Kurask | Leaf-bladed battleaxe (20727) | 1/1026 | 975 | 8.550 |
| Rune dragon | Dragon limbs (21918) | 1/800 | 1900 | 17.778 |
| Vyrewatch Sentinel | Blood shard (24777) | 1/1500 | 1775 | 16.667 |

## Meaning of coverage and held state

Three keys per rank is a design-content count before owned/enabled/source/actual-access/client/quote filtering. Unknown predicates remain unsupported; no gate is waived by compiler output. Higher-rank ownership retains lower ranks; one Challenge family and default2/upgraded3 offers remain. Creature-first/item-second sampling does not give multi-item God Wars bosses extra creature weight.

Lesser sources are the first-purchase included bundle; other sources are individual expansions. Product prices and numeric family frequencies remain unadopted. Composition values are pinned *leads*, not a declaration that every passive/phase form emits personal loot. Actual source/region/time/recipient correlation and durable once-only settlement remain mandatory. Keep item, require fresh active drop, no prior-possession exclusion, KC cap, pity or repricing.

Reference hours use source models and authored throughput, not measured training rates or guarantees. Rank rarity does not order task duration. Source provenance distinguishes direct A84 implementation evidence from mediated captured moderator statements and page-level Jagex-provided models. Remaining36 old hunts, broader candidates, Young variant and Turoth conflict are not silently imported. Original v1 and raw evidence are preserved.

Reproduce: `python tools/wiki_drops/build_challenge_core_v2.py`; validate source/quote/rank mutations with `python tools/wiki_drops/check_challenge_core_v2.py`. These are offline design checks, not future client acceptance.
