# Wheelbound

**Let the wheel decide.**

Wheelbound is a RuneLite plugin built around a simple idea: when you don't know what to do in Old School RuneScape, spin the wheel and let it choose.

## Current prototype

The first scaffold includes:

- RuneLite plugin lifecycle and metadata
- A RuneLite side panel
- A **SPIN** button
- A starter activity pool:
  - Bossing
  - Slayer
  - Skilling
  - Clue Scrolls
  - Questing
  - Money Making
- Optional spin-on-login behavior
- Optional in-game chat announcement

This is intentionally a small first milestone. The activity system will be expanded before we build the full wheel experience.

## Development

The project follows the official RuneLite example-plugin structure and uses Gradle.

Run RuneLite locally with:

```text
./gradlew run
```

On Windows:

```text
gradlew.bat run
```

## Roadmap

- [ ] Real visual spinning wheel
- [ ] Configurable activity categories
- [ ] Enable/disable individual activities
- [ ] Weighted entries
- [ ] Activity history
- [ ] Reroll / spin-again rules
- [ ] Boss-specific and skill-specific entries
- [ ] Challenge modes
- [ ] Persistent state
- [ ] Polished RuneLite UI

## Privacy

Wheelbound is designed to make its decisions locally. The initial implementation has no network calls or external web service dependency.
