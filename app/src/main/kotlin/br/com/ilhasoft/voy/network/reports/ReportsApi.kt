package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by lucasbarros on 08/01/18.
 */
interface ReportsApi {

    @GET("/api/reports/")
    fun getReports(@QueryMap parameters: Map<String, Int?>): Single<Response<Report>>

    @GET("/api/reports/{id}/")
    fun getReport(@Path("id") id: Int,
                  @QueryMap parameters: Map<String, Int?>): Single<Report>

    @POST("/api/reports/")
    fun saveReport(@Body body: ReportRequest): Single<Report>

    @PUT("/api/reports/{id}/")
    fun updateReport(@Path("id") id: Int,
                     @Body body: ReportRequest): Single<Report>

    //Files
    @Multipart
    @POST("/api/report-files/")
    fun saveFile(@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>,
                 @Part file: MultipartBody.Part): Single<ReportFile>

    @DELETE("/api/report-files/{id}/")
    fun deleteFile(@Path("id") id: Int): Completable

    @POST("/api/report-files/delete/")
    fun deleteFiles(@Query("ids") ids: String): Completable
}