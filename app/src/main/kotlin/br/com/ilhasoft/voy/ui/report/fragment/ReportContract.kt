package br.com.ilhasoft.voy.ui.report.fragment

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.base.BaseView
import java.io.File

interface ReportContract : BaseView {

    fun getStatus(): Int?

    fun fillReportsAdapter(reports: List<Report>)

    fun navigateToReportDetail(report: Report)

}