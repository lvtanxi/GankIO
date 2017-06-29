package com.lv.gankio.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.alibaba.android.arouter.launcher.ARouter
import com.lv.gankio.base.BaseActivity
import com.lv.gankio.util.Constant.ONE_DAY
import com.lv.gankio.util.Constant.ONE_HOUR
import com.lv.gankio.util.Constant.ONE_MINUTE
import com.pgyersdk.update.PgyUpdateManager
import com.pgyersdk.update.UpdateManagerListener
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


object PublicTools {
    /**
     * 获取目标时间和当前时间之间的差距

     * @param date date
     * *
     * @return String
     */
    fun getTimestampString(date: Date?): String {
        date ?: return ""
        val curDate = Date()
        val splitTime = curDate.time - date.time
        if (splitTime < 30 * ONE_DAY) {
            if (splitTime < ONE_MINUTE) {
                return "刚刚"
            }
            if (splitTime < ONE_HOUR) {
                return String.format("%d分钟前", splitTime / ONE_MINUTE)
            }

            if (splitTime < ONE_DAY) {
                return String.format("%d小时前", splitTime / ONE_HOUR)
            }

            return String.format("%d天前", splitTime / ONE_DAY)
        }
        val result: String = "M月d日 HH:mm"
        return SimpleDateFormat(result, Locale.CHINA).format(date)
    }

    fun string2date(date: String?): Date? {
        date ?: return null
        val dateStr = date.substring(0, date.lastIndexOf(".")).replace("T", " ")
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return df.parse(dateStr)
    }

    /**
     * Date（long） 转换 String

     * @param time   time
     * *
     * *
     * @return String
     */
    fun date2String(time: String): String = time.substring(0,time.indexOf("T"))


    /**
     * hide keyboard
     */
    fun hideKeyboardFrom(context: Context, view: View) {
        val inputMethodManager = context.getSystemService(
                Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * show keyboard
     */
    fun showKeyboardFrom(context: Context, view: View) {
        val inputMethodManager = context.getSystemService(
                Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 保存Bitmap为图片
     */
    @Throws(Exception::class)
    fun saveBitmap(bitmap: Bitmap?, picPath: String) {
        bitmap ?: throw RuntimeException("bitmap is empty")
        val f = File(picPath + Constant.SUFFIX_JPEG)
        if (f.exists()) {
            f.delete()
        }
        try {
            val out = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            bitmap.recycle()
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            bitmap.recycle()
            throw FileNotFoundException()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            bitmap.recycle()
            throw IOException()
        }

    }

    fun start2web(url: String) {
        ARouter.getInstance()
                .build("/gank/web")
                .withString("url", url)
                .navigation()
    }

    fun update(activity: BaseActivity, showToast: Boolean = false) {
        // 版本检测方式2：带更新回调监听
        PgyUpdateManager.register(activity, "大阿斯顿",
                object : UpdateManagerListener() {
                    override fun onNoUpdateAvailable() {
                        if (showToast)
                            activity.toastMessage("已经是最新版本了")
                    }

                    override fun onUpdateAvailable(result: String) {
                        val jsonData: JSONObject = JSONObject(result)
                        if ("0" == jsonData.getString("code")) {
                            val jsonObject = jsonData.getJSONObject("data")
                            AlertDialog.Builder(activity)
                                    .setTitle("发现新版本：v${jsonObject.optString("versionName", "")}")
                                    .setMessage(jsonObject.optString("releaseNote", ""))
                                    .setPositiveButton("确定",{ _,_ ->
                                        startDownloadTask(activity, jsonObject.optString("downloadURL", ""))
                                    })
                                    .setNegativeButton("取消",{ dialogInterface, _ -> dialogInterface.cancel()})
                                    .show()
                        }else{
                            activity.toastMessage("网络错误！")
                        }
                    }
                })
    }

}
