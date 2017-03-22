package me.camdenorrb.minibus.listener

import kotlin.reflect.KFunction

/**
 * Created by camdenorrb on 3/5/17.
 */

class ListenerFunction(val listener: MiniListener, val priority: ListenerPriority, val function: KFunction<*>): Comparable<ListenerFunction> {

	override fun compareTo(other: ListenerFunction): Int {
		return if (priority == other.priority) 1 else priority.order.compareTo(other.priority.order)
	}

}