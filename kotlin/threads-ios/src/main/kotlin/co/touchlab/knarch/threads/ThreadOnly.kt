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

package co.touchlab.knarch.threads

import platform.Foundation.*
import platform.darwin.*

/**
 * Class WeakReference encapsulates weak reference to an object, which could be used to either
 * retrieve a strong reference to an object, or return null, if object was already destoyed by
 * the memory manager.
 */
class ThreadOnly<T : Any> {
    /**
     * Creates a weak reference object pointing to an object. Weak reference doesn't prevent
     * removing object, and is nullified once object is collected.
     */
    constructor(referred: T) {
        val pointer = getThreadOnlyImpl(this)
        pointer.target = referred
    }

    fun access(proc: (value:T)->Unit){
        assertMainThread("access")
        runProc(proc)
    }

    private fun runProc(proc: (value:T)->Unit){
        proc(getThreadOnlyImpl(this).target as T)
    }

    private fun assertMainThread(methodName:String) {
        //isMainThread
        if (!NSThread.isMainThread())
        {
            throw IllegalStateException(("Cannot invoke " + methodName + " on a background"
                    + " thread"))
        }
    }
}
