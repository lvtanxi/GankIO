package com.lv.gankio.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.lv.gankio.R
import com.lv.gankio.base.BaseActivity
import com.lv.gankio.util.loadImage
import kotlinx.android.synthetic.main.activity_photo_view.*

/**
 * Date: 2017-06-29
 * Time: 14:18
 * Description:
 */
@Route(path = "/gank/photoview")
class PhotoViewActivity :BaseActivity(){
    override fun loadLayoutId()= R.layout.activity_photo_view
    override fun initData() {
        photo_view.enable()
    }

    override fun onProcessLogic() {
        photo_view.loadImage(intent.getStringExtra("url"))
    }
}