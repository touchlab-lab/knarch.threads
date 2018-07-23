package co.touchlab.multiplatform.architecture.threads

actual fun <X, Y> map(source:MutableLiveData<X>, func: (i:X)->Y ):MutableLiveData<Y>{
    val result = androidx.lifecycle.MediatorLiveData<Y>()
    result.addSource(source) { x ->
        if(x != null)
            result.value = func(x)
    }
    return result
}

actual fun <X, Y> switchMap(trigger:MutableLiveData<X>, func: (i:X)->MutableLiveData<Y>):MutableLiveData<Y>{
    val result = androidx.lifecycle.MediatorLiveData<Y>()
    result.addSource(trigger, object : Observer<X> {
        var mSource: androidx.lifecycle.MutableLiveData<Y>? = null

        override fun onChanged(x: X?) {
            if(x != null) {
                val newLiveData = func(x)
                if (mSource === newLiveData) {
                    return
                }
                if (mSource != null) {
                    result.removeSource(mSource!!)
                }
                mSource = newLiveData
                if (mSource != null) {
                    result.addSource(mSource!!) { y ->
                        if(y != null)
                            result.value = y
                    }
                }
            }
        }
    })
    return result
}