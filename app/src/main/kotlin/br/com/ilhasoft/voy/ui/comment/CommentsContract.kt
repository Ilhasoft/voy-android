package br.com.ilhasoft.voy.ui.comment

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.ReportComment

interface CommentsContract : BasicView {

    fun navigateBack()

    fun navigateToEditComment(reportComment: ReportComment?)

    fun navigateToRemoveComment(reportComment: ReportComment?)

    fun sendComment(reportComment: ReportComment?)

}