package br.com.ilhasoft.voy.ui.report

/**
 * Created by erickjones on 15/03/18.
 */
interface RequestReportListener {
    fun requestMoreReports(url: String, status: Int)
}