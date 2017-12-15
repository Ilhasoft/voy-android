package br.com.ilhasoft.voy.ui.comment

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Comment

interface CommentsContract : BasicView {

    fun navigateBack()

    fun navigateToEditComment(comment: Comment?)

    fun navigateToRemoveComment(comment: Comment?)

    fun sendComment(comment: Comment?)

}