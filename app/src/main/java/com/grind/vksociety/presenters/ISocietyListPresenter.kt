package com.grind.vksociety.presenters

interface ISocietyListPresenter {
    fun getSocietyList()
    fun unsubscribeGroups(listOfId: List<Long>)
}