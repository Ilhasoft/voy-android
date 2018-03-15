package br.com.ilhasoft.voy.ui.report

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.base.BaseView

/**
 * Created by developer on 11/01/18.
 */
interface ReportsContract : BaseView, CheckConnectionProvider{

    fun navigateBack()

    fun navigateToAddReport()

    fun navigateToReportDetail(report: Report)

    fun disableLoadDemand() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}