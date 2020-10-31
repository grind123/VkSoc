package com.grind.vksociety.fragments

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.grind.vksociety.App
import com.grind.vksociety.R
import com.grind.vksociety.models.MyActivity
import com.grind.vksociety.models.Society
import com.grind.vksociety.models.toDateFormat
import com.grind.vksociety.viewmodels.MyActivityViewModel

class MyActivityFragment: Fragment() {
    private lateinit var viewModel: MyActivityViewModel
    private lateinit var backButton: ImageView
    private lateinit var communityName: TextView
    private lateinit var lastActivity: TextView
    private lateinit var likesCount: TextView
    private lateinit var commentsCount: TextView
    private lateinit var repostsCount: TextView
    private lateinit var conclusion: LinearLayout
    private lateinit var actionButton: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(App())).get(MyActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = View.inflate(context, R.layout.fragment_my_activity, null)
        backButton = v.findViewById(R.id.imv_back)
        communityName = v.findViewById(R.id.tv_name)
        lastActivity = v.findViewById(R.id.tv_my_activity)
        likesCount = v.findViewById(R.id.tv_likes)
        commentsCount = v.findViewById(R.id.tv_comments)
        repostsCount = v.findViewById(R.id.tv_reposts)
        conclusion = v.findViewById(R.id.ll_conclusion)
        actionButton = v.findViewById(R.id.tv_action_button)

        backButton.setOnClickListener { fragmentManager?.popBackStack() }

        viewModel.myActivityData.observe({lifecycle},{
            setMyActivityData(it)
        })

        viewModel.getMyActivity(arguments?.getParcelable("item")!!)
        return v
    }

    private fun setMyActivityData(myActivity: MyActivity){
        communityName.text = "Название: ${myActivity.groupName}"
        if(myActivity.lastActivityDate > 0){
            lastActivity.text = "Ваша последняя активность: ${(myActivity.lastActivityDate * 1000).toDateFormat()}"
        } else {
            lastActivity.text = "Активность отсутствует"
        }

        likesCount.text = "Лайки: ${myActivity.likesCount}"
        commentsCount.text = "Комментарии: ${myActivity.commentsCount}"
        repostsCount.text = "Репосты: ${myActivity.repostsCount}"
        configureConclusive(myActivity.activityLevel)
        configureActionButton(myActivity.activityLevel)

    }

    private fun configureConclusive(activityLevel: Int){
        val title: TextView = conclusion.findViewById(R.id.tv_conclusion_title)
        val desc: TextView = conclusion.findViewById(R.id.tv_conclusive_desc)
        when(activityLevel){
            MyActivity.LOW_LEVEL_ACTIVITY -> {
                title.text = getString(R.string.low_activity_title)
                title.setTextColor(ContextCompat.getColor(context!!, android.R.color.black))
                desc.text = getString(R.string.low_activity_desc)
                desc.setTextColor(ContextCompat.getColor(context!!, R.color.light_text))
                conclusion.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.light_background))
            }
            MyActivity.HIGH_LEVEL_ACTIVITY -> {
                title.text = getString(R.string.high_activity_title)
                title.setTextColor(ContextCompat.getColor(context!!, R.color.green_text))
                desc.text = getString(R.string.high_activity_desc)
                desc.setTextColor(ContextCompat.getColor(context!!, R.color.green_text))
                conclusion.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.light_green_bg))
            }
        }
        conclusion.visibility = View.VISIBLE
    }

    private fun configureActionButton(activityLevel: Int){
        when(activityLevel){
            MyActivity.LOW_LEVEL_ACTIVITY -> {
                actionButton.text = "Отписаться"
                actionButton.setOnClickListener {
                    fragmentManager?.popBackStack()
                    fragmentManager?.popBackStack()
                    Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show()
                }
            }
            MyActivity.HIGH_LEVEL_ACTIVITY -> {
                actionButton.text = "Перейти в сообщество"
                actionButton.setOnClickListener {
                    val currSociety = arguments?.getParcelable<Society>("item")
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://vk.com/${currSociety?.screenName}")
                    startActivity(intent)
                }
            }
        }
        actionButton.visibility = View.VISIBLE
    }
}