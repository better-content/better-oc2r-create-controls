# OC2R Create CC Compatibility Layer — Developer Spec

## Purpose

Build a **Forge 1.20.1** mod in **Kotlin** that exposes the **documented Create CC: Tweaked integration API** through **OpenComputers II: Reimagined (OC2R)** devices.

This is a **compatibility layer**, not a fresh API.

### Product goal

If Create documents a CC peripheral method, object type, alias, event, or table schema, this mod should expose the same scripting contract through OC2R wherever technically practical.

### Non-goals

* Replacing or modifying Create's built-in CC integration.
* Reproducing ComputerCraft's peripheral/modem transport model.
* Designing a new OC2-native Create API.
* Supporting Fabric.
* Supporting Minecraft versions other than Forge 1.20.1.

---

## Target environment

* **Minecraft:** 1.20.1
* **Loader:** Forge
* **Language:** Kotlin
* **Primary dependency targets:**

  * Create 6.x on Forge 1.20.1
  * OpenComputers II: Reimagined on Forge 1.20.1
* **Testing:** Forge GameTests + focused scripted compatibility tests

---

## Source of truth

Use the **documented Create CC: Tweaked integration surface** as the primary compatibility contract.

Primary domains to mirror:

* Kinetics / control blocks
* Displays / operator interfaces
* Train peripherals
* Logistics peripherals
* Object surfaces / table codecs
* Event surfaces

Where runtime behavior differs from docs, preserve **documented behavior first** unless impossible. In those cases:

1. document the discrepancy,
2. keep behavior deterministic,
3. add regression tests,
4. prefer consistency with Create user expectations.

---

## Compatibility principles

### 1. Public API parity

For every covered peripheral:

* Match **method names** exactly.
* Match **argument order** exactly.
* Match **return shapes** exactly where practical.
* Match **alias names** exactly.
* Match **documented event names** exactly.
* Match **table/object schemas** exactly where practical.

### 2. OC2R transport, Create CC semantics

The device discovery model may remain OC2R-native.
The scripting surface must remain Create-CC-compatible.

### 3. Generic wrappers are internal only

Do not expose generic OC2R/Create behavior names as the primary public API.

Bad public API:

* `setScrollValue(64)`

Good public API:

* `setTargetSpeed(64)`

### 4. Deterministic failure behavior

Error cases must produce deterministic OC2R-visible Lua errors or equivalent failure results.
Where Create docs describe invalid states, mirror the spirit of those constraints.

### 5. Build toward full documented parity

This project is not "high-value subset only." It is a full compatibility effort with phased delivery.

---

## Covered API surface

# A. Kinetics and control

## Rotational Speed Controller

Required methods:

* `getTargetSpeed()`
* `setTargetSpeed(speed)`

Behavior notes:

* Clamp speed to documented range if Create does.
* Preserve sign semantics.
* Ensure write-through updates actual block state.

## Creative Motor

Required methods:

* `getGeneratedSpeed()`
* `setGeneratedSpeed(speed)`

Behavior notes:

* Clamp to documented range.
* Preserve exact sign behavior.

## Sequenced Gearshift

Required methods:

* `rotate(angle, [modifier])`
* `move(distance, [modifier])`
* `isRunning()`

Behavior notes:

* Support default modifier behavior.
* Preserve instruction completion semantics.

## Sticker

Required methods:

* `isExtended()`
* `isAttachedToBlock()`
* `extend()`
* `retract()`
* `toggle()`

## Speedometer

Required methods:

* `getSpeed()`

Required events:

* `speed_change`

## Stressometer

Required methods:

* `getStress()`
* `getStressCapacity()`

Required events:

* `stress_change`
* `overstressed`

# B. Displays and operator interfaces

## Display Link

Required methods:

* `setCursorPos(x, y)`
* `getCursorPos()`
* `getSize()`
* `isColor()`
* `isColour()`
* `write(text)`
* `writeBytes(bytes)`
* `clearLine()`
* `clear()`
* `update()`

Behavior notes:

* Support both color/colour aliases.
* Preserve buffering/update semantics.

## Nixie Tube

Required methods:

