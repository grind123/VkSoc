package com.grind.vksociety.redux

sealed class SideEffect {
    class LoadPage(val offset: Int): SideEffect()
    class EventError(val error: Throwable): SideEffect()
}