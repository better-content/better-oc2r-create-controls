package com.bettercontent.betteroc2rcreatecontrols.nativefixture;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;

/** Nonshipping, process-local copy of IDs from the one successful native preparse enumeration. */
public final class FilesystemDescriptorInventory {
    private static volatile Set<String> ids;
    private static volatile int captures;
    private FilesystemDescriptorInventory() {}
    public static void capture(Map<?, ?> originalResult) {
        ids = null;
        captures++;
        var copy = new HashSet<String>();
        for (Object id : originalResult.keySet()) copy.add(id.toString());
        ids = Set.copyOf(copy);
    }
    public static Set<String> requireSingleCapture() {
        var result = ids;
        if (captures != 1 || result == null)
            throw new AssertionError("Missing/failed/repeated native filesystem inventory capture");
        return result;
    }
}
