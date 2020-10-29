package com.grind.vksociety.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MainFragmentPagerAdapter(fm: FragmentManager,
                               behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT): FragmentPagerAdapter(fm, behavior) {

    private val fragmentList = mutableListOf<Fragment>()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Категории"
            1 -> "Все сообщества"
            2 -> "Для вас"
            else -> "Undefined"
        }
    }

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
    }


}