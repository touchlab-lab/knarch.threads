package co.touchlab.multiplatform.architecture.threads

actual fun <X, Y> map(source:MutableLiveData<X>, func: (i:X)->Y ):MutableLiveData<Y>{
    val result = MediatorLiveData<Y>()
    result.addSource(source, object : Observer<X> {
        override fun onChanged(x: X?) {
            if(x != null)
                result.setValue(func(x))
        }
    })
    return result
}

actual fun <X, Y> switchMap(trigger:MutableLiveData<X>, func: (i:X)->MutableLiveData<Y>):MutableLiveData<Y>{
    val result = MediatorLiveData<Y>()
    result.addSource(trigger, object : Observer<X> {
        var mSource: MutableLiveData<Y>? = null

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
                    result.addSource(mSource!!, object : Observer<Y> {
                        override fun onChanged(y: Y?) {
                            if(y != null)
                                result.setValue(y)
                        }
                    })
                }
            }
        }
    })
    return result
}