* `setText(text, [colour])`
* `setTextColour(colour)`
* `setTextColor(colour)`
* `setSignal(firstSignal, secondSignal)`

Behavior notes:

* Preserve spelling aliases.
* Preserve signal encoding behavior.

# C. Train control

## Train Station

Required methods:

* `assemble()`
* `disassemble()`
* `setAssemblyMode(assemblyMode)`
* `isInAssemblyMode()`
* `getStationName()`
* `setStationName(name)`
* `isTrainPresent()`
* `isTrainImminent()`
* `isTrainEnroute()`
* `getTrainName()`
* `setTrainName(name)`
* `hasSchedule()`
* `getSchedule()`
* `setSchedule(schedule)`
* `canTrainReach(destination)`
* `distanceTo(destination)`

Required events:

* `train_imminent`
* `train_arrival`
* `train_departure`

Behavior notes:

* Schedule round-trip fidelity matters.
* Route checks must match documented expectations.
* No silent fallback on invalid schedule data.

## Train Signal

Required methods:

* `getState()`
* `isForcedRed()`
* `setForcedRed(forced)`
* `getSignalType()`
* `cycleSignalType()`
* `listBlockingTrainNames()`

## Train Observer

Required methods:

* `isTrainPassing()`
* `getPassingTrainName()`

Required events:

* `train_passing`
* `train_passed`

## Train Schedule codec

Must support full documented schedule table shape, including:

* instruction list entries
* condition groups
* documented instruction IDs
* documented condition IDs
* validation
* round-trip serialization/deserialization

This is an API surface, not a helper.

# D. Logistics blocks

## Packager

Required methods:

* `getAddress()`
* `getItemDetail(slot)`
* `getPackage()`
* `list()`
* `makePackage()`

## Re-Packager

Implement parity with documented surface for the supported Create version.
Treat as first-class covered logistics block, not optional.

## Stock Ticker

Required methods:

* `getItemDetail(slot)`
* `getStockItemDetail(index)`
* `list()`
* `stock([detailed])`
* `requestFiltered(address, [filter1], [filter2], ...)`

Behavior notes:

* Preserve sparse/non-sparse list behaviors.
* Preserve filter DSL semantics.
* Preserve detailed vs non-detailed shapes.

## Redstone Requester

Required methods:

* `getAddress()`
* `getConfiguration()`
* `getRequest()`
* `request()`
* `setAddress(address)`

## Package Frogport

Required methods:

* `getAddress()`
* `getConfiguration()`
* `getItemDetail(slot)`
* `list()`
* `setAddress(address)`

## Postbox

Required methods:

* `getAddress()`
* `getConfiguration()`
* `getItemDetail(slot)`
* `list()`
* `setAddress(address)`

## Table Cloth

Required methods:

* `getAddress()`
* `getPriceTagCount()`
* `getPriceTagItem()`

Behavior notes:

* Respect documented availability conditions.

# E. Object surfaces

## Package Object

Required methods:

* `getAddress()`
* `getItemDetail(slot)`
* `getOrderData()`
* `isEditable()`
* `list()`

## Order Data Object

Required methods:

* `getCrafts()`
* `getIndex()`
* `getItemDetail()`
* `getLinkIndex()`
* `getOrderID()`

---

## Event bridge requirements

Implement a dedicated event bridge between Create state changes and OC2R-visible events.

### Required event names

* `speed_change`
* `stress_change`
* `overstressed`
* `train_imminent`
* `train_arrival`
* `train_departure`
* `train_passing`
* `train_passed`

### Event design rules

* Event names must match Create docs.
* Event payloads should match documented meaning as closely as possible.
* Prefer direct state hooks when feasible.
* Use polling only where necessary.
* Never emit duplicate events uncontrollably.
* Add debouncing/state tracking where required.

---

## Architecture

# 1. Public façade layer

Create one OC2R device façade per Create CC peripheral.

Examples:

