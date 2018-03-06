package br.com.ilhasoft.voy.ui.report.detail.carousel

import br.com.ilhasoft.support.core.mvp.BasicView

interface CarouselContract : BasicView {
    fun displayImage(filePath: String)
    fun displayVideo(filePath: String)
}