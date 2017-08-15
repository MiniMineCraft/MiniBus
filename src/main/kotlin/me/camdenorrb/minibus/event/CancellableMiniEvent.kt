package me.camdenorrb.minibus.event

/**
 * Created by camdenorrb on 3/5/17.
 */

open class CancellableMiniEvent : MiniEvent() {

	var cancelled: Boolean = false

}

