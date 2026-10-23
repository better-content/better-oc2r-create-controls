# Better OC2R Create Controls

Forge 1.20.1 / Java 17; mod ID `better_oc2r_create_controls`.
Source changes: `./gradlew verifyFull --no-daemon`; stage with `./gradlew stageRuntimeJar`.
Native full verification requires `BC_CUSTOM_MOD_JAR_DIR` binding canonical verified Compat
0.1.9 and Gameplay Notices 1.0.0 provider JARs. It retains both original tests and adds two
nonshipping OC2R compatibility tests (four required total). No sibling/old-artifact fallback.
Artifact: `build/libs/better-oc2r-create-controls-<version>.jar`.

Read [shared workspace policy](../../better-content-modpack/docs/policies/workspace.md)
and its linked testing/disposal policies. Docs-only changes use the shared document check
and `git diff --check`, not unrelated runtime builds.
