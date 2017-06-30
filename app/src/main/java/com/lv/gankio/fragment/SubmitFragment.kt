package com.lv.gankio.fragment

import android.support.v4.util.ArrayMap
import com.lv.gankio.R
import com.lv.gankio.base.BaseFragment
import com.lv.gankio.net.LoadingSubscriber
import com.lv.gankio.net.RetrofitClient
import com.lv.gankio.util.intoCompositeSubscription
import com.lv.gankio.util.io_main
import kotlinx.android.synthetic.main.fragment_submit.*

/**
 * Date: 2017-06-29
 * Time: 10:01
 * Description:
 */
class SubmitFragment : BaseFragment() {
    override fun loadLayoutId() = R.layout.fragment_submit

    override fun bindListener() {
        sub_btn.setOnClickListener { doCheck() }
    }

    private fun doCheck() {
        if (sub_address.text.isEmpty() || !sub_address.text.startsWith("http")) {
            sub_address.error = "请输入有效的url地址"
        } else if (sub_content.text.isEmpty()) {
            sub_content.error = "请输入必要的描述信息"
        } else {
            val params = ArrayMap<String, String>()
            params.put("url",sub_address.text.toString())
            params.put("desc",sub_content.text.toString())
            params.put("debug","true")
            params.put("who","吕檀溪")
            params.put("type",sub_spinner.selectedItem.toString())
            submitGank(params)
        }
    }

    fun submitGank(params: Map<String, String>) {
        sub_btn.isClickable=false
        sub_btn.startLoadAnim()
        RetrofitClient.getInstance()
                .apiInterface
                .submitGank(params)
                .io_main()
                .subscribe(LoadingSubscriber<Void>(baseActivity, {
                    success { toastSuccess("添加成功") }
                    finish {
                        sub_btn.isClickable=true
                        sub_btn.reset()
                    }
                }))
                .intoCompositeSubscription(compositeSubscription)
    }
}