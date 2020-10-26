package com.grind.vksociety.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.grind.vksociety.R

class MyActivityFragment: Fragment() {
    private lateinit var backButton: ImageView
    private lateinit var communityName: TextView
    private lateinit var lastActivity: TextView
    private lateinit var likesCount: TextView
    private lateinit var commentsCount: TextView
    private lateinit var repostsCount: TextView
    private lateinit var conclusion: LinearLayout
    private lateinit var actionButton: TextView

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

        return v
    }
}