# API Inventory

This inventory mirrors the public Create CC surface targeted by `computerbridge`.

## Phase 1: kinetics core

### Rotational Speed Controller

- `getTargetSpeed()`
- `setTargetSpeed(speed)`

### Creative Motor

- `getGeneratedSpeed()`
- `setGeneratedSpeed(speed)`

### Sequenced Gearshift

- `rotate(angle, [modifier])`
- `move(distance, [modifier])`
- `isRunning()`

### Sticker

- `isExtended()`
- `isAttachedToBlock()`
- `extend()`
- `retract()`
- `toggle()`

### Speedometer

- `getSpeed()`

Events:

- `speed_change`

### Stressometer

- `getStress()`
- `getStressCapacity()`

Events:

- `stress_change`
- `overstressed`

## Phase 2: displays and operator interfaces

### Display Link

- `setCursorPos(x, y)`
- `getCursorPos()`
- `getSize()`
- `isColor()`
- `isColour()`
- `write(text)`
- `writeBytes(bytes)`
- `clearLine()`
- `clear()`
- `update()`

### Nixie Tube

- `setText(text, [colour])`
- `setTextColour(colour)`
- `setTextColor(colour)`
- `setSignal(firstSignal, secondSignal)`

## Phase 3: train control

### Train Station

- `assemble()`
- `disassemble()`
- `setAssemblyMode(assemblyMode)`
- `isInAssemblyMode()`
- `getStationName()`
- `setStationName(name)`
- `isTrainPresent()`
- `isTrainImminent()`
- `isTrainEnroute()`
- `getTrainName()`
- `setTrainName(name)`
- `hasSchedule()`
- `getSchedule()`
- `setSchedule(schedule)`
- `canTrainReach(destination)`
- `distanceTo(destination)`

Events:

- `train_imminent`
- `train_arrival`
- `train_departure`

### Train Signal

- `getState()`
- `isForcedRed()`
- `setForcedRed(forced)`
- `getSignalType()`
- `cycleSignalType()`
- `listBlockingTrainNames()`

### Train Observer

- `isTrainPassing()`
- `getPassingTrainName()`

Events:

- `train_passing`
- `train_passed`

### Train Schedule codec

- Full schedule table encode/decode
- Instruction table IDs
- Condition group table IDs
- Validation
- Round-trip fidelity

## Phase 4: logistics and object surfaces

### Packager

- `getAddress()`
- `getItemDetail(slot)`
- `getPackage()`
- `list()`
- `makePackage()`

### Re-Packager

- Parity with the documented Create surface for the pinned Create version

### Stock Ticker

- `getItemDetail(slot)`
- `getStockItemDetail(index)`
- `list()`
- `stock([detailed])`
- `requestFiltered(address, [filter1], [filter2], ...)`

### Redstone Requester

- `getAddress()`
- `getConfiguration()`
- `getRequest()`
- `request()`
- `setAddress(address)`

### Package Frogport

- `getAddress()`
- `getConfiguration()`
- `getItemDetail(slot)`
- `list()`
- `setAddress(address)`

### Postbox

- `getAddress()`
- `getConfiguration()`
- `getItemDetail(slot)`
- `list()`
- `setAddress(address)`

### Table Cloth

- `getAddress()`
- `getPriceTagCount()`
- `getPriceTagItem()`

### Package Object

- `getAddress()`
- `getItemDetail(slot)`
- `getOrderData()`
- `isEditable()`
- `list()`

### Order Data Object

- `getCrafts()`
- `getIndex()`
- `getItemDetail()`
- `getLinkIndex()`
- `getOrderID()`

## Event bridge inventory

- `speed_change`
- `stress_change`
- `overstressed`
- `train_imminent`
- `train_arrival`
- `train_departure`
- `train_passing`
- `train_passed`
