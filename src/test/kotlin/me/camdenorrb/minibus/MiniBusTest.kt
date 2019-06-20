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

internal class GenericEvent<T>(var called: Boolean = false) : BenchmarkEvent()

internal class TestEvent(var count: Int = 0, var abc: String = ""): Cancellable()


internal class MiniBusTest: MiniListener {

	val miniBus = MiniBus()

	@Before
	fun setUp() { miniBus.register(this) }

	@Test
	@ExperimentalStdlibApi
	fun eventTest() {

		val calledEvent = miniBus(TestEvent())

		println("Count: ${calledEvent.count}  Order: ${calledEvent.abc}")

		check(calledEvent.count == 3) { "Not all events were called!" }
		check(calledEvent.abc == "a b c") { "The events were not called in order!" }
		check(calledEvent.isCancelled) { "Event was not cancelled after the last listener!" }


		var totalTime = 0L
		val benchmarkEvent = BenchmarkEvent()


		// Generic tests
		val negateTest = miniBus(GenericEvent<Int>())
		check(!negateTest.called) { "Negated generic event was called!" }

		val positiveTest = miniBus(GenericEvent<String>())
		check(positiveTest.called) { "Positive generic event wasn't called!" }


		// Benchmark

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

		check(miniBus.listenerTable.map.isEmpty()) {
			"Listeners not empty ${miniBus.listenerTable.map}"
		}
	}


	@EventWatcher
	fun BenchmarkEvent.onBenchMark() = Unit

	@EventWatcher
	fun GenericEvent<String>.onGenericEvent() {
		called = true
	}



	@EventWatcher(FIRST)
	fun TestEvent.onTest1() {
		count++
		abc += "a "
	}

	@EventWatcher(NORMAL)
	fun TestEvent.onTest3() {
		count++
		abc += "b "
	}

	@EventWatcher(LAST)
	fun TestEvent.onTest6() {
		count++
		abc += "c"
		isCancelled = true
	}

}