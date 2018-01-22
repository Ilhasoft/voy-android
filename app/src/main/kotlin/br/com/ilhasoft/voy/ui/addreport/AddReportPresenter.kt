package br.com.ilhasoft.voy.ui.addreport

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.AddReportFragmentType
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.network.files.FilesService
import br.com.ilhasoft.voy.network.reports.ReportService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


/**
 * Created by lucasbarros on 23/11/17.
 */
class AddReportPresenter(private val reportViewModel: ReportViewModel) :
        Presenter<AddReportContract>(AddReportContract::class.java) {

    private val reportService = ReportService()
    private val fileService = FilesService()

    override fun attachView(view: AddReportContract) {
        super.attachView(view)
        view.navigateToNext(AddReportFragmentType.MEDIAS)
    }

    fun onClickNavigateNext() {
        when (view.getVisibleFragmentType()) {
            AddReportFragmentType.MEDIAS -> view.navigateToNext(AddReportFragmentType.TITLE)
            AddReportFragmentType.TITLE -> view.navigateToNext(AddReportFragmentType.TAG)
            else -> saveReport()
        }
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    private fun saveReport() {
        val location = Location("point", arrayListOf(-127.33, 24.903))
        reportService.saveReport(reportViewModel.themeId,
                location,
                reportViewModel.description,
                reportViewModel.name,
                reportViewModel.tags.map { it.tag },
                reportViewModel.links)
                .flatMapObservable {
                    reportViewModel.report = it
                    Observable.fromIterable(reportViewModel.medias)
                }
                .flatMapSingle {
                    val file = getFile(it)
                    val mimeType = getMimeType(it)
                    fileService.saveFile(file.nameWithoutExtension,
                            file.name,
                            "",
                            file,
                            mimeType,
                            reportViewModel.report.id)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.dismissLoading() }
                .doOnComplete { view.navigateToThanks() }
                .subscribe({
                    //TODO: add on file array and add last image if media_type is image
                    reportViewModel.report.lastImage = it
                }, {
                    Timber.e(it)
                })
    }

    private fun getFile(uri: Uri) = view.getFileFromUri(uri)

    private fun getMimeType(uri: Uri) = view.getMimeTypeFromUri(uri)

}
