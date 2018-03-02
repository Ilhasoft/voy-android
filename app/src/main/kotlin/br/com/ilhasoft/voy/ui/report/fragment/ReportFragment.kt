package br.com.ilhasoft.voy.ui.report.fragment

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
import br.com.ilhasoft.support.recyclerview.decorations.LinearSpaceItemDecoration
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.FragmentReportsBinding
import br.com.ilhasoft.voy.databinding.ItemReportBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.shared.helpers.ResourcesHelper
import br.com.ilhasoft.voy.ui.base.BaseFragment
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailActivity
import br.com.ilhasoft.voy.ui.report.holder.ReportViewHolder

class ReportFragment : BaseFragment(), ReportContract {

    companion object {
        private const val EXTRA_STATUS = "status"
        const val APPROVED_STATUS = 1
        const val PENDING_STATUS = 2
        const val NOT_APPROVED_STATUS = 3

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

    private val binding: FragmentReportsBinding by lazy {
        FragmentReportsBinding.inflate(LayoutInflater.from(context))
    }
    private val presenter: ReportPresenter by lazy { ReportPresenter(SharedPreferences(context), ReportInteractorImpl(status)) }
    private val reportViewHolder: OnCreateViewHolder<Report, ReportViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            ReportViewHolder(ItemReportBinding.inflate(layoutInflater, parent, false),
                    presenter)
        }
    }
    private val reportsAdapter: AutoRecyclerAdapter<Report, ReportViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), reportViewHolder).apply {
            setHasStableIds(true)
        }
    }
    private val status: Int by lazy { arguments.getInt(EXTRA_STATUS) }
    private val itemsQuantityObserver by lazy { ObservableBoolean(false) }
    private val emptyStateObserver by lazy { ObservableBoolean(false) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        presenter.attachView(this)
        presenter.loadReportsData(status)
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

    override fun fillReportsAdapter(reports: List<Report>) {
        if (reports.isNotEmpty()) {
            binding.reportsQuantity = reports.size
            reportsAdapter.addAll(reports)
        }
    }

    override fun checkGreetings() {
        emptyStateObserver.set(reportsAdapter.itemCount <= 0)
        binding.run {
            greetings = getGreetings(status)
            createReportTip = getGreetingsTip(status)
            quantity.text = getQuantityMessage(reportsAdapter.itemCount, status)
        }
        itemsQuantityObserver.set(reportsAdapter.itemCount > 0)
    }

    //TODO pass projectID to query
    override fun navigateToReportDetail(report: Report) {
        startActivity(ReportDetailActivity.createIntent(context, report))
    }

    private fun setupView(binding: FragmentReportsBinding) = with(binding) {
        isBiggerThenZero = this@ReportFragment.itemsQuantityObserver
        isEmptyState = this@ReportFragment.emptyStateObserver
        setupRecyclerView(reports)
        this.presenter = this@ReportFragment.presenter
        val position = presenter!!.getAvatarPositionFromPreferences()
        drawableResId = ResourcesHelper.getAvatarsResources(activity)[position]
    }

    private fun getGreetings(status: Int): String = when (status) {
        APPROVED_STATUS -> getString(R.string.approved_greetings)
        PENDING_STATUS -> getString(R.string.pending_greetings)
        else -> getString(R.string.rejected_greetings)
    }

    private fun getGreetingsTip(status: Int): String = when (status) {
        APPROVED_STATUS -> getString(R.string.approved_greetings_tip)
        PENDING_STATUS -> getString(R.string.pending_greetings_tip)
        else -> getString(R.string.rejected_greetings_tip)
    }

    private fun getQuantityMessage(qtd: Int, status: Int): String = when (status) {
        APPROVED_STATUS ->
            getString(
                R.string.reports_quantity,
                qtd,
                getString(R.string.approved_fragment_title).toLowerCase()
            )
        PENDING_STATUS ->
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
        layoutManager = setupLayoutManager()
        addItemDecoration(setupItemDecoration())
        setHasFixedSize(true)
        adapter = reportsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    private fun setupItemDecoration(): LinearSpaceItemDecoration {
        val itemDecoration = LinearSpaceItemDecoration(LinearLayoutManager.VERTICAL)
        itemDecoration.space = DimensionHelper.toPx(context, 12f)
        return itemDecoration
    }

}
