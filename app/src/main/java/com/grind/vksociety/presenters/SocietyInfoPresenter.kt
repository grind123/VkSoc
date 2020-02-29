package com.grind.vksociety.presenters

import com.grind.vksociety.models.Society
import com.grind.vksociety.views.ISocietyInfoView
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class SocietyInfoPresenter(view: ISocietyInfoView) : ISocietyInfoPresenter {
    private val v = view


    override fun getSocietyInfo(currSociety: Society) {
        thread (start = true){
            val date = getDateOfLastPostInGroup(currSociety)
            val friendCount = getFriendCountInGroup(currSociety)

            v.onInfoPresent(
                currSociety.name,
                "${subsCountFormat(currSociety.membersCount)} подписчиков • $friendCount друзей",
                currSociety.description,
                "Последняя запись ${dateFormat(date)}",
                "http://vk.com/${currSociety.screenName}"
            )
        }
    }

    private fun getDateOfLastPostInGroup(currSociety: Society): Long {
        var lastPostDate = 0L
        val request = VKRequest<JSONObject>("wall.get")
        request.addParam("owner_id", "-${currSociety.id}")
        request.addParam("count", 2)
        val result = VK.executeSync(request)
        val responseList = result.getJSONObject("response")
            .getJSONArray("items")
        for (i in 0 until responseList.length()) {
            if (!(responseList.getJSONObject(i).has("is_pinned") &&
                        responseList.getJSONObject(i).getInt("is_pinned") == 1)
            ) {
                lastPostDate = responseList.getJSONObject(i).getLong("date") * 1000
                break
            }
        }
        return lastPostDate
    }

    private fun getFriendCountInGroup(currSociety: Society): Int{
        val request = VKRequest<JSONObject>("groups.getMembers")
            .addParam("group_id", currSociety.id)
            .addParam("filter", "friends")
        val result = VK.executeSync(request)
        val count = result.getJSONObject("response")
            .getInt("count")
        return count
    }

    private fun subsCountFormat(count: Int): String {
        if (count > 1000000) {
            return String.format("%.1fM", count.toDouble() / 1000000)
        } else if (count > 1000) {
            return String.format("%.1fK", count.toDouble() / 1000)
        } else return "$count"
    }

    private fun dateFormat(date: Long): String {
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        return format.format(Date(date))
    }
}