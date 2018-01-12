package br.com.ilhasoft.voy.network.files

import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.network.reports.Response
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by lucasbarros on 11/01/18.
 */
interface FilesApi {

    @GET("/api/report-files/")
    fun getFiles(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Single<Response<ReportFile>>

    @Multipart
    @POST("/api/report-files/")
    fun saveFile(@PartMap map: Map<String, RequestBody>,
                 @Part file: MultipartBody.Part): Single<ReportFile>

    // FIXME: Server error (returning "undefined")
    @DELETE("/api/report-files/{id}/")
    fun deleteFile(@Path("id") id: Int, @QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Single<Void>

}