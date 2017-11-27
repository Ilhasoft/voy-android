package br.com.ilhasoft.voy.shared.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by lucasbarros on 23/11/17.
 */
class SquareLinearLayout : LinearLayout {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }

}