package com.lv.gankio.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rx.subscriptions.CompositeSubscription

/**
 * User: 吕勇
 * Date: 2016-03-17
 * Time: 09:38
 * Description:所有Fragment的基类
 */
abstract class BaseFragment : Fragment() {
    protected var contentView: View? = null
    protected var hasCreateView = false
    protected var isFragmentVisible = false
    protected var needProcess = true
    protected lateinit var baseActivity: BaseActivity
    protected var compositeSubscription: CompositeSubscription? = null

    override fun onAttach(activity: Context?) {
        baseActivity = getActivity() as BaseActivity
        super.onAttach(activity)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.contentView = inflater.inflate(loadLayoutId(), null)
        return contentView
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (contentView == null)
            return
        hasCreateView = true
        if (isVisibleToUser) {
            onFragmentVisibleChange(true)
            isFragmentVisible = true
            return
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false)
            isFragmentVisible = false
        }
    }

    fun onFragmentVisibleChange(isVisible: Boolean) {
        if (isVisible&&needProcess) {
            needProcess = false
            onProcessLogic()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        bindListener()
        if (!hasCreateView && userVisibleHint) {
            onFragmentVisibleChange(true)
            isFragmentVisible = true
        }
    }


    /**
     * 为Fragment加载布局
     */
    protected abstract fun loadLayoutId(): Int


    /**
     * 初始化数
     */
    protected abstract fun initData()

    /**
     * 为控件设置监
     */
    protected open fun bindListener() {

    }

    /**
     * 逻辑操作，网络请求
     */
    protected open fun onProcessLogic() {

    }


    override fun onDestroyView() {
        if (compositeSubscription != null) {
            compositeSubscription?.unsubscribe()
            compositeSubscription = null
        }
        needProcess = true
        super.onDestroyView()
    }

    override fun onDestroy() {
        contentView = null
        super.onDestroy()
    }

}
