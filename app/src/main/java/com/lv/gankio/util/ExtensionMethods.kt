package com.lv.gankio.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lv.gankio.R
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Date: 2017-06-21
 * Time: 17:08
 * Description:
 */
fun <T> Observable<T>.io_main(): Observable<T> {
    return this.compose({ tObservable -> tObservable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) })
}

fun Subscription.intoCompositeSubscription(compositeSubscription: CompositeSubscription?) = compositeSubscription?.add(this)

fun <T : View> Context.inflate(@LayoutRes layoutId: Int): T {
    return LayoutInflater.from(this).inflate(layoutId, null) as T
}

fun Context.dp2px(dpVal: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, resources.displayMetrics)

fun View.setVisibility(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun String?.isNull() = this == null || this.isEmpty()

fun Context.getColorInt(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun Context.share(text: String, subject: String = ""): Boolean {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(android.content.Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(intent, null))
        return true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        return false
    }
}


fun Context.browse(url: String, newTask: Boolean = false): Boolean {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (newTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        return true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        return false
    }
}


fun ImageView.loadImage(url:String?){
    Glide.with(context)
            .load(url)
            .placeholder(R.mipmap.img_default_gray)
            .into(this)
}
