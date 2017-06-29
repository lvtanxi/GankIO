package com.lv.gankio.base

import android.support.v7.widget.LinearLayoutManager
import com.lcodecore.tkrefreshlayout.header.bezierlayout.BezierLayout
import com.lv.gankio.R
import com.lv.gankio.adapter.LBaseAdapter
import com.lv.gankio.util.RefreshListenerHelper
import com.lv.gankio.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_gank.*


/**
 * Date: 2017-06-24
 * Time: 16:57
 * Description:
 */
abstract class BaseRecyclerFragment<T> : BaseFragment(), RefreshListenerHelper.RefreshListener {

    protected var refreshListenerHelper: RefreshListenerHelper? = null

    override fun loadLayoutId() = R.layout.fragment_gank

    protected var baseAdapter: LBaseAdapter<T>? = null


    override fun initData() = initRecylerView()

    protected var loadMore = false


    protected fun initRecylerView() {
        val headerView = BezierLayout(context)
        refresh_Layout.setHeaderView(headerView)
        refresh_Layout.setOverScrollBottomShow(true)
        refresh_Layout.setAutoLoadMore(loadMore)
        baseAdapter = obtainBaseAdapter()
        baseAdapter?.addEmptyView(EmptyView(context), false)
        recycler_view.layoutManager = LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = baseAdapter?.obtainTargetAdapter()
    }

    abstract fun obtainBaseAdapter(): LBaseAdapter<T>?


    override fun bindListener() {
        refreshListenerHelper = RefreshListenerHelper(refresh_Layout, this)
    }

    override fun onProcessLogic(){
        refresh_Layout.startRefresh()
    }
    fun stopLoading(){
        refreshListenerHelper?.stopLoading()
        notifyDataSetChangedWrapper()
    }

    fun stopRefreshOrMore(isSuceess: Boolean) {
        refreshListenerHelper?.stopLoading()
        if (baseAdapter != null && refreshListenerHelper != null) {
            val loadOver = refreshListenerHelper!!.handlePage(baseAdapter!!.changeDataSize, isSuceess)
            notifyDataSetChangedWrapper()
            refresh_Layout.setEnableLoadmore(!loadOver)
        }
    }

    private fun notifyDataSetChangedWrapper(){
        if (baseAdapter != null ) {
            if (baseAdapter!!.dataIsEmpty())
                baseAdapter?.notifyDataSetChangedWrapper()
        }
    }


    fun addItems(items: List<T>?, isRefresh: Boolean = true) = baseAdapter?.addItems(items, isRefresh)

    protected fun getPageNo() = if (refreshListenerHelper == null) 1 else refreshListenerHelper!!.getpageNo()

    protected fun getPageSize() = if (refreshListenerHelper == null) 10 else refreshListenerHelper!!.getPageSize()


    override fun onDestroyView() {
        refreshListenerHelper = null
        baseAdapter = null
        super.onDestroyView()
    }


}