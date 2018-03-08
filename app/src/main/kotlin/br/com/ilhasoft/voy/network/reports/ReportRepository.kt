package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Report
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by felipe on 05/03/18.
 */
class ReportRepository(
    private val remoteReportDataSource: ReportDataSource,
    private val localDataSource: ReportDataSource,
    private val checkConnectionProvider: CheckConnectionProvider
) : ReportDataSource {

    override fun getReports(theme: Int?, project: Int?, mapper: Int?, status: Int?): Single<List<Report>> {
        return if (checkConnectionProvider.hasConnection()) {
            remoteReportDataSource.getReports(theme, project, mapper, status)
                .flatMap { saveOnCache(it) }
        } else {
            localDataSource.getReports(theme, project, mapper, status)
        }
    }

    // TODO real implementation
    override fun saveReport(report: Report): Single<Report> = Single.just(report)

    private fun saveOnCache(reports: List<Report>): Single<List<Report>> {
        return Flowable.just(reports)
            .flatMap { Flowable.fromIterable(it) }
            .flatMapSingle { localDataSource.saveReport(it) }
            .toList()
    }
}