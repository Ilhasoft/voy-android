package br.com.ilhasoft.voy.ui.addreport

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.AddReportFragmentType
import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.network.files.FilesService
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.helpers.LocationHelpers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddReportPresenter(
    private val reportViewModel: ReportViewModel,
    private val bound: List<List<Double>>,
    private val report: Report?,
    private val reportInteractor: AddReportInteractor
) : Presenter<AddReportContract>(AddReportContract::class.java) {

    private val boundPairs: List<Pair<Double, Double>> by lazy {
        if (bound.isNotEmpty())
            bound.map { Pair(it[0], it[1]) }
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
        reportInteractor.saveReport(
            ThemeData.themeId,
            userLocation,
            description,
            name,
            selectedTags,
            medias.map { getFile(it) },
            links
        )
            .doOnSubscribe { view.showLoading() }
            .doOnTerminate { view.dismissLoading() }
            .doOnComplete { view.navigateToThanks() }
            .subscribe({
                reportViewModel.report = it
            }, {
                Timber.e(it)
            })
    }

    private fun updateReport() = with(reportViewModel) {
        reportInteractor.updateReport(
            report.id,
            ThemeData.themeId,
            report.location!!,
            description,
            name,
            selectedTags,
            links,
            medias.map { it.toString() },
            mediasToSave().map { getFile(it) },
            mediasToDelete()
        )
            .doOnSubscribe { view.showLoading() }
            .doAfterTerminate { view.dismissLoading() }
            .subscribe({
                reportViewModel.report = it
                view.navigateToThanks()
            }, {
                Timber.e(it)
            })
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
