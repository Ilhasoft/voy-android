package br.com.ilhasoft.voy.ui.login

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity


import br.com.ilhasoft.support.core.app.IndeterminateProgressDialog

import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginContract {

    private val binding: ActivityLoginBinding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }

    private val presenter: LoginPresenter by lazy { LoginPresenter() }

    private val progress: IndeterminateProgressDialog by lazy {
        val progress = IndeterminateProgressDialog(this)
        progress.setMessage("Aguarde...")
        progress
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
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

    override fun showMessage(messageId: Int) {
        //showMessage(getString(messageid))
    }

    override fun showMessage(message: CharSequence) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showLoading() {
        progress.show()
    }

    override fun dismissLoading() {
        progress.dismiss()
    }

}
