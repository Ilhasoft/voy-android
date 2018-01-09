package br.com.ilhasoft.voy.ui.login

import android.util.Log
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.network.BaseFactory
import br.com.ilhasoft.voy.network.authorization.AuthorizationService
import br.com.ilhasoft.voy.shared.rx.RxHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginPresenter(private val preferences: Preferences) : Presenter<LoginContract>(LoginContract::class.java) {

    companion object {
        private const val TOKEN = "token"
    }

    private var authorizationService: AuthorizationService = AuthorizationService()

    override fun attachView(view: LoginContract) {
        super.attachView(view)
        if (preferences.contains(TOKEN)) {
            setTokenAndNavigateToHome(preferences.getString(TOKEN))
        }
    }

    fun onClickLogin(credentials: Credentials) {
        if (view.validate()) {
            authorizationService.loginWithCredentials(credentials)
                    .compose(RxHelper.defaultSchedulers())
                    .subscribe({
                        preferences.put(TOKEN, it.token)
                        setTokenAndNavigateToHome(it.token)
                    }, {
                        Log.e(LoginPresenter::class.toString(), it.localizedMessage)
                        view.showMessage(R.string.invalid_login)
                    })
        }
    }

    private fun setTokenAndNavigateToHome(token: String) {
        BaseFactory.accessToken = token
        view.navigateToHome()
    }

}