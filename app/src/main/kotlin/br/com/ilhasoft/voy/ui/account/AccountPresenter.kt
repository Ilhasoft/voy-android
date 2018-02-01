package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserService
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.loadControl
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class AccountPresenter(
        private val userService: UserService,
        private val preferences: Preferences
) : Presenter<AccountContract>(AccountContract::class.java) {

    private var avatarDrawableId: Int? = null
    private val compositeDisposable = CompositeDisposable()

    override fun start() {
        super.start()
        getUser()
    }

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }

    private fun getUser() {
        compositeDisposable.add(
                userService.getUser()
                        .fromIoToMainThread()
                        .loadControl(view)
                        .filter { it.isNotEmpty() }
                        .subscribe(
                                { view.setUser(it.first()) },
                                {
                                    ErrorHandlerHelper.showError(it) { msg ->
                                        view.showMessage(msg)
                                    }
                                }
                        )
        )
    }

    fun saveUser(user: User) {
        compositeDisposable.add(
                userService.editUser(user)
                        .fromIoToMainThread()
                        .loadControl(view)
                        .subscribe(
                                { view.userUpdatedMessage() },
                                {
                                    ErrorHandlerHelper.showError(it) { msg ->
                                        view.showMessage(msg)
                                    }
                                }
                        )
        )
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickSaveMyAccount() {
        Single.just(view)
                .filter { it.isValidUser() }
                .subscribe { view.saveUser() }
    }

    fun onClickSwitchAvatar() {
        view.navigateToSwitchAvatar()
    }

    fun onClickLogout() {
        preferences.remove(User.TOKEN)
        preferences.remove(User.ID)
        view.navigateToMakeLogout()
    }

    fun setSelectedAvatar(drawableId: Int?) {
        avatarDrawableId = drawableId
        view.swapAvatar()
    }

    fun getSelectedAvatar() = avatarDrawableId

}