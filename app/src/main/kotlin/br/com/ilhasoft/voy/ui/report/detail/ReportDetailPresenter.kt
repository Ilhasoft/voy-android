package br.com.ilhasoft.voy.ui.report.detail

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.*
import br.com.ilhasoft.voy.network.medias.MediasService
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.helpers.RxHelper
import br.com.ilhasoft.voy.ui.report.detail.carousel.CarouselFragment
import br.com.ilhasoft.voy.ui.report.detail.carousel.CarouselItem

class ReportDetailPresenter(private val preferences: Preferences) :
        Presenter<ReportDetailContract>(ReportDetailContract::class.java) {

    private val reportService: ReportService by lazy { ReportService() }
    private val reportMediasService: MediasService by lazy { MediasService() }
    var reportMedias: List<ReportMedia>? = null
    var indicator = Indicator(Uri.EMPTY, true)

    override fun attachView(view: ReportDetailContract?) {
        super.attachView(view)
        view?.showLoading()
        loadReportData()
        loadReportMedias()
        view?.dismissLoading()
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickReportAlert() {
        view.showReportAlert()
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

    private fun getReportId(): Int = view.getReportId()

    private fun getThemeId(): Int? = view.getThemeId()

    private fun getReportStatus(): Int? = view.getReportStatus()

    fun getThemeColor(): String? = view.getThemeColor()

    private fun showReportData(report: Report) {
        view.showReportData(report)
    }

    private fun loadReportData() {
        reportService.getReport(id = getReportId(), theme = getThemeId(),
                mapper = preferences.getInt(User.ID), status = getReportStatus())
                .compose(RxHelper.defaultSingleSchedulers())
                .subscribe({ showReportData(it) }, {})
    }

    private fun loadReportMedias() {
        reportMediasService.getMedias(reportId = getReportId())
                .compose(RxHelper.defaultFlowableSchedulers())
                .subscribe({ reportMedias = it }, {})
    }

    //TODO Make a better use of kotlin functions
    fun getCarouselItems(): List<CarouselItem>? {
        val carouselItems = mutableListOf<CarouselItem>()
        reportMedias?.map { reportMedia ->
            reportMedia.files.forEach {
                carouselItems.add(CarouselItem(CarouselFragment.newInstance(it)))
            }
        }
        return carouselItems
    }

    //TODO Make a better use of kotlin functions
    fun getIndicators(): List<Indicator>? {
        val indicators = mutableListOf<Indicator>()
        reportMedias?.mapIndexed { index, reportMedia ->
            reportMedia.files.forEach {
                indicators.add(Indicator(Uri.parse(it.file), false, index))
            }
        }
        return indicators
    }

}