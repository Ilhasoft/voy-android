package br.com.ilhasoft.voy.ui.report.detail.carousel

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.ReportFile

interface CarouselContract : BasicView {

    fun displayMedia(media: ReportFile?)

}