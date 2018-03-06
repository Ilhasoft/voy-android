package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

/**
 * Created by erickjones on 06/03/18.
 */
class ReportRepository(val remoteReportDataSource: ReportDataSource) : ReportDataSource {

    override fun getReports(
        page: Int?,
        page_size: Int?,
        theme: Int?,
        project: Int?,
        mapper: Int?,
        status: Int?
    ): Single<Response<Report>> =
        remoteReportDataSource.getReports(page, page_size, theme, project, mapper, status)

    override fun getReport(
        id: Int,
        theme: Int?,
        project: Int?,
        mapper: Int?,
        status: Int?
    ): Single<Report> =
        remoteReportDataSource.getReport(id, theme, project, mapper, status)

    override fun saveReport(
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        medias: List<File>
    ): Observable<Report> =
        remoteReportDataSource.saveReport(theme, location, description, name, tags, urls, medias)

    override fun updateReport(
        reportId: Int,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        newFiles: List<File>?,
        filesToDelete: List<Int>?
    ): Observable<Report> {
        return remoteReportDataSource.updateReport(
            reportId,
            theme,
            location,
            description,
            name,
            tags,
            urls,
            newFiles,
            filesToDelete
        )
    }

}