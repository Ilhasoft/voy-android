package br.com.ilhasoft.voy.ui.addreport

import android.databinding.DataBindingUtil
import android.os.Bundle
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityAddReportBinding
import br.com.ilhasoft.voy.ui.base.BaseActivity

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddReportActivity : BaseActivity(), AddReportContract {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityAddReportBinding>(this, R.layout.activity_add_report)
    }

    private val presenter by lazy { AddReportPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupToolbar()
        presenter.attachView(this)
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