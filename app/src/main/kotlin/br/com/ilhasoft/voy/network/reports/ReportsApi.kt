package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Report
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * Created by lucasbarros on 08/01/18.
 */
interface ReportsApi {

    @GET("/api/reports/")
    fun getReports(@QueryMap parameters: Map<String, Int?>): Single<Response<Report>>

    @POST("/api/reports/")
    fun createReport(@Body body: CreateReportRequest): Single<Report>

}