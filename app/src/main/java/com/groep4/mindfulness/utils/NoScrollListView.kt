package com.groep4.mindfulness.utils

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ListView

class NoScrollListView : ListView {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
    constructor(ctx: Context, attrs: AttributeSet, defStyle : Int) : super(ctx, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE shr(2) , MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom)
        var params: ViewGroup.LayoutParams = layoutParams
        params.height = measuredHeight
    }
}