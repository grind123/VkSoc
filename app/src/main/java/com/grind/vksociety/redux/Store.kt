package com.grind.vksociety.redux

class Store<T>{
    var state: State = State.Empty
    val reducer: Reducer = Reducer()


}