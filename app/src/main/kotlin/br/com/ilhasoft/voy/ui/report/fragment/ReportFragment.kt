package br.com.ilhasoft.voy.ui.report.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ilhasoft.support.core.helpers.DimensionHelper
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.support.recyclerview.adapters.OnDemandListener
import br.com.ilhasoft.support.recyclerview.decorations.LinearSpaceItemDecoration
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.FragmentReportsBinding
import br.com.ilhasoft.voy.databinding.ItemReportBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.shared.helpers.ResourcesHelper
import br.com.ilhasoft.voy.ui.base.BaseFragment
import br.com.ilhasoft.voy.ui.report.ReportStatus
import br.com.ilhasoft.voy.ui.report.ReportViewModel
import br.com.ilhasoft.voy.ui.report.ReportsActivity
import br.com.ilhasoft.voy.ui.report.RequestReportListener
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailActivity
import br.com.ilhasoft.voy.ui.report.holder.ReportViewHolder

class ReportFragment : BaseFragment(), ReportContract, OnDemandListener {

    companion object {
        private const val EXTRA_STATUS = "status"

        @JvmStatic
        fun newInstance(status: Int): ReportFragment {
            val args = Bundle()
            args.putInt(EXTRA_STATUS, status)
            return createWithArguments(args)
        }

        private fun createWithArguments(args: Bundle): ReportFragment {
            val fragment = ReportFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentReportsBinding
    private lateinit var presenter: ReportPresenter
    private lateinit var viewModel: ReportViewModel
    private val itemsQuantityObserver = ObservableBoolean(false)
    private val emptyStateObserver = ObservableBoolean(false)

    private val requestReportListener: RequestReportListener by lazy { activity as ReportsActivity }

    private val reportViewHolder: OnCreateViewHolder<Report, ReportViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            ReportViewHolder(ItemReportBinding.inflate(layoutInflater, parent, false),
                presenter)
        }
    }
    private val reportsAdapter: AutoRecyclerAdapter<Report, ReportViewHolder> by lazy {
        AutoRecyclerAdapter(reportViewHolder, this).apply {
            setHasStableIds(true)
        }
    }
    private val status: Int by lazy { arguments.getInt(EXTRA_STATUS) }
    private var page = 1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentReportsBinding.inflate(inflater!!, container, false)
        presenter = ReportPresenter(SharedPreferences(context))
        viewModel = ViewModelProviders.of(activity).get(ReportViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        loadReports()
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    //TODO pass projectID to query
    override fun navigateToReportDetail(report: Report) {
        startActivity(ReportDetailActivity.createIntent(context, report))
    }

    override fun onLoadMore() {
        if(viewModel.onDemandStatus) {
            requestReportListener.requestMoreReports(reportsAdapter.itemCount, status, page)
            page++
        }
    }

    private fun setupView(binding: FragmentReportsBinding) = with(binding) {
        isBiggerThenZero = this@ReportFragment.itemsQuantityObserver
        isEmptyState = this@ReportFragment.emptyStateObserver
        setupRecyclerView(reports)
        presenter?.let {
            val position = it.getAvatarPositionFromPreferences()
            drawableResId = ResourcesHelper.getAvatarsResources(activity)[position]
        }
    }

    private fun checkGreetings() {
        emptyStateObserver.set(reportsAdapter.itemCount <= 0)
        binding.run {
            greetings = getGreetings(status)
            createReportTip = getGreetingsTip(status)
            quantity = getQuantityMessage(reportsAdapter.itemCount, status)
        }
        itemsQuantityObserver.set(reportsAdapter.itemCount > 0)
    }

    private fun getGreetings(status: Int): String = when (getReportStatus(status)) {
        ReportStatus.APPROVED -> getString(R.string.approved_greetings)
        ReportStatus.PENDING -> getString(R.string.pending_greetings)
        else -> getString(R.string.rejected_greetings)
    }

    private fun getGreetingsTip(status: Int): String = when (getReportStatus(status)) {
        ReportStatus.APPROVED -> getString(R.string.approved_greetings_tip)
        ReportStatus.PENDING -> getString(R.string.pending_greetings_tip)
        else -> getString(R.string.rejected_greetings_tip)
    }

    private fun getQuantityMessage(qtd: Int, status: Int): String =  when (getReportStatus(status)) {
        ReportStatus.APPROVED ->
            getString(
                R.string.reports_quantity,
                qtd,
                getString(R.string.approved_fragment_title).toLowerCase()
            )
        ReportStatus.PENDING ->
            getString(
                R.string.reports_quantity,
                qtd,
                getString(R.string.pending_fragment_title).toLowerCase()
            )
        else ->
            getString(
                R.string.reports_quantity,
                qtd,
                getString(R.string.not_approved_fragment_title).toLowerCase()
            )
    }

    private fun setupRecyclerView(reports: RecyclerView) = with(reports) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        addItemDecoration(setupItemDecoration())
        setHasFixedSize(true)
        adapter = reportsAdapter
    }

    private fun setupItemDecoration(): LinearSpaceItemDecoration =
        LinearSpaceItemDecoration(LinearLayoutManager.VERTICAL).apply {
            space = DimensionHelper.toPx(context, 12f)
        }

    private fun loadReports() {
        viewModel.getReports(getReportStatus(status))
            .observe(activity, Observer<List<Report>> { reports ->
                reports?.let { fillReportsAdapter(it) }
            })
    }

    private fun getReportStatus(status: Int): ReportStatus = when(status) {
        ReportStatus.APPROVED.value -> ReportStatus.APPROVED
        ReportStatus.PENDING.value -> ReportStatus.PENDING
        else -> ReportStatus.UNAPPROVED
    }

    private fun fillReportsAdapter(reports: List<Report>) {
        if (reports.isNotEmpty()) {
            binding.reportsQuantity = reports.size
            reportsAdapter.addAll(reports)
        }

        checkGreetings()
    }
}
