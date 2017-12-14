package br.com.ilhasoft.voy.ui.comment

import br.com.ilhasoft.support.core.mvp.Presenter

class CommentsPresenter : Presenter<CommentsContract>(CommentsContract::class.java) {

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickPopupMenu() {
        view.showPopupMenu()
    }

}