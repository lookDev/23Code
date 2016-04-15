package com.mrper.code23.fewk.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import com.etsy.android.grid.StaggeredGridView;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.mrper.code23.R;

/**
 * Created by admin on 2016/3/11.
 * 瀑布流控件
 */
public class PullToRefreshStaggeredGridView extends PullToRefreshAdapterViewBase<StaggeredGridView> {

    public PullToRefreshStaggeredGridView(Context context) {
        super(context);
    }

    public PullToRefreshStaggeredGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshStaggeredGridView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshStaggeredGridView(Context context, Mode mode, PullToRefreshBase.AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public PullToRefreshBase.Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected StaggeredGridView createRefreshableView(Context context, AttributeSet attrs) {
        StaggeredGridView scrollView = new StaggeredGridView(context, attrs);
        scrollView.setId(R.id.PullToRefreshMultiColumnListView);
        return scrollView;
    }

    public Object getItemAtPosition(int position){
        ListAdapter adapter = getRefreshableView().getAdapter();
        if(adapter == null) return null;
        return adapter.getItem(position);
    }

}
