package br.com.ilhasoft.voy.network.files

import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.network.reports.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by lucasbarros on 11/01/18.
 */
interface FilesApi {

    @GET("/api/report-files/")
    fun getFiles(@QueryMap map: Map<String, @JvmSuppressWildcards Any?>): Single<Response<ReportFile>>

}