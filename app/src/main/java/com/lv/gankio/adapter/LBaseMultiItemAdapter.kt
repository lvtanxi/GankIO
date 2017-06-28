package com.lv.gankio.adapter

import android.util.SparseIntArray
import com.lv.gankio.adapter.decorate.LMultiItem

/**
 * Date: 2017-06-26
 * Time: 16:31
 * Description:
 */
abstract class LBaseMultiItemAdapter<M : LMultiItem>(vararg layoutResIds: Int) : LBaseAdapter<M>() {
    protected val layouts = SparseIntArray()

    init {
        if (layoutResIds.isNotEmpty()) {
            for ((index, value) in layoutResIds.withIndex())
                layouts.put(index, value)
        }
        addMultiItem()
    }

    override fun getItemViewType(position: Int)= layouts[getItemType(position)]

    //每一行的type
    fun getItemType(position: Int) = getItem(position).getItemType()

    private fun addMultiItem() {}


}