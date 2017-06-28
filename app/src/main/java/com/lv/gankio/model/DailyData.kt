package com.lv.gankio.model

import com.google.gson.annotations.SerializedName

/**
 * Date: 2017-06-24
 * Time: 15:55
 * Description:
 */
data class DailyData(
        @SerializedName("Android")
        var android: List<DetailsData>?,
        @SerializedName("App")
        var app: List<DetailsData>?,
        @SerializedName("iOS")
        var ios: List<DetailsData>?,
        @SerializedName("休息视频")
        var video: List<DetailsData>?,
        @SerializedName("前端")
        var js: List<DetailsData>?,
        @SerializedName("拓展资源")
        var res: List<DetailsData>?,
        @SerializedName("瞎推荐")
        var rec: List<DetailsData>?,
        @SerializedName("福利")
        var bonus: List<DetailsData>?)