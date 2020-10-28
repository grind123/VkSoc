package com.grind.vksociety.models

data class MyActivity(val groupName: String,
val lastActivityDate: Long,
val likesCount: Int,
val commentsCount: Int,
val repostsCount: Int) {
    companion object{
        const val HIGH_LEVEL_ACTIVITY = 1
        const val LOW_LEVEL_ACTIVITY = 2
    }
    val activityLevel: Int = calculateActivityLevel()

    private fun calculateActivityLevel(): Int{
        return LOW_LEVEL_ACTIVITY
    }
}