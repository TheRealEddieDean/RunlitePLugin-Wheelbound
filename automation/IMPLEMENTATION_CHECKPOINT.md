# Current implementation checkpoint

Authority: WB-D158 permits implementation; WB-D159 permits verified PR merges into wheelbound-mode. Protected main remains c8cc38ac9a7ab74d9bc10125388da7cc75af99fa.

Four-PR batch starts from verified 0407530abef2ebd79eef5a78207c1c880225d2bf (PR1 merged).

1. I01b.1: foundation snapshot codec. Versioned lifecycle/account/run/catalog envelope, SHA-256 corruption check, explicit constructor validation, foreign/future/unknown payload refusal; injected Gson. Six new JUnit methods include 55 lifecycle/obligation round trips. No full-mode payload or auto-migration is claimed.
2. I01b.2 next: account-separated atomic file snapshots and conflict/corruption/failure tests.
3. I01b.3 planned: durable pause/resume command receipts and collision/replay tests.
4. I01c planned: durable weighted draw commitments using frozen explicit pools; no gameplay quotas/prices or live admission rules invented.

Local wrapper Java networking remains unavailable; full Gradle/JUnit checks run in GitHub CI before each merge. Existing obsolete documentation validator catalog-count defect remains separately recorded. Check each PR's exact head and CI result before marking merged. No live client/runtime activation or Hub submission in this batch. Checkpoints on a PR describe prepared scope until its CI/merge is verified.
