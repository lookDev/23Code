package com.mrper.code23.fewk.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView

/**
 * Created by admin on 2016/4/19.
 */
class FullListView : ListView {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE,MeasureSpec.AT_MOST))
    }
}