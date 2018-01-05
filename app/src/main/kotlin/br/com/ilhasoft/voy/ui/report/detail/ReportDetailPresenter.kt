package br.com.ilhasoft.voy.ui.report.detail

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Indicator
import br.com.ilhasoft.voy.models.Report

class ReportDetailPresenter : Presenter<ReportDetailContract>(ReportDetailContract::class.java) {

    var report = Report()
        private set

    var indicator = Indicator(Uri.EMPTY,true)

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickPopupMenu() {
        view.showPopupMenu()
    }

    fun onClickCommentOnReport() {
        view.navigateToCommentReport()
    }

    fun swapPage(indicator: Indicator) {
        view.swapPage(indicator)
    }

    fun getIndicators(): Collection<Indicator> = report.mediaList.mapIndexed { index, it ->
        Indicator(it.uri,false, index)
    }

    fun setReportReference(report: Report){
        this.report = report
    }

}