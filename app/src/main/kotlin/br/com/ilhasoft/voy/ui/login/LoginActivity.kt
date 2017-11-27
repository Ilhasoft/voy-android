package br.com.ilhasoft.voy.ui.login

import android.databinding.DataBindingUtil
import android.os.Bundle


import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityLoginBinding
import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.ui.base.BaseActivity

class LoginActivity : BaseActivity(), LoginContract {

    private val binding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }

    private val presenter: LoginPresenter by lazy { LoginPresenter() }

    private val credentials by lazy { Credentials() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            credentials = this@LoginActivity.credentials
            presenter = this@LoginActivity.presenter
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


}
