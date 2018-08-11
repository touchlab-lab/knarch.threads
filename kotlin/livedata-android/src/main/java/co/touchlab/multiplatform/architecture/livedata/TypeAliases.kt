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
package co.touchlab.multiplatform.architecture.livedata

actual typealias MutableLiveData<T> = android.arch.lifecycle.MutableLiveData<T>
actual typealias MediatorLiveData<T> = android.arch.lifecycle.MediatorLiveData<T>
actual typealias Observer<T> = android.arch.lifecycle.Observer<T>

actual fun <S, T> MediatorLiveData<T>.addSource(source:MutableLiveData<S>, onChanged:Observer<S>){
    this.addSource(source as android.arch.lifecycle.LiveData<S>, onChanged)
}
actual fun <S, T> MediatorLiveData<T>.removeSource(toRemote:MutableLiveData<S>){
    this.removeSource(toRemote as android.arch.lifecycle.LiveData<S>)
}