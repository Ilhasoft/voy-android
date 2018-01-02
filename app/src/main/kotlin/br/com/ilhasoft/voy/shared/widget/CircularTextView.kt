package br.com.ilhasoft.voy.shared.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import br.com.ilhasoft.voy.R

/**
 * Created by jones on 12/27/17.
 */
class CircularTextView : TextView {

    private var circlePaint: Paint? = null
    private var circleColor: Int? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(R.styleable.CircularTextView)

        try{
            circleColor = a.getColor(R.styleable.CircularTextView_circleColor, R.color.vivid_purple_two_25)
        }finally {
            a.recycle()
        }
        circlePaint = Paint()
    }

    override fun onDraw(canvas: Canvas?) {
        val viewWidthHalf = measuredWidth / 2
        val viewHeightHalf = measuredHeight / 2

        var radius = if(viewWidthHalf > viewHeightHalf)
            viewHeightHalf - 10
        else
            viewWidthHalf - 10

        circleColor?.let {
            circlePaint?.color = it
        }

        canvas?.drawCircle(viewWidthHalf.toFloat(), viewHeightHalf.toFloat(),
                radius.toFloat(), circlePaint)
        super.onDraw(canvas)
    }

    fun setCircleColor(color: Int) {
        circleColor = color
        invalidate()
        requestLayout()
    }
}