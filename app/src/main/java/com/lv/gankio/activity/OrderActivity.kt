package com.lv.gankio.activity

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MenuItem
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.lv.gankio.R
import com.lv.gankio.adapter.LBaseAdapter
import com.lv.gankio.base.BaseActivity
import com.lv.gankio.helper.OnStartDragListener
import com.lv.gankio.helper.SimpleItemTouchHelperCallback
import com.lv.gankio.model.OrderData
import com.lv.gankio.util.Constant
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.rv_item_sort.view.*

/**
 * Date: 2017-06-28
 * Time: 13:47
 * Description:
 */
@Route(path = "/gank/order")
class OrderActivity : BaseActivity(), OnStartDragListener {

    private var itemTouchHelper: ItemTouchHelper? = null


    override fun loadLayoutId() = R.layout.activity_order


    private val baseAdapter = object : LBaseAdapter<OrderData>(R.layout.rv_item_sort, getDatas()) {
        override fun onBindItem(itemView: View, position: Int, model: OrderData) {
            itemView.order_text.text = model.name
            itemView.order_item.setBackgroundResource(model.bg)
        }

    }

    private fun getDatas(): MutableList<OrderData> {
        val array = ArrayList<OrderData>()
        Constant.sCategoryList.mapTo(array) { OrderData(it, Constant.sTypeColor[it]!!) }
        return array
    }


    override fun initData() {
        order_recycler.setHasFixedSize(true)
        order_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        order_recycler.adapter = baseAdapter
        val callback = SimpleItemTouchHelperCallback(baseAdapter, swipeEnabled = false)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper?.attachToRecyclerView(order_recycler)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper?.startDrag(viewHolder)
    }

    override fun onBackPressed() {
        var isChange = false
        val names = ArrayList<String>()
        for ((index, value) in baseAdapter.datas.withIndex()) {
            if (value.name != Constant.sCategoryList[index] && !isChange)
                isChange = true
            names.add(value.name)
        }
        if (isChange) {
            Constant.sCategryListChanged = true
            Constant.sCategoryList = names
        }
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }


}