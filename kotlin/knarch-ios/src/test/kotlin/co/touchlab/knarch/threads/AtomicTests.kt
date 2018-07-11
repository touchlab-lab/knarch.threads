package co.touchlab.knarch.threads

import kotlin.test.*
import konan.worker.*

class AtomicTests{
    @Test
    fun atomicRef(){
        val aref = AtomicReference("1")
        aref.compareAndSwap("2", "3")
        assertEquals("1", aref.get())
        aref.compareAndSwap(null, "4")
        assertEquals("1", aref.get())
        aref.compareAndSwap("1", "5")
        assertEquals("5", aref.get())
    }
}