package com.grind.vksociety.presenters

interface ISocietyListPresenter {
    fun getSocietyList(offset: Int)
    fun unsubscribeGroups(listOfId: List<Long>)
}