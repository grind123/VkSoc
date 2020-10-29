package com.grind.vksociety.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.grind.vksociety.models.Society
import com.vk.api.sdk.VK
import com.vk.api.sdk.requests.VKRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class RecommendListViewModel: ViewModel() {

    val recommendListData: LiveData<Map<String?, List<Society>>> = MutableLiveData<Map<String?, List<Society>>>()

     private fun getRecommendListByCategory(categoryName: String): List<Society>{
         val request = VKRequest<JSONObject>("groups.search")
         request.addParam("q", categoryName)
         request.addParam("fields", "description,activity,members_count")
         request.addParam("count", 10)
         request.addParam("sort", 0)

         val result = VK.executeSync(request)
         val list = mutableListOf<Society>()
         val items = result.getJSONObject("response")
             .getJSONArray("items")
         for (i in 0 until items.length()) {
             if(items.getJSONObject(i).getInt("is_member") == 0){
                 val item =
                     Gson().fromJson(items.getJSONObject(i).toString(), Society::class.java)
                 list.add(item)
             }
         }
         return list
     }

    fun makeRecommendation(setOfCategories: Set<String>){
        CoroutineScope(Dispatchers.IO).launch {
            val resultMap = mutableMapOf<String?, List<Society>>()
            setOfCategories.forEach {
                resultMap[it] = getRecommendListByCategory(it)
            }
            (recommendListData as MutableLiveData).postValue(resultMap)
        }
    }
}