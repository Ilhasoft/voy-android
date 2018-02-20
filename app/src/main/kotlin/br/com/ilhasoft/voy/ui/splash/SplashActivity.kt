package br.com.ilhasoft.voy.ui.splash

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivitySplashBinding
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.BaseFactory
import br.com.ilhasoft.voy.ui.home.HomeActivity
import br.com.ilhasoft.voy.ui.login.LoginActivity
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    private val preferences by lazy { SharedPreferences(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)

        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe({
                    if(preferences.contains(User.TOKEN)) {
                        BaseFactory.accessToken = preferences.getString(User.TOKEN)
                        startActivity(HomeActivity.createIntent(this))
                    }else {
                        startActivity(LoginActivity.createIntent(this))
                        finish()
                    }
                })
    }

}
