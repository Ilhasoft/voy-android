package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Report
import io.reactivex.Single

/**
 * Created by felipe on 05/03/18.
 */
interface ReportDataSource {

    fun getReports(): Single<List<Report>>
}