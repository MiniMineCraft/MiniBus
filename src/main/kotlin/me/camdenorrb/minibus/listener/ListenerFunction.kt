package me.camdenorrb.minibus.listener

import kotlin.reflect.KFunction


class ListenerFunction(val listener: MiniListener, val priority: ListenerPriority, val function: KFunction<*>) : Comparable<ListenerFunction> {

	operator fun invoke(event: Any) = function.call(listener, event)


	override fun compareTo(other: ListenerFunction)
		= if (priority == other.priority) 1 else priority.compareTo(other.priority)

}