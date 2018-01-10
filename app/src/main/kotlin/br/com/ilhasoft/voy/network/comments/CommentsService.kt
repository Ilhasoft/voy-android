package br.com.ilhasoft.voy.network.comments

import br.com.ilhasoft.voy.models.ReportComment
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by lucasbarros on 10/01/18.
 */
class CommentsService : ServiceFactory<CommentsApi>(CommentsApi::class.java) {

    fun getComments(reportId: Int): Flowable<List<ReportComment>> = api.getComments(reportId)

    fun getComment(commentId: Int, reportId: Int? = null): Single<ReportComment> = api.getComment(commentId, reportId)

    fun saveComment(text: String, createdBy: User?, reportId: Int): Single<ReportComment> {
        val requestBody = CreateCommentsRequest(text, createdBy, reportId)
        return api.saveComment(requestBody)
    }
}