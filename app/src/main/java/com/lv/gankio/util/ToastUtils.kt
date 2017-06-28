package com.lv.gankio.util

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.lv.gankio.R
import kotlinx.android.synthetic.main.toast_layout.view.*


/**
 * Date: 2017-06-23
 * Time: 10:21
 * Description:
 */
class ToastUtils private constructor(context: Context) {
    private val toast: Toast = Toast(context.applicationContext)
    private val layout: View = context.inflate(R.layout.toast_layout)

    init {
        /**
         * 获取状态栏高度——方法1
         * */
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0)
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        // Calculate ActionBar height
        val tv = TypedValue()
        var actionBarHeight = 0
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)

        val widthPixel = context.resources.displayMetrics.widthPixels
        val params = LinearLayout.LayoutParams(widthPixel, statusBarHeight + actionBarHeight)
        layout.toast_parent.layoutParams = params
        layout.setPadding(0, 0, 0, 0)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.view.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        val mtn = getFiled(toast, "mTN")
        mtn?.let {
            val mParams = getFiled(mtn, "mParams")
            if (mParams != null && mParams is WindowManager.LayoutParams) {
                val params2 = mParams
                params2.windowAnimations = R.style.Lite_Animation_Toast
            }
        }
    }



    private fun getFiled(any: Any, filedName: String): Any? {
        val filed = any.javaClass.getDeclaredField(filedName)
        filed?.isAccessible = true
        return filed?.get(any)
    }


    companion object {
        private var toastUtils: ToastUtils? = null
        fun getInstance(context: Context): ToastUtils {
            if (toastUtils == null)
                toastUtils = ToastUtils(context)
            return toastUtils!!
        }
    }

    fun toastError(message: CharSequence?) {
        toast(R.drawable.toast_error, message)
    }

    fun toastSuccess(message: CharSequence?) {
        toast(R.drawable.toast_success, message)
    }

    private fun toast(imageId: Int, message: CharSequence?) {
        message?.let {
            layout.toast_image.setImageResource(imageId)
            layout.toast_text.text = message
            toast.show()
        }
    }


}