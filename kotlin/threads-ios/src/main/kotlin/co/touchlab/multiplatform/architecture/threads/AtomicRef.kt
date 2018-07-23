package co.touchlab.multiplatform.architecture.threads

import konan.worker.*

actual class AtomicRef<T>actual constructor(t:T?){
    val store:konan.worker.AtomicReference<T>

    init {
        if(t != null)
            t.freeze()

        store = konan.worker.AtomicReference<T>(t)
    }

    actual fun compareAndSwapValue(expected: T?, new: T?): T?{
        return store.compareAndSwap(expected, new.freeze())
    }

    actual fun getValue(): T? = store.get()
}