package br.com.ilhasoft.voy.ui.account

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.users.UserService
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.loadControl
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class AccountPresenter(
        private val accountInteractor: AccountInteractor
) : Presenter<AccountContract>(AccountContract::class.java) {

    var avatarDrawableId: Int? = null
    private val compositeDisposable = CompositeDisposable()
    private val userService = UserService()

    override fun start() {
        super.start()
        loadUser()
        //getUser()
    }

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }

    private fun loadUser() {
        accountInteractor.getUser()
                .fromIoToMainThread()
                .subscribe(
                        { user ->
                            setAvatarByPosition(user.avatar.extractNumbers())
                            view.setUser(user)
                        },
                        {
                            ErrorHandlerHelper.showError(it) { msg ->
                                view.showMessage(msg)
                            }
                        }
                )

    }
//
//    private fun getUser() {
//        compositeDisposable.add(
//                userService.getUser()
//                        .fromIoToMainThread()
//                        .loadControl(view)
//                        .filter { it.isNotEmpty() }
//                        .doOnNext { setAvatarByPosition(it.first().avatar.extractNumbers()) }
//                        .subscribe(
//                                { view.setUser(it.first()) },
//                                {
//                                    ErrorHandlerHelper.showError(it) { msg ->
//                                        view.showMessage(msg)
//                                    }
//                                }
//                        )
//        )
//    }

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
//        preferences.remove(User.TOKEN)
//        preferences.remove(User.ID)
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