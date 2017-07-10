package com.lv.gankio.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.lv.gankio.R
import com.lv.gankio.adapter.LBaseAdapter
import com.lv.gankio.adapter.LBaseMultiItemAdapter
import com.lv.gankio.base.BaseRecyclerFragment
import com.lv.gankio.model.DetailsData
import com.lv.gankio.net.LoadingSubscriber
import com.lv.gankio.net.RetrofitClient
import com.lv.gankio.util.*
import com.tbruyelle.rxpermissions.RxPermissions
import kotlinx.android.synthetic.main.rv_item_image.view.*
import kotlinx.android.synthetic.main.rv_item_text.view.*
import rx.Observable
import java.io.File

/**
 * Date: 2017-06-27
 * Time: 14:17
 * Description:
 */
class GankFragment : BaseRecyclerFragment<DetailsData>() {
    private var category = "ALL"
    private var params = ""

    override fun initData() {
        loadMore = true
        super.initData()
        arguments?.let {
            category = arguments.getString("category", "ALL")
            params = arguments.getString("params", "")
        }
    }

    override fun onStartLoading(isRefresh: Boolean) {
        val content: Observable<List<DetailsData>>
        if (params == "") {
            content = RetrofitClient.getInstance()
                    .apiInterface
                    .getContent(category, getPageSize(), getPageNo())
        } else {
            content = RetrofitClient.getInstance()
                    .apiInterface
                    .search(category, params, getPageNo())
        }
        content.io_main()
                .subscribe(LoadingSubscriber<List<DetailsData>>(baseActivity, {
                    success { addItems(it, isRefresh) }
                    finish { stopRefreshOrMore(it) }
                }))
                .intoCompositeSubscription(compositeSubscription)
    }

    override fun obtainBaseAdapter(): LBaseAdapter<DetailsData>? {
        return object : LBaseMultiItemAdapter<DetailsData>(R.layout.rv_item_image, R.layout.rv_item_text) {
            override fun onBindItem(itemView: View, position: Int, model: DetailsData) {
                if (getItemType(position) == 0) {
                    itemView.imgPicture.loadImage(model.url)
                    itemView.ib_download.setOnClickListener { saveImage(model.url, model.desc) }
                } else {
                    itemView.tv_title.text = model.desc
                    itemView.tv_author.text = model.who
                    itemView.tv_type.text = model.type
                    itemView.tv_type.setBackgroundResource(Constant.sTypeColor[model.type]!!)
                    itemView.tv_date.text = PublicTools.getTimestampString(PublicTools.string2date(model.createdAt))
                }
            }
        }
    }

    private fun saveImage(url: String, desc: String) {
        RxPermissions(activity)
                .request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (!granted) {
                        toastFailure("请在设置中开启存储权限后再试")
                        return@subscribe
                    }
                    Glide.with(this).load(url)
                            .asBitmap()
                            .into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                    Observable.unsafeCreate<Void> { subscriber ->
                                        try {
                                            PublicTools.saveBitmap(resource, Environment.getExternalStorageDirectory().absolutePath + File.separator + desc)
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            subscriber.onError(e)
                                        }
                                        subscriber.onNext(null)
                                        subscriber.onCompleted()
                                    }.io_main()
                                            .subscribe { toastSuccess("保存成") }
                                            .intoCompositeSubscription(compositeSubscription)
                                }

                            })
                }

    }

    override fun bindListener() {
        super.bindListener()
        baseAdapter?.addOnItemClickListener { view, _, (_, _, _, _, _, type, url) ->PublicTools.startTargetActivity(type,url,baseActivity,view)}
    }

    companion object {
        fun newInstance(category: String, params: String = ""): GankFragment {
            val args = Bundle()
            args.putString("category", category)
            args.putString("params", params)
            val fragment = GankFragment()
            fragment.arguments = args
            return fragment
        }
    }
}