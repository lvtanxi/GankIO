package com.lv.gankio.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lv.gankio.adapter.wrapper.LBasicWrapper
import com.lv.gankio.adapter.wrapper.LEmptyViewWrapper
import com.lv.gankio.helper.ItemTouchHelperAdapter

/**
 * Date: 2017-06-26
 * Time: 11:12
 * Description:
 */
abstract class LBaseAdapter<T>(var defaultItemLayoutId: Int = 0, val datas: MutableList<T> = ArrayList<T>()) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {

    var changeDataSize = 0
    protected var isIgnoreCheckedChanged = true
    protected var targetAdapter: RecyclerView.Adapter<*>? = null
    var headerCount = 0
    protected var onItemClickListener: ((view: View, position: Int, mode: T) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LBaseViewHolder<T> {
        parent ?: throw RuntimeException("ViewGroup is empty")
        return LBaseViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false), this, this.onItemClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        isIgnoreCheckedChanged = true
        holder?.let {
            onBindItem(holder.itemView, position, getItem(position))
        }
        isIgnoreCheckedChanged = false
    }

    override fun getItemViewType(position: Int): Int {
        if (defaultItemLayoutId == 0)
            throw RuntimeException("请在 " + this.javaClass.simpleName + " 中重写 getItemViewType 方法返回布局资源 id，或者使用帶参数的构造方法")
        return defaultItemLayoutId
    }

    fun getItem(position: Int) = datas[position]

    override fun getItemCount() = datas.size

    protected abstract fun onBindItem(itemView: View, position: Int, model: T)


    fun notifyItemRangeInsertedWrapper(positionStart: Int, itemCount: Int) = obtainTargetAdapter()?.notifyItemRangeInserted(headerCount + positionStart, itemCount)


    fun notifyDataSetChangedWrapper() = obtainTargetAdapter()?.notifyDataSetChanged()

    fun obtainTargetAdapter(): RecyclerView.Adapter<*>? = if (targetAdapter == null) this else targetAdapter


    fun addItems(items: List<T>?) {
        if (items != null && !items.isEmpty()) {
            changeDataSize = items.size
            datas.addAll(datas.size, items)
            notifyItemRangeInsertedWrapper(datas.size, items.size)
            return
        }
        changeDataSize = 0
    }

    fun addItems(items: List<T>?, isRefresh: Boolean) {
        if (isRefresh) {
            datas.clear()
            if (items != null)
                datas.addAll(0, items)
            notifyDataSetChangedWrapper()
            return
        }
        addItems(items)
    }

    fun clearData() {
        datas.clear()
        notifyDataSetChangedWrapper()
    }

    fun notifyItemRemovedWrapper(position: Int) = obtainTargetAdapter()?.notifyItemRemoved(headerCount + position)


    fun removeItem(position: Int) {
        datas.removeAt(position - headerCount)
        notifyItemRemovedWrapper(position)
    }

    fun notifyItemInsertedWrapper(position: Int) = obtainTargetAdapter()?.notifyItemInserted(position + headerCount)

    fun addItem(position: Int, t: T) {
        datas.add(t)
        notifyItemInsertedWrapper(position)
    }

    fun addFirstItem(t: T) = addItem(0, t)

    fun addLastItem(t: T) = addItem(datas.size, t)

    fun notifyItemChangedWrapper(position: Int) = obtainTargetAdapter()?.notifyItemChanged(headerCount + position)

    fun setItem(position: Int, newData: T) {
        datas[position] = newData
        notifyItemChangedWrapper(position)
    }

    fun setItem(oldData: T, newData: T) = setItem(datas.indexOf(oldData), newData)

    fun notifyItemMovedWrapper(fromPosition: Int, toPosition: Int) = obtainTargetAdapter()?.notifyItemMoved(headerCount + fromPosition, headerCount + toPosition)

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        notifyItemChangedWrapper(fromPosition)
        notifyItemChangedWrapper(toPosition)

        // 要先执行上面的 notifyItemChanged,然后再执行下面的 moveItem 操作

        datas.add(toPosition, datas.removeAt(fromPosition))
        notifyItemMovedWrapper(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) = removeItem(position)


    fun addHeaderView(headerView: View) {
        getBasicWrapper().addHeaderView(headerView)
        headerCount++
    }

    fun addFooterView(footerView: View) = getBasicWrapper().addFooterView(footerView)

    fun addEmptyView(emptyView: View, isBasicWrapper: Boolean) {
        if (isBasicWrapper)
            getBasicWrapper().addEmptyView(emptyView)
        else
            getEmptyWrapper().addEmptyView(emptyView)
    }

    private fun getBasicWrapper(): LBasicWrapper {
        if (targetAdapter == null || targetAdapter !is LBasicWrapper) {
            synchronized(this@LBaseAdapter) {
                if (targetAdapter == null) {
                    targetAdapter = LBasicWrapper(this)
                }
            }
        }
        return targetAdapter as LBasicWrapper
    }

    private fun getEmptyWrapper(): LEmptyViewWrapper {
        if (targetAdapter == null || targetAdapter !is LEmptyViewWrapper) {
            synchronized(this@LBaseAdapter) {
                if (targetAdapter == null) {
                    targetAdapter = LEmptyViewWrapper(this)
                }
            }
        }
        return targetAdapter as LEmptyViewWrapper
    }

    fun adapterIsEmpty() = datas.size == 0

    fun addOnItemClickListener(onItemClickListener: (view: View, position: Int, mode: T) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

}