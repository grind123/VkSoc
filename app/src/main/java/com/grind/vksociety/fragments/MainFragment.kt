package com.grind.vksociety.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.grind.vksociety.R
import com.grind.vksociety.adapters.MainFragmentPagerAdapter

class MainFragment: Fragment() {

    private lateinit var abTitle: TextView
    private lateinit var cancelButton: TextView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = View.inflate(context, R.layout.main_fragment, null)
        abTitle = v.findViewById(R.id.tv_ab_title)
        cancelButton = v.findViewById(R.id.tv_ab_cancel_button)
        tabLayout = v.findViewById(R.id.tab_layout)
        viewPager = v.findViewById(R.id.view_pager)

        val adapter = MainFragmentPagerAdapter(fragmentManager!!).apply {
            addFragment(SocietyListFragment())
            addFragment(SocietyListFragment())
            addFragment(SocietyListFragment())
        }
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)


        return v
    }


}