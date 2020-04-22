package me.camdenorrb.minibus.event

import me.camdenorrb.minibus.listener.ListenerPriority
import me.camdenorrb.minibus.listener.ListenerPriority.NORMAL
import kotlin.annotation.AnnotationTarget.FUNCTION


@Target(FUNCTION)
annotation class EventWatcher(val priority: ListenerPriority = NORMAL)