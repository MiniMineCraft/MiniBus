@file:Suppress("UNCHECKED_CAST")

package me.camdenorrb.minibus.listener.table

import me.camdenorrb.minibus.listener.ListenerAction
import me.camdenorrb.minibus.listener.ListenerPriority
import me.camdenorrb.minibus.listener.ListenerPriority.NORMAL
import me.camdenorrb.minibus.listener.ListenerPriority.PriorityComparator
import java.util.*
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction


private typealias ListenerMap = MutableMap<KClass<out Any>, TreeMap<ListenerPriority, MutableList<ListenerAction<Any>>>>


class ListenerTable {

	val map: ListenerMap = mutableMapOf()


	fun find(event: Any) = map.entries.find { it.key.isInstance(event) }


	inline fun <reified T : Any> entries() = this[T::class]?.entries

	inline fun <reified T : Any> entries(priority: ListenerPriority) = this[T::class]?.get(priority)


	inline fun <reified T : Any> get() = this[T::class]

	inline fun <reified T : Any> get(priority: ListenerPriority) = this[T::class]?.get(priority)


	operator fun get(kClass: KClass<out Any>) = map[kClass]


	fun call(event: Any) = find(event)?.value?.values?.forEach {
		it.forEach { it(event) }
	}


	inline fun <reified T : Any> remove()
		= map.remove(T::class)

	inline fun <reified T : Any> remove(priority: ListenerPriority)
		= this[T::class]?.remove(priority)


	fun remove(action: ListenerAction<Any>) = map.values.forEach {
		it.values.forEach { it.remove(action) }
	}

	fun remove(callable: KCallable<Any>) = map.values.forEach {
		it.values.forEach { it.removeIf { it.callable == callable } }
	}


	inline fun <reified T : Any> add(action: ListenerAction<Any>, priority: ListenerPriority = NORMAL) {
		this.add(T::class as KClass<Any>, action, priority)
	}

	inline fun <reified T : Any> add(instance: Any, function: KFunction<Any>, priority: ListenerPriority = NORMAL) {
		this.add<T>(priority, ListenerAction.Function(instance, function))
	}

	inline fun <reified T : Any> add(priority: ListenerPriority = NORMAL, noinline block: T.() -> Any) {
		this.add<T>(ListenerAction.Lambda(block) as ListenerAction.Lambda<Any>, priority)
	}


	fun add(clazz: KClass<Any>, instance: Any, function: KFunction<Any>, priority: ListenerPriority = NORMAL) {
		this.add(clazz, ListenerAction.Function(instance, function), priority)
	}

	fun add(clazz: KClass<Any>, action: ListenerAction<Any>, priority: ListenerPriority = NORMAL) {
		map.getOrPut(clazz, { TreeMap(PriorityComparator) }).getOrPut(priority, { mutableListOf() }).add(action)
	}

}