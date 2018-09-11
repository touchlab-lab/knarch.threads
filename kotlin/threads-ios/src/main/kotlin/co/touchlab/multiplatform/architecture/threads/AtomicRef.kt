package co.touchlab.multiplatform.architecture.threads

import kotlin.native.*
import kotlin.native.concurrent.*

actual class AtomicRef<T> actual constructor(t: T) {
    val atom = AtomicReference<T>(t.freeze())
    actual var value: T
        get() = atom.value
        set(value) {
            atom.value = value.freeze()
        }
}