package com.bettercontent.betteroc2rcreatecontrols.compat.errors

class CreatePeripheralException(message: String) : IllegalStateException(message) {
    init {
        val log = com.bettercontent.betteroc2rcreatecontrols.diagnostics.PlaytestDiagnostics.LOG
        if (log.enabled("rpc")) log.event("rpc", "rpc.rejected", "unknown", "rejected", "peripheral_contract", { "" })
        // The original caller-visible exception and message remain unchanged; no raw RPC text is logged.
    }
}
