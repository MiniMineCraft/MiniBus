package me.camdenorrb.minibus

import me.camdenorrb.minibus.event.EventWatcher
import me.camdenorrb.minibus.event.type.Cancellable
import me.camdenorrb.minibus.listener.ListenerPriority.*
import me.camdenorrb.minibus.listener.MiniListener
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.system.measureNanoTime



internal open class BenchmarkEvent

internal class GenericEvent(var called: Boolean = false) : BenchmarkEvent()

internal class TestEvent(var count: Int = 0, var abc: String = ""): Cancellable()


internal class MiniBusTest: MiniListener {

	val miniBus = MiniBus()

	@Before
	fun setUp() { miniBus.register(this) }

	@Test
	fun eventTest() {

		val calledEvent = miniBus(TestEvent())

		println("Count: ${calledEvent.count}  Order: ${calledEvent.abc}")

		check(calledEvent.count == 6) { "Not all events were called!" }
		check(calledEvent.abc == "a1 a2 b1 b2 c1 c2") { "The events were not called in order!" }
		check(calledEvent.isCancelled) { "Event was not cancelled after the last listener!" }


		var totalTime = 0L
		val benchmarkEvent = BenchmarkEvent()

		/*
		val genericEvent: BenchmarkEvent = GenericEvent()
		miniBus(genericEvent)

		check((genericEvent as GenericEvent).called) { "Generic event wasn't called!" }
		*/

		/* Warm Up */
		for (i in 0..100_000) miniBus(benchmarkEvent)

		for (i in 1..1000) totalTime += measureNanoTime { miniBus(benchmarkEvent) }

		println("1000 * BenchEvent { Average: ${totalTime / 1000}/ns Total: $totalTime/ns  }")
		totalTime = 0

		val meow = "Meow"

		/* Warm Up */
		for (i in 0..100_000) miniBus(meow)

		for (i in 1..1000) totalTime += measureNanoTime { miniBus(meow) }
		println("1000 * NonExistent Event { Average: ${totalTime / 1000}/ns Total: $totalTime/ns }")

	}

	@After
	fun tearDown() {

		miniBus.unregister(this)

		if (miniBus.listenerTable.map.isNotEmpty()) {
			error("Listeners not empty")
		}
	}


	@EventWatcher
	fun BenchmarkEvent.onBenchMark() = Unit

	@EventWatcher
	fun GenericEvent.onGenericEvent() {
		called = true
	}



	@EventWatcher(FIRST)
	fun TestEvent.onTest1() {
		count++
		abc += "a1 "
	}

	@EventWatcher(FIRST)
	fun TestEvent.onTest2() {
		count++
		abc += "a2 "
	}

	@EventWatcher(NORMAL)
	fun TestEvent.onTest3() {
		count++
		abc += "b1 "
	}

	@EventWatcher(NORMAL)
	fun TestEvent.onTest4() {
		count++
		abc += "b2 "
	}

	@EventWatcher(LAST)
	fun TestEvent.onTest5() {
		count++
		abc += "c1 "
	}

	@EventWatcher(LAST)
	fun TestEvent.onTest6() {
		count++
		abc += "c2"
		isCancelled = true
	}

}