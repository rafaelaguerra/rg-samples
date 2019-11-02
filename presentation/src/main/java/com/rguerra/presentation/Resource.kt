package com.rguerra.presentation

const val GENERIC_ERROR = "GENERIC_ERROR"

sealed class Resource<T> {
    inline fun fold(
            onData: (T) -> Unit = {},
            onError: (String) -> Unit = {},
            onComplete: () -> Unit = {}
    ) {
        when (this) {
            is Data -> onData(data)
            is Failure -> onError(errorMsg)
            is EmptyResource -> onComplete()
        }
    }
}

class Data<T>(val data: T) : Resource<T>()
class Failure<T>(val errorMsg: String) : Resource<T>()
object EmptyResource : Resource<Void>()
