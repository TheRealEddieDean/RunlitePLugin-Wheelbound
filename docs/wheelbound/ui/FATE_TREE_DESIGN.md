# Fate Tree interaction and Java2D design

> **Current direction: [simple UI first](SIMPLE_UI_FIRST.md).** Use normal buttons and functional lists for initial testing; decorative styling and complex tree presentation are deferred. Earlier mockups are exploratory.

Status: first review concept, Category B. [Preview](previews/fate-tree.svg). Diagram uses real approved Tier-I names and the five-of-nine gate with illustrative ownership; it is a layout study, not a complete catalog or current account state. No preview-only nodes may enter the runtime graph.

## Layout and rule authority

Preserve D093 illuminated root/free introductory child and square icon nodes; D095 circular peer clusters inside a vertical progression tree. Four named branch regions: Skilling, Questing, Combat/Bossing, Fate Manipulation. Center the root above them; scroll/pan downward through branch tiers. Zoom-to-branch and global Fit keep the broader tree navigable. Tier-I skills are Cooking, Firemaking, Smithing, Crafting, Fletching, Agility, Thieving, Runecraft, Prayer. Current PROGRESSION/DECISIONS place Slayer in Tier II alongside Herblore, Farming, Construction, Sailing, Hunter; do not follow older conversation summaries moving it to an invented final tier. Tier gates five/nine and four/six count distinct purchased skills, not duplicates/active slices. Starting activities are not extra purchased tier nodes.

Use stable IDs and metadata from one versioned production catalog still to be authored under WB-TREE-01. ProgressionGraph currently has private ROOT/OWNED/ALL/ANY expressions and only canUnlock: renderer needs a read-only traversal/visitor, plus an authorized unique-count predicate. Do not infer prerequisites by reverse-engineering screen position or duplicating rules in a UI JSON file. Layout metadata is presentation-only: branch, tier, peer order, major/minor treatment and optional label anchor keyed to existing catalog IDs. Layout cannot grant ownership or change costs.

Catalog authoring must trace each node/benefit/gate to decisions, flag unresolved prices/gates and preserve lower-rank Challenge ownership. Challenge chain: ordinary Bossing Lesser AND Greater → first paid Challenge Lesser with included bundle → Standard → Greater → Superior. Shared target ownership, owned/enabled/ready separation, per-rank three eligible canonical targets and legal setting boundaries follow D147–150. Do not add filler unlock nodes just to draw gates. Count/AND/OR gate symbols are non-purchasable annotations unless an approved catalog explicitly defines a product.

## Interaction

Fixed top bar: search, branch filter, Fit, Reset, +/- and zoom percentage; FP/debt in global header. Fixed right inspector: selected node name/benefit, cost or “Price awaiting review”, owned/prerequisite/affordability/support states, explicit ALL/ANY and count progress, prerequisite route, Unlock. Search shows name/benefit results with status; click centers/selects without purchasing. Filters dim unrelated paths rather than changing the graph. Unknown catalog/price is disabled, never free.

Wheel zoom anchors graph point under cursor, clamp 0.4–2.0 initially; blank left-drag pans, click selects, movement >4 logical units becomes a drag. Dragging an icon pans only after threshold and cannot purchase on release. Fit frames visible catalog bounds with 32 margin. Reset clears pan/filter and returns root at 1.0, preserving ownership. Retain view only per catalog version; do not reuse coordinates across incompatible catalogs.

Purchased paths gold, eligible paths subdued blue, locked paths iron/dashed with lock labels; selected prerequisite route adds a pale outline. AND junction uses bracket + ALL; OR uses split + ANY; count shows milestone 5/5 separately from owned 5/9. OR route highlight displays alternatives, never claims one mandatory “best route”; no cheapest-route calculation with unapproved prices. Purchased lower ranks retain checks after higher purchase.

At overview (<0.65), show branch/major names and state glyphs, hide minor labels; at normal show node labels; at detail show costs where authored. Inspector text remains unscaled. Hover tooltip shows short name/state; full rules in keyboard-accessible inspector. Click Unlock opens a summary; exact quote/revision revalidated by service, success only after durable acknowledgment. No local ownership mutation from animation. Error/stale/lost-ack uses shared gateway behavior.

## Geometry and performance

`TreeViewTransform` stores logical translation and scalar zoom. screen = pan + zoom × world; hit = inverse transform(screen). Paint on Graphics2D.create(), concatenate view transform onto existing HiDPI transform, dispose copy. Do not overwrite default device transform. Use double precision; reject noninvertible/invalid values. Mouse coordinates are Swing logical units, not raw device pixels. Zoom anchoring solves newPan = mouse - newZoom × inverseOld(mouse).

Deterministic layout: branch lanes, tier rows, peer positions around fixed cluster centers with stable sorted metadata; reserve label bounds; edges behind nodes, orthogonal tier trunks and short radial peer links. Gate annotations derive from the actual requirement expression. Cache immutable node bounds/edge shapes by catalog version, not ownership. State repaint changes glyphs/path colors only. Cull world bounds against inverse viewport; simple bounds scan first, spatial grid only if profiling justifies. No persistent 60fps tree timer; repaint on interaction/state change, optional bounded purchase pulse disabled under reduced motion.

Provisional performance budgets, to measure rather than claim: 500-node synthetic stress fixture, p95 interaction repaint <16ms on recorded test hardware at 100%/200%; <50ms hit/search response; layout off EDT; no growth in live listeners after 50 reopen cycles. Stress data is test-only, never a new production catalog. Record dimensions, JDK, scale, node count and baseline; tune budgets from evidence.

## Required verification

Transform inverse round-trips at extremes, cursor-anchored zoom, fit/resizes, small/large monitor crossing, hit bounds/drag threshold; graph visitor parity with evaluator; ALL/ANY/unique count semantics; no cycles/dangling IDs; stable layouts; ownership/price/evidence state cross-product; lower ranks retained; stale account/revision callbacks rejected; no purchase on hover/drag/search; exact repeated command after lost ack. Headless paint snapshots validate glyph/labels/clipping, manual client review validates readability/focus and real scale. Real purchase tests wait for authoritative atomic progression commands (WB-TREE-05); synthetic snapshots must stay visibly labeled preview.

## Open boundaries

Final tree palette/composition and branch density need user visual approval. Production node catalog, explicit unresolved mastery/quest/boss gates and reviewed prices remain backend/content work, not invented design decisions. Missing B3 bitmap has no bearing on renderer foundations. No new gameplay choice is requested by this layout study.
