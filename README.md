# Better OC2R Create Controls

## Native source fixture safety

`verifyFull` retains the two authored Create controls GameTests and adds two nonshipping
OC2R compatibility GameTests (four required total) in a fresh `build/gametest/<UUID>/`
fixture. It no longer deletes `run/world` or shares the client working tree. Preparation
reads the actual ForgeGradle RunConfig, creates only absent comment-only server settings
(native defaults), and copies structures there. Existing settings and old worlds/logs
are untouched. Actual native startup/shutdown and exactly four completed/passed required tests are required.
ERROR/FATAL remains blocking except two exact pinned OC2R diagnostics, and only after the
fresh fixture's independently pinned bytecode/data and native functional proof validate.
No logger or upstream archive is modified. `verifyFast` checks settings preservation
and negative diagnostic/population controls without Minecraft. Retain complete prior
builds, libraries, fixtures and evidence before any authorized clean/reset.

The native invocation requires `BC_CUSTOM_MOD_JAR_DIR` containing the canonically verified
`better-compat-fixes-0.1.9.jar` (the owning production echo repair) and its mandatory
`better-gameplay-notices-1.0.0.jar` provider. ForgeGradle remaps those exact provider inputs;
there is no older-artifact fallback. `verifyPinnedOc2r` reproducibly resolves the declared
OC2R 2.2.12 Maven input and verifies the published archive, native implementation sites,
all filesystem resources, and malformed/missing descriptor negative controls. The Python
reader negatives are oracle controls, not deliberate failing native reloads.

The network fixture holds and forwards the original async fallback task exactly once,
reuses the caller's consumed heap/direct/sliced/read-only storage, and requires exact
nonempty echo payload and one native callback through real JVM localhost reachability.
It also exercises real UDP/TCP loopback through the native session layer with the native
library flag false, restoring state afterward. The filesystem fixture checks both actual
native descriptors, the complete 8 MiB registered rootfs, and every one of the 17 loaded
script-layer files. `README.md` is documentation, never an admitted JSON descriptor.
A CREATE_NEW `oc2r-compat-proof.json` binds the fresh directory, pinned inputs, provider
identity and capability results; its digest must occur exactly once in the retained native
log. Missing proof, partial tests, another logger/path/message, duplicate diagnostics,
corrupt native libraries, native data mismatch and failed fallback operations still fail.
Only after all own native assertions/audits succeed, a CREATE_NEW
`build/compatibility/oc2r-compatibility.json` support bundle embeds the proof, original
source-fixture receipt, exact parsed four-test/lifecycle observations, and original proof/log
paths with post-JVM hashes. An existing export is retained under a unique prior name, not
overwritten. No execution/cleanup verdict is fabricated: the canonical owner must independently
validate process exit and complete descendant cleanup before admitting the support artifact.
These source changes have not yet been compiled or run natively; source contracts alone
are not native or full-pack acceptance.

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

## Scope

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
