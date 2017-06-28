package com.lv.gankio.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.lv.gankio.helper.ItemTouchHelperViewHolder

/**
 * Date: 2017-06-26
 * Time: 11:22
 * Description:
 */
class LBaseViewHolder<T>(
        itemView: View, val recyclerViewAdapter: LBaseAdapter<T>,
        onItemClickListener: ((view: View, position: Int, mode: T) -> Unit)?) : RecyclerView.ViewHolder(itemView), ItemTouchHelperViewHolder {
    override fun onItemSelected() {}

    override fun onItemClear() {}


    init {
        itemView.setOnClickListener {
            if (it.id == itemView.id && onItemClickListener != null) {
                val positionWrapper = getAdapterPositionWrapper()
                onItemClickListener.invoke(it, positionWrapper, recyclerViewAdapter.getItem(positionWrapper))
            }
        }
    }

    private fun getAdapterPositionWrapper() = adapterPosition - recyclerViewAdapter.headerCount

}