package co.touchlab.multiplatform.architecture.threads

actual fun <X, Y> map(source:LiveData<X>, func: (i:X)->Y ):LiveData<Y>{
    val result = MediatorLiveData<Y>()
    result.addSource(source, object : Observer<X> {
        override fun onChanged(x: X) {
            result.setValue(func(x))
        }
    })
    return result
}

actual fun <X, Y> switchMap(trigger:LiveData<X>, func: (i:X)->LiveData<Y>):LiveData<Y>{
    val result = MediatorLiveData<Y>()
    result.addSource(trigger, object : Observer<X> {
        var mSource: LiveData<Y>? = null

        override fun onChanged(x: X) {
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
                    override fun onChanged(y: Y) {
                        result.setValue(y)
                    }
                })
            }
        }
    })
    return result
}
