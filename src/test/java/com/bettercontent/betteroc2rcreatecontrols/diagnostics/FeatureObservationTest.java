package com.bettercontent.betteroc2rcreatecontrols.diagnostics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public final class FeatureObservationTest {
    @Test public void nativeScalarsHaveExplicitStage() { var fields = FeatureObservation.fields("setTargetSpeed", 16f, 32f); assertTrue(fields.contains("operation=setTargetSpeed")); assertTrue(fields.contains("before=16.0")); assertTrue(fields.contains("after=32.0")); }
    @Test public void uncertaintyAndPrivacyStayExplicit() { var fields = FeatureObservation.fields("private_method_payload", "secret_before", "secret_after"); assertFalse(fields.contains("secret")); assertFalse(fields.contains("private")); assertTrue(fields.contains("unknown_operation")); assertTrue(fields.contains("before=UNKNOWN")); }
}
