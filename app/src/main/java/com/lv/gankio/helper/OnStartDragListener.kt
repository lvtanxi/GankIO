package com.lv.gankio.helper

import android.support.v7.widget.RecyclerView



/**
 * Date: 2017-06-28
 * Time: 14:30
 * Description:
 */
interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.

     * @param viewHolder The holder of the view to drag.
     */
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)

}