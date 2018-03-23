package br.com.ilhasoft.voy.ui.login

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.BaseFactory
import br.com.ilhasoft.voy.network.authorization.AuthorizationRepository
import br.com.ilhasoft.voy.network.users.UserRepository
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class LoginPresenter(
    private val authorizationRepository: AuthorizationRepository,
    private val userRepository: UserRepository,
    private val preferences: Preferences,
    private val scheduler: BaseScheduler
) : Presenter<LoginContract>(LoginContract::class.java) {

    fun checkPreferences() {
        if (preferences.contains(User.TOKEN)) {
            BaseFactory.accessToken = preferences.getString(User.TOKEN)
            view.navigateToHome()
        }
    }

    fun onClickLogin(credentials: Credentials) {
        if (view.validate()) {
            authorizationRepository.loginWithCredentials(credentials)
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.dismissLoading() }
                .doOnNext({
                    preferences.put(User.TOKEN, it.token)
                    BaseFactory.accessToken = it.token
                })
                .flatMapSingle { userRepository.getUser() }
                .fromIoToMainThread(scheduler)
                .delay(300, TimeUnit.MILLISECONDS, scheduler.computation())
                .onMainThread(scheduler)
                .subscribe(
                    { checkUser(it) },
                    { handlerError(it) }
                )
        }
    }

    private fun handlerError(throwable: Throwable) {
        if (throwable is HttpException && throwable.code() == 400) {
            view.showMessage(R.string.invalid_login)
        } else {
            ErrorHandlerHelper.showError(throwable, R.string.http_request_error) { msg ->
                view.showMessage(msg)
            }
        }
    }

    private fun checkUser(user: User?) {
        if (user != null && user.isMapper) {
            navigateToHome(user)
        } else {
            preferences.clear()
            view.showMessage(R.string.invalid_user)
        }
    }

    private fun navigateToHome(user: User) {
        user.apply {
            preferences.apply {
                put(User.ID, id)
                put(User.USERNAME, username)
                put(User.AVATAR, avatar)
                put(User.EMAIL, email)
            }
        }
        view.navigateToHome()
    }
}