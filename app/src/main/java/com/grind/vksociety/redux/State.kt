package com.grind.vksociety.redux

sealed class State {
    object Empty: State()
    object EmptyProgress: State()
    data class DataError<T>(val error: Throwable, val offset: Int?, val data: List<T>?): State()
    data class Data<T>(val offset: Int, val data: List<T>): State()
    data class NewPageProgress<T>(val offset: Int, val data: List<T>): State()
    data class FullData<T>(val offset: Int, val data: List<T>): State()
}