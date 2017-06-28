package com.lv.gankio.util

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout

/**
 * Date: 2017-06-24
 * Time: 17:11
 * Description:
 */
class RefreshListenerHelper(val refreshLayout: TwinklingRefreshLayout, val refreshListener: RefreshListener) : RefreshListenerAdapter() {
    private var isRefresh = true
    private var pageSize = 10
    private var pageNo = 1

    init {
        refreshLayout.setOnRefreshListener(this)
    }

    override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
        isRefresh = true
        pageNo = 1
        refreshListener.onStartLoading(isRefresh)
    }

    override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
        isRefresh = false
        pageNo++
        refreshListener.onStartLoading(isRefresh)
    }

    fun stopLoading() {
        if (isRefresh)
            refreshLayout.finishRefreshing()
        else
            refreshLayout.finishLoadmore()
    }

    fun handlePage(changeDataSize: Int, isSuceess: Boolean) =  (changeDataSize != pageSize) && isSuceess

    fun getPageSize()=pageSize

    fun getpageNo()=pageNo


    interface RefreshListener {
        fun onStartLoading(isRefresh: Boolean)
    }

}