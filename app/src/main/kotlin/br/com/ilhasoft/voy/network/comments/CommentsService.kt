package br.com.ilhasoft.voy.network.comments

import br.com.ilhasoft.voy.models.ReportComment
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

/**
 * Created by lucasbarros on 10/01/18.
 */
class CommentsService : ServiceFactory<CommentsApi>(CommentsApi::class.java), CommentDataSource {

    override fun getComments(reportId: Int): Flowable<List<ReportComment>> = api.getComments(reportId)

    override fun saveComment(text: String, reportId: Int): Maybe<ReportComment> {
        val requestBody = CreateCommentsRequest(text, reportId)
        return api.saveComment(requestBody)
    }

    override fun deleteComment(commentId: Int, reportId: Int?): Completable =
            api.deleteComment(commentId, reportId)

}