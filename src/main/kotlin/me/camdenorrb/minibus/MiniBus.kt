@file:Suppress("UNCHECKED_CAST")

package me.camdenorrb.minibus

import me.camdenorrb.minibus.event.EventWatcher
import me.camdenorrb.minibus.listener.ListenerFunction
import me.camdenorrb.minibus.listener.MiniListener
import java.lang.reflect.Modifier.isStatic
import java.util.*
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility.PUBLIC
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

/**
 * Created by camdenorrb on 3/5/17.
 */
class MiniBus {

	val listenerMap = mutableMapOf<KClass<out Any>, TreeSet<ListenerFunction>>()


	fun cleanUp() { listenerMap.clear() }


	operator fun <T : Any> invoke(event: T): T {
		listenerMap.filter { it.key.isInstance(event) }.forEach { it.value.forEach { it(event) } }
		return event
	}


	fun unregister(listener: MiniListener, function: KCallable<*>) = listenerMap.forEach {
		it.value.removeIf { listener == it.listener && function == it.function }
	}

	fun unregister(listener: MiniListener) = listener::class.declaredFunctions.forEach {
		if (it.findAnnotation<EventWatcher>() != null) unregister(listener, it)
	}


	fun register(vararg listeners: MiniListener) = listeners.forEach { register(it) }

	fun register(listener: MiniListener) = listener::class.declaredFunctions.forEach {

		it.isAccessible = true

		if (it.visibility != PUBLIC || it.javaMethod?.modifiers?.let { isStatic(it) } == null) return@forEach

		val priority = it.findAnnotation<EventWatcher>()?.priority ?: return@forEach
		val event = it.parameters.getOrNull(1)?.type?.jvmErasure as? KClass<Any> ?: return@forEach

		listenerMap.getOrPut(event, { sortedSetOf() }).add(ListenerFunction(listener, priority, it))
	}

}