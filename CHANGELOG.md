# Changelog

## 0.1.0 - 2026-04-29

### Added

- Initial release of `oc2r_create_bridge` for Forge `1.20.1`.
- OC2R block-device providers for Create kinetic/control blocks:
  - `creative_motor`
  - `rotation_speed_controller`
  - `sequenced_gearshift`
  - `speedometer`
  - `sticker`
  - `stressometer`
- Event bridge support for:
  - `speed_change`
  - `stress_change`
  - `overstressed`
- Unit test coverage for event tracker behavior.

### Packaging

- Cleaned release artifact to include only `oc2r_create_bridge` code and metadata.

## Unreleased

- Normalize the project identity to `oc2r-create-bridge / oc2r_create_bridge (formerly computerbridge)`; this is a clean break with no legacy aliases or migrations.
