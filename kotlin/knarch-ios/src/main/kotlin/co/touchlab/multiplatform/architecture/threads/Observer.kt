package co.touchlab.multiplatform.architecture.threads

actual interface Observer<T> {
    actual fun onChanged(t: T)
}
