package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import br.com.ilhasoft.voy.shared.extensions.loadControl
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class AccountPresenter(
        private val accountInteractor: AccountInteractor
) : Presenter<AccountContract>(AccountContract::class.java) {

    var avatarDrawableId: Int? = null
    private val compositeDisposable = CompositeDisposable()

    override fun start() {
        super.start()
        loadUser()
    }

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }

    private fun loadUser() {
        compositeDisposable.add(
                accountInteractor.getUser()
                        .subscribe(
                                {
                                    it?.apply {
                                        setAvatarByPosition(avatar.extractNumbers())
                                        view.setUser(it)
                                    }
                                },
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
                accountInteractor.editUser(user)
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

    private fun setAvatarByPosition(position: String) {
        view.setAvatarByPosition(position.toInt())
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickSaveMyAccount() {
        compositeDisposable.add(
                Single.just(view)
                        .filter { it.isValidUser() }
                        .subscribe { view.saveUser() }
        )
    }

    fun onClickSwitchAvatar() {
        view.navigateToSwitchAvatar()
    }

    fun onClickLogout() {
        accountInteractor.removeUserPreferencesEntries();
        view.navigateToMakeLogout()
    }

    fun onClickLock() {
        view.changeLockState()
    }

    fun setSelectedAvatar(drawableId: Int?, position: Int) {
        avatarDrawableId = drawableId
        drawableId?.let { view.swapAvatar(it, position) }
    }

    fun getSelectedAvatar() = avatarDrawableId

}