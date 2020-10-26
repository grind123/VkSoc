package com.grind.vksociety.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.grind.vksociety.models.Friend
import com.grind.vksociety.models.Society
import com.grind.vksociety.models.SocietyInfo
import com.vk.api.sdk.VK
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import kotlin.concurrent.thread

class SocietyInfoViewModel : ViewModel() {

    val societyInfoData: LiveData<SocietyInfo> = MutableLiveData<SocietyInfo>()

    fun getSocietyInfo(currSociety: Society) {
        thread(start = true) {
            val date = getDateOfLastPostInGroup(currSociety)
            val friendList = getFriendsInGroup(currSociety)
            val info = SocietyInfo(
                currSociety.id,
                currSociety.name,
                currSociety.description,
                currSociety.membersCount,
                date,
                "http://vk.com/${currSociety.screenName}",
                friendList
            )

            (societyInfoData as MutableLiveData).postValue(info)
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

    private fun getFriendsInGroup(currSociety: Society): List<Friend> {
        val request = VKRequest<JSONObject>("groups.getMembers")
            .addParam("group_id", currSociety.id)
            .addParam("filter", "friends")
            .addParam("fields", "photo_50")
        val result = VK.executeSync(request)
        val friendsList = result.getJSONObject("response")
            .getJSONArray("items")
        val resultList = mutableListOf<Friend>()
        for(i in 0 until friendsList.length()){
            val friend = Gson().fromJson(friendsList.get(i).toString(), Friend::class.java)
            resultList.add(friend)
        }
        return resultList
    }
}