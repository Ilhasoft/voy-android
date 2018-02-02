package br.com.ilhasoft.voy.ui.base

import android.support.annotation.StringRes

/**
 * Created by felipe on 01/02/18.
 */
interface BaseView : LoadView {

    fun showMessage(@StringRes messageId: Int)

    fun showMessage(message: CharSequence)
}