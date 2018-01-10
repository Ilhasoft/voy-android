package br.com.ilhasoft.voy.ui.comment

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.ReportComment

class CommentsPresenter : Presenter<CommentsContract>(CommentsContract::class.java) {

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickEditComment(reportComment: ReportComment?) {
        view.navigateToEditComment(reportComment)
    }

    fun onClickRemoveComment(reportComment: ReportComment?) {
        view.navigateToRemoveComment(reportComment)
    }

    fun onClickSendComment(reportComment: ReportComment?) {
        view.sendComment(reportComment)
    }

}