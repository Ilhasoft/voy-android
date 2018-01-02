package br.com.ilhasoft.voy.ui.report.detail

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Indicator
import br.com.ilhasoft.voy.models.Media
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.report.detail.carousel.CarouselItem

class ReportDetailPresenter : Presenter<ReportDetailContract>(ReportDetailContract::class.java) {

    var report = Report(title = "Just a Title to the Report",
            description = "Just a Simple Description!",
            mediaList = mutableListOf(
                    Media(Uri.parse("content://com.android.providers.media.documents/document/image%3A7062")),
                    Media(Uri.parse("content://com.android.providers.media.documents/document/image%3A7063")),
                    Media(Uri.parse("content://com.android.providers.media.documents/document/image%3A7064"))
            ))
        private set

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickPopupMenu() {
        view.showPopupMenu()
    }

    fun onClickCommentOnReport() {
        view.navigateToCommentReport()
    }

    fun getIndicators(): Collection<Indicator> = report.mediaList.map { _ ->
        Indicator("", false)
    }

}