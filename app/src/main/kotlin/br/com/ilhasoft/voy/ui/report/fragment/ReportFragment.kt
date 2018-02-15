package br.com.ilhasoft.voy.ui.report.fragment

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun getStatus(): Int? = status

    override fun fillReportsAdapter(reports: List<Report>) {
        reports.let {
            binding.noReports = it.isNotEmpty().not()
            if (it.isNotEmpty()) {
                binding.reportsQuantity = it.size
                reportsAdapter.addAll(it)
                reportsAdapter.notifyDataSetChanged()
            } else {
                binding.run {
                    greetings = getGreetings(status)
                    createReportTip = getGreetingsTip(status)
                }
            }
        }
    }

    //TODO pass projectID to query
    override fun navigateToReportDetail(report: Report) {
        startActivity(ReportDetailActivity.createIntent(context, report))
    }

    override fun navigateToEditReport(report: Report?) {}

    private fun setupView() {
        binding.run {
            setupRecyclerView(reports)
            presenter = this@ReportFragment.presenter
        }
    }

    private fun getGreetings(status: Int): String {
        status.let {
            return when (it) {
                APPROVED_STATUS -> getString(R.string.approved_greetings)
                PENDING_STATUS -> getString(R.string.pending_greetings)
                else -> getString(R.string.rejected_greetings)
            }
        }
    }

    private fun getGreetingsTip(status: Int): String {
        status.let {
            return when (it) {
                APPROVED_STATUS -> getString(R.string.approved_greetings_tip)
                PENDING_STATUS -> getString(R.string.pending_greetings_tip)
                else -> getString(R.string.rejected_greetings_tip)
            }
        }
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
