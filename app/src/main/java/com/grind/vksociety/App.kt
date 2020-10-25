package com.grind.vksociety

import android.app.Application
import android.content.Context
import com.vk.api.sdk.VK

class App : Application() {
    companion object{
        var appContext: Context? = null
        const val vkAccessKey: String = "8d0688738d0688738d0688731b8d6971a388d068d068873d35e60d255e68e5597057f21"
        var screenHeight = 0
        var screenWidth = 0
    }


    override fun onCreate() {
        super.onCreate()
        appContext = this
        VK.initialize(this)
    }

}