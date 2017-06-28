package com.lv.gankio.helper

import android.support.v7.widget.helper.ItemTouchHelper



/**
 * Date: 2017-06-28
 * Time: 14:30
 * Description:
 */
/**
 * Interface to notify an item ViewHolder of relevant callbacks from [ ].

 * @author Paul Burke (ipaulpro)
 */
interface ItemTouchHelperViewHolder {

    /**
     * Called when the [ItemTouchHelper] first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    fun onItemSelected()


    /**
     * Called when the [ItemTouchHelper] has completed the move or swipe, and the active item
     * state should be cleared.
     */
    fun onItemClear()
}
