package br.com.ilhasoft.voy.network.comments

import br.com.ilhasoft.voy.models.ReportComment
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

/**
 * Created by developer on 01/03/18.
 */
interface CommentDataSource {

    fun getComments(reportId: Int): Flowable<List<ReportComment>>

    fun saveComment(text: String, reportId: Int): Maybe<ReportComment>

    fun deleteComment(commentId: Int, reportId: Int? = null): Completable

}