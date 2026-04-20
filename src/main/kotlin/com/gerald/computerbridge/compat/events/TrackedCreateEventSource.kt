package com.gerald.computerbridge.compat.events

interface TrackedCreateEventSource {
    fun hasSubscribers(): Boolean

    fun isValidSource(): Boolean

    fun bridgeTick()
}
