package br.com.ilhasoft.voy.ui.report

/**
 * Created by felipe on 16/03/18.
 */
interface LoadReports {

    fun loadReports(theme: Int, reportStatus: Int?, page: Int?, pageSize: Int)
}