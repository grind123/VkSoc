package com.grind.vksociety.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.grind.vksociety.models.Society
import com.grind.vksociety.redux.Action
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.requests.VKRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class SocietyByCategoryViewModel: ViewModel() {

    val groupsByCategoriesData: LiveData<Map<String?, List<Society>>> = MutableLiveData<Map<String?, List<Society>>>()


     fun getGroupsByCategories(){
         val request = VKRequest<JSONObject>("groups.get")
         request.addParam("extended", 1)
         request.addParam("fields", "description,activity,members_count")
         request.addParam("count", 1000)
         VK.execute(request, object : VKApiCallback<JSONObject> {
             override fun fail(error: Exception) {
                 Log.e("getSocietyList", error.message)
             }

             override fun success(result: JSONObject) {
                 CoroutineScope(Dispatchers.Default).launch {
                     val list = mutableListOf<Society>()
                     val items = result.getJSONObject("response")
                         .getJSONArray("items")
                     for (i in 0 until items.length()) {
                         val item =
                             Gson().fromJson(items.getJSONObject(i).toString(), Society::class.java)
                         list.add(item)
                     }
                     list.reverse()
                     val resultMap = list.filter { it.activity != null }
                         .groupBy { it.activity }
                         .filter { !it.key!!.contains(":") && !it.key!!.contains(".") }
                     (groupsByCategoriesData as MutableLiveData).postValue(resultMap)
                 }
             }
         })

     }
}