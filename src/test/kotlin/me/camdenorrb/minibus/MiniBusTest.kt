package me.camdenorrb.minibus

import me.camdenorrb.minibus.event.CancellableMiniEvent
import me.camdenorrb.minibus.event.EventWatcher
import me.camdenorrb.minibus.listener.ListenerPriority.*
import me.camdenorrb.minibus.listener.MiniListener
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by camdenorrb on 3/29/17.
 */

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
	}

	@After
	fun tearDown() { miniBus.cleanUp() }


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
	}

}

internal data class TestEvent(var count: Int = 0, var abc: String = ""): CancellableMiniEvent()