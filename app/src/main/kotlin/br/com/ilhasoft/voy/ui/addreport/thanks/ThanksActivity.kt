package br.com.ilhasoft.voy.ui.addreport.thanks

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityThanksBinding
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.home.HomeActivity

class ThanksActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, ThanksActivity::class.java)
    }

    private val binding: ActivityThanksBinding by lazy {
        DataBindingUtil.setContentView<ActivityThanksBinding>(this, R.layout.activity_thanks)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            done.setOnClickListener { startActivity(HomeActivity.createIntent(this@ThanksActivity)) }
        }
    }

}
