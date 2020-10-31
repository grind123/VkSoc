package com.grind.vksociety

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.grind.vksociety.fragments.IntroFragment
import com.grind.vksociety.fragments.MainFragment
import com.grind.vksociety.fragments.SocietyListFragment
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.utils.VKUtils.getDisplayMetrics


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getDisplayMetrics()

        if (savedInstanceState == null) {
            VK.login(this, listOf(VKScope.GROUPS, VKScope.WALL))
            VK.setConfig(VKApiConfig(context = applicationContext, lang = "ru", validationHandler = null))
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!VK.onActivityResult(requestCode, resultCode, data, object :
                VKAuthCallback {

                override fun onLogin(token: VKAccessToken) {
                    App.vkAccessKey = token.accessToken
                    supportFragmentManager.beginTransaction()
                        .add(R.id.main_container, MainFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commitAllowingStateLoss()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.main_container, IntroFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(IntroFragment::class.java.simpleName)
                        .commitAllowingStateLoss()
                }

                override fun onLoginFailed(errorCode: Int) {
                    Toast.makeText(App.appContext, "Auth error", Toast.LENGTH_SHORT).show()
                }

            })) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getDisplayMetrics() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        App.screenHeight = metrics.heightPixels
        App.screenWidth = metrics.widthPixels
    }

}
