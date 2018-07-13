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

import kotlinx.cinterop.COpaquePointer
import konan.internal.ExportForCppRuntime

// Clear holding the counter object, which refers to the actual object.
internal class ThreadOnlyCounter(var referred: COpaquePointer?) {
    // Spinlock, potentially taken when materializing or removing 'referred' object.
    var lock: Int = 0

    var target:Any? = null
}

// Get a counter from non-null object.
@SymbolName("Konan_getThreadOnlyImpl")
external internal fun getThreadOnlyImpl(referent: Any): ThreadOnlyCounter

// Create a counter object.
@ExportForCppRuntime
internal fun makeThreadOnlyCounter(referred: COpaquePointer) = ThreadOnlyCounter(referred)
