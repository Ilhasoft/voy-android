package br.com.ilhasoft.voy.ui.base

import br.com.ilhasoft.voy.db.report.ReportDbHelper
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File

/**
 * Created by lucasbarros on 20/02/18.
 */
class AutoSendInteractorImpl : AutoSendInteractor {

    companion object {
        private var sendingPendingReports = false
    }

    private val reportService by lazy { ReportService() }
    private val reportDbHelper by lazy { ReportDbHelper() }

    private fun getFromDb() = reportDbHelper.getReports().onMainThread()

    override fun sendPendingReports() {
        if (!sendingPendingReports) {
            getFromDb().observeOn(Schedulers.newThread())
                    .doOnSubscribe { sendingPendingReports = true }
                    .doOnTerminate { sendingPendingReports = false }
                    .flatMap {
                        Flowable.fromIterable(it)
                    }
                    .flatMap {
                        reportService.saveReport(it.theme, it.location!!, it.description, it.name,
                                it.tags, it.urls, it.files.map { File(it.file) }).toFlowable(BackpressureStrategy.DROP)
                                .doOnComplete { reportDbHelper.removeReport(it.internalId!!) }
                    }
                    .subscribe({}, {
                        Timber.e(it)
                    })
        }
    }
}