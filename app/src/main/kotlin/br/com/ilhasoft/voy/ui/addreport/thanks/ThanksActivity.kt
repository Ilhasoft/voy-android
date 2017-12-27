package br.com.ilhasoft.voy.ui.addreport.thanks

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle


import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityThanksBinding
import br.com.ilhasoft.voy.ui.addreport.AddReportActivity
import br.com.ilhasoft.voy.ui.base.BaseActivity


class ThanksActivity : BaseActivity(), ThanksContract {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, ThanksActivity::class.java)
    }

    private val binding: ActivityThanksBinding by lazy {
        DataBindingUtil.setContentView<ActivityThanksBinding>(this, R.layout.activity_thanks)
    }

    private val presenter: ThanksPresenter by lazy { ThanksPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            presenter = this@ThanksActivity.presenter
        }
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
        startActivity(AddReportActivity.createIntent(this))
    }

    override fun onClickClose() {
        finish()
    }

    override fun onBackPressed() {
        this.onClickClose()
    }


}
