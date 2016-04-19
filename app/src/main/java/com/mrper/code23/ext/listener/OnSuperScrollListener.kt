package com.mrper.code23.ext.listener

import android.app.Application
import android.widget.AbsListView
import com.bumptech.glide.Glide

/**
 * Created by Mrper on 16/04/17.
 *
 */
open class OnSuperScrollListener(val context: Application) : AbsListView.OnScrollListener {

    private var scrollState: Int = AbsListView.OnScrollListener.SCROLL_STATE_IDLE

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        when(scrollState){
            AbsListView.OnScrollListener.SCROLL_STATE_IDLE,
            AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL-> Glide.with(context).resumeRequestsRecursive()
            AbsListView.OnScrollListener.SCROLL_STATE_FLING -> Glide.with(context).pauseRequestsRecursive()
        }
    }

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        this@OnSuperScrollListener.scrollState = scrollState
    }

}