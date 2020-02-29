package com.grind.vksociety.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.grind.vksociety.R
import com.grind.vksociety.models.Society
import com.grind.vksociety.presenters.SocietyInfoPresenter
import com.grind.vksociety.views.ISocietyInfoView


class SocietyInfoFragment: Fragment(), ISocietyInfoView {

    private lateinit var name: TextView
    private lateinit var subsCount: TextView
    private lateinit var description: TextView
    private lateinit var lastPost: TextView
    private lateinit var openButton: TextView
    private lateinit var dismissButton: ImageView
    private lateinit var bgAlpha: View

    private val presenter = SocietyInfoPresenter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = View.inflate(context, R.layout.fragment_info, null)
        name = v.findViewById(R.id.tv_name)
        subsCount = v.findViewById(R.id.tv_subs_count)
        description = v.findViewById(R.id.tv_desc)
        lastPost = v.findViewById(R.id.tv_last_post)
        openButton = v.findViewById(R.id.tv_open_button)
        dismissButton = v.findViewById(R.id.imv_dismiss)
        bgAlpha = v.findViewById(R.id.bg_alpha)
        return v
    }

    override fun onStart() {
        super.onStart()
        presenter.getSocietyInfo(arguments?.getSerializable("item") as Society)
        dismissButton.setOnClickListener { fragmentManager?.popBackStack() }
        bgAlpha.setOnTouchListener { t, event -> true }
    }

    override fun onInfoPresent(name: String, subsCount: String, desc: String, lastPost: String, url: String) {
        activity?.runOnUiThread {
            this.name.text = name
            this.subsCount.text = subsCount
            this.description.text = desc
            this.lastPost.text = lastPost
            openButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
    }
}