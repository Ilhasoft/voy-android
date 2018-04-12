package br.com.ilhasoft.voy.ui.report.detail

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Indicator
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import br.com.ilhasoft.voy.ui.report.detail.carousel.CarouselFragment
import br.com.ilhasoft.voy.ui.report.detail.carousel.CarouselItem
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class ReportDetailPresenter(
        private val report: Report,
        private val reportRepository: ReportRepository,
        private val preferences: Preferences,
        private val scheduler: BaseScheduler,
        private val connectionProvider: CheckConnectionProvider)
    : Presenter<ReportDetailContract>(ReportDetailContract::class.java) {

    var indicator = Indicator(Uri.EMPTY, true)
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickPopupMenu() {
        view.showPopupMenu()
    }

    fun onClickLink(link: String) {
        view.navigateToLink(link)
    }

    fun onClickCommentOnReport() {
        view.navigateToCommentReport()
    }

    fun swapPage(indicator: Indicator) {
        view.swapPage(indicator)
    }

    fun loadReportData() {
        val getReportFlow: Single<Report> = if (connectionProvider.hasConnection()) {
            reportRepository.getReport(
                    id = report.id, theme = report.theme,
                    mapper = preferences.getInt(User.ID), status = report.status
            ).fromIoToMainThread(scheduler)
        } else {
            Single.just(report).onMainThread(scheduler)
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

    fun getCarouselItems(report: Report): List<CarouselItem> {
        val carouselItems = mutableListOf<CarouselItem>()
        report.files.forEach {
            carouselItems.add(CarouselItem(CarouselFragment.newInstance(it)))
        }
        return carouselItems
    }

    fun getIndicators(report: Report): List<Indicator> {
        val indicators = mutableListOf<Indicator>()
        report.files.filterIndexed { index, reportFile ->
            indicators.add(Indicator(Uri.parse(reportFile.file), false, index))
        }
        return indicators
    }

}