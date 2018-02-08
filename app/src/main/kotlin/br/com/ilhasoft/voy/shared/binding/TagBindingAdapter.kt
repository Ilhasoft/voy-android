package br.com.ilhasoft.voy.shared.binding

import android.databinding.BindingAdapter
import android.graphics.drawable.GradientDrawable
import android.widget.TextView

/**
 * Created by erickjones on 06/02/18.
 */
class TagBindingAdapter {

    companion object {
        @JvmStatic
        @BindingAdapter(value = *arrayOf("backgroundColor", "textColor", "textSize"), requireAll = true)
        fun loadTextAppearance(textView: TextView, backgroundColor: Int, colorRes: Int, size: Float) = with(textView) {
            val shape = GradientDrawable()
            shape.cornerRadius = 10F
            shape.setColor(backgroundColor)
            background = shape
            setTextColor(colorRes)
            textSize = size
        }
    }
}