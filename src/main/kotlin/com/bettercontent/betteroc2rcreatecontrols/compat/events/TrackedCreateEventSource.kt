package com.bettercontent.betteroc2rcreatecontrols.compat.events

interface TrackedCreateEventSource {
    fun hasSubscribers(): Boolean

    fun isValidSource(): Boolean

    fun bridgeTick()
}
