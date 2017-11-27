package br.com.ilhasoft.voy.ui.login

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Credentials

class LoginPresenter : Presenter<LoginContract>(LoginContract::class.java) {

    fun onClickLogin(credentials: Credentials) {
        if (view.validate()) {
            view.navigateToHome()
        }
    }

}