# Better OC2R Create Controls

## Native source fixture safety

`verifyFull` runs the two authored native GameTests in a fresh `build/gametest/<UUID>/`
fixture. It no longer deletes `run/world` or shares the client working tree. Preparation
reads the actual ForgeGradle RunConfig, creates only absent comment-only server settings
(native defaults), and copies structures there. Existing settings and old worlds/logs
are untouched. Actual native startup/shutdown, exactly two completed/passed required
tests and zero ERROR/FATAL records are required. `verifyFast` checks settings preservation
and negative diagnostic/population controls without Minecraft. Retain complete prior
builds, libraries, fixtures and evidence before any authorized clean/reset.

## Playtest diagnostics

`bc.playtest.better_oc2r_create_controls.rpc` reports changed/unchanged native setter
results through the existing machine-operation boundary and typed contract rejections.
Operation IDs are an authored allowlist and before/after values are primitive scalars
only: raw RPC text, JSON, exception messages and subscriber identities are excluded.
`subscriptions` reports first/last subscriber edges, never getter/poll/frame output.
Native position is a causal machine identity, not a player identity.

INFO startup supplies enabled state, version, integration presence and exact owner-artifact
source metadata when available (UNKNOWN otherwise). Categories share the repository-local
Java17 helper's120 DEBUG/TRACE details/minute, burst20,256 keys and2048-character bound.
Native END ticks emit30-second summaries; server stop clears aggregation without renewing
TRACE or the bucket. Standalone remains native INFO; the pack selects scoped DEBUG via
its logger profile/`bc.playtest.level`. Common six-case and RPC projection/privacy tests
run with the source gate. Mutation observation is not downstream event-sink delivery or
save durability; existing native exceptions and domain events remain authoritative.

## Scope and authority

This repository owns its mod-specific behavior and authoring inputs. Read [local instructions](AGENTS.md)
and the [shared documentation/policy index](../../better-content-modpack/docs/README.md).


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
- Forge `47.4.13`
- KotlinForForge `4.11.0`
- Create `6.0.8`
- OC2R `2.2.12`

## Community and support

For modpack and mod discussion, playtest feedback, and bug reports, join the [Better Content Discord](https://discord.gg/EkRnZbzqS9).

## Identity

The clean-break canonical identity is repository/artifact `better-oc2r-create-controls`, mod ID `better_oc2r_create_controls`, and Maven group `com.bettercontent`. Legacy `computerbridge` identifiers are not migrated.
