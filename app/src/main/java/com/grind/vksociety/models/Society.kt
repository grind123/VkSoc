package com.grind.vksociety.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Society(@SerializedName("id") var id: Long,
                   @SerializedName("name") var name: String,
                   @SerializedName("screen_name") var screenName: String,
                   @SerializedName("photo_200") var logoUrl: String,
                   @SerializedName("activity") var activity: String? = "Unknown",
                   @SerializedName("members_count") var membersCount: Int,
                   @SerializedName("description") var description: String): Parcelable {
}