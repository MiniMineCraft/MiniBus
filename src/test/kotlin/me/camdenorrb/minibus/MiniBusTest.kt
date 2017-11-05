package me.camdenorrb.minibus

import me.camdenorrb.minibus.event.CancellableMiniEvent
import me.camdenorrb.minibus.event.EventWatcher
import me.camdenorrb.minibus.listener.ListenerPriority.*
import me.camdenorrb.minibus.listener.MiniListener
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.system.measureNanoTime



class BenchmarkEvent

class TestEvent(var count: Int = 0, var abc: String = ""): CancellableMiniEvent()


class MiniBusTest: MiniListener {

	val miniBus = MiniBus()

	@Before
	fun setUp() { miniBus.register(this) }

	@Test
	fun eventTest() {

		val calledEvent = miniBus(TestEvent())

		println("Count: ${calledEvent.count}  Order: ${calledEvent.abc}")

		check(calledEvent.count == 6) { "Not all events were called!" }
		check(calledEvent.abc == "aabbcc") { "The events were not called in order!" }
		check(calledEvent.isCancelled) { "Event was not cancelled after the last listener!" }


		var totalTime = 0L
		val benchmarkEvent = BenchmarkEvent()

		/* Warm Up */ for (i in 0..100_000) miniBus(benchmarkEvent)

		for (i in 1..1000) totalTime += measureNanoTime { miniBus(benchmarkEvent) }

		println("1000 * BenchEvent { Average: ${totalTime / 1000}/ns Total: $totalTime/ns  }")
		totalTime = 0

		val meow = "Meow"

		/* Warm Up */ for (i in 0..100_000) miniBus(meow)

		for (i in 1..1000) totalTime += measureNanoTime { miniBus(meow) }
		println("1000 * NonExistent Event { Average: ${totalTime / 1000}/ns Total: $totalTime/ns }")

	}

	@After
	fun tearDown() { miniBus.cleanUp() }


	@EventWatcher
	fun BenchmarkEvent.onBenchMark() = Unit


	@EventWatcher(FIRST)
	fun TestEvent.onTest1() {
		count++
		abc += 'a'
	}

	@EventWatcher(FIRST)
	fun TestEvent.onTest2() {
		count++
		abc += 'a'
	}

	@EventWatcher(NORMAL)
	fun TestEvent.onTest3() {
		count++
		abc += 'b'
	}

	@EventWatcher(NORMAL)
	fun TestEvent.onTest4() {
		count++
		abc += 'b'
	}

	@EventWatcher(LAST)
	fun TestEvent.onTest5() {
		count++
		abc += 'c'
	}

	@EventWatcher(LAST)
	fun TestEvent.onTest6() {
		count++
		abc += 'c'
		isCancelled = true
	}

}