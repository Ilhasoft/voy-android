package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

/**
 * Created by erickjones on 06/03/18.
 */
class ReportRepository(
    private val remoteReportDataSource: ReportDataSource,
    private val localDataSource: ReportDataSource,
    private val connectionProvider: CheckConnectionProvider
) : ReportDataSource {

    override fun getReports(theme: Int?, project: Int?, mapper: Int?, status: Int?, page: Int?, page_size: Int?): Single<Pair<Int, List<Report>>> {
        return if (connectionProvider.hasConnection()) {
            remoteReportDataSource.getReports(theme, project, mapper, status)
        } else {
            localDataSource.getReports(theme, project, mapper, status)
        }
    }

    override fun saveReport(report: Report): Single<Report> = Single.just(report)

    override fun saveReports(reports: List<Report>): Single<List<Report>> {
        return if (connectionProvider.hasConnection()) {
            localDataSource.saveReports(reports)
        } else {
            Single.just(reports)
        }
    }

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

    override fun saveFile(file: File, reportId: Int): Single<ReportFile> =
            remoteReportDataSource.saveFile(file, reportId)
}