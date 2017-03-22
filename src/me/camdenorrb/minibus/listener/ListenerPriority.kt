package me.camdenorrb.minibus.listener

/**
 * Created by camdenorrb on 3/5/17.
 */

enum class ListenerPriority {

	FIRST, NORMAL, LAST;


	companion object {

		val DEFAULT_COMPARATOR = Comparator<ListenerPriority> { o1, o2 ->
			if (o1 == o2) 1 else o1.ordinal.compareTo(o2.ordinal)
		}

	}

}