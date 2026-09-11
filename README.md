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
- No runtime network calls or external service dependency

This is intentionally a small first milestone. The activity system will be expanded before we build the full wheel experience.

## Development

The project follows the current official RuneLite example-plugin structure. The build targets Java 11, matching the example plugin's current compiler configuration. citeturn3file0turn4file0

Install Java 11 and Gradle, then run:

```text
gradle run
```

The repository also includes a GitHub Actions build that compiles the project on every push and pull request.

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

Wheelbound makes its activity decisions locally. The plugin itself does not contact a third-party server or transmit your IP address. Network access is only needed by the development/build tooling to retrieve RuneLite and Gradle dependencies.
