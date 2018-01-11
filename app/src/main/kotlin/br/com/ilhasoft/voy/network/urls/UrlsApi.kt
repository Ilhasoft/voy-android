package br.com.ilhasoft.voy.network.urls

import br.com.ilhasoft.voy.models.ReportUrl
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by lucasbarros on 10/01/18.
 */
interface UrlsApi {

    @GET("/api/report-urls/")
    fun getUrls(@Query("report") id: Int): Flowable<List<ReportUrl>>

    @POST("/api/report-urls/")
    fun saveUrl(@Body body: CreateUrlRequest): Single<ReportUrl>

}