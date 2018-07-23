package co.touchlab.multiplatform.architecture.threads

expect class AtomicRef<T>(t:T?=null){
    fun compareAndSwapValue(expected: T?, new: T?): T?
    fun getValue(): T?
}