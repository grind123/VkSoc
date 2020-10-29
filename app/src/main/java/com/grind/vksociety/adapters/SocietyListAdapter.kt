package com.grind.vksociety.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grind.vksociety.App
import com.grind.vksociety.R
import com.grind.vksociety.fragments.OnGroupItemsListener
import com.grind.vksociety.models.Society
import de.hdodenhof.circleimageview.CircleImageView


class SocietyListAdapter(
    private val clickListener: OnGroupItemsListener,
    private val longClickListener: OnGroupItemLongClickListener
) :
    RecyclerView.Adapter<SocietyListAdapter.SocietyHolder>() {


    private var items = mutableListOf<Society>()
    private var containerHeight = App.screenWidth / 3
    val selectedItemsList = mutableListOf<Long>()

    class SocietyHolder(v: View) : RecyclerView.ViewHolder(v) {
        val logo: CircleImageView = v.findViewById(R.id.cimv_logo)
        val name: TextView = v.findViewById(R.id.tv_name)
        val checkFrame: ViewGroup = v.findViewById(R.id.cl_check_frame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocietyHolder {
        val v = View.inflate(parent.context, R.layout.item_society, null).apply {
            val layoutParams =
                LinearLayout.LayoutParams(containerHeight, LinearLayout.LayoutParams.WRAP_CONTENT)
            setLayoutParams(layoutParams)
        }
        return SocietyHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SocietyHolder, position: Int) {
        val item = items[position]
        if (selectedItemsList.contains(item.id)) {
            holder.checkFrame.visibility = View.VISIBLE
        } else {
            holder.checkFrame.visibility = View.INVISIBLE
        }

        holder.name.text = item.name
        Glide.with(holder.itemView).load(item.logoUrl).centerInside().into(holder.logo)

        holder.itemView.setOnClickListener {
//            Log.i("Coordinates", "x = ${holder.itemView.x}; y = ${holder.itemView.y}")
            clickListener.onItemClick(item)
        }
        holder.itemView.setOnLongClickListener {
            if (holder.checkFrame.visibility == View.INVISIBLE) {
                holder.checkFrame.visibility = View.VISIBLE
                selectedItemsList.add(item.id)
                longClickListener.onLongItemClick(item.id, selectedItemsList.size)
                clickListener.onSelectItemsCountChanged(selectedItemsList.size)
                return@setOnLongClickListener true
            } else {
                holder.checkFrame.visibility = View.INVISIBLE
                selectedItemsList.remove(item.id)
                longClickListener.onLongItemClick(item.id, selectedItemsList.size)
                clickListener.onSelectItemsCountChanged(selectedItemsList.size)
                return@setOnLongClickListener true
            }
        }
    }

    fun setItems(list: MutableList<Society>) {
        items = list
    }

    fun getItems(): MutableList<Society> {
        return items
    }

    fun clearAllSelectedItems(){
        selectedItemsList.clear()
        notifyItemRangeChanged(0, items.size)
    }

    interface OnGroupItemLongClickListener {
        fun onLongItemClick(societyId: Long, selectedItemsCount: Int)
    }

}