package br.com.ilhasoft.voy.ui.report.detail.carousel

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.ReportFile

class CarouselPresenter : Presenter<CarouselContract>(CarouselContract::class.java) {

    fun displayMedia(media: ReportFile?) {
        media?.apply {
            if (mediaType == ReportFile.TYPE_VIDEO) {
                view.displayVideo(file)
            } else {
                view.displayImage(file)
            }
        }
    }

}