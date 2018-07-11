package co.touchlab.multiplatform.architecture.threads


actual fun <X, Y> map(source:MutableLiveData<X>, func: (i:X)->Y ):MutableLiveData<Y>{
    return android.arch.lifecycle.Transformations.map(source, WrapFunction(func))
}

private class WrapFunction(func: (i:X)->Y):android.arch.core.util.Function<X, Y>{
    override fun apply(i:X):Y = func(i)
}

actual fun <X, Y> switchMap(trigger:MutableLiveData<X>, func: (i:X)->MutableLiveData<Y>)
        :MutableLiveData<Y>{
    android.arch.lifecycle.Transformations.switchMap(trigger, WrapFunction(func))
}
