package com.grind.vksociety.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grind.vksociety.R
import com.grind.vksociety.adapters.SocietyListAdapter
import com.grind.vksociety.models.Society
import com.grind.vksociety.presenters.SocietyListPresenter
import com.grind.vksociety.utils.ItemOffsetDecoration
import com.grind.vksociety.views.ISocietyListView

class SocietyListFragment: Fragment(), ISocietyListView {

    private val presenter = SocietyListPresenter(this)
    private lateinit var rv: RecyclerView
    private val adapter = SocietyListAdapter(object: SocietyListAdapter.OnItemClickListener{
        override fun onItemClick(currSociety: Society) {
            fragmentManager!!.beginTransaction()
                .add(R.id.main_container, SocietyInfoFragment()
                    .apply { arguments = Bundle().apply { putSerializable("item", currSociety) } })
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(this.javaClass.simpleName)
                .commit()
        }

    })
    private val lm = GridLayoutManager(context, 3)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = View.inflate(context, R.layout.fragmetn_society_list, null)
        rv = v.findViewById(R.id.rv)
        return v
    }

    override fun onStart() {
        super.onStart()
        presenter.getSocietyList()
    }

    override fun onListPresent(items: List<Society>) {

        rv.addItemDecoration(ItemOffsetDecoration(8))
        rv.layoutManager = lm
        rv.adapter = adapter
        adapter.setItems(items)
    }
}