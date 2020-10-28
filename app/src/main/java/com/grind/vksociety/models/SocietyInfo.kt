package com.grind.vksociety.models

import java.text.SimpleDateFormat
import java.util.*

fun Int.toMembersCountFormat(): String {
    if (this > 1000000) {
        return String.format("%.1fM", this.toDouble() / 1000000)
    } else if (this > 1000) {
        return String.format("%.1fK", this.toDouble() / 1000)
    } else return "$this"
}

fun Long.toDateFormat(): String {
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    return format.format(Date(this))
}

data class SocietyInfo(
    val id: Long,
    val name: String,
    val desc: String,
    val membersCount: Int,
    val lastPostDate: Long,
    val url: String,
    val friendsInSociety: List<Friend>
) {
}