* `RotationSpeedControllerDevice`
* `CreativeMotorDevice`
* `SequencedGearshiftDevice`
* `StickerDevice`
* `SpeedometerDevice`
* `StressometerDevice`
* `DisplayLinkDevice`
* `NixieTubeDevice`
* `TrainStationDevice`
* `TrainSignalDevice`
* `TrainObserverDevice`
* `PackagerDevice`
* `StockTickerDevice`
* `RedstoneRequesterDevice`
* `TableClothDevice`
* `PackageObjectDevice`
* `OrderDataObjectDevice`

These façade classes own the public callback names.

# 2. Adapter layer

Use dedicated internal adapters to isolate Create internals from public façade classes.

Examples:

* `RotationSpeedControllerAdapter`
* `CreativeMotorAdapter`
* `TrainStationAdapter`
* `StockTickerAdapter`
* `DisplayLinkAdapter`

Adapter responsibilities:

* read/write Create state,
* perform validation,
* expose clean Kotlin-facing operations,
* hide reflection/mixin/accessor ugliness.

# 3. Codec layer

Create explicit codecs for all structured data.

Required codec domains:

* Train schedule encode/decode
* Package object encode/decode / wrappers
* Order data wrappers
* Inventory detail tables
* Sparse list encoding
* Stock ticker result encoding
* Filter DSL parsing/validation
* Enum/string conversion
* Error translation

Codec design rules:

* No ad-hoc tables sprinkled across device classes.
* All major table schemas must be centralized.
* Round-trip tests required for mutable schemas.

# 4. Event subsystem

Dedicated subsystem to observe state changes and dispatch OC2R-visible events.

Responsibilities:

* state caching
* edge detection
* deduplication
* payload normalization
* test hooks for GameTests

# 5. Registration layer

Provide stable device providers for each supported block/object surface.
Avoid implicit accidental exposure without tests.

---

## Package structure suggestion

```text
src/main/kotlin/com/yourname/oc2rcreatecompat/
  api/
    devices/
    objects/
    events/
  compat/
    adapters/
    providers/
    codecs/
    validation/
    errors/
  integration/
    create/
    oc2r/
  gametest/
  util/
```

More concrete split:

```text
api/devices/
  RotationSpeedControllerDevice.kt
  CreativeMotorDevice.kt
  SequencedGearshiftDevice.kt
  StickerDevice.kt
  SpeedometerDevice.kt
  StressometerDevice.kt
  DisplayLinkDevice.kt
  NixieTubeDevice.kt
  TrainStationDevice.kt
  TrainSignalDevice.kt
  TrainObserverDevice.kt
  PackagerDevice.kt
  RePackagerDevice.kt
  StockTickerDevice.kt
  RedstoneRequesterDevice.kt
  FrogportDevice.kt
  PostboxDevice.kt
  TableClothDevice.kt

api/objects/
  PackageObjectDevice.kt
  OrderDataObjectDevice.kt

compat/adapters/
  kinetics/
  display/
  trains/
  logistics/

compat/codecs/
  TrainScheduleCodec.kt
  PackageObjectCodec.kt
  OrderDataCodec.kt
  InventoryTableCodec.kt
  StockTickerCodec.kt
  FilterDslCodec.kt

compat/events/
  CreateEventBridge.kt
  SpeedometerEventTracker.kt
  StressometerEventTracker.kt
  TrainStationEventTracker.kt
  TrainObserverEventTracker.kt
```

---

## Implementation rules

### Rule 1: No public callback drift

Do not rename methods into OC2R-flavored names if Create already documents a public name.

### Rule 2: No generic behavior leakage

Generic Create behavior wrappers can be used internally, but public docs/tests must target the Create CC names.

### Rule 3: No silent no-ops

If a documented operation cannot be applied because state is invalid, error clearly.

### Rule 4: Prefer direct Create internals over brittle simulation

If the underlying Create block already stores or computes the canonical state, use that.
Do not infer behavior from world side effects if direct state access is available.

### Rule 5: Keep version targeting explicit

This is for **Forge 1.20.1 + Create 6.x**.
Do not over-generalize for future versions early.

### Rule 6: Test every public method

No public method ships without at least one success-path test and one invalid/edge test.

---

## Phased delivery

# Phase 1 — kinetics core

Ship first:

* Rotational Speed Controller
* Creative Motor
* Sequenced Gearshift
* Sticker
* Speedometer
* Stressometer

