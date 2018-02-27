package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.voy.models.Report
import io.reactivex.Flowable

/**
 * Created by lucasbarros on 09/02/18.
 */
interface ReportInteractor {
    fun getReports(
        page: Int? = null,
        pageSize: Int? = null,
        theme: Int? = null,
        mapper: Int? = null,
        status: Int? = null
    ): Flowable<List<Report>>
}