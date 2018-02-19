package br.com.ilhasoft.voy.ui.login

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.BaseFactory
import br.com.ilhasoft.voy.network.authorization.AuthorizationService
import br.com.ilhasoft.voy.network.users.UserService
import br.com.ilhasoft.voy.shared.helpers.RxHelper
import timber.log.Timber

class LoginPresenter(private val preferences: Preferences) : Presenter<LoginContract>(LoginContract::class.java) {

    private var authorizationService: AuthorizationService = AuthorizationService()
    private var userService = UserService()

    override fun attachView(view: LoginContract) {
        super.attachView(view)
        if (preferences.contains(User.TOKEN)) {
            BaseFactory.accessToken = preferences.getString(User.TOKEN)
            view.navigateToHome()
        }
    }

    fun onClickLogin(credentials: Credentials) {
        if (view.validate()) {
            authorizationService.loginWithCredentials(credentials)
                    .doOnNext({
                        preferences.put(User.TOKEN, it.token)
                        BaseFactory.accessToken = it.token
                    })
                    .concatMap { userService.getUser() }
                    .compose(RxHelper.defaultFlowableSchedulers())
                    .subscribe({
                        if (it.isNotEmpty() && it.first().isMapper) {
                            preferences.put(User.ID, it.first().id)
                            view.navigateToHome()
                        } else if (!it.first().isMapper) {
                            preferences.clear()
                            view.showMessage(R.string.invalid_user)
                        }
                    }, {
                        Timber.e(it)
                        view.showMessage(R.string.invalid_login)
                    })
        }
    }
}