# ComputerBridge

Forge `1.20.1` Kotlin mod that exposes Create's documented CC: Tweaked scripting surface through OpenComputers II: Reimagined devices.

## Release scope (`0.1.0`)

- OC2R provider registration for supported Create block entities
- Peripheral adapters for Create kinetic/control blocks
- Polling event bridge for `speed_change`, `stress_change`, and `overstressed`
- Runtime-safe error reporting for unsupported block entities

## Verification

```bash
./gradlew verifyFast
./gradlew verifyFull
./gradlew stageRuntimeJar
```

## Dev

```bash
./gradlew runClient
```

## Compatibility

- Minecraft `1.20.1`
- Forge `47.4.10`
- KotlinForForge `4.11.0`
- Create `6.0.8`
- OC2R `2.2.12`

## Community and support

For modpack and mod discussion, playtest feedback, and bug reports, join the [Better Content Discord](https://discord.gg/EkRnZbzqS9).
