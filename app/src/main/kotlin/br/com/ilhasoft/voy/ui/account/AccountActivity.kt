package br.com.ilhasoft.voy.ui.account


import android.databinding.DataBindingUtil
import android.os.Bundle


import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityAccountBinding
import br.com.ilhasoft.voy.ui.base.BaseActivity

class AccountActivity : BaseActivity(), AccountContract {

    private val binding: ActivityAccountBinding by lazy {
        DataBindingUtil.setContentView<ActivityAccountBinding>(this, R.layout.activity_account)
    }

    private val presenter: AccountPresenter by lazy { AccountPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
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

    private fun setupView() {
        binding.run {
            presenter = this@AccountActivity.presenter
            toolbar?.apply {
                buttonBack.setOnClickListener { onBackPressed() }
            }
        }
    }

}
