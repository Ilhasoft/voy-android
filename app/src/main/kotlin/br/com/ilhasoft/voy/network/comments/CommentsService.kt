package br.com.ilhasoft.voy.network.comments

import br.com.ilhasoft.voy.models.ReportComment
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Flowable

/**
 * Created by lucasbarros on 10/01/18.
 */
class CommentsService : ServiceFactory<CommentsApi>(CommentsApi::class.java) {

    fun getComments(reportId: Int): Flowable<List<ReportComment>> = api.getComments(reportId)

}