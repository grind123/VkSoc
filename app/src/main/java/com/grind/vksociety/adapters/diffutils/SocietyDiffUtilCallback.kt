package com.grind.vksociety.adapters.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.grind.vksociety.models.Society

class SocietyDiffUtilCallback(
    private val oldItems: List<Society>,
    private val newItems: List<Society>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}