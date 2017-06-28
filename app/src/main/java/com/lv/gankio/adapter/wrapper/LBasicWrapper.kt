package com.lv.gankio.adapter.wrapper

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.lv.gankio.adapter.LBaseViewHolder
import com.lv.gankio.widget.EmptyView

/**
 * Date: 2017-06-26
 * Time: 11:20
 * Description:
 */
class LBasicWrapper(val innerAdapter: RecyclerView.Adapter< RecyclerView.ViewHolder>) : RecyclerView.Adapter< RecyclerView.ViewHolder>() {
    private val headerViews = SparseArrayCompat<View>()
    private val footViews = SparseArrayCompat<View>()
    private val emptyViews = SparseArrayCompat<View>()


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):  RecyclerView.ViewHolder{
        if (headerViews.get(viewType) != null)
            return object : RecyclerView.ViewHolder(headerViews.get(viewType)){}
        else if (emptyViews.get(viewType) != null)
            return object : RecyclerView.ViewHolder(emptyViews.get(viewType)){}
        else if (footViews.get(viewType) != null)
            return object : RecyclerView.ViewHolder(footViews.get(viewType)){}
        else
            return innerAdapter.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (isHeaderView(position) || isFooterView(position))
            return
        else if (isEmptyView(position)) {
            val empty = emptyViews.get(getItemViewType(position))
            if (empty is EmptyView)
                empty.showEmptyView()
            return
        }
        innerAdapter.onBindViewHolder(holder, position - getHeadersCount())
    }

    override fun getItemViewType(position: Int) =
            if (isHeaderView(position))
                headerViews.keyAt(position)
            else if (isEmptyView(position))
                emptyViews.keyAt(position - getHeadersCount())
            else if (isFooterView(position))
                footViews.keyAt(position - getHeadersCount() - getRealItemCount())
            else
                innerAdapter.getItemViewType(position - getHeadersCount())


    override fun getItemCount() = getHeadersCount() + getFootersCount() + getRealItemCount()

    private fun isHeaderView(position: Int) = position < getHeadersCount()

    private fun isFooterView(position: Int) = position >= getHeadersCount() + getRealItemCount()

    private fun isEmptyView(position: Int) = position == getHeadersCount() && innerAdapter.itemCount == 0

    fun addHeaderView(view: View) = headerViews.put(headerViews.size() + BASE_ITEM_TYPE_HEADER, view)

    fun addFooterView(view: View) = footViews.put(footViews.size() + BASE_ITEM_TYPE_FOOTER, view)

    fun addEmptyView(view: View) = emptyViews.put(emptyViews.size() + BASE_ITEM_TYPE_EMPTY, view)


    fun getHeadersCount() = headerViews.size()

    private fun getRealItemCount() = if (innerAdapter.itemCount == 0) emptyViews.size() else innerAdapter.itemCount

    fun getFootersCount() = footViews.size()

    companion object {
        val BASE_ITEM_TYPE_HEADER = 1024
        val BASE_ITEM_TYPE_FOOTER = 2048
        val BASE_ITEM_TYPE_EMPTY = 5096
    }

}