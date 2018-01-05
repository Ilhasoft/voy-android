package br.com.ilhasoft.voy.ui.report.detail

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Indicator
import br.com.ilhasoft.voy.models.Media
import br.com.ilhasoft.voy.models.Report

class ReportDetailPresenter : Presenter<ReportDetailContract>(ReportDetailContract::class.java) {

    //FIXME remove after consuming API
    var report = Report(title = "Just a Title to the Report",
            description = "Just a Simple Description!",
            createdAt = "12/21/2017",
            mediaList = mutableListOf(
                    Media(type = Media.TYPE_IMAGE, url = "http://voy-dev.ilhasoft.mobi/media/content/2017/12/15/48acb0e838d8be41.png"),
                    Media(type = Media.TYPE_IMAGE, url = "http://voy-dev.ilhasoft.mobi/media/content/2017/12/15/6e74dc48996e9b06.png"),
                    Media(type = Media.TYPE_IMAGE, url = "http://voy-dev.ilhasoft.mobi/media/content/2017/12/15/cd414beb7cee9115.png"),
                    Media(type = Media.TYPE_VIDEO, url = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4")
            ))
        private set

    var indicator = Indicator(Uri.EMPTY, true)

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
        Indicator(it.uri, false, index)
    }

    fun setReportReference(report: Report) {
        this.report = report
    }

}