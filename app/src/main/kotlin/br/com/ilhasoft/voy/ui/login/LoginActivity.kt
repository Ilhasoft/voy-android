package br.com.ilhasoft.voy.ui.login

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import br.com.ilhasoft.support.validation.Validator
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityLoginBinding
import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.home.HomeActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import java.util.concurrent.TimeUnit

class LoginActivity : BaseActivity(), LoginContract {

    companion object {
        private const val TAG = "LoginActivity"
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

    private val presenter: LoginPresenter by lazy { LoginPresenter(SharedPreferences(this@LoginActivity)) }
    private val validator: Validator by lazy { Validator(binding) }
    private val credentials by lazy { Credentials() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        startListener()
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
        startActivity(HomeActivity.createIntent(this))
        finish()
    }

    private fun setupView() {
        binding.run {
            credentials =
//                    if (BuildConfig.DEBUG)
//                Credentials("pirralho", "123456")
//            else
                this@LoginActivity.credentials
            presenter = this@LoginActivity.presenter
        }
    }

    private fun startListener() {
        val usernameObservable = createEditTextObservable(binding.username)
        val passwordObservable = createEditTextObservable(binding.password)

        Observables.combineLatest(usernameObservable, passwordObservable,
                { username, password -> username.isNotEmpty() && password.isNotEmpty() })
                .subscribe({ binding.login.isEnabled = it },
                        { Log.e(TAG, "Error ", it) })
    }

    private fun createEditTextObservable(editText: EditText) = RxTextView.textChanges(editText)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())

}
