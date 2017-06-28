package com.lv.gankio.activity

import android.view.KeyEvent
import android.widget.ArrayAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.lv.gankio.R
import com.lv.gankio.base.BaseActivity
import com.lv.gankio.fragment.GankFragment
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Date: 2017-06-28
 * Time: 15:56
 * Description:
 */
@Route(path = "/gank/search")
class SearchActivity : BaseActivity() {

    override fun loadLayoutId() = R.layout.activity_search
    override fun initData() {
        super.initData()
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.dummy_items, R.layout.spinner_item_text)
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown_list)
        sp_category.adapter = adapter
    }

    override fun bindListener() {
        tv_search.setOnClickListener { changeFragment() }
        et_search.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
                changeFragment()
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun changeFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_content,GankFragment.newInstance(sp_category.selectedItem.toString(),et_search.text.toString()),sp_category.selectedItem.toString())
        transaction.commit()
    }
}