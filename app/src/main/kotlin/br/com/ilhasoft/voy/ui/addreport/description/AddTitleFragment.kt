package br.com.ilhasoft.voy.ui.addreport.description

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.support.validation.Validator
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.databinding.FragmentAddDescriptionBinding
import br.com.ilhasoft.voy.databinding.ItemLinkBinding
import br.com.ilhasoft.voy.db.report.ReportDbHelper
import br.com.ilhasoft.voy.network.reports.ReportRepository
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.ui.addreport.AddReportInteractorImpl
import br.com.ilhasoft.voy.ui.addreport.ReportViewModel
import br.com.ilhasoft.voy.ui.addreport.ReportViewModelFactory
import br.com.ilhasoft.voy.ui.base.BaseFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import java.util.concurrent.TimeUnit

class AddTitleFragment : BaseFragment(), CheckConnectionProvider {

    private val binding: FragmentAddDescriptionBinding by lazy {
        FragmentAddDescriptionBinding.inflate(LayoutInflater.from(context))
    }

    private val reportViewModel by lazy {
        val factory = ReportViewModelFactory(
            AddReportInteractorImpl(ReportRepository(ReportService(), ReportDbHelper(Realm.getDefaultInstance()), this))
        )
        ViewModelProviders.of(activity, factory).get(ReportViewModel::class.java)
    }

    private lateinit var compositeDisposable: CompositeDisposable

    private val validator: Validator by lazy { Validator(binding) }

    private val linkAdapter: AutoRecyclerAdapter<String, LinkViewHolder> by lazy {
        AutoRecyclerAdapter<String, LinkViewHolder>(linkViewHolder).apply {
            setHasStableIds(true)
        }
    }
    private val linkViewHolder: OnCreateViewHolder<String, LinkViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            LinkViewHolder(ItemLinkBinding.inflate(layoutInflater, parent, false), reportViewModel)
        }
    }
    private val sameLinkDialog by lazy {
        AlertDialog.Builder(context)
                .setTitle(R.string.feedback_list_title)
                .setMessage(R.string.feedback_list_message)
                .setNegativeButton(android.R.string.ok, { dialog, _ -> dialog.dismiss() })
                .setCancelable(true)
                .create()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupView()
        startTitleListeners()
        startLinkListeners()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
                reportViewModel.linkAdded.subscribe({
                    linkAdded(it)
                })
        )
        compositeDisposable.add(
                reportViewModel.linkRemoved.subscribe({
                    linkRemoved(it)
                })
        )
        compositeDisposable.add(
                reportViewModel.linkAlreadyExist.subscribe({
                    sameLinkDialog.show()
                })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onResume() {
        super.onResume()
        reportViewModel.setButtonEnable(reportViewModel.name?.isNotBlank() == true)
        reportViewModel.setButtonTitle(R.string.next)
    }

    override fun hasConnection(): Boolean = ConnectivityManager.isConnected()

    private fun setupView() {
        binding.run {
            reportViewModel = this@AddTitleFragment.reportViewModel
            hasLinks = true
            allowLinks = this@AddTitleFragment.reportViewModel.allowLinks
        }
        setupLinkList()
    }

    private fun setupLinkList() = with(binding.linkList) {
        layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        adapter = linkAdapter.apply {
            clear()
            addAll(reportViewModel.links)
        }
    }

    private fun startTitleListeners() {
        val titleNotEmptyObservable = createEditTextObservable(binding.title)
        titleNotEmptyObservable.subscribe {
            reportViewModel.setButtonEnable(it.isNotBlank())
        }
    }

    private fun startLinkListeners() {
        val validLinkObservable = RxTextView.textChangeEvents(binding.link)
        validLinkObservable.subscribe {
            val validLink = it.text().length > "http://".length
                    && reportViewModel.verifyListSize()
                    && validator.validate()
            binding.addLink.isEnabled = validLink
        }
    }

    private fun linkRemoved(link: String) {
        linkAdapter.remove(link)
    }

    private fun linkAdded(link: String) {
        linkAdapter.add(0, link)
        binding.link.text.clear()
    }

    private fun createEditTextObservable(editText: EditText) = RxTextView.textChanges(editText)
            .debounce(350, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())

}
