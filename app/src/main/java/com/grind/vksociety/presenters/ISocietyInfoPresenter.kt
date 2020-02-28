package com.grind.vksociety.presenters

import com.grind.vksociety.models.Society

interface ISocietyInfoPresenter {
    fun getSocietyInfo(currSociety: Society)
}