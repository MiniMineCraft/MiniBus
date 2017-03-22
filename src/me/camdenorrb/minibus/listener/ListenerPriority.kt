package me.camdenorrb.minibus.listener

/**
 * Created by camdenorrb on 3/5/17.
 */

enum class ListenerPriority(val order: Int) {

	FIRST(0), NORMAL(1), LAST(2);


	fun byOrder(order: Int): ListenerPriority? {
		return when(order) {
			0 -> FIRST
			1 -> NORMAL
			2 -> LAST
			else -> null
		}
	}


	companion object {

		val DEFAULT_COMPARATOR = Comparator<ListenerPriority> { o1, o2 ->
			if (o1 == o2) 1 else o1.order.compareTo(o2.order)
		}

	}

}