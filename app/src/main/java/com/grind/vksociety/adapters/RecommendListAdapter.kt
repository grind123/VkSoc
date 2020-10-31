package com.grind.vksociety.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grind.vksociety.R
import com.grind.vksociety.animations.AnimationTouchListener
import com.grind.vksociety.fragments.OnGroupItemsListener
import com.grind.vksociety.models.Society
import de.hdodenhof.circleimageview.CircleImageView

class RecommendListAdapter(private val clickListener: OnGroupItemsListener): RecyclerView.Adapter<RecommendListAdapter.CategoryHolder>() {

    var itemsList: List<Pair<String?, List<Society>>> = listOf()

    class CategoryHolder(view: View): RecyclerView.ViewHolder(view){
        val categoryName: TextView = view.findViewById(R.id.tv_category_name)
        val groupCount: TextView = view.findViewById(R.id.tv_group_count)
        val showAllButton: TextView = view.findViewById(R.id.tv_show_all_button)
        val groupContainersList = mutableListOf<View>(
            view.findViewById(R.id.item_1),
            view.findViewById(R.id.item_2),
            view.findViewById(R.id.item_3),
            view.findViewById(R.id.item_4),
            view.findViewById(R.id.item_5),
            view.findViewById(R.id.item_6)
        )

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = View.inflate(parent.context, R.layout.item_groups_by_catecory, null).apply {
            layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        }
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val groupsList = itemsList[position].second
        holder.categoryName.text = itemsList[position].first
        holder.showAllButton.visibility = View.INVISIBLE
        holder.groupCount.visibility = View.INVISIBLE

        holder.itemView.setOnTouchListener(AnimationTouchListener())

        setDataToGroupContainer(holder, groupsList)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    private fun setDataToGroupContainer(holder: CategoryHolder, data: List<Society>){
        if(data.size > 6){
            for(i in 0 until 6){
                val society = data[i]
                val view = holder.groupContainersList[i]
                val logo = view.findViewById<CircleImageView>(R.id.cimv_logo)
                val groupName = view.findViewById<TextView>(R.id.tv_name)

                Glide.with(holder.itemView).load(society.logoUrl).centerInside().into(logo)
                groupName.text = society.name
                view.visibility = View.VISIBLE
                view.setOnClickListener {
                    clickListener.onItemClick(society)
                }
            }
        } else {
            for(i in 0 until 6){
                if(i < data.size){
                    val society = data[i]
                    val view = holder.groupContainersList[i]
                    val logo = view.findViewById<CircleImageView>(R.id.cimv_logo)
                    val groupName = view.findViewById<TextView>(R.id.tv_name)

                    Glide.with(holder.itemView).load(society.logoUrl).centerInside().into(logo)
                    groupName.text = society.name
                    view.visibility = View.VISIBLE
                    view.setOnClickListener {
                        clickListener.onItemClick(society)
                    }
                } else {
                    val view = holder.groupContainersList[i]
                    view.visibility = View.INVISIBLE
                }

            }
            if(data.size <= 3){
                goneSecondRawContainers(holder)
            }
        }
    }

    private fun goneSecondRawContainers(holder: CategoryHolder){
        for(i in 3 until 6){
            holder.groupContainersList[i].visibility = View.GONE
        }
    }
}