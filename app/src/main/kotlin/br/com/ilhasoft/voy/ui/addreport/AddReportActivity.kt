package br.com.ilhasoft.voy.ui.addreport

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import br.com.ilhasoft.support.core.app.IndeterminateProgressDialog
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityAddReportBinding
import br.com.ilhasoft.voy.models.AddReportFragmentType
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.shared.helpers.FileHelper
import br.com.ilhasoft.voy.ui.addreport.description.AddTitleFragment
import br.com.ilhasoft.voy.ui.addreport.medias.AddMediasFragment
import br.com.ilhasoft.voy.ui.addreport.tag.AddTagsFragment
import br.com.ilhasoft.voy.ui.addreport.thanks.ThanksActivity
import br.com.ilhasoft.voy.ui.base.BaseActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import permissions.dispatcher.*


/**
 * Created by lucasbarros on 23/11/17.
 */
@RuntimePermissions
class AddReportActivity : BaseActivity(), AddReportContract {

    companion object {
        private const val REQUEST_CHECK_SETTINGS: Int = 100
        private const val EXTRA_REPORT: String = "report"

        @JvmStatic
        fun createIntent(context: Context,
                         report: Report? = null): Intent =
                Intent(context, AddReportActivity::class.java).apply {
                    putExtra(EXTRA_REPORT, report)
                }
    }

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityAddReportBinding>(this, R.layout.activity_add_report)
    }

    private val locationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    private val reportViewModel by lazy { ViewModelProviders.of(this).get(ReportViewModel::class.java) }

    private val presenter by lazy {
        AddReportPresenter(reportViewModel,
                ThemeData.themeBounds,
                intent.extras.getParcelable(EXTRA_REPORT))
    }

    private val locationRequest by lazy {
        LocationRequest().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }
    }
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                presenter.onLocationLoaded(location)
            }
        }
    }

    private val loadLocationDialog by lazy {
        IndeterminateProgressDialog(this).apply {
            setMessage(getString(R.string.getting_location))
            setCancelable(false)
        }
    }

    private val permissionDeniedDialog by lazy {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.permission_denied))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
    }

    private val addMediasFragment by lazy { AddMediasFragment() }
    private val addTitleFragment by lazy { AddTitleFragment() }
    private val addTagsFragment by lazy { AddTagsFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpObservables()
        setupView()
        presenter.attachView(this@AddReportActivity)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onPause() {
        presenter.pause()
        super.onPause()
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                checkLocation()
            } else {
                permissionDeniedDialog.show()
            }
        }
    }

    override fun navigateToNext(type: AddReportFragmentType) {
        when (type) {
            AddReportFragmentType.MEDIAS -> displayFragment(addMediasFragment)
            AddReportFragmentType.TITLE -> displayFragment(addTitleFragment)
            else -> displayFragment(addTagsFragment)
        }
    }

    override fun checkLocation() {
        loadLocationWithPermissionCheck()
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
        presenter.requestingUpdates = true
    }

    override fun dismissLoadLocationDialog() {
        loadLocationDialog.dismiss()
    }

    override fun stopGettingLocation() {
        locationClient.removeLocationUpdates(mLocationCallback)
    }

    override fun navigateToThanks() {
        startActivity(ThanksActivity.createIntent(this))
        finish()
    }

    override fun navigateBack() {
        if (getVisibleFragmentType() == AddReportFragmentType.MEDIAS)
            finish()
        else
            super.onBackPressed()
    }

    override fun getVisibleFragmentType(): AddReportFragmentType {
        return when {
            addMediasFragment.isVisible -> AddReportFragmentType.MEDIAS
            addTitleFragment.isVisible -> AddReportFragmentType.TITLE
            else -> AddReportFragmentType.TAG
        }
    }

    override fun showOutsideDialog() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.outside_theme_bounds))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
                .show()
    }

    override fun getFileFromUri(uri: Uri) = FileHelper.createFileFromUri(this, uri)

    override fun getMimeTypeFromUri(uri: Uri) = FileHelper.getMimeTypeFromUri(this, uri)

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun loadLocation() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        task.addOnCompleteListener {
            try {
                it.getResult(ApiException::class.java)
                showLoadLocationDialog()
                locationClient.requestLocationUpdates(locationRequest, mLocationCallback, null)
            } catch (apiException: ApiException) {
                when (apiException.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> resolveLocationEnable(apiException)
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        showUnknownErrorDialog()
                    }
                }
            }
        }
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun showRationaleForLocation(request: PermissionRequest) {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.rationale_location_description))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { _, _ -> request.proceed() }
                .setNegativeButton(R.string.deny) { _, _ -> request.cancel() }
                .show()
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onLocationDenied() {
        permissionDeniedDialog.show()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onLocationNeverAskAgain() {
        permissionDeniedDialog.show()
    }

    private fun setupView() {
        binding.apply {
            presenter = this@AddReportActivity.presenter
            toolbar?.btnAction?.isEnabled = reportViewModel.buttonEnable.value == true
        }
    }

    private fun setUpObservables() {
        reportViewModel.buttonEnable.observe(this,
                Observer {
                    it?.let {
                        binding.toolbar?.btnAction?.isEnabled = it
                    }
                })
        reportViewModel.buttonTitle.observe(this,
                Observer {
                    it?.let {
                        binding.toolbar?.actionName = resources.getString(it)
                    }
                })
    }

    private fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
    }

    private fun showLoadLocationDialog() {
        loadLocationDialog.show()
    }

    private fun showUnknownErrorDialog() {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.something_went_wrong))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { _, _ -> finish() }
    }

    private fun resolveLocationEnable(apiException: ApiException) {
        try {
            val resolvable = apiException as ResolvableApiException
            resolvable.startResolutionForResult(this@AddReportActivity, REQUEST_CHECK_SETTINGS)
        } catch (e: SendIntentException) {
            // Ignore the error.
        } catch (e: ClassCastException) {
            // Ignore, should be an impossible error.
        }
    }

}