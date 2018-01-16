package br.com.ilhasoft.voy.ui.addreport

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityAddReportBinding
import br.com.ilhasoft.voy.models.AddReportFragmentType
import br.com.ilhasoft.voy.models.Media
import br.com.ilhasoft.voy.ui.addreport.medias.AddMediasFragment
import br.com.ilhasoft.voy.ui.addreport.thanks.ThanksActivity
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.shared.OnReportChangeListener

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddReportActivity : BaseActivity(), AddReportContract, OnReportChangeListener {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, AddReportActivity::class.java)
    }

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityAddReportBinding>(this, R.layout.activity_add_report)
    }
    private val presenter by lazy { AddReportPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val reportViewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)
        reportViewModel.buttonEnable.observe(this,
                Observer {
                    it?.let {
                        changeActionButtonStatus(it)
                    }
                })
        reportViewModel.buttonTitle.observe(this,
                Observer {
                    it?.let {
                        changeActionButtonName(it)
                    }
                })
        setupView()
        setupToolbar()
        presenter.attachView(this@AddReportActivity)
    }

    override fun displayFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, tag)

//        if (tag != AddMediasFragment.TAG)
        if (fragment !is AddMediasFragment)
            transaction.addToBackStack(tag)

        transaction.commit()
    }

    override fun changeActionButtonStatus(status: Boolean) {
        binding.toolbar?.enabled = status
    }

    override fun changeActionButtonName(resourceId: Int) {
        binding.toolbar?.actionName = resources.getString(resourceId)
    }

    override fun updateReportMedias(mediaList: MutableList<Media>) {
        presenter.updateReportMedias(mediaList)
    }

    override fun updateNextFragmentReference(nextFragment: AddReportFragmentType) {
        presenter.updateNextFragmentReference(nextFragment)
    }

    override fun updateExternalLinksList(externalLinks: MutableList<String>) {
        presenter.updateExternalLinksList(externalLinks)
    }

    override fun displayThanks() {
        finish()
        startActivity(ThanksActivity.createIntent(this))
    }

    override fun navigateBack() {
        super.onBackPressed()
    }

    private fun setupView() {
        binding.run {
            presenter = this@AddReportActivity.presenter
        }
    }

    private fun setupToolbar() {
        binding.toolbar?.run {
            presenter = this@AddReportActivity.presenter
        }
    }

}