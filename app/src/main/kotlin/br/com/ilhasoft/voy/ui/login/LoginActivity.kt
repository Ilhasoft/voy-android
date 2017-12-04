package br.com.ilhasoft.voy.ui.login

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import br.com.ilhasoft.support.validation.Validator
import br.com.ilhasoft.voy.BuildConfig
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityLoginBinding
import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.ui.base.BaseActivity

class LoginActivity : BaseActivity(), LoginContract {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            return intent
        }
    }

    private val binding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }

    private val presenter: LoginPresenter by lazy { LoginPresenter() }
    private val validator: Validator by lazy { Validator(binding) }
    private val credentials by lazy { Credentials() }

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

    override fun validate(): Boolean = validator.validate()

    override fun showErrorMessage(message: CharSequence) {}

    override fun navigateToHome() {
        showMessage(getString(R.string.login_success))
    }

    private fun setupView() {
        binding.run {
            credentials = if (BuildConfig.DEBUG) Credentials(getString(R.string.username_dev),
                    getString(R.string.password_dev))
            else this@LoginActivity.credentials
            presenter = this@LoginActivity.presenter
        }
    }

}
