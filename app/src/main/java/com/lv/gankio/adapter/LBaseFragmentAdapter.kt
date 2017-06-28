package com.lv.gankio.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * User: 吕勇
 * Date: 2016-05-05
 * Time: 14:39
 * Description:FragmentStatePagerAdapter基类
 */
class LBaseFragmentAdapter(fm: FragmentManager, val fragments: Array<Fragment?>, val titles: List<String>? = null) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int) = if (titles == null) "" else titles[position]
}
