package me.camdenorrb.minibus.listener

import kotlin.reflect.KCallable
import kotlin.reflect.KFunction


sealed class ListenerAction<in T : Any>(val callable: KCallable<Any>) : (T) -> Unit {

	class Lambda<T : Any>(val block: Lambda<T>.(T) -> Unit): ListenerAction<T>(block::invoke) {

		override fun invoke(event: T) {
			block(event)
		}

	}

	class Function<in T : Any>(val instance: Any, val function: KFunction<Any>): ListenerAction<T>(function) {

		override fun invoke(event: T) {
			function.call(instance, event)
		}

	}

}