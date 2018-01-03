package br.com.ilhasoft.voy.ui.addreport.description

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.support.validation.Validator
import br.com.ilhasoft.voy.BR
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.FragmentAddDescriptionBinding
import br.com.ilhasoft.voy.databinding.ItemLinkBinding
import br.com.ilhasoft.voy.models.Fragments
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.addreport.AddReportActivity
import br.com.ilhasoft.voy.ui.base.BaseFragment
import br.com.ilhasoft.voy.ui.shared.OnReportChangeListener
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import java.util.concurrent.TimeUnit

class AddDescriptionFragment : BaseFragment(), AddDescriptionFragmentContract {

    companion object {
        const val TAG = "Description"
    }

    private val binding: FragmentAddDescriptionBinding by lazy {
        FragmentAddDescriptionBinding.inflate(LayoutInflater.from(context))
    }
    private val presenter: AddDescriptionFragmentPresenter by lazy { AddDescriptionFragmentPresenter() }
    private val linkAdapter: AutoRecyclerAdapter<String, LinkViewHolder> by lazy {
        AutoRecyclerAdapter<String, LinkViewHolder>(linkViewHolder).apply {
            setHasStableIds(true)
        }
    }
    private val linkViewHolder: OnCreateViewHolder<String, LinkViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            LinkViewHolder(ItemLinkBinding.inflate(layoutInflater, parent, false), presenter)
        }
    }
    private val validator: Validator by lazy { Validator(binding) }
    private val reportListener: OnReportChangeListener by lazy { activity as AddReportActivity }
    private val sameLinkDialog by lazy {
        AlertDialog.Builder(context)
                .setTitle(R.string.feedback_list_title)
                .setMessage(R.string.feedback_list_message)
                .setNegativeButton(android.R.string.ok, { dialog, _ -> dialog.dismiss() })
                .setCancelable(true)
                .create()
    }
    private var link: String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupView()
        startTitleListeners()
        startLinkListeners()
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.setReportReference(arguments.getParcelable(Report.TAG))
        binding.report = presenter.report
    }

    override fun onResume() {
        super.onResume()
        reportListener.changeActionButtonName(R.string.next)
        reportListener.updateNextFragmentReference(Fragments.THEME)
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

    override fun changeActionButtonStatus(status: Boolean) {
        reportListener.changeActionButtonStatus(status)
    }

    override fun updateAdapterList(externalLinks: MutableList<String>) {
        linkAdapter.setList(externalLinks)
        updateReportExternalLinksList(externalLinks)
        changeAddLinkButton(presenter.verifyListSize())
    }

    override fun updateReportExternalLinksList(externalLinks: MutableList<String>) {
        reportListener.updateExternalLinksList(externalLinks)
    }

    override fun displaySameListElementFeedback() {
        sameLinkDialog.show()
    }

    private fun setupView() {
        binding.run {
            report = this@AddDescriptionFragment.presenter.report
            addLink.setOnClickListener { onCLickAddLink() }
            hasLinks = true
            canAddLink = false
        }
        changeActionButtonStatus(false)
        setupLinkList(binding.linkList)
    }

    private fun setupLinkList(linkList: RecyclerView) = with(linkList) {
        layoutManager = setupLayoutManager()
        adapter = linkAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager? =
            LinearLayoutManager(context, LinearLayout.VERTICAL, false)

    private fun onCLickAddLink() {
        link?.let {
            presenter.addLink(it)
        }
        binding.link.text.clear()
    }

    private fun changeAddLinkButton(status: Boolean) {
        binding.canAddLink = status
        binding.notifyPropertyChanged(BR.canAddLink)
    }

    private fun startTitleListeners() {
        val titleNotEmptyObservable = createEditTextObservable(binding.title)
        val titleEmptyObservable = createEditTextObservable(binding.title)

        titleNotEmptyObservable.filter { it -> !it.isNotEmpty() }
                .subscribe {
                    changeActionButtonStatus(false)
                }

        titleEmptyObservable.filter { it -> it.isNotEmpty() }
                .subscribe { changeActionButtonStatus(true) }

    }

    private fun startLinkListeners() {
        val validLinkObservable = RxTextView.textChangeEvents(binding.link)
        val linkNotEmptyObservable = RxTextView.textChangeEvents(binding.link)

        linkNotEmptyObservable.filter { it.text().isNotEmpty() }
                .subscribe { e ->
                    link = e.text().toString()
                    validator.enableValidation(binding.link)
                }

        linkNotEmptyObservable.filter { it.text().isBlank() }
                .subscribe { validator.disableValidation(binding.link) }

        Observables.combineLatest(validLinkObservable, linkNotEmptyObservable,
                { _, link -> validator.validate() && link.text().isNotEmpty() && presenter.verifyListSize() })
                .subscribe({ status -> changeAddLinkButton(status) },
                        { Log.e(Report.TAG, "Error ", it) })

    }

    private fun createEditTextObservable(editText: EditText) = RxTextView.textChanges(editText)
            .debounce(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())

}
