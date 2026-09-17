# Changelog

## 1.0.0 - Initial release candidate

- Bossing, Skilling, Combat Achievements, Pet Hunting and Questing wheels.
- Filters and individual entry checklists, with saved preferences.
- Optional XP-target wheel after a skill spin.
- Optional specific unfinished Combat Achievement result, respecting included tiers.
- Java 11 CI and the official example Gradle wrapper.
- Release fixes for custom-label rendering, dialog cleanup, tooltip escaping,
  missing artwork, and batched account-state refreshes.
- Named custom wheels with entry creation, toggles and deletion.
- Centered animated wheels, result cards and Wheelbound artwork.
- Account-aware pool updates and cancellation on account/session changes.
- Escaped custom-entry text, including tooltips.

The structured Wheelbound challenge mode is planned separately and is not part
of this release. Plugin Hub acceptance is pending submission and review.

Versioning: use 1.0.x for fixes, 1.x.0 for compatible features, and a new major
version for breaking changes. Keep build.gradle and runelite-plugin.properties
versions aligned; tag the reviewed release commit v1.0.0 when publishing.
