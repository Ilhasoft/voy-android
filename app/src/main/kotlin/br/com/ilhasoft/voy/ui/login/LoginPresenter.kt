package br.com.ilhasoft.voy.ui.login

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.BaseFactory
import br.com.ilhasoft.voy.network.authorization.AuthorizationRepository
import br.com.ilhasoft.voy.network.users.UserRepository
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper
import br.com.ilhasoft.voy.shared.helpers.RxHelper
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import retrofit2.HttpException
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LoginPresenter(
        private val authorizationRepository: AuthorizationRepository,
        private val userRepository: UserRepository,
        private val preferences: Preferences,
        private val scheduler: BaseScheduler) : Presenter<LoginContract>(LoginContract::class.java) {

    override fun attachView(view: LoginContract) {
        super.attachView(view)
        if (preferences.contains(User.TOKEN)) {
            BaseFactory.accessToken = preferences.getString(User.TOKEN)
            view.navigateToHome()
        }
    }

    fun onClickLogin(credentials: Credentials) {
        if (view.validate()) {
            authorizationRepository.loginWithCredentials(credentials)
                    .doOnNext({
                        preferences.put(User.TOKEN, it.token)
                        BaseFactory.accessToken = it.token
                    })
                    .concatMap { userRepository.getUser() }
                    .compose(RxHelper.defaultFlowableSchedulers())
                    .doOnNext {
                        if (it != null && it.isMapper)
                            view.showMessage(R.string.login_success)
                    }
                    .delay(1, TimeUnit.SECONDS)
                    .observeOn(scheduler.ui())
                    .subscribe({
                        if (it != null && it.isMapper) {
                            it.apply {
                                preferences.apply {
                                    put(User.ID, id)
                                    put(User.USERNAME, username)
                                    put(User.AVATAR, avatar)
                                    put(User.EMAIL, email)
                                }
                            }
                            view.navigateToHome()
                        } else {
                            preferences.clear()
                            view.showMessage(R.string.invalid_user)
                        }
                    }, {
                        Timber.e(it)
                        if (it is HttpException && it.code() == 400) {
                            view.showMessage(R.string.invalid_login)
                        } else {
                            ErrorHandlerHelper.showError(it, R.string.http_request_error) { msg ->
                                view.showMessage(msg)
                            }
                        }
                    })
        }
    }
}