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
import br.com.ilhasoft.voy.ui.addreport.description.AddTitleFragment
import br.com.ilhasoft.voy.ui.addreport.medias.AddMediasFragment
import br.com.ilhasoft.voy.ui.addreport.tag.AddTagsFragment
import br.com.ilhasoft.voy.ui.addreport.thanks.ThanksActivity
import br.com.ilhasoft.voy.ui.base.BaseActivity

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddReportActivity : BaseActivity(), AddReportContract {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, AddReportActivity::class.java)
    }

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityAddReportBinding>(this, R.layout.activity_add_report)
    }

    private val reportViewModel by lazy { ViewModelProviders.of(this).get(ReportViewModel::class.java) }

    private val presenter by lazy { AddReportPresenter(reportViewModel) }

    private val addMediasFragment by lazy { AddMediasFragment() }
    private val addDescriptionFragment by lazy { AddTitleFragment() }
    private val addTagsFragment by lazy { AddTagsFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpObservables()
        setupView()
        presenter.attachView(this@AddReportActivity)
    }

    override fun navigateToNext(type: AddReportFragmentType) {
        when (type) {
            AddReportFragmentType.MEDIAS -> displayFragment(addMediasFragment)
            AddReportFragmentType.TITLE -> displayFragment(addDescriptionFragment)
            else -> displayFragment(addTagsFragment)
        }
    }

    override fun navigateToThanks() {
        startActivity(ThanksActivity.createIntent(this))
        finish()
    }

    override fun navigateBack() {
        super.onBackPressed()
    }

    override fun getVisibleFragmentType(): AddReportFragmentType {
        return when {
            addMediasFragment.isVisible -> AddReportFragmentType.MEDIAS
            addDescriptionFragment.isVisible -> AddReportFragmentType.TITLE
            else -> AddReportFragmentType.TAG
        }
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

}