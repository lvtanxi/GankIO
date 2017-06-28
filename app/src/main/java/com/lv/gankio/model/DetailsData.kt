package com.lv.gankio.model

import com.lv.gankio.adapter.decorate.LMultiItem
import java.util.*

/**
 * Date: 2017-06-24
 * Time: 15:54
 * Description:
 */
data class DetailsData(var _id: String,
                       var createdAt: String,
                       var desc: String,
                       var publishedAt: Date,
                       var source: String,
                       var type: String,
                       var url: String,
                       var isUsed: Boolean,
                       var who: String,
                       var images: List<String>?):LMultiItem{
    override fun getItemType()= if("福利"==type) 0 else 1

}