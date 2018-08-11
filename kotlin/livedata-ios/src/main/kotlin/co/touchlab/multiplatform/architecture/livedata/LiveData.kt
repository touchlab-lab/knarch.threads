package co.touchlab.multiplatform.architecture.livedata

import platform.Foundation.*
import platform.darwin.*
import konan.worker.*
import co.touchlab.knarch.threads.*
import co.touchlab.multiplatform.architecture.threads.*

actual fun <S, T> MediatorLiveData<T>.addSource(source:MutableLiveData<S>, onChanged:Observer<S>){
    this.addSource(source as LiveData<S>, onChanged)
}
actual fun <S, T> MediatorLiveData<T>.removeSource(toRemote:MutableLiveData<S>){
    this.removeSource(toRemote as LiveData<S>)
}

actual class MediatorLiveData<T>:MutableLiveData<T>() {
    private val mSources = ThreadLocalImpl<HashMap<LiveData<*>, Source<*>>>()

    init {
        mSources.set(HashMap())
    }
    /**
     * Starts to listen the given {@code source} LiveData, {@code onChanged} observer will be called
     * when {@code source} value was changed.
     * <p>
     * {@code onChanged} callback will be called only when this {@code MediatorLiveData} is active.
     * <p> If the given LiveData is already added as a source but with a different Observer,
     * {@link IllegalArgumentException} will be thrown.
     *
     * @param source the {@code LiveData} to listen to
     * @param onChanged The observer that will receive the events
     * @param <S> The type of data hold by {@code source} LiveData
     */
    fun <S> addSource(source:LiveData<S>, onChanged:Observer<S>) {
        assertMainThread("addSource")
        val e = Source<S>(source, onChanged)
        val existing = mSources.get()!!.get(source)
            if (existing != null && existing.mObserver !== onChanged)
            {
                throw IllegalArgumentException(
                        "This source was already added with the different observer")
            }

        mSources.get()!!.put(source, e)

        if (existing != null)
            {
            return
        }
                e.plug()
    }

    /**
     * Stops to listen the given {@code LiveData}.
     *
     * @param toRemote {@code LiveData} to stop to listen
     * @param <S> the type of data hold by {@code source} LiveData
     */

    fun <S> removeSource(toRemote:LiveData<S>) {
        assertMainThread("removeSource")
        val source = mSources.get()!!.remove(toRemote)
        if (source != null)
        {
            source.unplug()
        }
    }
    /*@CallSuper
    protected fun onActive() {
        for (source in mSources)
        {
            source.getValue().plug()
        }
    }
    @CallSuper
    protected fun onInactive() {
        for (source in mSources)
        {
            source.getValue().unplug()
        }
    }*/
    private class Source<V> internal constructor(liveData:LiveData<V>, observer:Observer<V>):Observer<V> {
        internal val mLiveData:LiveData<V>
        internal val mObserver:Observer<V>
        internal var mVersion = START_VERSION
        init{
            mLiveData = liveData
            mObserver = observer
        }
        internal fun plug() {
            mLiveData.observeForever(this)
        }
        internal fun unplug() {
            mLiveData.removeObserver(this)
        }
        override fun onChanged(v:V?) {
            if (mVersion != mLiveData.version.get())
            {
                mVersion = mLiveData.version.get()
                mObserver.onChanged(v)
            }
        }
    }
}

actual open class MutableLiveData<T>actual constructor():LiveData<T>(){
    public actual override fun postValue(value: T){
        super.postValue(value)
    }
    public actual override fun setValue(value: T){
        super.setValue(value)
    }
    actual override fun getValue():T? = super.getValue()
}

abstract class LiveData<T> {
    private val mObservers = ThreadLocalImpl<HashMap<Observer<T>, LifecycleBoundObserver<T>>>()
    private val mData = AtomicReference(NOT_SET)
    // when setData is called, we set the pending data and actual data swap happens on the main
    // thread
    private val mPendingData = AtomicReference(NOT_SET)
    internal val version = AtomicInt(START_VERSION)
    private val mDispatchingValue = AtomicBoolean(false)
    private val mDispatchInvalidated = AtomicBoolean(false)
    private val mPostValueRunnable = {
        val oldValue:Any? = mPendingData.compareAndSwap(mPendingData.get(), NOT_SET)
        if(oldValue !== NOT_SET)
            setValue(oldValue as T)
    }

    init {
        assertMainThread("init")
        mObservers.set(HashMap())
    }

