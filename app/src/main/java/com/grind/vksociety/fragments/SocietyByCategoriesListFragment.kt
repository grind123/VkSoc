package com.grind.vksociety.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grind.vksociety.App
import com.grind.vksociety.R
import com.grind.vksociety.adapters.SocietyByCategoryAdapter
import com.grind.vksociety.adapters.SocietyListAdapter
import com.grind.vksociety.viewmodels.SocietyByCategoryViewModel

class SocietyByCategoriesListFragment(private val listener: OnGroupItemsListener) : Fragment(){

    private lateinit var viewModel: SocietyByCategoryViewModel

    private lateinit var rv: RecyclerView
    private lateinit var adapter: SocietyByCategoryAdapter
    private lateinit var unsubscribeButton: TextView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(App())).get(SocietyByCategoryViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = View.inflate(context, R.layout.fragment_society_list, null)
        rv = v.findViewById(R.id.rv)
        unsubscribeButton = v.findViewById(R.id.tv_unsubscribe_button)

        adapter = initAdapter()
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter

        unsubscribeButton.setOnClickListener {
            listener.unsubscribe(adapter.selectedItemsList.toList())
            unsubscribeNotifyAdapter(adapter)
            listener.onSelectItemsCountChanged(adapter.selectedItemsList.size)
            unsubscribeButton.visibility = View.GONE
        }

        viewModel.groupsByCategoriesData.observe({lifecycle}, {
            adapter.itemsList = it.toList().sortedByDescending { it.second.size }
            adapter.notifyDataSetChanged()
        })

        viewModel.getGroupsByCategories()



        return v
    }

    private fun initAdapter(): SocietyByCategoryAdapter{
        return SocietyByCategoryAdapter(listener, object : SocietyListAdapter.OnGroupItemLongClickListener {
            override fun onLongItemClick(societyId: Long, selectedItemsCount: Int) {
                if (selectedItemsCount > 0) {
                    unsubscribeButton.visibility = View.VISIBLE
                } else {
                    unsubscribeButton.visibility = View.GONE
                }
            }
        })


    }

    fun clearAllSelectedGroups() {
        adapter.clearAllSelectedItems()
        unsubscribeButton.visibility = View.GONE
        listener.onSelectItemsCountChanged(adapter.selectedItemsList.size)
    }

    private fun unsubscribeNotifyAdapter(adapter: SocietyByCategoryAdapter){
        val selectedItemsList = adapter.selectedItemsList
//        val oldList = adapter.itemsList.toList()
        adapter.itemsList.forEach { pair ->
            val filter = pair.second.filter { selectedItemsList.contains(it.id) }
            pair.second.removeAll(filter)
        }
        adapter.selectedItemsList.clear()
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
//        val callback =
//            SocietyByCategoryDiffUtilCallback(oldList, adapter.itemsList)
//        DiffUtil.calculateDiff(callback).dispatchUpdatesTo(adapter)
    }
}