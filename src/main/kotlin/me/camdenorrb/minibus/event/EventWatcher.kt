package me.camdenorrb.minibus.event

import me.camdenorrb.minibus.listener.ListenerPriority

/**
 * Created by camdenorrb on 3/5/17.
 */

@Target(AnnotationTarget.FUNCTION)
annotation class EventWatcher(val priority: ListenerPriority = ListenerPriority.NORMAL)