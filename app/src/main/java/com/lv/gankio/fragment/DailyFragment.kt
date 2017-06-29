package com.lv.gankio.fragment

import android.view.View
import com.lv.gankio.R
import com.lv.gankio.adapter.LBaseAdapter
import com.lv.gankio.base.BaseRecyclerFragment
import com.lv.gankio.model.DateData
import com.lv.gankio.model.DetailsData
import com.lv.gankio.net.LoadingSubscriber
import com.lv.gankio.net.RetrofitClient
import com.lv.gankio.util.*
import kotlinx.android.synthetic.main.rv_item_daily.view.*
import java.util.*

/**
 * Date: 2017-06-24
 * Time: 17:32
 * Description:
 */
class DailyFragment : BaseRecyclerFragment<DetailsData>() {


    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    override fun initData() {
        super.initData()
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH) + 1
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    override fun onStartLoading(isRefresh: Boolean) {
        RetrofitClient
                .getInstance()
                .apiInterface
                .getDaily(year, month, day)
                .map {
                    val datas = ArrayList<DetailsData>()
                    it.android?.let { datas.addAll(it) }
                    it.app?.let { datas.addAll(it) }
                    it.bonus?.let { datas.addAll(it) }
                    it.ios?.let { datas.addAll(it) }
                    it.js?.let { datas.addAll(it) }
                    it.rec?.let { datas.addAll(it) }
                    it.res?.let { datas.addAll(it) }
                    it.video?.let { datas.addAll(it) }
                    datas
                }
                .io_main()
                .subscribe(LoadingSubscriber(baseActivity, {
                    success {
                        addItems(it)
                    }
                    finish { stopRefreshOrMore(it) }
                }))
                .intoCompositeSubscription(compositeSubscription)
    }

    override fun obtainBaseAdapter(): LBaseAdapter<DetailsData>? = object : LBaseAdapter<DetailsData>(R.layout.rv_item_daily) {
        override fun onBindItem(itemView: View, position: Int, model: DetailsData) {
            itemView.tv_title_daily.text = model.desc.trim({ it <= ' ' })
            itemView.tv_date_daily.text = PublicTools.date2String(model.publishedAt)
            itemView.tv_type_daily.text = model.type
            itemView.tv_type_daily.setText(model.type)
                    .setSlantedBackgroundColor(Constant.sTypeColor[model.type]!!)
            itemView.iv_daily.visibility = View.VISIBLE
            if (model.images != null && model.images!!.isNotEmpty()) {
                itemView.iv_daily.loadImage(model.images!![0])
            } else {
                if (model.type == "福利") {
                    itemView.iv_daily.loadImage(model.url)
                } else {
                    itemView.iv_daily.visibility = View.GONE
                }
            }
        }
    }

    override fun bindListener() {
        super.bindListener()
        RxBus.toObservable(DateData::class.java)
                .io_main()
                .subscribe {
                    year = it.year
                    month = it.month + 1
                    day = it.day
                    onProcessLogic()
                }.intoCompositeSubscription(compositeSubscription)
        baseAdapter?.addOnItemClickListener { _, _, (_, _, _, _, _, _, url) -> PublicTools.start2web(url) }
    }
}