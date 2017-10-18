package me.camdenorrb.minibus.listener


enum class ListenerPriority {

	FIRST, NORMAL, LAST;


	companion object PriorityComparator : Comparator<ListenerPriority> {

		override fun compare(current: ListenerPriority, other: ListenerPriority): Int
			= if (current == other) 1 else current.compareTo(other)

	}

}
