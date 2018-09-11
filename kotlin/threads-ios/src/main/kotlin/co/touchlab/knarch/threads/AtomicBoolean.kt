package co.touchlab.knarch.threads

import kotlin.native.*
import kotlin.native.concurrent.*

class AtomicBoolean(arg: Boolean = false) {
    private val FALSE = 0
    private val TRUE = 1

    private fun makeInt(arg: Boolean) = if (arg) {
        TRUE
    } else {
        FALSE
    }

    private fun makeBoolean(arg: Int) = arg != FALSE
    private val data = AtomicInt(makeInt(arg))

    fun compareAndSwap(expect: Boolean, update: Boolean): Boolean {
        return makeBoolean(data.compareAndSwap(makeInt(expect), makeInt(update)))
    }

    var value: Boolean
        get() = makeBoolean(data.value)
        set(value) {
            data.value = makeInt(value)
        }
}