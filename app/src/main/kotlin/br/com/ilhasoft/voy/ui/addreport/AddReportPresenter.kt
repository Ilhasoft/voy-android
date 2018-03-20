package br.com.ilhasoft.voy.ui.addreport

import android.net.Uri
import android.os.Build
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.support.rxgraphics.FileCompressor
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.db.report.ReportFileDbModel
import br.com.ilhasoft.voy.models.AddReportFragmentType
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.shared.helpers.FileHelper
import br.com.ilhasoft.voy.shared.helpers.LocationHelpers
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddReportPresenter(
    private val reportViewModel: ReportViewModel,
    private val bound: List<List<Double>>,
    private val report: Report?,
    private val reportInteractor: AddReportInteractor,
    private val scheduler: BaseScheduler
) : Presenter<AddReportContract>(AddReportContract::class.java) {

    private val boundPairs: List<Pair<Double, Double>> by lazy {
        if (bound.isNotEmpty())
            bound.map { Pair(it[0], it[1]) }
        else
            listOf()
    }
    private lateinit var userLocation: Location
    var isFinalStep = false
    var requestingUpdates = false

    override fun attachView(view: AddReportContract) {
        super.attachView(view)
        report?.let { reportViewModel.setUpWithReport(it) }
        view.navigateToNext(AddReportFragmentType.MEDIAS)
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

    private fun sendReport() {
        isFinalStep = true
        view.checkLocation()
    }

    private fun saveReport() = with(reportViewModel) {
        Flowable.fromIterable(medias)
            .subscribeOn(Schedulers.io())
            .map { Uri.parse(it) }
            .flatMap {
                val file = getFile(it)
                if (FileHelper.imageTypes.contains(getMimeType(it))) {
                    FileCompressor.compressPicture(file, 1280, 720, 80)
                } else {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN)
                        Flowable.just(file)
                    else
                        FileCompressor.compressVideo(file)
                }
            }
            .toList()
            .onMainThread()
            .flatMapObservable {
                reportInteractor.saveReport(
                    report.internalId,
                    ThemeData.themeId,
                    userLocation,
                    description,
                    name,
                    selectedTags,
                    it,
                    links
                )
            }
            .doOnSubscribe { view.showLoading() }
            .doOnTerminate { view.dismissLoading() }
            .doOnComplete { view.navigateToThanks() }
            .subscribe({
                reportViewModel.report = it
            }, {
                it.printStackTrace()
                Timber.e(it)
            })
    }

    private fun updateReport() = with(reportViewModel) {
        val mediasToSave = mediasToSave()
        Flowable.fromIterable(mediasToSave)
            .subscribeOn(scheduler.io())
            .map { Uri.parse(it) }
            .flatMap {
                val file = getFile(it)
                if (FileHelper.imageTypes.contains(getMimeType(it)))
                    FileCompressor.compressPicture(file, 1280, 720, 80)
                else
                    FileCompressor.compressVideo(file)
            }
            .toList()
            .observeOn(scheduler.ui())
            .flatMapObservable {
                reportInteractor.updateReport(
                    report.internalId,
                    report.id,
                    ThemeData.themeId,
                    report.location!!,
                    description,
                    name,
                    selectedTags,
                    links,
                    medias.map { ReportFileDbModel().apply { file = it } }.toMutableList(),
                    it,
                    mediasToDelete()
                )
            }
            .doOnSubscribe { view.showLoading() }
            .doAfterTerminate { view.dismissLoading() }
            .doOnComplete { view.navigateToThanks() }
            .subscribe(
                { reportViewModel.report = it },
                {
                    it.printStackTrace()
                    Timber.e(it)
                }
            )
    }

    private fun getFile(uri: Uri) = view.getFileFromUri(uri)

    private fun getMimeType(uri: Uri) = view.getMimeTypeFromUri(uri)

    private fun checkedToContinue(isInsideBounds: Boolean) {
        if (!isInsideBounds) {
            view.showOutsideDialog()
        }
        pause()
    }

    private fun checkIfCanContinue(onSuccess: (Boolean) -> Unit, onError: (() -> Unit)? = null) {
        LocationHelpers.isLocationInsidePolygon(
            userLocation.coordinates[1],
            userLocation.coordinates[0],
            boundPairs
        )
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .subscribe({
                onSuccess(it)
            }, {
                onError?.invoke()
                Timber.e(it)
            })
    }

}