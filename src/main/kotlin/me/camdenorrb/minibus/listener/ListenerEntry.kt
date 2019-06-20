package me.camdenorrb.minibus.listener

data class ListenerEntry<T : Any>(val priority: ListenerPriority, val action: ListenerAction<T>) : Comparable<ListenerEntry<*>> {

	override fun compareTo(other: ListenerEntry<*>): Int {
		val priority2 = other.priority
		// A weird fix for removing listener entries, the hashcode comparison
		return if (priority == priority2) this.hashCode().compareTo(other.hashCode()) else priority.compareTo(priority2)
	}

}