package com.grind.vksociety.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Society(@SerializedName("id") var id: Long,
                   @SerializedName("name") var name: String,
                   @SerializedName("screen_name") var screenName: String,
                   @SerializedName("photo_200") var logoUrl: String,
                   @SerializedName("activity") var activity: String,
                   @SerializedName("members_count") var membersCount: Int,
                   @SerializedName("description") var description: String): Serializable {
}