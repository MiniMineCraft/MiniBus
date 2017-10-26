@file:Suppress("UNCHECKED_CAST")

package me.camdenorrb.minibus

import me.camdenorrb.minibus.event.EventWatcher
import me.camdenorrb.minibus.listener.ListenerAction
import me.camdenorrb.minibus.listener.ListenerAction.Lambda
import me.camdenorrb.minibus.listener.ListenerPriority
import me.camdenorrb.minibus.listener.ListenerPriority.NORMAL
import me.camdenorrb.minibus.listener.MiniListener
import me.camdenorrb.minibus.listener.table.ListenerTable
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility.PUBLIC
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure


class MiniBus {

	val listenerTable = ListenerTable()


	fun cleanUp() {
		listenerTable.map.clear()
	}


	operator fun <T : Any> invoke(event: T): T {
		listenerTable.call(event)
		return event
	}


	fun unregister(action: ListenerAction<Any>)
		= listenerTable.remove(action)

	fun unregister(callable: KCallable<Any>)
		= listenerTable.remove(callable)

	fun unregister(lambda: Any.() -> Unit)
		= listenerTable.remove(lambda::invoke)


	fun unregister(listener: MiniListener) = listener::class.declaredFunctions.forEach {
		if (it.findAnnotation<EventWatcher>() != null) unregister(it as KCallable<Any>)
	}


	fun register(vararg listeners: MiniListener)
		= listeners.forEach { register(it) }


	inline fun <reified T : Any> register(action: ListenerAction<Any>, priority: ListenerPriority = NORMAL)
		= listenerTable.add<T>(action, priority)

	inline fun <reified T : Any> register(priority: ListenerPriority = NORMAL, noinline block: Lambda<T>.(T) -> Unit)
		= listenerTable.add(priority, block)


	fun register(listener: MiniListener) = listener::class.declaredFunctions.forEach {

		if (it.visibility != PUBLIC) return

		if (!it.isAccessible) it.isAccessible = true

		val priority = it.findAnnotation<EventWatcher>()?.priority ?: return@forEach
		val event = it.parameters[1].type.jvmErasure as? KClass<Any> ?: error("Unable to register event!")

		listenerTable.add(event, listener, it as KFunction<Any>, priority)
	}

}