package com.grind.vksociety.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grind.vksociety.App
import com.grind.vksociety.R
import com.grind.vksociety.adapters.RecommendListAdapter
import com.grind.vksociety.viewmodels.RecommendListViewModel

class RecommendListFragment(private val listener: OnGroupItemsListener) : Fragment(){

    private lateinit var viewModel: RecommendListViewModel

    private lateinit var emptyListFrame: ConstraintLayout
    private lateinit var rv: RecyclerView
    private lateinit var adapter: RecommendListAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(App.recommendListViewModelStore, ViewModelProvider.AndroidViewModelFactory(App())).get(RecommendListViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = View.inflate(context, R.layout.fragment_recommend_list, null)
        rv = v.findViewById(R.id.rv)
        emptyListFrame = v.findViewById(R.id.cl_empty_list_frame)

        adapter = RecommendListAdapter(listener)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter

        viewModel.recommendListData.observe({lifecycle}, {
            if(emptyListFrame.visibility == View.VISIBLE){
                emptyListFrame.visibility = View.GONE
            }
            adapter.itemsList = it.toList()
            adapter.notifyDataSetChanged()
        })



        return v
    }

}