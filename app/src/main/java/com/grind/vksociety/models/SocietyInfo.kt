package com.grind.vksociety.models

import java.text.SimpleDateFormat
import java.util.*

fun SocietyInfo.membersCountFormat(count: Int): String {
    if (count > 1000000) {
        return String.format("%.1fM", count.toDouble() / 1000000)
    } else if (count > 1000) {
        return String.format("%.1fK", count.toDouble() / 1000)
    } else return "$count"
}

fun SocietyInfo.dateFormat(date: Long): String {
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    return format.format(Date(date))
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