@file:Suppress("UNCHECKED_CAST")

package me.camdenorrb.minibus

import me.camdenorrb.minibus.event.EventWatcher
import me.camdenorrb.minibus.event.MiniEvent
import me.camdenorrb.minibus.listener.ListenerFunction
import me.camdenorrb.minibus.listener.MiniListener
import java.util.*
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.jvmErasure

/**
 * Created by camdenorrb on 3/5/17.
 */

class MiniBus {

	val listenerMap = mutableMapOf<KClass<out MiniEvent>, TreeSet<ListenerFunction>>()


	fun cleanUp() { listenerMap.clear() }


	operator fun <T: MiniEvent> invoke(event: T): T {
		listenerMap[event::class]?.forEach { it.function.call(it.listener, event) }
		return event
	}


	fun unregister(listener: MiniListener, function: KCallable<*>) {
		function.findAnnotation<EventWatcher>() ?: return
		listenerMap.forEach { it.value.removeIf { listener == it.listener && function == it.function } }
	}

	fun unregister(listener: MiniListener) {
		listener::class.memberFunctions.forEach { function ->
			function.findAnnotation<EventWatcher>() ?: return@forEach
			listenerMap.forEach { it.value.removeIf { listener == it.listener && function == it.function } }
		}
	}


	fun register(vararg listeners: MiniListener) { listeners.forEach { register(it) } }

	fun register(listener: MiniListener) {
		listener::class.memberFunctions.forEach {
			val priority = it.findAnnotation<EventWatcher>()?.priority ?: return@forEach
			val event = it.parameters.getOrNull(1)?.type?.jvmErasure as? KClass<out MiniEvent> ?: return@forEach

			listenerMap.computeIfAbsent(event, { sortedSetOf() }).add(ListenerFunction(listener, priority, it))
		}
	}

}