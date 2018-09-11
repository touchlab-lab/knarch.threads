package co.touchlab.multiplatform.architecture.threads

actual class AtomicRef<T>actual constructor(t:T){
    val atom = java.util.concurrent.atomic.AtomicReference<T>(t)
    actual var value: T
        get() = atom.get()
        set(value) {
            atom.set(value)
        }
}