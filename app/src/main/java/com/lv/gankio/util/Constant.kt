package com.lv.gankio.util

import com.lv.gankio.R
import java.util.*

object Constant {

    val ONE_SECOND: Long = 1000
    val ONE_MINUTE = ONE_SECOND * 60
    val ONE_HOUR = ONE_MINUTE * 60
    val ONE_DAY = ONE_HOUR * 24

    val SUFFIX_JPEG = ".jpg"

      var sTypeColor: HashMap<String, Int> = object : HashMap<String, Int>() {
          init {
              put("all", R.drawable.bg_all_tag)
              put("Android", R.drawable.bg_android_tag)
              put("iOS", R.drawable.bg_ios_tag)
              put("瞎推荐", R.drawable.bg_rec_tag)
              put("拓展资源", R.drawable.bg_res_tag)
              put("App", R.drawable.bg_app_tag)
              put("福利", R.drawable.bg_bonus_tag)
              put("前端", R.drawable.bg_js_tag)
              put("休息视频", R.drawable.bg_video_tag)
          }
      }

    var sCategoryList: ArrayList<String> = object : ArrayList<String>() {
        init {
            add("all")
            add("App")
            add("Android")
            add("iOS")
            add("前端")
            add("拓展资源")
            add("瞎推荐")
            add("休息视频")
            add("福利")
        }
    }
    var sCategryListChanged: Boolean=false


}
