package br.com.ilhasoft.voy.ui.report.detail

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.PopupMenu
import br.com.ilhasoft.support.core.helpers.DimensionHelper
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.support.recyclerview.decorations.SpaceItemDecoration
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.databinding.ActivityReportDetailBinding
import br.com.ilhasoft.voy.databinding.ItemIndicatorBinding
import br.com.ilhasoft.voy.databinding.ItemTagBinding
import br.com.ilhasoft.voy.db.report.ReportDbHelper
import br.com.ilhasoft.voy.models.*
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.schedulers.StandardScheduler
import br.com.ilhasoft.voy.shared.widget.WrapContentViewPager
import br.com.ilhasoft.voy.ui.addreport.AddReportActivity
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.comment.CommentsActivity
import br.com.ilhasoft.voy.ui.home.HomeActivity
import br.com.ilhasoft.voy.ui.report.ReportStatus
import br.com.ilhasoft.voy.ui.report.detail.carousel.CarouselAdapter
import br.com.ilhasoft.voy.ui.report.detail.carousel.CarouselItem
import br.com.ilhasoft.voy.ui.report.detail.holder.IndicatorViewHolder
import br.com.ilhasoft.voy.ui.shared.TagViewHolder
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import io.realm.Realm

class ReportDetailActivity : BaseActivity(), ReportDetailContract,
        PopupMenu.OnMenuItemClickListener, ViewPager.OnPageChangeListener,
        CheckConnectionProvider {

    companion object {
        private const val EXTRA_REPORT = "extraReport"

        fun createIntent(context: Context, report: Report): Intent {
            return Intent(context, ReportDetailActivity::class.java)
                    .putExtra(EXTRA_REPORT, report)
        }
    }

    private val report: Report? by lazy { intent.getParcelableExtra<Report?>(EXTRA_REPORT) }
    private val binding: ActivityReportDetailBinding by lazy {
        DataBindingUtil.setContentView<ActivityReportDetailBinding>(this, R.layout.activity_report_detail)
    }
    private val presenter: ReportDetailPresenter by lazy {
        ReportDetailPresenter(
            report!!,
            ReportRepository(ReportService(), ReportDbHelper(Realm.getDefaultInstance(), StandardScheduler()), this),
            SharedPreferences(this),
            StandardScheduler(),
            this
        )
    }
    private val carouselAdapter by lazy { CarouselAdapter(supportFragmentManager, mutableListOf()) }
    private val indicatorViewHolder: OnCreateViewHolder<Indicator, IndicatorViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            IndicatorViewHolder(ItemIndicatorBinding.inflate(layoutInflater, parent, false), presenter)
        }
    }
    private val indicatorAdapter: AutoRecyclerAdapter<Indicator, IndicatorViewHolder> by lazy {
        AutoRecyclerAdapter<Indicator, IndicatorViewHolder>(indicatorViewHolder).apply {
            setHasStableIds(false)
        }
    }
    private val tagViewHolder: OnCreateViewHolder<String, TagViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            TagViewHolder(ItemTagBinding.inflate(layoutInflater, parent, false), tagDataUI, null)
        }
    }
    private val tagsAdapter: AutoRecyclerAdapter<String, TagViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), tagViewHolder).apply {
            setHasStableIds(true)
        }
    }

    private val tagDataUI: TagDataUI by lazy { setupTagData() }

    private lateinit var popupMenu: PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.attachView(this)
        presenter.loadReportData()
        if (report?.status == ReportStatus.UNAPPROVED.value) showReportAlert()
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

    override fun navigateBack() = onBackPressed()

    override fun showReportAlert() {
        val spannable = SpannableString(report?.lastNotification ?: "")
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.darkish_pink)),
                0, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.issues_related_report))
                .setMessage(spannable)
                .setPositiveButton(getString(R.string.edit_report_label).toUpperCase(), { _, _ -> navigateToEditReport() })
                .setNegativeButton(getString(R.string.close_dialog_label).toUpperCase(), { alert, _ ->  alert.dismiss() })
                .show()
        dialog.getButton(Dialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.charcoal_grey))
    }

    override fun showPopupMenu() = popupMenu.show()

    override fun navigateToCommentReport() {
        startActivity(CommentsActivity.createIntent(this, report?.id ?: 0))
    }

    override fun swapPage(indicator: Indicator) {
        binding.viewMedias?.viewPager?.setCurrentItem(indicator.position, true)
    }

    override fun getThemeColor(): String? = binding.report?.themeColor

    override fun showReportData(report: Report) {
        binding.apply {
            viewToolbar?.name?.setTextColor(ThemeData.themeColor)
            name.setTextColor(ThemeData.themeColor)
            createdOn.setTextColor(ThemeData.themeColor)
            this.report = report
        }
        setupMediasView()
        tagsAdapter.addAll(report.tags.sortedWith(String.CASE_INSENSITIVE_ORDER))
        tagsAdapter.notifyDataSetChanged()
    }

    override fun populateIndicator(indicators: List<Indicator>) {
        indicatorAdapter.addAll(indicators)
        indicatorAdapter[Indicator.INITIAL_POSITION].selected = true
    }

    override fun populateCarousel(carouselItems: List<CarouselItem>) {
        carouselAdapter.addAll(carouselItems)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.edit -> {
            navigateToEditReport()
            true
        }
        R.id.share -> {
            startActivity(Intent.createChooser(createShareIntent(), resources.getString(R.string.share_title)))
            true
        }
        else -> false
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        indicatorAdapter.single { it -> it.selected }.selected = false
        indicatorAdapter[position].selected = true
        presenter.indicator = indicatorAdapter[position]
        indicatorAdapter.notifyDataSetChanged()
    }

    override fun hasConnection(): Boolean = ConnectivityManager.isConnected()

    private fun setupView() {
        binding.presenter = this@ReportDetailActivity.presenter
        setupToolbar()
        setupRecyclerView(binding.tags)
    }

    private fun setupToolbar() = binding.viewToolbar?.let {
        setupPopupMenu(it.expandedMenu)
        it.titleColor = ThemeData.themeColor
        it.enableDividerLine = true
    }

    private fun setupPopupMenu(expandedMenu: ImageButton) {
        popupMenu = PopupMenu(this, expandedMenu)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.menuInflater.inflate(R.menu.report_detail, popupMenu.menu)
        showMenuOptionBasedOnReportState(popupMenu.menu)
    }

    private fun showMenuOptionBasedOnReportState(menu: Menu) {
        report?.status.let {
            if (it ==  ReportStatus.APPROVED.value)
                menu.apply {
                    findItem(R.id.edit).isVisible = false
                    findItem(R.id.share).isVisible = true
                }
        }
    }

    private fun createShareIntent(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_text) +
                    " http://voy-dev.ilhasoft.mobi/project/${HomeActivity.projectName}/report/${report?.id}")
            type = "text/plain"
        }
    }

    private fun setupRecyclerView(tags: RecyclerView) = with(tags) {
        layoutManager = FlexboxLayoutManager(this@ReportDetailActivity).apply {
            flexWrap = FlexWrap.WRAP
        }
        setHasFixedSize(false)
        adapter = tagsAdapter
    }

    private fun setupMediasView() {
        binding.viewMedias?.run {
            setupViewPager(viewPager)
            setupIndicatorRecyclerView(indicators)
        }
    }

    private fun setupViewPager(viewPager: WrapContentViewPager) = with(viewPager) {
        addOnPageChangeListener(this@ReportDetailActivity)
        offscreenPageLimit = carouselAdapter.count
        adapter = carouselAdapter
    }

    private fun setupIndicatorRecyclerView(indicatorsList: RecyclerView) = with(indicatorsList) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this@ReportDetailActivity,
                LinearLayoutManager.HORIZONTAL, false)
        adapter = indicatorAdapter
    }

    private fun navigateToEditReport() = startActivity(AddReportActivity.createIntent(this, report))

    private fun setupTagData(): TagDataUI {
        return TagDataUI().apply {
            selectedColor = ThemeData.themeColor
            textSelectedColor = ContextCompat.getColor(this@ReportDetailActivity, R.color.white_three)
            textSize = 10F
        }
    }

}
