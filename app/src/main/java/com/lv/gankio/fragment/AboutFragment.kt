package com.lv.gankio.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lv.gankio.R
import com.lv.gankio.base.BaseActivity
import com.lv.gankio.util.PublicTools
import com.lv.gankio.util.share
import com.vansuita.materialabout.builder.AboutBuilder

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = AboutBuilder.with(context)
                .setPhoto(R.mipmap.ic_launcher)
                .setCover(R.mipmap.profile_cover)
                .setName("吕檀溪")
                .setSubTitle("Android Developer")
                .setBrief("人生就像一杯茶，不会苦一辈子，但总会苦一阵子，没有开始的苦，就没有后来的甜。")
                .setAppName(R.string.app_name)
                .addGitHubLink("lvtanxi")
                .addEmailLink("lvtanxi@163.com")
                .addWebsiteLink("https://lvtanxi.github.io")
                .addAndroidLink("https://my.oschina.net/u/1269023")
                .setVersionNameAsAppSubTitle()
                .addAction(R.mipmap.share,
                        R.string.share_app,
                         { context.share("https://www.pgyer.com/BStY") })
                .addAction(R.mipmap.update,
                        R.string.update_app) { PublicTools.update(activity as BaseActivity,true)  }
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .build()
        return view
    }

}
