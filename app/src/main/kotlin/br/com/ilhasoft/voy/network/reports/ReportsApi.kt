package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Report
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by lucasbarros on 08/01/18.
 */
interface ReportsApi {

    @GET("/api/reports/")
    fun getReports(@QueryMap parameters: Map<String, Int?>): Single<ReportsListResponse>

    @GET("/api/reports/{id}/")
    fun getReport(@Path("id") id: Int,
                  @QueryMap parameters: Map<String, Int?>): Single<Report>

    @POST("/api/reports/")
    fun saveReport(@Body body: CreateReportRequest): Single<Report>

    // FIXME: Server with error (returning error 500)
    @PUT("/api/reports/{id}/")
    fun updateReport(@Path("id") id: Int,
                     @QueryMap parameters: Map<String, Int?>,
                     @Body body: CreateReportRequest): Single<Report>

    // TODO: implement update patch

    // FIXME: Server with error (returning undefined)
    @DELETE("/api/reports/{id}/")
    fun deleteReport(@Path("id") id: Int,
                     @QueryMap parameters: Map<String, Int?>): Single<Void>
}