package com.grind.vksociety.presenters

import android.util.Log
import com.grind.vksociety.App
import com.grind.vksociety.models.Society
import com.grind.vksociety.views.ISocietyInfoView
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class SocietyInfoPresenter(view: ISocietyInfoView): ISocietyInfoPresenter {
    private val v = view

    private var lastPostDate = 0L

    override fun getSocietyInfo(currSociety: Society) {
        getDateOfLastPostInGroup(currSociety)
    }

//    private fun getFriendList(groupId: Long) {
//        val request = VKRequest<JSONObject>("friends.get")
//        VK.execute(request, object : VKApiCallback<JSONObject> {
//            override fun fail(error: Exception) {
//            }
//
//            override fun success(result: JSONObject) {
//                val list = mutableListOf<Int>()
//                val listOfId = result.getJSONObject("response")
//                    .getJSONArray("items")
//                for (i in 0 until listOfId.length()) {
//                    list.add(listOfId.getInt(i))
//                }
//                calculateFriendsInGroup(groupId, list)
//            }
//        })
//    }
//
//    private fun calculateFriendsInGroup(groupId:Long, list: List<Int>){
//        val request = VKRequest<JSONObject>("groups.isMember")
//        request.addParam("access_token", App.vkAccessKey)
//        request.addParam("group_id", list.joinToString { "$it" })
//        request.addParam("user_ids", groupId)
//        VK.execute(request, object : VKApiCallback<JSONObject>{
//            override fun fail(error: Exception) {
//                Log.e("calculateFriendsInGroup", error.message)
//            }
//
//            override fun success(result: JSONObject) {
//                var count = 0
//                val responseList = result.getJSONObject("response")
//                    .getJSONArray("items")
//                for(i in 0 until responseList.length()){
//                    if(responseList.getInt(i) == 1) count++
//                }
//                friendsInGroup = count
//            }
//        })
//    }

    private fun getDateOfLastPostInGroup(currSociety: Society){
        val request = VKRequest<JSONObject>("wall.get")
        request.addParam("owner_id", "-${currSociety.id}")
        request.addParam("count", 2)
        VK.execute(request, object : VKApiCallback<JSONObject>{
            override fun fail(error: Exception) {
            }

            override fun success(result: JSONObject) {
                val responseList = result.getJSONObject("response")
                    .getJSONArray("items")
                for(i in 0 until responseList.length()){
                    if(!(responseList.getJSONObject(i).has("is_pinned") &&
                        responseList.getJSONObject(i).getInt("is_pinned") == 1)){
                        lastPostDate = responseList.getJSONObject(i).getLong("date") * 1000
                        break
                    }
                }
                v.onInfoPresent(currSociety.name,
                    "${subsCountFormat(currSociety.membersCount)} подписчиков",
                    currSociety.description,
                    dateFormat(lastPostDate),
                "http://vk.com/${currSociety.screenName}"
                    )
            }
        })
    }

    private fun subsCountFormat(count: Int): String{
        if(count > 1000000){
            return String.format("%.1fM",count.toDouble()/1000000)
        } else if(count > 1000){
            return String.format("%.1fK",count.toDouble()/1000)
        } else return "$count"
    }

    private fun dateFormat(date: Long): String{
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        return format.format(Date(date))
    }
}