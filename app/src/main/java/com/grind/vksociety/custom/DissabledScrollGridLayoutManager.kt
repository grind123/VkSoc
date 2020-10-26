package com.grind.vksociety.custom

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class DisabledScrollGridLayoutManager(context: Context?, count: Int): GridLayoutManager(context, count) {
    var isDisabledScroll = false
    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && !isDisabledScroll
    }
}