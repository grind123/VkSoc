package com.grind.vksociety.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.grind.vksociety.App
import com.grind.vksociety.R
import com.grind.vksociety.adapters.AllGroupOfCategoryListAdapter
import com.grind.vksociety.adapters.SocietyListAdapter
import com.grind.vksociety.custom.DisabledScrollGridLayoutManager
import com.grind.vksociety.models.Society
import com.grind.vksociety.adapters.diffutils.SocietyDiffUtilCallback
import com.grind.vksociety.viewmodels.MainViewModel

class AllGroupsOfCategoryFragment(
    private val itemPosition: Int,
    private val itemsList: List<Society>,
    private val notifier: AllGroupOfCategoryListAdapter.ItemNotifier
) : Fragment() {

    private lateinit var backButton: ImageView
    private lateinit var abTitle: TextView
    private lateinit var cancelButton: TextView
    private lateinit var rv: RecyclerView
    private lateinit var adapter: AllGroupOfCategoryListAdapter
    private lateinit var unsubscribeButton: TextView
    private lateinit var lm: DisabledScrollGridLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = View.inflate(context, R.layout.fragment_all_groups_of_categories, null)
        backButton = v.findViewById(R.id.imv_back)
        abTitle = v.findViewById(R.id.tv_ab_title)
        cancelButton = v.findViewById(R.id.tv_ab_cancel_button)
        rv = v.findViewById(R.id.rv)
        unsubscribeButton = v.findViewById(R.id.tv_unsubscribe_button)

        backButton.setOnClickListener { fragmentManager?.popBackStack() }
        abTitle.text = itemsList.first().activity

        initAdapter()
        initRecyclerView()
        initListeners()

        return v
    }

    private fun initListeners() {
        unsubscribeButton.setOnClickListener {
            ViewModelProvider(
                App.mainViewModelStore,
                ViewModelProvider.AndroidViewModelFactory(App())
            ).get(MainViewModel::class.java)
                .unsubscribeGroups(adapter.selectedItemsList)
            val newList = adapter.getItems().filter { !adapter.selectedItemsList.contains(it.id) }
                .toMutableList()
            val callback = SocietyDiffUtilCallback(adapter.getItems(), newList)
            adapter.setItems(newList)
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(adapter)
            adapter.selectedItemsList.clear()
            unsubscribeButton.visibility = View.GONE
            changeActionBarBySelectedItemsCount(adapter.selectedItemsList.size)
        }

        cancelButton.setOnClickListener {
            clearAllSelectedGroups()
            changeActionBarBySelectedItemsCount(adapter.selectedItemsList.size)
        }
    }


    private fun initAdapter() {
        adapter =
            AllGroupOfCategoryListAdapter(object :
                AllGroupOfCategoryListAdapter.OnItemClickListener {
                override fun onItemClick(society: Society) {
                    addFragment(R.id.main_container, SocietyInfoFragment().apply {
                        arguments = Bundle().apply {
                            putInt("mode", SocietyInfoFragment.WITH_MY_ACTIVITY_MODE)
                            putParcelable("item", society)
                        }
                    })
                }
            },
                object : SocietyListAdapter.OnGroupItemLongClickListener {
                    override fun onLongItemClick(societyId: Long, selectedItemsCount: Int) {
                        changeActionBarBySelectedItemsCount(selectedItemsCount)
                        if (selectedItemsCount > 0) {
                            unsubscribeButton.visibility = View.VISIBLE
                        } else {
                            unsubscribeButton.visibility = View.GONE
                        }
                    }
                })
        adapter.setItems(itemsList.toMutableList())

    }

    private fun initRecyclerView() {
        lm = DisabledScrollGridLayoutManager(context, 3)
        rv.layoutManager = lm
        rv.adapter = adapter
    }

    private fun changeActionBarBySelectedItemsCount(count: Int){
        if(count > 0){
            abTitle.text = "Выбрано: $count"
            cancelButton.visibility = View.VISIBLE
        } else {
            abTitle.text = itemsList.first().activity
            cancelButton.visibility = View.GONE
        }

    }


    fun clearAllSelectedGroups() {
        adapter.clearAllSelectedItems()
        unsubscribeButton.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        notifier.onNeedNotifyItem(itemPosition, adapter.getItems())
    }

}