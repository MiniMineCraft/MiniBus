/*
package me.camdenorrb.minibus

import me.camdenorrb.minibus.event.EventWatcher
import me.camdenorrb.minibus.listener.ListenerPriority
import me.camdenorrb.minibus.listener.MiniListener
import kotlin.system.measureNanoTime

open class BenchmarkEvent


class GenericEvent(var called: Boolean = false) : BenchmarkEvent()

class GenericEvent2<T : Any>(var called: Boolean = false) : BenchmarkEvent()


fun main() {
	MiniBusTest().apply {
		setUp()
		eventTest()
		tearDown()
	}
}

class MiniBusTest: MiniListener {

	val miniBus = MiniBus()

	fun setUp() {
		miniBus.register(this)
	}

	fun eventTest() {

		val calledEvent = miniBus(TestEvent())

		println("Count: ${calledEvent.count}  Order: ${calledEvent.abc}")

		check(calledEvent.count == 6) { "Not all events were called!" }
		check(calledEvent.abc == "a1 a2 b1 b2 c1 c2") { "The events were not called in order!" }
		check(calledEvent.isCancelled) { "Event was not cancelled after the last listener!" }


		var totalTime = 0L
		val benchmarkEvent = BenchmarkEvent()


		val genericEventString = GenericEvent2<String>()

		miniBus(genericEventString)

		println(genericEventString.called)


		*/
/*
		val genericEvent: BenchmarkEvent = GenericEvent()
		miniBus(genericEvent)

		check((genericEvent as GenericEvent).called) { "Generic event wasn't called!" }
		*//*


		*/
/* Warm Up *//*

		for (i in 0..100_000) miniBus(benchmarkEvent)

		for (i in 1..1000) totalTime += measureNanoTime { miniBus(benchmarkEvent) }

		println("1000 * BenchEvent { Average: ${totalTime / 1000}/ns Total: $totalTime/ns  }")
		totalTime = 0

		val meow = "Meow"

		*/
/* Warm Up *//*

		for (i in 0..100_000) miniBus(meow)

		for (i in 1..1000) totalTime += measureNanoTime { miniBus(meow) }
		println("1000 * NonExistent Event { Average: ${totalTime / 1000}/ns Total: $totalTime/ns }")

	}

	fun tearDown() {
		miniBus.cleanUp()
	}


	@EventWatcher
	fun onBenchMark(event: BenchmarkEvent) = Unit

	@EventWatcher
	fun onGenericEvent(event: GenericEvent) {
		event.called = true
	}

	@EventWatcher
	fun onGenericEventString(event: GenericEvent2<String>) {
		event.called = true
	}

	@EventWatcher
	fun onGenericEventInt(event: GenericEvent2<Int>) {
		error("Int generic event shouldn't have been called!")
	}


	@EventWatcher(ListenerPriority.FIRST)
	fun onTest1(event: TestEvent) {
		event.count++
		event.abc += "a1 "
	}

	@EventWatcher(ListenerPriority.FIRST)
	fun onTest2(event: TestEvent) {
		event.count++
		event.abc += "a2 "
	}

	@EventWatcher(ListenerPriority.NORMAL)
	fun onTest3(event: TestEvent) {
		event.count++
		event.abc += "b1 "
	}

	@EventWatcher(ListenerPriority.NORMAL)
	fun onTest4(event: TestEvent) {
		event.count++
		event.abc += "b2 "
	}

	@EventWatcher(ListenerPriority.LAST)
	fun onTest5(event: TestEvent) {
		event.count++
		event.abc += "c1 "
	}

	@EventWatcher(ListenerPriority.LAST)
	fun onTest6(event: TestEvent) {
		event.count++
		event.abc += "c2"
		event.isCancelled = true
	}

}
*/
