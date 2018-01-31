package br.com.ilhasoft.voy.ui.report

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityReportsBinding
import br.com.ilhasoft.voy.models.Bound
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.addreport.AddReportActivity
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.report.adapter.NavigationItem
import br.com.ilhasoft.voy.ui.report.adapter.ReportsAdapter
import br.com.ilhasoft.voy.ui.report.fragment.ReportFragment

/**
 * Created by developer on 11/01/18.
 */
class ReportsActivity : BaseActivity(), ReportsContract {

    companion object {
        var themeId = 0
        var themeColor: Int = 0
        var themeBounds: Bound? = null

        @JvmStatic
        private val EXTRA_THEME_NAME = "themeName"
        private val EXTRA_THEME_BOUNDS = "themeBounds"

        @JvmStatic
        fun createIntent(context: Context, themeId: Int,
                         themeName: String, themeColor: String, themeBounds: Bound): Intent {
            ReportsActivity.themeId = themeId
            ReportsActivity.themeColor = Color.parseColor(context.getString(R.string.color_hex, themeColor))
            ReportsActivity.themeBounds = themeBounds

            val intent = Intent(context, ReportsActivity::class.java)
            intent.putExtra(EXTRA_THEME_NAME, themeName)
            intent.putExtra(EXTRA_THEME_BOUNDS, themeBounds)
            return intent
        }
    }

    private val binding: ActivityReportsBinding by lazy {
        DataBindingUtil.setContentView<ActivityReportsBinding>(this, R.layout.activity_reports)
    }
    private val presenter: ReportsPresenter by lazy { ReportsPresenter() }
    private val themeName: String by lazy { intent.extras.getString(EXTRA_THEME_NAME) }
    private val themeBounds by lazy { intent.extras.getParcelable<Bound>(EXTRA_THEME_BOUNDS) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.attachView(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun navigateBack() {
        finish()
    }

    override fun navigateToAddReport() {
        startActivity(AddReportActivity.createIntent(this, themeBounds))
    }

    private fun setupView() {
        binding.apply {
            setUpToolBar()
            presenter = this@ReportsActivity.presenter
            setupTabs()
        }
    }

    private fun setUpToolBar() = binding.viewToolbar?.let {
        it.title = this@ReportsActivity.themeName
        it.titleColor = ReportsActivity.themeColor
    }

    private fun setupTabs() {
        val adapter = ReportsAdapter(supportFragmentManager, createNavigationItems())
        binding.apply {
            viewPager.let {
                it.adapter = adapter
                it.offscreenPageLimit = adapter.count
            }
            tabLayout.setupWithViewPager(binding.viewPager)
        }
    }

    private fun createNavigationItems(): MutableList<NavigationItem> {
        val approved = NavigationItem(ReportFragment.newInstance(ReportFragment.APPROVED_STATUS),
                getString(R.string.approved_fragment_title))
        val pending = NavigationItem(ReportFragment.newInstance(ReportFragment.PENDING_STATUS),
                getString(R.string.pending_fragment_title))
        val rejected = NavigationItem(ReportFragment.newInstance(ReportFragment.NOT_APPROVED_STATUS),
                getString(R.string.not_approved_fragment_title))
        return mutableListOf(approved, pending, rejected)
    }

}
