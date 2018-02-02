package br.com.ilhasoft.voy.ui.report.detail

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Indicator
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.report.detail.carousel.CarouselItem

interface ReportDetailContract : BasicView {

    fun navigateBack()

    fun showReportAlert()

    fun showPopupMenu()

    fun navigateToCommentReport()

    fun swapPage(indicator: Indicator)

    fun getThemeColor(): String?

    fun showReportData(report: Report)

    fun populateIndicator(indicators: List<Indicator>)

    fun populateCarousel(carouselItems: List<CarouselItem>)
}