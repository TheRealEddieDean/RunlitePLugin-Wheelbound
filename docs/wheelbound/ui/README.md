# Wheelbound UI planning packet

> **APPROVED initial simple UI.** [Implementation handoff, delivery milestones and approval record](UI_IMPLEMENTATION_HANDOFF.md). Plain-control implementations may proceed after checks; optional visual polish is deferred.

> [36-screen simple UI coverage and delivery order](SIMPLE_UI_SCREEN_COVERAGE.md) · [Navigable plain-control preview](previews/simple-screens.html). These expand existing task acceptance; they are not implemented plugin screens.

> **Current direction: [simple UI first](SIMPLE_UI_FIRST.md).** Use normal buttons and functional lists for initial testing; decorative styling and complex tree presentation are deferred. Earlier mockups are exploratory.

Prepared 2026-10-29 America/Chicago. Audit base: `223950ca4088fb69cb3ca918376a52140eca630d` (merged PR #57). The user approved the initial plain-control direction. This packet documents its implementation backlog; it does not claim working mode screens or approve future elaborate visuals.

- [Audit and integration evidence](UI_AUDIT.md)
- [Architecture](UI_ARCHITECTURE.md)
- [Design system and component contracts](UI_DESIGN_SYSTEM.md)
- [Fate Tree](FATE_TREE_DESIGN.md)
- [Screen inventory and flows](UI_SCREEN_INVENTORY.md)
- [Implementation backlog](UI_IMPLEMENTATION_BACKLOG.md)
- [Review checkpoints](UI_REVIEW_CHECKPOINTS.md)
- [Preview gallery](previews/README.md)

Canonical decisions in [DECISIONS](../DECISIONS.md) prevail over historical summaries. In particular: automatic bounties (D112), hilt 5 (D117), indestructible/storable Questing (D121), ranked Challenge (D144–154), GF4 (D156), distinct Taint slice (D157), implementation authority (D158) and existing autonomous merge authority (D159). The user explicitly approved the simple UI and merging PR #58. Future elaborate Category B designs remain separate. It does not stop other authorized implementation.

Independent planning ownership: `design/wheelbound-ui-planning`, scoped to `docs/wheelbound/ui/**` and append-only discovery pointers in the two automation backlog documents. No claim on the supervisor's gameplay tasks, global lease, source code or automation scripts. The user explicitly authorizes this concurrent disjoint work. The supervisor remains running.
