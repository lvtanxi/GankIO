package com.lv.gankio.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.view.View
import android.view.ViewAnimationUtils

/**
 * Date: 2017-06-29
 * Time: 10:54
 * Description:
 */
object AnimUtils {
    val ROTATE_MILLS = 300L
    val MINI_RADIUS = 0f

    fun showAsCircular(view: View, startRadius: Float = MINI_RADIUS, durationMills: Long = ROTATE_MILLS) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            showView(view)
        } else {
            startCircular(durationMills, startRadius, view, true)
        }
    }

    fun hideAsCircular(view: View, endRadius: Float = MINI_RADIUS, durationMills: Long = ROTATE_MILLS) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            hideView(view)
        } else {
            startCircular(durationMills, endRadius, view, false)
        }
    }

    @TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
    private fun startCircular(durationMills: Long, targetRadius: Float, view: View, showView: Boolean) {
        val cx = (view.left + view.right) / 2
        val cy = (view.top + view.bottom) / 2

        // 勾股定理 & 进一法
        val initialRadius = Math.sqrt((view.width * view.width + view.height * view.height).toDouble()).toInt() + 1f
        val anim: Animator
        if (showView) {
            view.visibility = View.VISIBLE
            anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, targetRadius, initialRadius)
        } else {
            anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, targetRadius)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.INVISIBLE
                }
            })
        }
        anim.duration = durationMills
        anim.start()
    }

    fun showView(view: View) {
        ObjectAnimator.ofFloat<View>(view, View.SCALE_X, 0f, 1f)
                .setDuration(ROTATE_MILLS)
                .start()
        view.visibility = View.VISIBLE
    }

    fun hideView(view: View) {
        ObjectAnimator.ofFloat<View>(view, View.SCALE_X, 1f, 0f)
                .setDuration(ROTATE_MILLS)
                .start()
        view.visibility = View.GONE
    }


}