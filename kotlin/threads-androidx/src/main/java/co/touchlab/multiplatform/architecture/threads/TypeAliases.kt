/*
 * Copyright (c) 2018 Touchlab Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.touchlab.multiplatform.architecture.threads

import java.lang.ThreadLocal

actual typealias ThreadLocalImpl<T> = ThreadLocal<T>

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
actual annotation class NativeThreadLocal

actual typealias MutableLiveData<T> = androidx.lifecycle.MutableLiveData<T>
actual typealias MediatorLiveData<T> = androidx.lifecycle.MediatorLiveData<T>
actual typealias Observer<T> = androidx.lifecycle.Observer<T>

actual fun <S, T> MediatorLiveData<T>.addSource(source:MutableLiveData<S>, onChanged:Observer<S>){
    this.addSource(source as androidx.lifecycle.LiveData<S>, onChanged)
}

actual fun <S, T> MediatorLiveData<T>.removeSource(toRemote:MutableLiveData<S>){
    this.removeSource(toRemote as androidx.lifecycle.LiveData<S>)
}