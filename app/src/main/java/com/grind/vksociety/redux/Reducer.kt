package com.grind.vksociety.redux

class Reducer {

    fun <T> reduce(
        action: Action,
        state: State,
        sideEffectListener: (SideEffect) -> Unit
    ): State {
        when (action) {
            is Action.NewPage<*> -> {
                when (state) {
                    is State.Empty -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(0))
                        return State.EmptyProgress
                    }
                    is State.EmptyProgress -> return State.EmptyProgress
                    is State.Data<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(action.offset))
                        return State.NewPageProgress(action.offset, action.data)
                    }
                    is State.FullData<*> -> return State.FullData(action.offset, action.data)
                    is State.NewPageProgress<*> -> return State.NewPageProgress(
                        action.offset,
                        action.data
                    )
                    is State.DataError<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(0))
                        return State.NewPageProgress(
                            action.offset ?: 0,
                            action.data ?: listOf()
                        )
                    }
                }
            }
            is Action.Refresh -> {
                when (state) {
                    is State.Empty -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(0))
                        return State.EmptyProgress
                    }
                    is State.EmptyProgress -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(0))
                        return State.EmptyProgress
                    }
                    is State.Data<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(0))
                        return State.EmptyProgress
                    }
                    is State.FullData<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(0))
                        return State.EmptyProgress
                    }
                    is State.NewPageProgress<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(0))
                        return State.EmptyProgress
                    }
                    is State.DataError<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(0))
                        return State.EmptyProgress
                    }
                }
            }
            is Action.LoadSuccess<*> -> {
                when (state) {
                    is State.EmptyProgress -> {
                        if (action.data.isNotEmpty())
                            return State.Data(action.offset, action.data)
                        else
                            return State.FullData(action.offset, action.data)
                    }
                    is State.NewPageProgress<*> -> {
                        if (action.data.isNotEmpty())
                            return State.Data(action.offset, action.data)
                        else
                            return State.FullData(action.offset, action.data)
                    }
                    is State.Empty -> return State.EmptyProgress
                    is State.Data<*> -> return State.EmptyProgress
                    is State.FullData<*> -> return State.EmptyProgress
                    is State.DataError<*> -> return State.EmptyProgress
                }
            }
            is Action.LoadError ->
                when (state) {
                    is State.EmptyProgress -> return State.DataError(action.error, 0, listOf<T>())
                    is State.NewPageProgress<*> -> return State.DataError(
                        action.error,
                        state.offset,
                        state.data
                    )
                    is State.Empty -> return State.EmptyProgress
                    is State.Data<*> -> return State.EmptyProgress
                    is State.FullData<*> -> return State.EmptyProgress
                    is State.DataError<*> -> return State.EmptyProgress
                }
        }
    }
}