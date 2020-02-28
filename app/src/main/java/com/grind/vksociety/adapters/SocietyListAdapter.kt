package com.grind.vksociety.adapters

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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


class SocietyListAdapter(listener: OnItemClickListener): RecyclerView.Adapter<SocietyListAdapter.SocietyHolder>() {

    private var items = listOf<Society>()
    private val listener = listener
    private var logoHeight = (App.screenWidth - (16*2).toPx - (8*6).toPx) / 3


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
        holder.name.text = item.name
        Glide.with(holder.itemView).load(item.logoUrl).centerInside().into(holder.logo)

        holder.itemView.setOnClickListener{listener.onItemClick(item)}
        holder.itemView.setOnLongClickListener {
            if(holder.checkFrame.visibility == View.INVISIBLE){
                holder.checkFrame.visibility = View.VISIBLE
                return@setOnLongClickListener true
            } else {
                holder.checkFrame.visibility = View.INVISIBLE
                return@setOnLongClickListener true
            }
        }
    }

    fun setItems(list: List<Society>){
        items = list
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(currSociety: Society)
    }
}