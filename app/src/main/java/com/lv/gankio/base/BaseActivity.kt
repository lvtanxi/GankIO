package com.lv.gankio.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.transition.Explode
import android.view.MenuItem
import android.view.Window
import com.lv.gankio.R
import com.lv.gankio.activity.GankActivity
import com.lv.gankio.net.WidgetInterface
import com.lv.gankio.util.ToastUtils
import rx.subscriptions.CompositeSubscription

/**
 * Date: 2017-06-23
 * Time: 17:11
 * Description:
 */
abstract class BaseActivity : AppCompatActivity(), WidgetInterface {
    protected var coolbar: Toolbar? = null
    protected var compositeSubscription: CompositeSubscription? = CompositeSubscription()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            window.exitTransition = Explode()
            window.exitTransition = Explode()
        }
        setContentView(loadLayoutId())
        initToolbar()
        initData()
        bindListener()
        onProcessLogic()
    }

    protected fun initToolbar() {
        coolbar = findViewById(R.id.toolbar) as Toolbar?
        if (coolbar == null)
            return
        if (this !is GankActivity)
            coolbar?.setNavigationIcon(R.drawable.back)
        setSupportActionBar(coolbar)
    }

    /**
     * 为Activity加载布局文件
     */
    protected abstract fun loadLayoutId(): Int

    /**
     * 初始化数剧
     */
    protected open fun initData() {

    }

    /**
     * 为控件设置监听
     */
    protected open fun bindListener() {

    }

    /**
     * 逻辑操作，网络请求
     */
    protected open fun onProcessLogic() {

    }

    override fun showLoadingView() {

    }

    override fun hideLoadingView() {

    }

    override fun toastFailure(message: String?) = ToastUtils.getInstance(this).toastError(message)

    override fun toastSuccess(message: String?) = ToastUtils.getInstance(this).toastSuccess(message)


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        if (compositeSubscription != null) {
            compositeSubscription?.unsubscribe()
            compositeSubscription = null
        }
        coolbar = null
        super.onDestroy()
    }


}