package com.grind.vksociety.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.grind.vksociety.App
import com.grind.vksociety.R
import com.grind.vksociety.adapters.MainFragmentPagerAdapter
import com.grind.vksociety.models.Society
import com.grind.vksociety.viewmodels.MainViewModel

fun Fragment.addFragment(container: Int, fragment: Fragment){
    fragmentManager!!.beginTransaction()
        .add(container, fragment)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        .addToBackStack(this.javaClass.simpleName)
        .commit()

}

class MainFragment: Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var abTitle: TextView
    private lateinit var cancelButton: TextView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private var selectedItemsCountOnFragment1 = 0
    private var selectedItemsCountOnFragment2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(App.mainViewModelStore, ViewModelProvider.AndroidViewModelFactory(App())).get(MainViewModel::class.java)
    }

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
            addFragment(SocietyByCategoriesListFragment(object : OnGroupItemsListener{
                override fun unsubscribe(listOfIds: List<Long>) {
                    viewModel.unsubscribeGroups(listOfIds)
                }

                override fun onSelectItemsCountChanged(count: Int) {
                    selectedItemsCountOnFragment1 = count
                    changeActionBarBySelectedItemsCount(selectedItemsCountOnFragment1)
                }

                override fun onItemClick(society: Society) {
                    addFragment(R.id.main_container, SocietyInfoFragment()
                        .apply {
                            arguments = Bundle().apply {
                                putParcelable("item", society)
                                putInt("mode", SocietyInfoFragment.WITH_MY_ACTIVITY_MODE )
                            }
                        })
                }

            }))

            addFragment(SocietyListFragment(object : OnGroupItemsListener{
                override fun unsubscribe(listOfIds: List<Long>) {
                    viewModel.unsubscribeGroups(listOfIds)
                }

                override fun onSelectItemsCountChanged(count: Int) {
                    selectedItemsCountOnFragment2 = count
                    changeActionBarBySelectedItemsCount(selectedItemsCountOnFragment2)
                }

                override fun onItemClick(society: Society) {
                    addFragment(R.id.main_container, SocietyInfoFragment()
                        .apply {
                            arguments = Bundle().apply {
                                putParcelable("item", society)
                                putInt("mode", SocietyInfoFragment.WITH_MY_ACTIVITY_MODE )
                            }
                        })
                }

            }))

            addFragment(RecommendListFragment(object : OnGroupItemsListener{
                override fun unsubscribe(listOfIds: List<Long>) {
                    //Not need implemented
                }

                override fun onSelectItemsCountChanged(count: Int) {
                    //Not need implemented
                }

                override fun onItemClick(society: Society) {
                    addFragment(R.id.main_container, SocietyInfoFragment()
                        .apply {
                            arguments = Bundle().apply {
                                putParcelable("item", society)
                                putInt("mode", SocietyInfoFragment.WITHOUT_MY_ACTIVITY_MODE )
                            }
                        })
                }
            }))
        }

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object :ViewPager.SimpleOnPageChangeListener(){
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> changeActionBarBySelectedItemsCount(selectedItemsCountOnFragment1)
                    1 -> changeActionBarBySelectedItemsCount(selectedItemsCountOnFragment2)
                    2 -> {
                        abTitle.text = "Рекомендации"
                        cancelButton.visibility = View.GONE
                    }
                }
            }
        })

        initListeners()

        return v
    }

    private fun initListeners(){
        cancelButton.setOnClickListener {
            val fragment =
                (viewPager.adapter as MainFragmentPagerAdapter).getItem(viewPager.currentItem)
            when(fragment){
                is SocietyListFragment -> fragment.clearAllSelectedGroups()
                is SocietyByCategoriesListFragment -> fragment.clearAllSelectedGroups()
            }
        }
    }

    private fun changeActionBarBySelectedItemsCount(count: Int){
        if(count > 0){
            abTitle.text = "Выбрано: $count"
            cancelButton.visibility = View.VISIBLE
        } else {
            abTitle.text = "Отписаться"
            cancelButton.visibility = View.GONE
        }

    }


}