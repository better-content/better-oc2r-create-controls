# ComputerBridge

Forge `1.20.1` Kotlin mod that exposes Create's documented CC: Tweaked scripting surface through OpenComputers II: Reimagined devices.

## Current scope

- Build and package scaffolding for `computerbridge`
- Create + OC2R dependency wiring
- Phase 1 kinetic device scaffolding
- OC2R block-device provider registration
- Polling event bridge for `speed_change`, `stress_change`, and `overstressed`
- Source audit and API inventory docs

## Commands

```bash
./gradlew build
./gradlew runClient
./gradlew runGameTestServer
```

## Notes

- The project is pinned to Create `6.0.8` on Forge `1.20.1`.
- The OC2R dependency is pinned to Modrinth release `2.2.12`, which was the latest listed `1.20.1` Forge release on 2026-04-09.
- Phase 1 currently covers the kinetic/control surfaces described in `spec.md`.
