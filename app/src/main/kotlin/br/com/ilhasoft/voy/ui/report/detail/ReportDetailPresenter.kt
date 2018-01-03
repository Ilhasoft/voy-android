package br.com.ilhasoft.voy.ui.report.detail

import android.net.Uri
import android.provider.MediaStore
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Indicator
import br.com.ilhasoft.voy.models.Media
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.report.detail.carousel.CarouselItem

class ReportDetailPresenter : Presenter<ReportDetailContract>(ReportDetailContract::class.java) {

    var report = Report(title = "Just a Title to the Report",
            description = "Just a Simple Description!",
            createdAt = "12/21/2017",
            mediaList = mutableListOf(
                    Media(Uri.parse("http://voy-dev.ilhasoft.mobi/media/content/2017/12/15/46a874c7b756b3e8.png"), Media.TYPE_IMAGE),
                    Media(Uri.parse("http://voy-dev.ilhasoft.mobi/media/content/2017/12/15/c11c57e94de79377.png"), Media.TYPE_IMAGE),
                    Media(Uri.parse("http://voy-dev.ilhasoft.mobi/media/content/2017/12/15/5bb69ae0fbe8bacb.png"), Media.TYPE_IMAGE),
                    Media(Uri.parse("https://ilhacloud-dev.s3.amazonaws.com/clickcondo/files/068da4615b049e88cc8fa703c163ca3f_VIDEO_20161205_175619_435095857.mp4"), Media.TYPE_VIDEO)
            ))
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

    fun swapPage(indicator: Indicator){
        view.swapPage(indicator)
    }

    fun getIndicators(): Collection<Indicator> = report.mediaList.mapIndexed { index, it ->
        Indicator(it.uri,false, index)
    }

    fun setReportReference(report: Report){
        this.report = report
    }

}