# KNArch.threads

Basic threading support for mobile Kotlin multiplatform. Mostly for [Droidcon](https://github.com/touchlab/DroidconKotlin/).
You can use this, but something better should be coming really soon, so I'd expect it to be replaced pretty soon.

## LiveData

Simple implementation of LiveData to allow a reactive(ish) architecture on Android and iOS.

## ThreadLocal

Multiplatform ThreadLocal instance. In Kotlin/Native, you can't share non-frozen data among threads. This allow you to have non-frozen data attached to an instance, provided you only access the thread's instance in that thread. Mostly used for lambda's that get run in the main thread.

## AtomicRef

Multiplatform access to AtomicReference. Critical for "mutable" references in frozen data on Kotlin/Native. The data itself isn't mutable, but you can change the data referred to itself. So mutable-ish.
