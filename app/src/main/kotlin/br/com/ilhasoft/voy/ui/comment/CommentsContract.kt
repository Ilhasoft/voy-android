package br.com.ilhasoft.voy.ui.comment

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.ui.base.EmptyView
import br.com.ilhasoft.voy.ui.base.LoadView

interface CommentsContract : BasicView, LoadView, EmptyView {

    fun navigateBack()

    fun navigateToRemoveComment(comment: CommentUIModel)

    fun populateComments(comments: List<CommentUIModel>)

    fun commentCreated()

    fun isValidCommentBodyState(): Boolean
}