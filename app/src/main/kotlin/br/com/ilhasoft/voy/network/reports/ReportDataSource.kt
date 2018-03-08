package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Report
import io.reactivex.Single

/**
 * Created by felipe on 05/03/18.
 */
interface ReportDataSource {

    fun getReports(theme: Int? = null, project: Int? = null,
                   mapper: Int? = null, status: Int? = null): Single<List<Report>>
    fun saveReport(report: Report): Single<Report>
}