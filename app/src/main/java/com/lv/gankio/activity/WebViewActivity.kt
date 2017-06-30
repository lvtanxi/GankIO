package com.lv.gankio.activity

import android.content.ClipboardManager
import android.content.Context
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.lv.gankio.R
import com.lv.gankio.base.BaseActivity
import com.lv.gankio.util.browse
import com.lv.gankio.util.share
import kotlinx.android.synthetic.main.activity_web.*

/**
 * Date: 2017-06-27
 * Time: 10:42
 * Description:
 */
@Route(path = "/gank/web")
class WebViewActivity : BaseActivity() {
    @Autowired
    lateinit var url:String

    override fun loadLayoutId(): Int {
        ARouter.getInstance().inject(this)
        return R.layout.activity_web
    }

    override fun initData() {
        initWebViewSettings()
        wv_content.removeJavascriptInterface("searchBoxJavaBridge_")
        wv_content.removeJavascriptInterface("accessibilityTraversal")
        wv_content.removeJavascriptInterface("accessibility")
    }

    override fun onProcessLogic() = wv_content.loadUrl(url)

    private fun initWebViewSettings() {
        val settings = wv_content.settings
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.setAppCacheEnabled(true)
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.setSupportZoom(true)
        settings.savePassword = false
        wv_content.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressbar.progress = newProgress
                if (newProgress == 100) {
                    progressbar.visibility = View.GONE
                } else {
                    progressbar.visibility = View.VISIBLE
                }
            }


            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                setTitle(title)
            }
        })
        wv_content.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                url?.let { view.loadUrl(it) }
                return true
            }
        })
    }

    public override fun onPause() {
        wv_content.onPause()
        super.onPause()
    }

    public override fun onResume() {
        wv_content.onResume()
        super.onResume()
    }

    public override fun onDestroy() {
        wv_content.destroy()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.web_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.share -> share(url)
            R.id.openinbrowse ->browse(url)
            R.id.copyurl -> {
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.text = url
                toastSuccess("已复制到剪切板")
            }
            R.id.refresh ->wv_content.reload()
            else -> { }
        }
        return true
    }
}