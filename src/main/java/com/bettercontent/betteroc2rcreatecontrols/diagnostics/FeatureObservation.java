package com.bettercontent.betteroc2rcreatecontrols.diagnostics;

/** Pure projection of values already observed by the owning native decision. */
public final class FeatureObservation {
    private FeatureObservation() {}
    public static String fields(String operation, Object before, Object after) { String authored = switch(operation) { case "setGeneratedSpeed", "setTargetSpeed", "rotate", "move", "extend", "retract", "toggle" -> operation; default -> "unknown_operation"; }; return PlaytestLog.fields("operation", authored, "before", scalar(before), "after", scalar(after)); }
    private static Object scalar(Object value) { return value instanceof Boolean || value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double ? value : "UNKNOWN"; }
}
