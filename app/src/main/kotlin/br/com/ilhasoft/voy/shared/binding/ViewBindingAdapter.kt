package br.com.ilhasoft.voy.shared.binding

import android.databinding.BindingAdapter
import android.view.View

/**
 * Created by felipe on 01/03/18.
 */
@BindingAdapter("isVisible")
fun isVisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}