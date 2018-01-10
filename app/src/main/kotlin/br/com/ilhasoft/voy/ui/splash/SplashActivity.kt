package br.com.ilhasoft.voy.ui.splash

import android.databinding.DataBindingUtil
import android.os.Bundle
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivitySplashBinding
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.login.LoginActivity
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)

        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe({
                    startActivity(LoginActivity.createIntent(this))
                    finish()
                })
    }

}
