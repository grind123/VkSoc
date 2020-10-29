package com.grind.vksociety.utils

import androidx.recyclerview.widget.DiffUtil
import com.grind.vksociety.models.Society

class SocietyByCategoryDiffUtilCallback(
    private val oldItems: List<Pair<String?, MutableList<Society>>>,
    private val newItems: List<Pair<String?, MutableList<Society>>>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].first == newItems[newItemPosition].first
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].first == newItems[newItemPosition].first &&
                oldItems[oldItemPosition].second.size == newItems[newItemPosition].second.size
    }
}