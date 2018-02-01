package br.com.ilhasoft.voy.ui.addreport

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.*
import br.com.ilhasoft.voy.network.files.FilesService
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.helpers.LocationHelpers
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddReportPresenter(private val reportViewModel: ReportViewModel, private val bound: Bound,
                         private val report: Report?) :
        Presenter<AddReportContract>(AddReportContract::class.java) {

    private val reportService = ReportService()
    private val fileService = FilesService()
    private val boundPairs: List<Pair<Double, Double>> by lazy {
        if (bound.coordinates.isNotEmpty())
            bound.coordinates[0].map { Pair(it[1], it[0]) }
        else
            listOf()
    }
    private lateinit var userLocation: Location
    private var isFinalStep = false
    var requestingUpdates = false

    override fun attachView(view: AddReportContract) {
        super.attachView(view)
        report?.let { reportViewModel.setUpWithReport(it) }
        view.navigateToNext(AddReportFragmentType.MEDIAS)
    }

    fun onClickNavigateNext() {
        when (view.getVisibleFragmentType()) {
            AddReportFragmentType.MEDIAS -> view.navigateToNext(AddReportFragmentType.TITLE)
            AddReportFragmentType.TITLE -> view.navigateToNext(AddReportFragmentType.TAG)
            else -> sendReport()
        }
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onLocationLoaded(location: android.location.Location) {
        view.dismissLoadLocationDialog()
        userLocation = Location("point", arrayListOf(location.longitude, location.latitude))
        checkBounds()
    }

    fun resume() {
        if (!requestingUpdates) {
            requestingUpdates = true
            view.checkLocation()
        }
    }

    fun pause() {
        requestingUpdates = false
        view.stopGettingLocation()
    }

    private fun onFileSaved(reportFile: ReportFile) {
        reportViewModel.report.files.add(reportFile)
        if (reportFile.mediaType == "image")
            reportViewModel.report.lastImage = reportFile
    }

    private fun onSavedReportAndStartSaveFiles(report: Report): Observable<Uri>? {
        reportViewModel.report = report
        return Observable.fromIterable(reportViewModel.medias)
    }

    private fun saveFile(uri: Uri): Single<ReportFile> {
        val file = getFile(uri)
        val mimeType = getMimeType(uri)
        return fileService.saveFile(file.nameWithoutExtension, file.name, file,
                mimeType, reportViewModel.report.id)
    }

    private fun sendReport() {
        isFinalStep = true
        view.checkLocation()
    }

    private fun saveReport() = with(reportViewModel) {
        reportService.saveReport(themeId, userLocation, description, name, tags, links)
                .flatMapObservable { onSavedReportAndStartSaveFiles(it) }
                .flatMapSingle { saveFile(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.dismissLoading() }
                .doOnComplete { view.navigateToThanks() }
                .subscribe({
                    onFileSaved(it)
                }, {
                    Timber.e(it)
                })
    }

    private fun updateReport() {
        if (reportViewModel.mediasToDelete().isNotEmpty()) {
            deleteFilesAndUpdateReport()
        } else {
            updateReportIntern()
        }
    }

    private fun deleteFilesAndUpdateReport() {
        Observable.fromIterable(reportViewModel.mediasToDelete())
                .flatMapCompletable { fileUrl ->
                    var fileToDelete: ReportFile? = null
                    reportViewModel.report.files.forEach {
                        if (it.file == fileUrl.toString())
                            fileToDelete = it
                    }
                    fileToDelete?.let { fileService.deleteFile(it.id) }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.dismissLoading() }
                .doOnComplete { updateReportIntern() }
                .subscribe({}, {
                    Timber.e(it)
                })
    }

    private fun updateReportIntern() {
        if (reportViewModel.hasNewMedias()) {
            updateReportWithFiles()
        } else {
            updateReportWithoutFiles()
        }
    }

    private fun updateReportWithoutFiles() = with(reportViewModel) {
        reportService.updateReport(report.id, themeId, report.location!!, description, name,
                tags, links)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showLoading() }
                .doAfterTerminate { view.dismissLoading() }
                .subscribe({
                    reportViewModel.report = it
                    view.navigateToThanks()
                }, {
                    Timber.e(it)
                })
    }

    private fun updateReportWithFiles() = with(reportViewModel) {
        reportService.updateReport(report.id, themeId, report.location!!, description, name,
                tags, links)
                .flatMapObservable {
                    reportViewModel.report = report
                    Observable.fromIterable(reportViewModel.mediasToSave())
                }
                .flatMapSingle { saveFile(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.dismissLoading() }
                .doOnComplete { view.navigateToThanks() }
                .subscribe({
                    onFileSaved(it)
                }, {
                    Timber.e(it)
                })
    }

    private fun getFile(uri: Uri) = view.getFileFromUri(uri)

    private fun getMimeType(uri: Uri) = view.getMimeTypeFromUri(uri)

    private fun checkBounds() {
        if (isFinalStep) {
            checkIfCanContinue({ checkedToSave(it) }, { isFinalStep = false })
        } else {
            checkIfCanContinue({ checkedToContinue(it) })
        }
    }

    private fun checkedToSave(isInsideBounds: Boolean) {
        when {
            reportViewModel.report.id == 0 && isInsideBounds -> {
                saveReport()
                pause()
            }
            reportViewModel.report.id != 0 -> {
                updateReport()
                pause()
            }
            else -> {
                view.showMessage(R.string.outside_theme_bounds)
                isFinalStep = false
            }
        }
    }

    private fun checkedToContinue(isInsideBounds: Boolean) {
        if (!isInsideBounds) {
            view.showOutsideDialog()
        }
        pause()
    }

    private fun checkIfCanContinue(onSuccess: (Boolean) -> Unit, onError: (() -> Unit)? = null) {
        LocationHelpers.isLocationInsidePolygon(userLocation.coordinates[1], userLocation.coordinates[0], boundPairs)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onSuccess(it)
                }, {
                    onError?.invoke()
                    Timber.e(it)
                })
    }

}
