package com.grind.vksociety.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.grind.vksociety.R

class IntroFragment: Fragment() {

    private lateinit var dismissButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = View.inflate(context, R.layout.fragment_intro, null)
        dismissButton = v.findViewById(R.id.tv_dismiss_button)
        dismissButton.setOnClickListener { fragmentManager?.popBackStack() }
        return v
    }


}