Reason:

* highest gameplay value,
* bounded scope,
* strong automation payoff,
* easiest parity wins.

# Phase 2 — operator interfaces

Ship next:

* Display Link
* Nixie Tube

Reason:

* enables dashboards/HMIs,
* still bounded,
* good demonstration value.

# Phase 3 — train control

Ship next:

* Train Station
* Train Signal
* Train Observer
* Train Schedule codec

Reason:

* major Create gameplay domain,
* strong compatibility value,
* more stateful and schema-heavy.

# Phase 4 — logistics

Ship next:

* Packager
* Re-Packager
* Stock Ticker
* Redstone Requester
* Frogport
* Postbox
* Table Cloth
* Package Object
* Order Data Object

Reason:

* most complex schema work,
* richest object surfaces,
* highest parity burden.

---

## Testing requirements

# Test framework

* Forge GameTests
* Supplementary scripted Lua-side parity checks where helpful

# Tier 1 — method parity tests

For every method:

* valid invocation
* invalid invocation
* boundary values
* alias parity where applicable

Examples:

* `setTargetSpeed(256)` succeeds
* `setTargetSpeed(999)` clamps or errors per intended compatibility policy
* `isColour()` matches `isColor()`

# Tier 2 — state mutation tests

Examples:

* speed controller changes actual target speed
* creative motor speed updates live output state
* sticker extends/retracts correctly
* sequenced gearshift runs exactly one generated sequence

# Tier 3 — event tests

Examples:

* speed change emits `speed_change`
* stress change emits `stress_change`
* overstress emits `overstressed`
* observer emits `train_passing` and `train_passed`
* station emits imminent/arrival/departure lifecycle

# Tier 4 — codec round-trip tests

Required for:

* train schedules
* package object projections
* order data projections
* stock ticker detailed results
* inventory table/list shapes

# Tier 5 — integration scene tests

Build scripted Create worlds for:

* kinetic control rig
* dashboard/display rig
* station schedule control rig
* stock ticker request rig
* logistics object extraction rig

---

## Developer agent tasks

### Task 1: Source audit

Inspect Create source and OC2R APIs to map each documented peripheral to:

* block/entity class
* relevant internal state holders
* event hook points
* schema/data classes
* edge/error cases

Deliverable:

* `docs/source_audit.md`

### Task 2: façade/API inventory

Create a definitive API inventory file listing every public callback/event/object in this spec.

Deliverable:

* `docs/api_inventory.md`

### Task 3: scaffolding

Set up project structure, Gradle, Kotlin, registration plumbing, and test harness.

Deliverable:

* compilable mod skeleton

### Task 4: Phase 1 implementation

Implement kinetics core with tests.

Deliverable:

* working Phase 1 + GameTests

### Task 5: Phase 2 implementation

Implement Display Link and Nixie Tube with tests.

### Task 6: Phase 3 implementation

Implement train surfaces and schedule codec with tests.

### Task 7: Phase 4 implementation

Implement logistics and object surfaces with tests.

### Task 8: documentation

Generate end-user docs that show OC2R usage with Create-CC-compatible method names.

---

## Acceptance criteria

The mod is acceptable when:

* every documented covered surface exists,
* public method names match Create docs,
* alias methods work,
* required events fire reliably,
* object/table schemas are stable and tested,
* no critical method is only exposed through a generic internal name,
* Forge 1.20.1 Create automation works reliably in scripted tests.

---

## Risks

### High-risk areas

* Train schedule schema parity
* Stock Ticker filter DSL parity
* Package / Order Data object fidelity
* Event lifecycle edge cases for trains

### Medium-risk areas

* Display buffering semantics
* Nixie signal encoding parity
* Error message compatibility

### Lower-risk areas

* Rotational Speed Controller
* Creative Motor
* Speedometer
* Stressometer
* Sequenced Gearshift
* Sticker

---

## Design stance

This project should be opinionated in one way only:

> **Use Create's CC API as the contract.**

Everything else should serve that.

No API bikeshedding.
No unnecessary abstraction theater.
No user-facing drift.

Build the compatibility layer, test it hard, and keep the public surface boringly faithful.

