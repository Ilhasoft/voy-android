package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

/**
 * Created by erickjones on 06/03/18.
 */
interface ReportDataSource {

    fun getReports(
        page: Int? = null,
        page_size: Int? = null,
        theme: Int? = null,
        project: Int? = null,
        mapper: Int? = null,
        status: Int? = null
    ): Single<Response<Report>>

    fun getReport(
        id: Int,
        theme: Int? = null,
        project: Int? = null,
        mapper: Int? = null,
        status: Int? = null
    ): Single<Report>

    fun saveReport(
        theme: Int, location: Location, description: String?, name: String,
        tags: List<String>, urls: List<String>?, medias: List<File>
    ): Observable<Report>

    fun updateReport(
        reportId: Int,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        newFiles: List<File>?,
        filesToDelete: List<Int>?
    ): Observable<Report>
}