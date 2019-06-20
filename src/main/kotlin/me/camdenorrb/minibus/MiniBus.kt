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
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

// TODO: Use star projection more often

class MiniBus {

	val listenerTable = ListenerTable()


	fun cleanUp() {
		listenerTable.map.clear()
	}


	@ExperimentalStdlibApi
	inline operator fun <reified T : Any> invoke(event: T): T {
		listenerTable.call<T>(event)
		return event
	}


	fun unregister(callable: KCallable<*>)
		= listenerTable.remove(callable)

	fun unregister(action: ListenerAction<*>)
		= listenerTable.remove(action)

	fun unregister(lambda: Any.() -> Unit)
		= listenerTable.remove(lambda::invoke)


	fun unregister(listener: MiniListener) {
		listener::class.declaredFunctions.forEach {
			if (it.findAnnotation<EventWatcher>() == null) return@forEach
			unregister(it)
		}
	}


	fun register(vararg listeners: MiniListener)
		= listeners.forEach { register(it) }


	@ExperimentalStdlibApi
	inline fun <reified T : Any> register(action: ListenerAction<T>, priority: ListenerPriority = NORMAL)
		= listenerTable.add(action, priority)

	@ExperimentalStdlibApi
	inline fun <reified T : Any> register(priority: ListenerPriority = NORMAL, noinline block: Lambda<T>.(T) -> Unit)
		= listenerTable.add(priority, block)


	fun register(listener: MiniListener) = listener::class.declaredFunctions.forEach {

		//if (it.visibility != PUBLIC) return@forEach

		if (!it.isAccessible) it.isAccessible = true

		val priority = it.findAnnotation<EventWatcher>()?.priority ?: return@forEach
		//it.parameters[1]::type::returnType
		//val event = it.parameters[1].type.jvmErasure as? KClass<Any> ?: error("Unable to register event!")

		val event = it.parameters[1].type
		//println("${listener::class.simpleName} $it")
		println("Registered $event")

		listenerTable.add(event, listener, it as KFunction<Any>, priority)
		//println(listenerTable[event]?.joinToString { it.action.callable.toString() })
	}

}