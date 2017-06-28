package com.lv.gankio.adapter.decorate

/**
 * Date: 2017-06-26
 * Time: 16:26
 * Description:
 */
interface LMultiItem {
    fun getItemType():Int

    companion object {
        val SECTION_HEADER_VIEW = 444
    }
}