package br.com.ilhasoft.voy.ui.report

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
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.ui.addreport.AddReportActivity
import br.com.ilhasoft.voy.ui.base.BaseFragment
import br.com.ilhasoft.voy.ui.report.detail.ReportDetailActivity
import br.com.ilhasoft.voy.ui.report.holder.ReportViewHolder

class ReportsFragment : BaseFragment(), ReportsContract {

    companion object {
        private const val EXTRA_TYPE = "type"

        @JvmStatic
        fun newInstance(type: String?): ReportsFragment {
            val args = Bundle()
            args.putString(EXTRA_TYPE, type)
            return createWithArguments(args)
        }

        private fun createWithArguments(args: Bundle): ReportsFragment {
            val fragment = ReportsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val binding: FragmentReportsBinding by lazy {
        FragmentReportsBinding.inflate(LayoutInflater.from(context))
    }
    private val presenter: ReportsPresenter by lazy { ReportsPresenter() }
    private val reportViewHolder:
            OnCreateViewHolder<Report, ReportViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            ReportViewHolder(ItemReportBinding.inflate(layoutInflater, parent, false),
                    presenter)
        }
    }
    private val reportsAdapter:
            AutoRecyclerAdapter<Report, ReportViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), reportViewHolder).apply {
            setHasStableIds(true)
        }
    }
    private val type: String? by lazy { arguments?.getString(EXTRA_TYPE) }

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

    override fun navigateToAddReport() {
        startActivity(AddReportActivity.createIntent(context))
    }

    override fun navigateToReportDetail(report: Report) =
            startActivity(ReportDetailActivity.createIntent(context))

    private fun setupView() {
        binding.run {
            noReports = false
            greetings = getGreetings(type)
            createReportTip = getGreetingsTip(type)
            presenter = this@ReportsFragment.presenter
        }
        setupRecyclerView(binding.reports)
    }

    private fun getGreetings(type: String?): String {
        type.let {
            return when {
                it.equals(getString(R.string.approved_fragment_title), true) -> getString(R.string.approved_greetings)
                it.equals(getString(R.string.pending_fragment_title), true) -> getString(R.string.pending_greetings)
                else -> getString(R.string.rejected_greetings)
            }
        }
    }

    private fun getGreetingsTip(type: String?): String {
        type.let {
            return when {
                it.equals(getString(R.string.approved_fragment_title), true) -> getString(R.string.approved_greetings_tip)
                it.equals(getString(R.string.pending_fragment_title), true) -> getString(R.string.pending_greetings_tip)
                else -> getString(R.string.rejected_greetings_tip)
            }
        }
    }

    private fun setupRecyclerView(reports: RecyclerView) = with(reports) {
        layoutManager = setupLayoutManager()
        addItemDecoration(setupItemDecoration())
        setHasFixedSize(true)
        reportsAdapter.addAll(resources.getStringArray(R.array.Reports).map { it ->
            Report(title = it, description = it)
        })
        adapter = reportsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    private fun setupItemDecoration(): LinearSpaceItemDecoration {
        val itemDecoration = LinearSpaceItemDecoration(LinearLayoutManager.VERTICAL)
        itemDecoration.space = DimensionHelper.toPx(context, 12f)
        return itemDecoration
    }

    private fun getReportList(): MutableList<Report> = mutableListOf(
            Report("A Title For a Report 1", "12/27/2017", "Just a Description 1", Theme("Project 1", "Just a Name 1", "Just a Description 1")),
            Report("A Title For a Report 2", "12/26/2017", "Just a Description 2", Theme("Project 2", "Just a Name 2", "Just a Description 2")),
            Report("A Title For a Report 3", "12/25/2017", "Just a Description 3", Theme("Project 3", "Just a Name 3", "Just a Description 3")),
            Report("A Title For a Report 4", "12/24/2017", "Just a Description 4", Theme("Project 4", "Just a Name 4", "Just a Description 4")),
            Report("A Title For a Report 5", "12/23/2017", "Just a Description 5", Theme("Project 5", "Just a Name 5", "Just a Description 5")))

}
