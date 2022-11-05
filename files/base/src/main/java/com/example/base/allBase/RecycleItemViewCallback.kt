package com.example.base.allBase

import android.view.View

/**
 * * Created by asadul haque on 11/14/22.
 * pran-rfl group
 */
interface RecycleItemViewCallback<T> {
    fun onItemViewClicked(item: T?, position: Int)
    fun onItemViewClicked(item: T?, position: Int, view: View? = null) {
        onItemViewClicked(item, position)
    }
}