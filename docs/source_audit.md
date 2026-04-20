# Source Audit

Audit date: 2026-04-09.

Reference set:

- Create Forge `1.20.1` release `6.0.8`, Maven artifact `6.0.8-289`
- Create upstream source branch `mc1.20.1/dev`
- OC2R upstream branch `1.20.1`
- OC2R Modrinth release `2.2.12`, published 2026-01-24

## OC2R integration seam

The compatibility layer is best implemented as OC2R `ObjectDevice` wrappers backed by annotated Kotlin façade classes.

Relevant OC2R APIs:

- `li.cil.oc2.api.bus.device.object.Callback`
- `li.cil.oc2.api.bus.device.object.ObjectDevice`
- `li.cil.oc2.api.bus.device.provider.BlockDeviceProvider`
- `li.cil.oc2.api.util.Registries`
- `li.cil.oc2.api.bus.device.rpc.RPCEventSource`
- `li.cil.oc2.api.bus.device.rpc.IEventSink`

Event payload shape in OC2R is a JSON object with at least an `event` property. The upstream `RedstoneInterfaceBlockEntity` posts:

- `{"event":"redstone","side":"...","level":...}`

That pattern is the baseline for Create event forwarding.

## Create CC surface map

### Phase 1: kinetics core

| Surface | Create CC class | Create runtime class | Notes |
| --- | --- | --- | --- |
| Rotational Speed Controller | `SpeedControllerPeripheral` | `SpeedControllerBlockEntity` | Uses `targetSpeed: ScrollValueBehaviour`; Create CC directly calls `setValue()` / `getValue()`. |
| Creative Motor | `CreativeMotorPeripheral` | `CreativeMotorBlockEntity` | Uses `generatedSpeed: ScrollValueBehaviour`; block entity exposes `getGeneratedSpeed()`. |
| Sequenced Gearshift | `SequencedGearshiftPeripheral` | `SequencedGearshiftBlockEntity` | Creates `Instruction` entries, appends `END`, then calls `run(0)`. |
| Sticker | `StickerPeripheral` | `StickerBlockEntity` | Writes the `StickerBlock.EXTENDED` blockstate directly. |
| Speedometer | `SpeedGaugePeripheral` | `SpeedGaugeBlockEntity` | Getter is `getSpeed()`. CC event source is `KineticsChangeEvent`, with `speed_change` payload using `0` when overstressed. |
| Stressometer | `StressGaugePeripheral` | `StressGaugeBlockEntity` | Uses `getNetworkStress()` and `getNetworkCapacity()`. CC events are `stress_change` and `overstressed`. |

Relevant Create event carrier:

- `com.simibubi.create.compat.computercraft.events.KineticsChangeEvent`

Relevant state available for polling:

- `KineticBlockEntity.isOverStressed()`
- `KineticBlockEntity.getSpeed()`
- `StressGaugeBlockEntity.getNetworkStress()`
- `StressGaugeBlockEntity.getNetworkCapacity()`

### Phase 2: displays / operator interfaces

| Surface | Create CC class | Create runtime class | Notes |
| --- | --- | --- | --- |
| Display Link | `DisplayLinkPeripheral` | `DisplayLinkBlockEntity` | Requires write buffering parity and `color/colour` aliases. |
| Nixie Tube | `NixieTubePeripheral` | `NixieTubeBlockEntity` | Requires text aliases plus signal encoding parity. |

### Phase 3: train control

| Surface | Create CC class | Create runtime class | Notes |
| --- | --- | --- | --- |
| Train Station | `StationPeripheral` | `StationBlockEntity`, `GlobalStation`, `Train`, `Schedule` | Full schedule round-trip and destination path behavior live here. |
| Train Signal | `SignalPeripheral` | `SignalBlockEntity` | Signal type/state parity required. |
| Train Observer | `TrackObserverPeripheral` | `TrackObserverBlockEntity` | Event lifecycle is `train_passing` / `train_passed`. |
| Train schedule codec | `StationPeripheral` helpers | `Schedule`, schedule instruction and condition classes | Create CC converts NBT schedule tags to snake_case Lua tables and back. |

Relevant Create event carriers:

- `StationTrainPresenceEvent`
- `TrainPassEvent`
- `SignalStateChangeEvent`

### Phase 4: logistics and object surfaces

| Surface | Create CC class | Create runtime class | Notes |
| --- | --- | --- | --- |
| Packager | `PackagerPeripheral` | `PackagerBlockEntity` | Inventory-like sparse list behavior and package object projection. |
| Re-Packager | `RepackagerPeripheral` | `RepackagerBlockEntity` | First-class parity surface. |
| Stock Ticker | `StockTickerPeripheral` | `StockTickerBlockEntity`, `PackageOrder`, `PackageOrderWithCrafts` | Highest-risk filter DSL and detailed stock table surface. |
| Redstone Requester | `RedstoneRequesterPeripheral` | `RedstoneRequesterBlockEntity` | Address/config/request contract. |
| Package Frogport | `FrogportPeripheral` | `FrogportBlockEntity` | Address/config/inventory surface. |
| Postbox | `PostboxPeripheral` | `PostboxBlockEntity` | Address/config/inventory surface. |
| Table Cloth | `TableClothShopPeripheral` | `TableClothBlockEntity` | Price-tag availability semantics matter. |
| Package object | `PackageLuaObject` | `PackageItem` | Object projection for package stack data. |
| Order data object | `PackageOrderLuaObject` | `PackageOrder`, `PackageOrderWithCrafts` | Exposed nested order information surface. |

Relevant Create event carriers:

- `PackageEvent`
- `RepackageEvent`

## Implementation takeaways

- Phase 1 does not require binding to Create's CC peripheral classes directly. The logic is small, stable, and readable enough to re-express against the block entities.
- OC2R block-device providers can be registered from this mod using `DeferredRegister.create(Registries.BLOCK_DEVICE_PROVIDER, "computerbridge")`.
- OC2R push events should be emitted as JSON payloads through `RPCEventSource`, with one JSON object per Create event.
- Create's train schedule table conversion in `StationPeripheral` is the source of truth for later codec work, including snake_case key normalization and explicit schedule validation.
