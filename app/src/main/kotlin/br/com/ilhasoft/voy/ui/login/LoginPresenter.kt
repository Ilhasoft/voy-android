package br.com.ilhasoft.voy.ui.login

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.network.BaseFactory
import br.com.ilhasoft.voy.network.authorization.AuthorizationService
import br.com.ilhasoft.voy.network.users.UserService
import br.com.ilhasoft.voy.shared.helpers.RxHelper
import timber.log.Timber

class LoginPresenter(private val preferences: Preferences) : Presenter<LoginContract>(LoginContract::class.java) {

    companion object {
        private const val TOKEN = "token"
        private const val USER_ID = "userId"
    }

    private var authorizationService: AuthorizationService = AuthorizationService()
    private var userService = UserService()

    override fun attachView(view: LoginContract) {
        super.attachView(view)
        if (preferences.contains(TOKEN)) {
            BaseFactory.accessToken = preferences.getString(TOKEN)
            view.navigateToHome()
        }
    }

    fun onClickLogin(credentials: Credentials) {
        if (view.validate()) {
            authorizationService.loginWithCredentials(credentials)
                    .doOnNext({
                        preferences.put(TOKEN, it.token)
                        BaseFactory.accessToken = it.token
                    })
                    .concatMap { userService.getUser() }
                    .compose(RxHelper.defaultFlowableSchedulers())
                    .subscribe({
                        if (it.isNotEmpty()) {
                            preferences.put(USER_ID, it[0].id)
                            view.navigateToHome()
                        }
                    }, {
                        Timber.e(it)
                        view.showMessage(R.string.invalid_login)
                    })
        }
    }
}