package com.grind.vksociety.presenters

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.Gson
import com.grind.vksociety.models.Society
import com.grind.vksociety.views.ISocietyListView
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.requests.VKBooleanRequest
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.concurrent.thread

class SocietyListPresenter(view: ISocietyListView) : ISocietyListPresenter {
    private val v = view

    override fun getSocietyList() {
        val request = VKRequest<JSONObject>("groups.get")
        request.addParam("extended", 1)
        request.addParam("fields", "description,activity,members_count")
        VK.execute(request, object : VKApiCallback<JSONObject> {
            override fun fail(error: Exception) {
                Log.e("getSocietyList", error.message)
            }

            override fun success(result: JSONObject) {
                Log.e("getSocietyList", result.toString())
                val list = mutableListOf<Society>()
                val items = result.getJSONObject("response")
                    .getJSONArray("items")
                for (i in 0 until items.length()) {
                    val item =
                        Gson().fromJson(items.getJSONObject(i).toString(), Society::class.java)
                    list.add(item)
                    Log.e("SocietyItem", item.toString())
                }
                v.onListPresent(list.reversed())
            }
        })
    }

    override fun unsubscribeGroups(listOfId: List<Long>) {
        thread(start = true) {
            for (id in listOfId) {
                val response = unsubscribe(id)
                val int = response.getInt("response")
                Log.e("Unsub", "$int")
            }
            getSocietyList()
        }
    }

    private fun unsubscribe(id: Long): JSONObject {
        val request = VKRequest<JSONObject>("groups.leave")
            .addParam("group_id", id)
        return VK.executeSync(request)

    }
}