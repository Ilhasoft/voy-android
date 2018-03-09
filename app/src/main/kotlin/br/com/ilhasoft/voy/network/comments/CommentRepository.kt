package br.com.ilhasoft.voy.network.comments

import br.com.ilhasoft.voy.models.ReportComment
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

/**
 * Created by developer on 01/03/18.
 */
class CommentRepository(private val remoteCommentDataSource: CommentDataSource) : CommentDataSource {

    override fun getComments(reportId: Int): Flowable<List<ReportComment>> =
            remoteCommentDataSource.getComments(reportId)

    override fun saveComment(text: String, reportId: Int): Maybe<ReportComment> =
            remoteCommentDataSource.saveComment(text, reportId)

    override fun deleteComment(commentId: Int, reportId: Int?): Completable =
            remoteCommentDataSource.deleteComment(commentId, reportId)

}