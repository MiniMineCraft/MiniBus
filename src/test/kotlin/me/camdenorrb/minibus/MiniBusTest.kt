package me.camdenorrb.minibus

import me.camdenorrb.minibus.event.CancellableMiniEvent
import me.camdenorrb.minibus.event.EventWatcher
import me.camdenorrb.minibus.event.MiniEvent
import me.camdenorrb.minibus.listener.ListenerPriority.*
import me.camdenorrb.minibus.listener.MiniListener
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.system.measureNanoTime

/**
 * Created by camdenorrb on 3/29/17.
 */

internal class BenchmarkEvent: MiniEvent()

internal data class TestEvent(var count: Int = 0, var abc: String = ""): CancellableMiniEvent()


internal class MiniBusTest: MiniListener {

	val miniBus = MiniBus()

	@Before
	fun setUp() { miniBus.register(this) }

	@Test
	fun eventTest() {
		val calledEvent = miniBus(TestEvent())

		println("Count: ${calledEvent.count}  Order: ${calledEvent.abc}")

		if (calledEvent.count != 3) error("Not all events were called!")
		if (calledEvent.abc != "abc") error("The events were not called in order!")
		if (calledEvent.cancelled.not()) error("Event was not cancelled after the last listener!")

		// Do a warm up before benchmarking.
		for (i in 0..1000) {}

		val benchmarkEvent = BenchmarkEvent()
		val benchResults = measureNanoTime { miniBus(benchmarkEvent) }

		println("It took $benchResults nanoseconds to run the Benchmark event.")
	}

	@After
	fun tearDown() { miniBus.cleanUp() }


	@EventWatcher
	fun onBenchMark(event: BenchmarkEvent) = Unit

	@EventWatcher(FIRST)
	fun onTest1(event: TestEvent) {
		event.count++
		event.abc += 'a'
	}

	@EventWatcher(NORMAL)
	fun onTest2(event: TestEvent) {
		event.count++
		event.abc += 'b'
	}

	@EventWatcher(LAST)
	fun onTest3(event: TestEvent) {
		event.count++
		event.abc += 'c'
		event.cancelled = true
	}

}