package br.com.ilhasoft.voy.network.comments

import br.com.ilhasoft.voy.models.ReportComment
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by lucasbarros on 10/01/18.
 */
interface CommentsApi {

    @GET("/api/report-comments/")
    fun getComments(@Query("report") id: Int): Flowable<List<ReportComment>>

    @GET("/api/report-comments/{id}/")
    fun getComment(@Path("id") id: Int, @Query("report") reportId: Int?): Single<ReportComment>
}