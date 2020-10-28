package com.grind.vksociety.fragments

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grind.vksociety.App
import com.grind.vksociety.R
import com.grind.vksociety.adapters.SocietyListAdapter
import com.grind.vksociety.custom.DisabledScrollGridLayoutManager
import com.grind.vksociety.getScreenWidth
import com.grind.vksociety.models.Society
import com.grind.vksociety.redux.Action
import com.grind.vksociety.utils.ItemOffsetDecoration
import com.grind.vksociety.utils.SocietyDiffUtilCallback
import com.grind.vksociety.viewmodels.SocietyListViewModel

class SocietyListFragment : Fragment(){

    private val listForUnsubscribe = mutableListOf<Long>()
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
        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(App())).get(SocietyListViewModel::class.java)
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
            viewModel.unsubscribeGroups(listForUnsubscribe)
            adapter.selectedList.clear()
            unsubscribeButton.visibility = View.GONE
        }

        viewModel.societyListData.observe({lifecycle}, {
            listForUnsubscribe.clear()
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

    private fun initAdapter(){
        adapter = SocietyListAdapter(object : SocietyListAdapter.OnItemClickListener {
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
        })

    }

    private fun initRecyclerView(){
//        rv.addItemDecoration(ItemOffsetDecoration(0))
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

    private fun highlightView(x: Float, y: Float, data: List<View>){
        data.forEach {
            val rect = Rect(it.left, it.top, it.right, it.bottom)
            if(rect.contains(x.toInt(), y.toInt())){
                it.findViewById<View>(R.id.cl_check_frame).visibility = View.VISIBLE
            }
        }
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