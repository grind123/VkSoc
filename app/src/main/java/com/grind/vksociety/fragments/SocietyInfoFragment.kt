package com.grind.vksociety.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.grind.vksociety.App
import com.grind.vksociety.R
import com.grind.vksociety.models.Friend
import com.grind.vksociety.models.Society
import com.grind.vksociety.models.dateFormat
import com.grind.vksociety.models.membersCountFormat
import com.grind.vksociety.viewmodels.SocietyInfoViewModel


class SocietyInfoFragment: Fragment(){

    private lateinit var viewModel: SocietyInfoViewModel
    private lateinit var name: TextView
    private lateinit var description: TextView
    private lateinit var subsCount: TextView
    private lateinit var lastPost: TextView
    private lateinit var myActivity: TextView
    private lateinit var openButton: TextView
    private lateinit var dismissButton: ImageView
    private lateinit var bgAlpha: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(App())).get(SocietyInfoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = View.inflate(context, R.layout.fragment_info, null)
        name = v.findViewById(R.id.tv_name)
        description = v.findViewById(R.id.tv_desc)
        subsCount = v.findViewById(R.id.tv_subs_count)
        lastPost = v.findViewById(R.id.tv_last_post)
        myActivity = v.findViewById(R.id.tv_my_activity)
        openButton = v.findViewById(R.id.tv_open_button)
        dismissButton = v.findViewById(R.id.imv_close)
        bgAlpha = v.findViewById(R.id.bg_alpha)

        viewModel.societyInfoData.observe({lifecycle}, {info ->
            activity?.runOnUiThread {
                this.name.text = info.name
                if(info.desc.isNotBlank()){
                    this.description.text = info.desc
                    (view?.findViewById(R.id.ic2) as View).visibility = View.VISIBLE
                    this.description.visibility = View.VISIBLE
                    this.description.setOnClickListener {
                        (it as TextView).maxLines = Int.MAX_VALUE
                        it.ellipsize = null
                    }
                }

                this.subsCount.text = "${info.membersCountFormat(info.membersCount)} подписчиков • ${info.friendsInSociety.size} друзей"
                this.lastPost.text = "Последняя запись ${info.dateFormat(info.lastPostDate)}"
                loadFriendsAvatars(info.friendsInSociety)
                myActivity.setOnClickListener {
                    //TODO next fragment
                }
                openButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(info.url)
                    startActivity(intent)
                }
            }

        })

        viewModel.getSocietyInfo(arguments?.getSerializable("item") as Society)
        return v
    }

    private fun initListeners(){
        dismissButton.setOnClickListener { fragmentManager?.popBackStack() }
        bgAlpha.setOnClickListener{fragmentManager?.popBackStack()}
    }

    override fun onStart() {
        super.onStart()
        initListeners()
    }

    private fun loadFriendsAvatars(friendList: List<Friend>){
        if(friendList.isNullOrEmpty()) return
        if(friendList.isNotEmpty())
            Glide.with(this).load(friendList[0].avatarUrl).into(view!!.findViewById(R.id.imv_friend_1))
        if(friendList.size > 1)
            Glide.with(this).load(friendList[1].avatarUrl).into(view!!.findViewById(R.id.imv_friend_2))
        if(friendList.size > 2)
            Glide.with(this).load(friendList[2].avatarUrl).into(view!!.findViewById(R.id.imv_friend_3))
    }
}