package com.grind.vksociety.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grind.vksociety.App
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.requests.VKRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val recommendedViewModel = ViewModelProvider(App.recommendListViewModelStore, ViewModelProvider.AndroidViewModelFactory(App())).get(RecommendListViewModel::class.java)
    private val setOfUnsubscribeCategories = mutableSetOf<String>()


    fun unsubscribeGroups(listOfId: List<Long>) {
        if(App.REAL_UNSUBSCRIBE_MODE){
            CoroutineScope(Dispatchers.IO).launch {
                for (id in listOfId) {
                    unsubscribe(id)
                }
            }
        }

        addCategoryToSet(listOfId)

    }


    private fun unsubscribe(id: Long){
        val request = VKRequest<JSONObject>("groups.leave")
            .addParam("group_id", id)
         VK.execute(request, object : VKApiCallback<JSONObject>{
             override fun fail(error: Exception) {
                Toast.makeText(App.appContext, "Error", Toast.LENGTH_SHORT).show()
             }

             override fun success(result: JSONObject) {
                 val int = result.getInt("response")
                 Log.e("Unsub", "$int")
             }

         })

    }

    private fun addCategoryToSet(listOfId: List<Long>){
        val request = VKRequest<JSONObject>("groups.getById")
            .addParam("group_ids", listOfId)
            .addParam("fields", "activity")
        VK.execute(request,object : VKApiCallback<JSONObject>{
            override fun fail(error: Exception) {
                Toast.makeText(App.appContext, "Error", Toast.LENGTH_SHORT).show()
            }

            override fun success(result: JSONObject) {
                val items = result.getJSONArray("response")
                for (i in 0 until items.length()) {
                    val category = (items[i] as JSONObject).getString("activity")
                    if(category != "Закрытая группа" && category != "Открытая группа")
                        setOfUnsubscribeCategories.add(category)
                }
                recommendedViewModel.makeRecommendation(setOfUnsubscribeCategories)
            }

        } )
    }
}