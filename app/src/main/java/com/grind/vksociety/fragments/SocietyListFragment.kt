package com.grind.vksociety.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grind.vksociety.R
import com.grind.vksociety.adapters.SocietyListAdapter
import com.grind.vksociety.models.Society
import com.grind.vksociety.presenters.SocietyListPresenter
import com.grind.vksociety.redux.Action
import com.grind.vksociety.utils.ItemOffsetDecoration
import com.grind.vksociety.utils.SocietyDiffUtilCallback
import com.grind.vksociety.views.ISocietyListView

class SocietyListFragment : Fragment(), ISocietyListView {

    private val listForUnsubscribe = mutableListOf<Long>()
    private val presenter = SocietyListPresenter(this)
    private lateinit var rv: RecyclerView
    private lateinit var adapter: SocietyListAdapter
    private lateinit var unsubscribeButton: TextView
    private val lm = GridLayoutManager(context, 3)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = View.inflate(context, R.layout.fragmetn_society_list, null)
        rv = v.findViewById(R.id.rv)
        unsubscribeButton = v.findViewById(R.id.tv_unsubscribe_button)

        adapter = SocietyListAdapter(object : SocietyListAdapter.OnItemClickListener {
            override fun onItemClick(currSociety: Society) {
                fragmentManager!!.beginTransaction()
                    .add(R.id.main_container, SocietyInfoFragment()
                        .apply {
                            arguments = Bundle().apply { putSerializable("item", currSociety) }
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
        })
        rv.addItemDecoration(ItemOffsetDecoration(8))
        rv.layoutManager = lm
        rv.adapter = adapter
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!rv.canScrollVertically(1)){
                    presenter.reduceAction(Action.NewPage<Society>(adapter.itemCount, adapter.getItems()))
                }
            }
        })
        return v
    }

    override fun onStart() {
        super.onStart()
        presenter.reduceAction(Action.NewPage<Society>(0, listOf()))
        unsubscribeButton.setOnClickListener {
            presenter.unsubscribeGroups(listForUnsubscribe)
            adapter.selectedList.clear()
            unsubscribeButton.visibility = View.GONE
        }
    }

    override fun onListPresent(items: List<Society>) {
        listForUnsubscribe.clear()
        val newList = mutableListOf<Society>()
        newList.addAll(adapter.getItems())
        newList.addAll(items)
        val callback = SocietyDiffUtilCallback(adapter.getItems(), newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        adapter.setItems(newList)
        diffResult.dispatchUpdatesTo(adapter)
    }
}