package me.camdenorrb.minibus.listener

data class ListenerEntry<T : Any>(val priority: ListenerPriority, val action: ListenerAction<T>) : Comparable<ListenerEntry<*>> {

	override fun compareTo(other: ListenerEntry<*>): Int {
		val priority2 = other.priority
		return if (priority == priority2) 1 else priority.compareTo(priority2)
	}

}