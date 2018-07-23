package co.touchlab.multiplatform.architecture.threads

actual class AtomicRef<T>actual constructor(t:T?):java.util.concurrent.atomic.AtomicReference<T>(t){
    actual fun compareAndSwapValue(expected: T?, new: T?): T?{
        val updated = compareAndSet(expected, new)
        if(updated)
            return expected
        else
            return null
    }
    actual fun getValue(): T? = get()
}