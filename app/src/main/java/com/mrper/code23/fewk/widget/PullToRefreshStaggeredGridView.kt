package com.mrper.code23.fewk.widget


import android.content.Context
import android.util.AttributeSet
import com.etsy.android.grid.StaggeredGridView
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase
import com.handmark.pulltorefresh.library.PullToRefreshBase
import com.mrper.code23.R
import com.mrper.code23.data.adapter.DemoAdapter

/**
 * Created by admin on 2016/3/11.
 * 瀑布流控件
 */
class PullToRefreshStaggeredGridView : PullToRefreshAdapterViewBase<StaggeredGridView> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, mode: PullToRefreshBase.Mode) : super(context, mode)

    constructor(context: Context, mode: PullToRefreshBase.Mode, animStyle: PullToRefreshBase.AnimationStyle) : super(context, mode, animStyle)

    override fun getPullToRefreshScrollDirection(): PullToRefreshBase.Orientation = PullToRefreshBase.Orientation.VERTICAL

    override fun createRefreshableView(context: Context, attrs: AttributeSet): StaggeredGridView {
        val scrollView = StaggeredGridView(context, attrs)
        scrollView.id = R.id.PullToRefreshMultiColumnListView
        return scrollView
    }

    fun getItemAtPosition(position: Int): Any? {
        val adapter = refreshableView.adapter ?: return null
        return adapter.getItem(position)
    }

    /** 滑动大顶部  */
    fun smoothScroll2Top() {
        val contentView = refreshableView
        contentView.resetToTop()
        val adapter = contentView.adapter as? DemoAdapter?
        adapter?.notifyDataSetChanged()
    }

}
