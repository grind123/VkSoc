package com.grind.vksociety.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.requests.VKRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel : ViewModel() {

    fun unsubscribeGroups(listOfId: List<Long>) {
        CoroutineScope(Dispatchers.IO).launch {
            for (id in listOfId) {
                unsubscribe(id)
            }
        }
    }


    private fun unsubscribe(id: Long){
        val request = VKRequest<JSONObject>("groups.leave")
            .addParam("group_id", id)
         VK.execute(request, object : VKApiCallback<JSONObject>{
             override fun fail(error: Exception) {

             }

             override fun success(result: JSONObject) {
                 val int = result.getInt("response")
                 Log.e("Unsub", "$int")
             }

         })

    }
}