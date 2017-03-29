package me.camdenorrb.minibus.listener

import me.camdenorrb.minibus.event.MiniEvent
import kotlin.reflect.KFunction

/**
 * Created by camdenorrb on 3/5/17.
 */

class ListenerFunction(val listener: MiniListener, val priority: ListenerPriority, val function: KFunction<*>): Comparable<ListenerFunction> {

	operator fun <T : MiniEvent> invoke(event: T) {
		function.call(listener, event)
	}

	override fun compareTo(other: ListenerFunction): Int {
		return if (priority == other.priority) 1 else priority.compareTo(other.priority)
	}

}