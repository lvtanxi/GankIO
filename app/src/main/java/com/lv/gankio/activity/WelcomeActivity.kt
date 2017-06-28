package com.lv.gankio.activity

import com.alibaba.android.arouter.launcher.ARouter
import com.lv.gankio.R
import com.lv.gankio.base.BaseActivity
import com.lv.gankio.util.intoCompositeSubscription
import com.lv.gankio.util.io_main
import kotlinx.android.synthetic.main.activity_welcome.*
import rx.Observable
import java.util.concurrent.TimeUnit

class WelcomeActivity : BaseActivity() {
    override fun loadLayoutId() = R.layout.activity_welcome

    override fun initData() {
        welDescribe.init("Hello world")
    }

    override fun onProcessLogic() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .io_main()
                .subscribe {
                    ARouter.getInstance().build("/gank/home").navigation()
                    finish()
                }.intoCompositeSubscription(compositeSubscription)
    }


}
