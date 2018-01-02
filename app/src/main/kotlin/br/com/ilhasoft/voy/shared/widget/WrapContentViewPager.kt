package br.com.ilhasoft.voy.shared.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet

/**
 * Created by jones on 12/27/17.
 */
class WrapContentViewPager : ViewPager {

    private var currentPagePosition = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val child = getChildAt(currentPagePosition)
        var heightMeasure = heightMeasureSpec
        child?.let {
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            var childHeight = child.measuredHeight
            heightMeasure = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasure)
    }

    fun reMeasureCurrentPage(position: Int) {
        currentPagePosition = position
        requestLayout()
    }
}