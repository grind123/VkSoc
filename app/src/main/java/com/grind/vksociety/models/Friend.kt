package com.grind.vksociety.models

import com.google.gson.annotations.SerializedName

data class Friend(
    val id: Long = 0,
    @SerializedName("photo_50") val avatarUrl: String? = null
) {
}