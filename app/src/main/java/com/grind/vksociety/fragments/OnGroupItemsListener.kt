package com.grind.vksociety.fragments

import com.grind.vksociety.models.Society

interface OnGroupItemsListener {
    fun unsubscribe(listOfIds: List<Long>)
    fun onSelectItemsCountChanged(count: Int)
    fun onItemClick(society: Society)
}