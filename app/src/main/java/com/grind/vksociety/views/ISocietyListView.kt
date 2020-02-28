package com.grind.vksociety.views

import com.grind.vksociety.models.Society

interface ISocietyListView {
    fun onListPresent(items: List<Society>)
}