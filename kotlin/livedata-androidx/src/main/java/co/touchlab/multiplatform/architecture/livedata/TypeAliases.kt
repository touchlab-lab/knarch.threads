package co.touchlab.multiplatform.architecture.livedata

actual typealias MutableLiveData<T> = androidx.lifecycle.MutableLiveData<T>
actual typealias MediatorLiveData<T> = androidx.lifecycle.MediatorLiveData<T>
actual typealias Observer<T> = androidx.lifecycle.Observer<T>

actual fun <S, T> MediatorLiveData<T>.addSource(source:MutableLiveData<S>, onChanged:Observer<S>){
    this.addSource(source as androidx.lifecycle.LiveData<S>, onChanged)
}

actual fun <S, T> MediatorLiveData<T>.removeSource(toRemote:MutableLiveData<S>){
    this.removeSource(toRemote as androidx.lifecycle.LiveData<S>)
}