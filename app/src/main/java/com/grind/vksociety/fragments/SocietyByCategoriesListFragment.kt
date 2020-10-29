package com.grind.vksociety.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grind.vksociety.App
import com.grind.vksociety.R
import com.grind.vksociety.adapters.SocietyByCategoryAdapter
import com.grind.vksociety.adapters.SocietyListAdapter
import com.grind.vksociety.models.Society
import com.grind.vksociety.viewmodels.SocietyByCategoryViewModel

class SocietyByCategoriesListFragment : Fragment(){

    private val listForUnsubscribe = mutableListOf<Long>()
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
        val v = View.inflate(context, R.layout.fragmetn_society_list, null)
        rv = v.findViewById(R.id.rv)
        unsubscribeButton = v.findViewById(R.id.tv_unsubscribe_button)

        initAdapter()
        initRecyclerView()

        unsubscribeButton.setOnClickListener {
//            viewModel.unsubscribeGroups(listForUnsubscribe)
//            adapter?.selectedList?.clear()
            unsubscribeButton.visibility = View.GONE
        }

        viewModel.groupsByCategoriesData.observe({lifecycle}, {
            adapter.itemsList = it.toList().sortedByDescending { it.second.size }
            adapter.notifyDataSetChanged()
        })

        viewModel.getGroupsByCategories()



        return v
    }

    private fun initAdapter(){
        adapter = SocietyByCategoryAdapter(/*object : SocietyListAdapter.OnItemClickListener {
            override fun onItemClick(currSociety: Society) {
                fragmentManager!!.beginTransaction()
                    .add(R.id.main_container, SocietyInfoFragment()
                        .apply {
                            arguments = Bundle().apply { putParcelable("item", currSociety) }
                        })
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(this.javaClass.simpleName)
                    .commit()
            }

            override fun onLongItemClick(societyId: Long) {
                if (listForUnsubscribe.contains(societyId)) {
                    listForUnsubscribe.remove(societyId)
                } else {
                    listForUnsubscribe.add(societyId)
                }
                if (listForUnsubscribe.isNotEmpty()) {
                    unsubscribeButton.visibility = View.VISIBLE
                } else {
                    unsubscribeButton.visibility = View.GONE
                }
            }
        }*/)

    }

    private fun initRecyclerView(){
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter
//        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (!rv.canScrollVertically(1)) {
//                    viewModel.reduceAction(
//                        Action.NewPage<Society>(
//                            adapter.itemCount,
//                            adapter.getItems()
//                        )
//                    )
//                }
//            }
//        })
    }
}