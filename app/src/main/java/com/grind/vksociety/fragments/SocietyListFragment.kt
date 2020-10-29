package com.grind.vksociety.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.grind.vksociety.App
import com.grind.vksociety.R
import com.grind.vksociety.adapters.SocietyListAdapter
import com.grind.vksociety.custom.DisabledScrollGridLayoutManager
import com.grind.vksociety.models.Society
import com.grind.vksociety.redux.Action
import com.grind.vksociety.utils.SocietyDiffUtilCallback
import com.grind.vksociety.viewmodels.SocietyListViewModel

class SocietyListFragment(private val listener: OnGroupItemsListener) : Fragment() {
    private lateinit var viewModel: SocietyListViewModel

    private lateinit var rv: RecyclerView
    private lateinit var adapter: SocietyListAdapter
    private lateinit var unsubscribeButton: TextView
    private lateinit var lm: DisabledScrollGridLayoutManager

//    private val slideAndPickTouchListener: View.OnTouchListener = object : View.OnTouchListener{
//        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//            when(event?.action){
//                MotionEvent.ACTION_DOWN -> {
//                    Log.i("Coordinates", "down x = ${event.x}; y = ${event.y}")
//                    return false
//                }
//                MotionEvent.ACTION_MOVE -> {
////                    (rv.layoutManager as DisabledScrollGridLayoutManager).isDisabledScroll = true
//                    Log.i("Coordinates", "move x = ${event.x}; y = ${event.y}")
//                    highlightView(event.x, event.y, adapter.displayedViews)
//                    return false
//                }
//                MotionEvent.ACTION_UP -> {
//                    (rv.layoutManager as DisabledScrollGridLayoutManager).isDisabledScroll = false
//                    rv.setOnTouchListener(defaultTouchListener)
//                    return false
//                }
//                else -> return false
//            }
//        }
//
//    }
//
//    private val defaultTouchListener: View.OnTouchListener = object : View.OnTouchListener{
//        var isFirstMove = true
//        var startX = 0f
//        var startY = 0f
//        var delta = 200f
//        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//            when(event?.action){
//                MotionEvent.ACTION_DOWN -> {
//                    Log.i("Coordinates", "down x = ${event.x}; y = ${event.y}")
//                    return false
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    Log.i("Coordinates", "every x = ${event.x}; y = ${event.y}")
//                    if(isFirstMove){
//                        Log.i("Coordinates", "x = ${event.x}; y = ${event.y}")
//                        startX = event.x
//                        startY = event.y
//                        isFirstMove = false
//                    } else {
//                        if(startX + delta < event.x || startX > event.x + delta){ // horizontal move
//                            (rv.layoutManager as DisabledScrollGridLayoutManager).isDisabledScroll = true
//                            isFirstMove = true
//                            rv.setOnTouchListener(slideAndPickTouchListener)
//                        }
//                    }
//                    return false
//                }
//                MotionEvent.ACTION_UP -> {
//                    isFirstMove = true
//                    return false
//                }
//                else -> return false
//            }
//        }
//
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(App())
        ).get(SocietyListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = View.inflate(context, R.layout.fragment_society_list, null)
        rv = v.findViewById(R.id.rv)
        unsubscribeButton = v.findViewById(R.id.tv_unsubscribe_button)

        initAdapter()
        initRecyclerView()
        initListeners()


        viewModel.societyListData.observe({ lifecycle }, {
//            listForUnsubscribe.clear()
            val newList = mutableListOf<Society>()
            newList.addAll(adapter.getItems())
            newList.addAll(it)
            val callback = SocietyDiffUtilCallback(adapter.getItems(), newList)
            val diffResult = DiffUtil.calculateDiff(callback)
            adapter.setItems(newList)
            diffResult.dispatchUpdatesTo(adapter)
        })

        viewModel.reduceAction(Action.NewPage<Society>(0, listOf()))

        return v
    }

    private fun initListeners() {
        unsubscribeButton.setOnClickListener {
            listener.unsubscribe(adapter.selectedItemsList.toList())
            val newList = adapter.getItems().filter { !adapter.selectedItemsList.contains(it.id) }.toMutableList()
            val callback = SocietyDiffUtilCallback(adapter.getItems(), newList)
            adapter.setItems(newList)
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(adapter)
            adapter.selectedItemsList.clear()
            listener.onSelectItemsCountChanged(adapter.selectedItemsList.size)
            unsubscribeButton.visibility = View.GONE

        }
    }


    private fun initAdapter() {
        adapter =
            SocietyListAdapter(listener, object : SocietyListAdapter.OnGroupItemLongClickListener {
                override fun onLongItemClick(societyId: Long, selectedItemsCount: Int) {
                    if (selectedItemsCount > 0) {
                        unsubscribeButton.visibility = View.VISIBLE
                    } else {
                        unsubscribeButton.visibility = View.GONE
                    }
                }
            })

    }

    private fun initRecyclerView() {
        lm = DisabledScrollGridLayoutManager(context, 3)
        rv.layoutManager = lm
        rv.adapter = adapter
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!rv.canScrollVertically(1)) {
                    viewModel.reduceAction(
                        Action.NewPage<Society>(
                            adapter.itemCount,
                            adapter.getItems()
                        )
                    )
                }
            }
        })
    }

    fun clearAllSelectedGroups() {
        adapter.clearAllSelectedItems()
        unsubscribeButton.visibility = View.GONE
        listener.onSelectItemsCountChanged(adapter.selectedItemsList.size)
    }

    override fun onStop() {
        super.onStop()
        Log.i("Lifecycle", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Lifecycle", "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("Lifecycle", "onDetach")
    }
}