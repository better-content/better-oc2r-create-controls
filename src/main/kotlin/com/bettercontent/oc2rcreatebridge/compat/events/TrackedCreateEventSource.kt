package com.bettercontent.oc2rcreatebridge.compat.events

interface TrackedCreateEventSource {
    fun hasSubscribers(): Boolean

    fun isValidSource(): Boolean

    fun bridgeTick()
}
