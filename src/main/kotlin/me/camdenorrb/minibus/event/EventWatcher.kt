package me.camdenorrb.minibus.event

import me.camdenorrb.minibus.listener.ListenerPriority
import me.camdenorrb.minibus.listener.ListenerPriority.NORMAL
import kotlin.annotation.AnnotationTarget.FUNCTION

/**
 * Created by camdenorrb on 3/5/17.
 */

@Target(FUNCTION)
annotation class EventWatcher(val priority: ListenerPriority = NORMAL)