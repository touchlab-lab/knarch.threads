package co.touchlab.multiplatform.architecture.threads

//Can't expect/actual LiveData because of this issue https://youtrack.jetbrains.com/issue/KT-19848

expect open class MutableLiveData<T>(){
    fun getValue():T?
    open fun setValue(value:T)
    open fun postValue(value:T)
}

expect interface Observer<T>{
    fun onChanged(t: T?)
}

expect class MediatorLiveData<T>(){
    fun <S> addSource(source:MutableLiveData<S>, onChanged:Observer<S>)
    fun <S> removeSource(toRemote:MutableLiveData<S>)
}
