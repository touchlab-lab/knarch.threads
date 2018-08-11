package co.touchlab.multiplatform.architecture.livedata

expect fun <X, Y> map(source:MutableLiveData<X>, func: (i:X)->Y ):MutableLiveData<Y>

fun <X, Y> MutableLiveData<X>.map(func: (i:X)->Y ):MutableLiveData<Y> = map(this, func)

expect fun <X, Y> switchMap(trigger:MutableLiveData<X>, func: (i:X)->MutableLiveData<Y>):MutableLiveData<Y>

fun <X, Y> MutableLiveData<X>.switchMap(func: (i:X)->MutableLiveData<Y>):MutableLiveData<Y> = switchMap(this, func)
