package co.touchlab.multiplatform.architecture.threads

/**
 * Wrapper for multiplatform atomic ref access
 */
expect class AtomicRef<T>(t:T){
    var value:T
}