package br.com.ilhasoft.voy.ui.addreport

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.db.report.ReportDbHelper
import br.com.ilhasoft.voy.db.theme.ThemeDbHelper
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * Created by lucasbarros on 09/02/18.
 */
class AddReportInteractorImpl : AddReportInteractor {

    private val reportService by lazy { ReportService() }
    private val reportDbHelper by lazy { ReportDbHelper() }

    private val themeDbHelper by lazy { ThemeDbHelper() }

    override fun saveReport(theme: Int, location: Location, description: String?, name: String,
                            tags: List<String>, medias: List<File>, urls: List<String>?): Observable<Report> {
        return reportDbHelper.saveReport(theme, location, description, name, tags, medias.map { it.absolutePath }, urls)
                .onMainThread()
                .observeOn(Schedulers.io())
                .flatMapObservable {
                    if (ConnectivityManager.isConnected()) {
                        reportService.saveReport(theme, location, description, name, tags, urls, medias)
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnComplete { reportDbHelper.removeReport(it.internalId!!) }
                    } else {
                        Observable.just(it)
                    }
                }
                .observeOn(Schedulers.io())
    }

    override fun getTags(themeId: Int): Flowable<MutableList<String>> {
        return themeDbHelper.getThemeTags(themeId).onMainThread()
    }
}