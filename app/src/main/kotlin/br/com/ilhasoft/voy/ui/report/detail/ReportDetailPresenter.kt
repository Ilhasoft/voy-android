package br.com.ilhasoft.voy.ui.report.detail

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.models.Indicator
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper
import br.com.ilhasoft.voy.ui.report.detail.carousel.CarouselFragment
import br.com.ilhasoft.voy.ui.report.detail.carousel.CarouselItem
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class ReportDetailPresenter(
    private val report: Report,
    private val preferences: Preferences,
    private val reportService: ReportService
) : Presenter<ReportDetailContract>(ReportDetailContract::class.java) {

    var indicator = Indicator(Uri.EMPTY, true)
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun attachView(view: ReportDetailContract?) {
        super.attachView(view)
        loadReportData()
    }

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
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

    private fun loadReportData() {

        val getReportFlow: Single<Report> = if (ConnectivityManager.isConnected()) {
            reportService.getReport(
                id = report.id, theme = report.theme,
                mapper = preferences.getInt(User.ID), status = report.status
            ).fromIoToMainThread()
        } else {
            Single.just(report).onMainThread()
        }

        compositeDisposable.add(
            getReportFlow
                .doOnSubscribe { view.showLoading() }
                .doAfterTerminate { view.dismissLoading() }
                .doOnSuccess { view.showReportData(it) }
                .doOnSuccess { view.populateIndicator(getIndicators(it)) }
                .subscribe(
                    {
                        view.populateCarousel(getCarouselItems(it))
                    },
                    {
                        ErrorHandlerHelper.showError(it, R.string.http_request_error) { msg ->
                            view.showMessage(msg)
                        }
                    }
                )
        )
    }

    private fun getCarouselItems(report: Report): List<CarouselItem> {
        val carouselItems = mutableListOf<CarouselItem>()
        report.files.forEach {
            carouselItems.add(CarouselItem(CarouselFragment.newInstance(it)))
        }
        return carouselItems
    }

    private fun getIndicators(report: Report): List<Indicator> {
        val indicators = mutableListOf<Indicator>()
        report.files.filterIndexed { index, reportFile ->
            indicators.add(Indicator(Uri.parse(reportFile.file), false, index))
        }
        return indicators
    }

}