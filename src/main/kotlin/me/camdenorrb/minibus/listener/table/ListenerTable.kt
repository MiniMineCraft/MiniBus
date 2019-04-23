@file:Suppress("UNCHECKED_CAST")

package me.camdenorrb.minibus.listener.table

import me.camdenorrb.minibus.listener.ListenerAction
import me.camdenorrb.minibus.listener.ListenerAction.Lambda
import me.camdenorrb.minibus.listener.ListenerEntry
import me.camdenorrb.minibus.listener.ListenerPriority
import me.camdenorrb.minibus.listener.ListenerPriority.NORMAL
import java.util.*
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.isSubclassOf


//private typealias ListenerMapEntry = Map.Entry<KClass<out Any>, TreeMap<ListenerPriority, MutableList<ListenerAction<Any>>>>


class ListenerTable {

	val map = mutableMapOf<KClass<out Any>, TreeSet<ListenerEntry<Any>>>()


	fun find(event: Any): TreeSet<ListenerEntry<Any>>? {
		val eventClass = event::class
		return map.entries.find { it.key.isSubclassOf(eventClass) }?.value
	}


	inline fun <reified T : Any> entries(): TreeSet<ListenerEntry<Any>>? {
		return this[T::class]
	}

	inline fun <reified T : Any> entries(priority: ListenerPriority): List<ListenerEntry<Any>>? {
		return entries<T>()?.filter { it.priority == priority }
	}


	inline fun <reified T : Any> get(): TreeSet<ListenerEntry<Any>>? {
		return this[T::class]
	}

	inline fun <reified T : Any> get(priority: ListenerPriority): List<ListenerEntry<Any>>? {
		return get<T>()?.filter { it.priority == priority }
	}


	operator fun get(kClass: KClass<out Any>) = map[kClass]


	fun call(event: Any) {

		/*map.forEach {
			println("${it.key} [${it.value.joinToString { it.action.callable.toString() }}]")
		}

		println("----")*/


		find(event)?.forEach {
			it.action(event)
		}
		//println()
	}


	inline fun <reified T : Any> remove(): TreeSet<ListenerEntry<Any>>? {
		return map.remove(T::class)
	}

	inline fun <reified T : Any> remove(priority: ListenerPriority): Boolean? {
		return this[T::class]?.removeIf { it.priority == priority }
	}


	fun remove(action: ListenerAction<*>) = map.values.removeIf { value ->
		value.removeIf { it.action == action }
		value.isEmpty()
	}

	fun remove(callable: KCallable<*>) = map.values.removeIf { value ->
		value.removeIf { it.action.callable == callable }
		value.isEmpty()
	}


	inline fun <reified T : Any> add(action: ListenerAction<T>, priority: ListenerPriority = NORMAL) {
		this.add(T::class as KClass<Any>, action as ListenerAction<Any>, priority)
	}

	inline fun <reified T : Any> add(instance: Any, function: KFunction<Any>, priority: ListenerPriority = NORMAL) {
		this.add<T>(ListenerAction.Function(instance, function), priority)
	}

	inline fun <reified T : Any> add(priority: ListenerPriority = NORMAL, noinline block: Lambda<T>.(T) -> Unit) {
		this.add<T>(Lambda(block) as Lambda<Any>, priority)
	}


	fun add(clazz: KClass<Any>, instance: Any, function: KFunction<Any>, priority: ListenerPriority = NORMAL) {
		this.add(clazz, ListenerAction.Function(instance, function), priority)
	}

	fun add(clazz: KClass<Any>, action: ListenerAction<Any>, priority: ListenerPriority = NORMAL) {
		map.getOrPut(clazz) { TreeSet() }.add(ListenerEntry(priority, action))
	}

}