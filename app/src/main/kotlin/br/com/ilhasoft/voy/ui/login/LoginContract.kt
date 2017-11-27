package br.com.ilhasoft.voy.ui.login

import br.com.ilhasoft.support.core.mvp.BasicView

interface LoginContract : BasicView {

    fun validate(): Boolean

    fun showErrorMessage(message: CharSequence)

    fun navigateToHome()

}