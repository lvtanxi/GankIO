package com.lv.gankio.fragment

import android.support.v4.app.Fragment
import com.lv.gankio.R
import com.lv.gankio.adapter.LBaseFragmentAdapter
import com.lv.gankio.base.BaseFragment
import com.lv.gankio.util.Constant
import kotlinx.android.synthetic.main.fragment_sort.*

/**
 * Date: 2017-06-27
 * Time: 14:10
 * Description:
 */
class SortFragment : BaseFragment() {
    private var mCurrentTag = "all"
    override fun loadLayoutId() = R.layout.fragment_sort

    override fun initData() {
        sort_view_pager.removeAllViews()
        val array = arrayOfNulls<Fragment>(Constant.sCategoryList.size)
        for ((index, item) in Constant.sCategoryList.withIndex()) {
            array[index] = GankFragment.newInstance(item)
        }
        sort_view_pager.adapter = LBaseFragmentAdapter(childFragmentManager, array, Constant.sCategoryList)
        sort_tab_laout.setupWithViewPager(sort_view_pager)
    }

    override fun onResume() {
        super.onResume()
        if (Constant.sCategryListChanged) {
            Constant.sCategryListChanged = false
            initData()
            Constant.sCategoryList.indices
                    .filter { Constant.sCategoryList[it] == mCurrentTag }
                    .forEach { sort_view_pager.setCurrentItem(it, true) }
        }
    }

    override fun onPause() {
        super.onPause()
        sort_view_pager?.let { mCurrentTag = Constant.sCategoryList[it.currentItem] }
    }

}