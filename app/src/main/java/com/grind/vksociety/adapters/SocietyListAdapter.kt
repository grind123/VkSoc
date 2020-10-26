package com.grind.vksociety.adapters

import android.content.res.Resources
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grind.vksociety.App
import com.grind.vksociety.R
import com.grind.vksociety.models.Society
import de.hdodenhof.circleimageview.CircleImageView

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


class SocietyListAdapter(private val listener: OnItemClickListener): RecyclerView.Adapter<SocietyListAdapter.SocietyHolder>() {


    private var items = listOf<Society>()
    private var logoHeight = (App.screenWidth - (16).toPx - (8*6).toPx) / 3
    val selectedList = mutableListOf<Long>()

    class SocietyHolder(v: View): RecyclerView.ViewHolder(v){
        val logo: CircleImageView = v.findViewById(R.id.cimv_logo)
        val name: TextView = v.findViewById(R.id.tv_name)
        val checkFrame: ViewGroup = v.findViewById(R.id.cl_check_frame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocietyHolder {
        val v = View.inflate(parent.context, R.layout.item_society, null)
            v.findViewById<CircleImageView>(R.id.cimv_logo).apply {
                layoutParams = ConstraintLayout.LayoutParams(logoHeight, logoHeight)
            }
        return SocietyHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SocietyHolder, position: Int) {
        val item = items[position]
        if(selectedList.contains(item.id)){
            holder.checkFrame.visibility = View.VISIBLE
        } else {
            holder.checkFrame.visibility = View.INVISIBLE
        }

        holder.name.text = item.name
        Glide.with(holder.itemView).load(item.logoUrl).centerInside().into(holder.logo)

        holder.itemView.setOnClickListener{
//            Log.i("Coordinates", "x = ${holder.itemView.x}; y = ${holder.itemView.y}")
            listener.onItemClick(item)
        }
        holder.itemView.setOnLongClickListener {
            listener.onLongItemClick(item.id)
            if(holder.checkFrame.visibility == View.INVISIBLE){
                holder.checkFrame.visibility = View.VISIBLE
                selectedList.add(item.id)
                return@setOnLongClickListener true
            } else {
                holder.checkFrame.visibility = View.INVISIBLE
                selectedList.remove(item.id)
                return@setOnLongClickListener true
            }
        }
    }

    fun setItems(list: List<Society>){
        items = list
    }

    fun getItems(): List<Society>{
        return items
    }

    interface OnItemClickListener{
        fun onItemClick(currSociety: Society)
        fun onLongItemClick(societyId: Long)
    }
}