package com.grind.vksociety.redux

sealed class Action {
    //view actions
    object Refresh : Action()
    data class NewPage<T>(val offset: Int, val data: List<T> ): Action()

    //server actions
    data class LoadSuccess<T>(val offset: Int, val data: List<T>) : Action()
    data class LoadError(val error: Throwable) : Action()
}