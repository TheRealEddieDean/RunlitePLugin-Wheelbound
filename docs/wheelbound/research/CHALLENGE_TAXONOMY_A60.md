# A60 — corrected source taxonomy and grouping overlay

Input is the untouched uploaded archive pinned by A58. This overlay reviews all 2,929 parsed pages, not just the first three sources. It does not edit the collector output. Reproduce with `python tools/wiki_drops/build_taxonomy_review.py INPUT/catalog.json OUTPUT.json`.

| Corrected kind | Page records |
| --- | ---: |
| Monster | 1,437 |
| Boss | 161 |
| Item, scenery or pet; removed from creature table | 855 |
| Encounter component; grouped under parent | 32 |
| Entity unresolved; excluded from selection | 444 |

[Machine overlay](CHALLENGE_TAXONOMY_A60.json) retains 1,339 changed/quarantined labels by source path and original label, plus 32 parent-component mappings and alternate-mode exclusions. These page counts are not eligible encounter counts. The previous 203-boss/1,754-monster export is superseded for **curated selection only**; raw evidence stays unchanged.

Explicit Items/Scenery/Pets categories take precedence over relational categories such as Items dropped by monster. A combat/Slayer infobox is required for automatic positive creature identity. Only direct Bosses plus creature evidence establishes a boss in this overlay. Ancestry alone no longer promotes a monster to boss. Unresolved entities remain unavailable rather than being guessed from names or combat level.

The named children of Nightmare/Phosani, Zulrah, Araxxor, Scurrius, Abyssal Sire, Royal Titans, Cerberus, Vorkath, Corp, Barrows, Zalcano, Whisperer, Yama and Maggot King are not separate Challenge wheel entries. Grouping does not authorize their drops to satisfy a parent's task. Other ancestry-only labels become ordinary monsters where supported, rather than being forced into a guessed parent. Sulphur Nagua, for example, is no longer called a boss merely because discovery passed through a boss category.

Normal variants on one source page share one future encounter key. Different encounter rules remain distinct: Kraken versus cave kraken, ordinary versus Phosani Nightmare, and different Wilderness bosses are not collapsed. Alternate-mode Nightmare Zone, Deadman, Echo, Realm-of-Memories, Leagues and alpha pages are explicitly excluded from v1 normal-world contexts. Shared NPC IDs alone do not establish equivalence; Arianwyn and Nightmare Zone, and Nightmare totems, retain contextual distinctions. The curated catalog supplies explicit selected-source NPC/form mappings; these do not multiply creature odds.

Validation: counts sum to 2,929; every changed source path is unique and resolves to the uploaded record; Oathplate helm/Fishy salvage/Steel Chest leave the creature pool; every component has parent metadata and cannot independently count toward diversity. No runtime source adapter is enabled. Follow-on A61 authors the finite item/context roster from this corrected corpus.

A62 cross-check correction: all parent links now require the named parent in the captured introduction. Scion/Spawn belong to Abyssal Sire, Void Flare to Yama, Ur-maggot larvae to Maggot King. Ordinary Araxyte is an independent Slayer monster, not a boss component. These corrected five inferences supersede the initial A60 overlay; the raw source and immutable A60 commit remain intact. The current overlay has32 components/1,437 monsters. None affects the25-key authored hunt roster.
