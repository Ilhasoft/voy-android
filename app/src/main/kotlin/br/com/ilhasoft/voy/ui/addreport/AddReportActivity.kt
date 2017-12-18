package br.com.ilhasoft.voy.ui.addreport

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.databinding.DataBindingUtil
import android.os.Bundle
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityAddReportBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.addreport.medias.AddMediasFragment
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

    private var report: Report =  Report()

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityAddReportBinding>(this, R.layout.activity_add_report)
    }

    private val presenter by lazy { AddReportPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupToolbar()
        startFragment(AddMediasFragment(), "Medias", setupReportBundle(Bundle()))
        presenter.attachView(this)
    }

    override fun onMediaChange(listSize: Int) {
        binding.toolbar?.enabled = listSize > 0
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

    private fun startFragment(fragment: Fragment, tag: String, bundle: Bundle?) {
        bundle?.let {
            fragment.arguments = it
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, tag)
                .commit()
    }

    private fun setupReportBundle(bundle: Bundle): Bundle? = with(bundle) {
        putParcelable("Report", report)
        return bundle
    }
}