    /**
     * Returns the current value.
     * Note that calling this method on a background thread does not guarantee that the latest
     * value set will be received.
     *
     * @return the current value
     */
    /**
     * Sets the value. If there are active observers, the value will be dispatched to them.
     * <p>
     * This method must be called from the main thread. If you need set a value from a background
     * thread, you can use {@link #postValue(Object)}
     *
     * @param value The new value
     */
    open fun getValue():T?{
        val data = mData.get()
        if (data !== NOT_SET)
        {
            return data as T?
        }
        return null
    }

    protected open fun setValue(value:T){
        assertMainThread("setValue")
        version.increment()
        mData.compareAndSwap(mData.get(), value.freeze())
        dispatchValue()
    }

    private fun dispatchValue(){
    val observerMap = mObservers.get()!!
        for(observer in observerMap.values){
            considerNotify(observer)
        }
    }

    private fun considerNotify(observer:LifecycleBoundObserver<T>) {
        val ver = version.get()
        val lastVer: Int = observer.lastVersion.get()
        if (lastVer >= ver)
        {
            return
        }
        observer.lastVersion.compareAndSwap(lastVer, ver)
        observer.observer.onChanged(mData.get()!! as T)
    }

    private fun dispatchingValue(initiatorArg:LifecycleBoundObserver<T>?) {
        var initiator:LifecycleBoundObserver<T>? = initiatorArg
        if (mDispatchingValue.get())
        {
            mDispatchInvalidated.compareAndSwap(false, true)
            return
        }
        mDispatchingValue.compareAndSwap(false, true)
        do
        {
            mDispatchInvalidated.compareAndSwap(true, false)
            if (initiator != null)
            {
                considerNotify(initiator)
                initiator = null
            }
        }
        while (mDispatchInvalidated.get())
        mDispatchingValue.compareAndSwap(true, false)
    }

    /**
     * Adds the given observer to the observers list. This call is similar to
     * {@link LiveData#observe(LifecycleOwner, Observer)} with a LifecycleOwner, which
     * is always active. This means that the given observer will receive all events and will never
     * be automatically removed. You should manually call {@link #removeObserver(Observer)} to stop
     * observing this LiveData.
     * While LiveData has one of such observers, it will be considered
     * as active.
     * <p>
     * If the observer was already added with an owner to this LiveData, LiveData throws an
     * {@link IllegalArgumentException}.
     *
     * @param observer The observer that will receive the events
     */
//    @MainThread
    fun observeForever(observer:Observer<T>) {
        assertMainThread("observeForever")
        val wrapper = LifecycleBoundObserver(observer)
        mObservers.get()!!.put(observer, wrapper)
    }

    fun removeObserver(observer:Observer<T>){
        assertMainThread("removeObserver")
        mObservers.get()!!.remove(observer)
    }

    /**
     * Posts a task to a main thread to set the given value. So if you have a following code
     * executed in the main thread:
     * <pre class="prettyprint">
     * liveData.postValue("a");
     * liveData.setValue("b");
     * </pre>
     * The value "b" would be set at first and later the main thread would override it with
     * the value "a".
     * <p>
     * If you called this method multiple times before a main thread executed a posted task, only
     * the last value would be dispatched.
     *
     * @param value The new value
     */
    protected open fun postValue(value:T) {

        /*val result  = detachObjectGraph { value.freeze() as Any }

        dispatch_async(dispatch_get_main_queue()){
            val mainResult = attachObjectGraph<Any>(result) as T
            setValue(mainResult)
        }*/

        val postTask:Boolean = mPendingData.compareAndSwap(mPendingData.get(), value.freeze()) === NOT_SET
        if (!postTask)
        {
            return
        }

        dispatch_async(dispatch_get_main_queue()){
            mPostValueRunnable()
        }
    }

    /**
     * Returns true if this LiveData has observers.
     *
     * @return true if this LiveData has observers
     */
    fun hasObservers():Boolean {
        assertMainThread("hasObservers")
        return mObservers.get()!!.size > 0
    }

    internal class LifecycleBoundObserver<T>(observer:Observer<T>) {
        val observer:Observer<T>
        var active:Boolean = false
        var lastVersion = AtomicInt(START_VERSION)
        init{
            this.observer = observer
        }
    }

    companion object {
        internal val START_VERSION = -1
        private val NOT_SET = Any()
    }
}

private fun assertMainThread(methodName:String) {
    //isMainThread
    if (!NSThread.isMainThread())
    {
        throw IllegalStateException(("Cannot invoke " + methodName + " on a background"
                + " thread"))
    }
}