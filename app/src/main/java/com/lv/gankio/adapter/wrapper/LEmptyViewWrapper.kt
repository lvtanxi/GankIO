package com.lv.gankio.adapter.wrapper

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.lv.gankio.widget.EmptyView

/**
 * Date: 2017-06-26
 * Time: 11:20
 * Description:
 */
class LEmptyViewWrapper(val innerAdapter: RecyclerView.Adapter< RecyclerView.ViewHolder>) : RecyclerView.Adapter< RecyclerView.ViewHolder>() {
    private val emptyViews = SparseArrayCompat<View>()
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):  RecyclerView.ViewHolder {
        if (isEmpty() && emptyViews.get(viewType) != null)
            return object :RecyclerView.ViewHolder(emptyViews.get(viewType)){}
        return innerAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount() = if (isEmpty()) emptyViews.size() else innerAdapter.itemCount


    override fun getItemViewType(position: Int) = if (isEmpty()) BASE_ITEM_TYPE_EMPTY else innerAdapter.getItemViewType(position)


    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder?, position: Int) {
        if (isEmpty()){
            val empty=emptyViews.get(getItemViewType(position))
            if(empty is EmptyView)
                empty.showEmptyView()
            return
        }
        innerAdapter.onBindViewHolder(holder, position)
    }

    companion object {
        val BASE_ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1
    }

    fun isEmpty() = emptyViews.size() > 0 && innerAdapter.itemCount == 0

    fun addEmptyView(view: View) {
        emptyViews.put(emptyViews.size() + BASE_ITEM_TYPE_EMPTY, view)
    }

}