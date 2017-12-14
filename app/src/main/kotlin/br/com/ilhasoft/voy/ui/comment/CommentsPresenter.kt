package br.com.ilhasoft.voy.ui.comment

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Comment

class CommentsPresenter : Presenter<CommentsContract>(CommentsContract::class.java) {

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickEditComment(comment: Comment?) {
        view.navigateToEditComment(comment)
    }

    fun onClickRemoveComment(comment: Comment?) {
        view.navigateToRemoveComment(comment)
    }

}