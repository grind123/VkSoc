package com.grind.vksociety.views

interface ISocietyInfoView {
    fun onInfoPresent(name: String, subsCount: String, desc: String, lastPost: String, url: String)
}