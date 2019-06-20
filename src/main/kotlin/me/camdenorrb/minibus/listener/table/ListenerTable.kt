@file:Suppress("UNCHECKED_CAST")

package me.camdenorrb.minibus.listener.table

import me.camdenorrb.minibus.listener.ListenerAction
import me.camdenorrb.minibus.listener.ListenerAction.Lambda
import me.camdenorrb.minibus.listener.ListenerEntry
import me.camdenorrb.minibus.listener.ListenerPriority
import me.camdenorrb.minibus.listener.ListenerPriority.NORMAL
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf


//private typealias ListenerMapEntry = Map.Entry<KClass<out Any>, TreeMap<ListenerPriority, MutableList<ListenerAction<Any>>>>


class ListenerTable {

	val map = mutableMapOf<KType, ConcurrentSkipListSet<ListenerEntry<Any>>>()


	@ExperimentalStdlibApi
	inline fun <reified T> find(): Set<ListenerEntry<Any>>? {
		return map.entries.find { it.key.isSubtypeOf(typeOf<T>()) }?.value
	}


	@ExperimentalStdlibApi
	inline fun <reified T : Any> entries(): Set<ListenerEntry<Any>>? {
		return map[typeOf<T>()]
	}

	@ExperimentalStdlibApi
	inline fun <reified T : Any> entries(priority: ListenerPriority): List<ListenerEntry<Any>>? {
		return entries<T>()?.filter { it.priority == priority }
	}


	@ExperimentalStdlibApi
	inline fun <reified T> get(): Set<ListenerEntry<Any>>? {
		return map[typeOf<T>()]
	}

	@ExperimentalStdlibApi
	inline fun <reified T> get(priority: ListenerPriority): List<ListenerEntry<Any>>? {
		return map[typeOf<T>()]?.filter { it.priority == priority }
	}


	//inline operator fun <reified T> get() = map[typeOf<T>()]


	@ExperimentalStdlibApi
	inline fun <reified T> call(event: Any) {

		/*map.forEach {
			println("${it.key} [${it.value.joinToString { it.action.callable.toString() }}]")
		}

		println("----")*/


		find<T>()?.forEach {
			it.action(event)
		}
		//println()
	}


	@ExperimentalStdlibApi
	inline fun <reified T> remove(): Set<ListenerEntry<Any>>? {
		return map.remove(typeOf<T>())
	}


	@ExperimentalStdlibApi
	inline fun <reified T> remove(priority: ListenerPriority): Boolean? {
		return map[typeOf<T>()]?.removeIf { it.priority == priority }
	}


	fun remove(action: ListenerAction<*>) = map.values.removeIf { value ->
		value.removeIf { it.action == action }
		value.isEmpty()
	}

	fun remove(callable: KCallable<*>) = map.values.removeIf { value ->
		value.removeIf { it.action.callable == callable }
		value.isEmpty()
	}


	@ExperimentalStdlibApi
	inline fun <reified T : Any> add(action: ListenerAction<T>, priority: ListenerPriority = NORMAL) {
		this.add(typeOf<T>(), action as ListenerAction<Any>, priority)
	}

	@ExperimentalStdlibApi
	inline fun <reified T : Any> add(instance: Any, function: KFunction<Any>, priority: ListenerPriority = NORMAL) {
		this.add<T>(ListenerAction.Function(instance, function), priority)
	}

	@ExperimentalStdlibApi
	inline fun <reified T : Any> add(priority: ListenerPriority = NORMAL, noinline block: Lambda<T>.(T) -> Unit) {
		this.add<T>(Lambda(block) as Lambda<Any>, priority)
	}



	fun add(type: KType, instance: Any, function: KFunction<Any>, priority: ListenerPriority = NORMAL) {
		this.add(type, ListenerAction.Function(instance, function), priority)
	}

	fun add(type: KType, action: ListenerAction<Any>, priority: ListenerPriority = NORMAL) {
		map.getOrPut(type) { ConcurrentSkipListSet() }.add(ListenerEntry(priority, action))
	}

}