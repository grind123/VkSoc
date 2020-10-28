package com.grind.vksociety.models

import com.google.gson.annotations.SerializedName

data class WallPost(var id: Long = 0,
                    var date: Long = 0,
                    var like: Int = 0, // 0 - нет лайка, 1 - есть лайк
                    var repost: Int = 0 // 0 - репост не сделан, 1 - репост сделан
     )
