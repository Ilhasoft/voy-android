package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Report
import io.reactivex.Single

/**
 * Created by felipe on 05/03/18.
 */
class ReportRepository(private val remoteReportDataSource: ReportDataSource) : ReportDataSource {

    override fun getReports(): Single<List<Report>> = remoteReportDataSource.getReports()
}