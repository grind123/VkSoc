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

class SocietyByCategoryAdapter(
    private val clickListener: OnGroupItemsListener,
    private val longClickListener: SocietyListAdapter.OnGroupItemLongClickListener,
    private val allGroupsShower: AllGroupsShower
) : RecyclerView.Adapter<SocietyByCategoryAdapter.CategoryHolder>() {

    var itemsList: List<Pair<String?, MutableList<Society>>> = listOf()
    val selectedItemsList = mutableListOf<Long>()

    class CategoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.tv_category_name)
        val groupsCount: TextView = view.findViewById(R.id.tv_group_count)
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
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
        }
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val groupsList = itemsList[position].second
        holder.categoryName.text = itemsList[position].first
        holder.groupsCount.text = "${groupsList.size}"

        if (groupsList.size < 6) {
            holder.showAllButton.visibility = View.GONE
            holder.showAllButton.setOnClickListener(null)
        } else {
            holder.showAllButton.visibility = View.VISIBLE
            holder.showAllButton.setOnClickListener { allGroupsShower.showAllGroups(position, groupsList) }
        }

        holder.groupContainersList.forEach { it.setOnTouchListener(AnimationTouchListener()) }
        setDataToGroupContainer(holder, groupsList)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    private fun setDataToGroupContainer(holder: CategoryHolder, data: List<Society>) {
        if (data.size > 6) {
            for (i in 0 until 6) {
                val society = data[i]
                val view = holder.groupContainersList[i]
                val logo = view.findViewById<CircleImageView>(R.id.cimv_logo)
                val groupName = view.findViewById<TextView>(R.id.tv_name)
                val checkFrame = view.findViewById<ConstraintLayout>(R.id.cl_check_frame)

                if (selectedItemsList.contains(society.id)) {
                    checkFrame.visibility = View.VISIBLE
                } else {
                    checkFrame.visibility = View.INVISIBLE
                }
                Glide.with(holder.itemView).load(society.logoUrl).centerInside().into(logo)
                groupName.text = society.name
                view.visibility = View.VISIBLE
                view.setOnClickListener {
                    clickListener.onItemClick(society)
                }
                view.setOnLongClickListener {
                    if (checkFrame.visibility == View.INVISIBLE) {
                        checkFrame.visibility = View.VISIBLE
                        selectedItemsList.add(society.id)
                        longClickListener.onLongItemClick(society.id, selectedItemsList.size)
                        clickListener.onSelectItemsCountChanged(selectedItemsList.size)
                        return@setOnLongClickListener true
                    } else {
                        checkFrame.visibility = View.INVISIBLE
                        selectedItemsList.remove(society.id)
                        longClickListener.onLongItemClick(society.id, selectedItemsList.size)
                        clickListener.onSelectItemsCountChanged(selectedItemsList.size)
                        return@setOnLongClickListener true
                    }
                }
            }
        } else {
            for (i in 0 until 6) {
                if (i < data.size) {
                    val society = data[i]
                    val view = holder.groupContainersList[i]
                    val logo = view.findViewById<CircleImageView>(R.id.cimv_logo)
                    val groupName = view.findViewById<TextView>(R.id.tv_name)
                    val checkFrame = view.findViewById<ConstraintLayout>(R.id.cl_check_frame)

                    if (selectedItemsList.contains(society.id)) {
                        checkFrame.visibility = View.VISIBLE
                    } else {
                        checkFrame.visibility = View.INVISIBLE
                    }

                    Glide.with(holder.itemView).load(society.logoUrl).centerInside().into(logo)
                    groupName.text = society.name
                    view.visibility = View.VISIBLE
                    view.setOnClickListener {
                        clickListener.onItemClick(society)
                    }
                    view.setOnLongClickListener {
                        if (checkFrame.visibility == View.INVISIBLE) {
                            checkFrame.visibility = View.VISIBLE
                            selectedItemsList.add(society.id)
                            longClickListener.onLongItemClick(society.id, selectedItemsList.size)
                            clickListener.onSelectItemsCountChanged(selectedItemsList.size)
                            return@setOnLongClickListener true
                        } else {
                            checkFrame.visibility = View.INVISIBLE
                            selectedItemsList.remove(society.id)
                            longClickListener.onLongItemClick(society.id, selectedItemsList.size)
                            clickListener.onSelectItemsCountChanged(selectedItemsList.size)
                            return@setOnLongClickListener true
                        }
                    }
                } else {
                    val view = holder.groupContainersList[i]
                    view.visibility = View.INVISIBLE
                }

            }
            if (data.size <= 3) {
                goneSecondRawContainers(holder)
            }
        }
    }

    private fun goneSecondRawContainers(holder: CategoryHolder) {
        for (i in 3 until 6) {
            holder.groupContainersList[i].visibility = View.GONE
        }
    }

    fun clearAllSelectedItems() {
        selectedItemsList.clear()
        notifyItemRangeChanged(0, itemsList.size)
    }

    interface AllGroupsShower {
        fun showAllGroups(itemPosition: Int, groupsList: MutableList<Society>)
    }
}