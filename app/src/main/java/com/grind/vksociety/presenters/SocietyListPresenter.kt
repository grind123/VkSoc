package com.grind.vksociety.presenters

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.Gson
import com.grind.vksociety.App
import com.grind.vksociety.models.Society
import com.grind.vksociety.redux.Action
import com.grind.vksociety.redux.SideEffect
import com.grind.vksociety.redux.Store
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
    val store = Store<Society>()

    fun reduceAction(action: Action){
        val newState = store.reducer.reduce<Society>(action,
            store.state,
            { when(it){
                is SideEffect.LoadPage -> thread(start = true) {
                    getSocietyList(it.offset)
                }
                is SideEffect.EventError -> Toast.makeText(App.appContext, "Event Error", Toast.LENGTH_SHORT).show()
            } }
        )
        store.state = newState
    }

    override fun getSocietyList(offset: Int) {
        val request = VKRequest<JSONObject>("groups.get")
        request.addParam("extended", 1)
        request.addParam("fields", "description,activity,members_count")
        request.addParam("count", 12)
        request.addParam("offset", offset)
        VK.execute(request, object : VKApiCallback<JSONObject> {
            override fun fail(error: Exception) {
                Log.e("getSocietyList", error.message)
                reduceAction(Action.LoadError(error))
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
                val action = Action.LoadSuccess(offset, list.reversed())
                reduceAction(action)
                v.onListPresent(action.data)
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
//            getSocietyList()
        }
    }

    private fun unsubscribe(id: Long): JSONObject {
        val request = VKRequest<JSONObject>("groups.leave")
            .addParam("group_id", id)
        return VK.executeSync(request)

    }

}