package co.touchlab.multiplatform.architecture.livedata

actual interface Observer<T> {
    actual fun onChanged(t: T?)
}
