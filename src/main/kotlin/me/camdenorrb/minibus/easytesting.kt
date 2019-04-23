package me.camdenorrb.minibus

import me.camdenorrb.minibus.event.CancellableMiniEvent
import me.camdenorrb.minibus.event.EventWatcher
import me.camdenorrb.minibus.listener.ListenerAction
import me.camdenorrb.minibus.listener.ListenerEntry
import me.camdenorrb.minibus.listener.ListenerPriority.FIRST
import me.camdenorrb.minibus.listener.ListenerPriority.NORMAL
import me.camdenorrb.minibus.listener.MiniListener
import me.camdenorrb.minibus.type.TypeHolder


class GenericThing<S : Any>(val value: S)

class Thing
class TestEvent(var count: Int = 0, var abc: String = ""): CancellableMiniEvent()


class Listener : MiniListener {

	@EventWatcher
	fun test(event: TestEvent) {
		println("Called")
	}

	@EventWatcher
	fun testt2(event: TestEvent) {
		println("Called")
	}

	@EventWatcher
	fun test3(event: TestEvent) {
		println("Called")
	}

	/*
	@EventWatcher
	fun test4(event: BenchmarkEvent) {
		println("Called")
	}*/

}

fun main() {

	val listener = Listener()

	val function = ListenerAction.Function<Any>(listener, ::main)
	println(sortedSetOf(ListenerEntry(FIRST, function), ListenerEntry(NORMAL, function)))

	val thing = GenericThing("Meow")

	println(thing::class.java.isAssignableFrom(GenericThing(2)::class.java))

	println(object : TypeHolder<String>() {}.type)
	println(object : TypeHolder<Int>() {}.type)
	//println(type1.isSubtypeOf(type2))

	//val miniBus = MiniBus()
	//miniBus.register(Listener())

	//miniBus(TestEvent())
	//miniBus(BenchmarkEvent())
	/*
	val thing = GenericThing("Meow")
	val thingHolder = TypeHolder.of<GenericThing<String>>()

	val holder1 = object : TypeHolder<List<String>>() {}
	val holder2 = object : TypeHolder<List<Any>>() {}

	println(holder1.type)
	println(holder2.type)


	println(holder1.type == holder2.type)
	println(holder2.type.isSubtypeOf(holder1.type))
	println(holder2.type.isSupertypeOf(holder1.type))*/

	//println(thingHolder.type)
	//println(thing.javaClass as Type)

	//println(thing::class.supertypes.joinToString())
	//println(thingHolder::class.allSupertypes.joinToString { it.arguments.joinToString() })

	//val holder1 = TypeHolder.of<Map<Int, String>>()
	//val holder2 = TypeHolder.of<Map<Int, Int>>()

	//println(holder1.isAssignableFrom(holder2))
	//println((holder1.type as ParameterizedType).actualTypeArguments.joinToString())
	//println(holder1.type == holder2.raw)
	//compare(holder1.type, holder2.type)
	//println(holder.type.typeName)
	//TypeHolder<String>(thing.javaClass)
}

/*
fun compare(type1: Type, type2: Type): Boolean {

	println("type1 = ${type1.typeName}")
	println("type2 = ${type2.typeName}")

	if (type1 == type2) {
		return true
	}

	val name1 = type1.typeName
	val name2 = type2.typeName

	val genericIndex1 = name1.indexOf('<').takeUnless { it == -1 } ?: return false
	val genericIndex2 = name2.indexOf('<').takeUnless { it == -1 } ?: return false

}*/


/*
abstract class TypeHolder<T : Any>(val raw: KClass<in T>) {

	val type by lazy {
		(javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
	}



	/*
	fun Type.raw(): Class<*> {

		if (this is Class<*>) {
			println("Here1")
			return this
		}

		if (this is ParameterizedType) {
			println("Here2")
			return checkNotNull(this.rawType as? Class<*>)
		}

		if (this is GenericArrayType) {
			println("Here3")
			return java.lang.reflect.Array.newInstance(genericComponentType.raw(), 0).javaClass
		}

		if (this is TypeVariable<*>) {
			println("Here4")
			return Any::class.java
		}

		if (this is WildcardType) {
			println("Here5")
			return this.upperBounds[0].raw()
		}

		error("Couldn't get type of ${this::class.simpleName}")
	}*/


	companion object {

		inline fun <reified T : Any> of(): TypeHolder<T> {
			return object : TypeHolder<T>(T::class) {}
		}

	}

}*/