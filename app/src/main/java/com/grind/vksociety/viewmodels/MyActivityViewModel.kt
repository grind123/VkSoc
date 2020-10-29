package com.grind.vksociety.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grind.vksociety.models.MyActivity
import com.grind.vksociety.models.Society
import com.grind.vksociety.models.WallPost
import com.vk.api.sdk.VK
import com.vk.api.sdk.requests.VKRequest
import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.random.Random

class MyActivityViewModel : ViewModel() {
    companion object {
        private const val UNIX_SECOND_MONTH = 60 * 60 * 24 * 30
    }

    val myActivityData: LiveData<MyActivity> = MutableLiveData<MyActivity>()

    fun getMyActivity(society: Society) {
        CoroutineScope(Dispatchers.IO).launch {
            val wallPostsData = getWallPostsData(society.id)
            val commentsCount = getCommentsCountMock()
            withContext(Dispatchers.Default) {
                val myActivity = MyActivity(
                    society.name,
                    wallPostsData.filter { it.like == 1 || it.repost == 1 }
                        .maxBy { it.date }?.date ?: 0,
                    wallPostsData.filter { it.like == 1 }.size,
                    commentsCount,
                    wallPostsData.filter { it.repost == 1 }.size
                )
                (myActivityData as MutableLiveData).postValue(myActivity)
            }
        }
    }

    private suspend fun getWallPostsData(groupId: Long): List<WallPost> {
        var needMorePosts = true
        var currOffset = 0
        val timeLine = System.currentTimeMillis() / 1000 - UNIX_SECOND_MONTH
        val wallPostList = mutableListOf<WallPost>()
        while (needMorePosts) {
            val request = VKRequest<JSONObject>("wall.get")
                .addParam("owner_id", groupId * -1)
                .addParam("count", 100)
                .addParam("offset", currOffset)
            val result = VK.executeSync(request)

            withContext(Dispatchers.Default) {
                val postsJsonArray = result.getJSONObject("response").getJSONArray("items")
                if (postsJsonArray.length() > 0) {
                    for (i in 0 until postsJsonArray.length()) {
                        val date = (postsJsonArray[i] as JSONObject).getLong("date")
                        if (date > timeLine) {
                            val wallPost = WallPost()
                            wallPost.date = date
                            wallPost.id = (postsJsonArray[i] as JSONObject).getLong("id")
                            wallPost.like = (postsJsonArray[i] as JSONObject).getJSONObject("likes")
                                .getInt("user_likes")
                            wallPost.repost =
                                (postsJsonArray[i] as JSONObject).getJSONObject("reposts")
                                    .getInt("user_reposted")
                            wallPostList.add(wallPost)
                        } else {
                            needMorePosts = false
                            break
                        }
                    }
                } else {
                    needMorePosts = false
                }
                currOffset += 100
            }
        }

        return wallPostList
    }


    /**
     * To much long parsing in runtime, use mock for get quick result
     */
    private suspend fun getCommentsCount(groupId: Long, wallPostsData: List<WallPost>): Int {
        var commentsCount = 0

        val currUserId = (VK.executeSync(VKRequest<JSONObject>("users.get"))
            .getJSONArray("response")[0] as JSONObject).getLong("id")

        wallPostsData.forEach { post ->
            var needMoreLoad = true
            var currOffset = 0
            while (needMoreLoad) {
                val request = VKRequest<JSONObject>("wall.getComments")
                    .addParam("owner_id", groupId * -1)
                    .addParam("post_id", post.id)
                    .addParam("offset", currOffset)
                val commentsJsonArray =
                    VK.executeSync(request).getJSONObject("response").getJSONArray("items")

                withContext(Dispatchers.Default) {
                    for (i in 0 until commentsJsonArray.length()) {
                        if ((commentsJsonArray[i] as JSONObject).getLong("from_id") == currUserId)
                            commentsCount++
                    }
                    if (commentsJsonArray.length() < 100) {
                        needMoreLoad = false
                    } else {
                        currOffset += 100
                    }
                }
            }
        }

        return commentsCount
    }

    private fun getCommentsCountMock(): Int = 0
}