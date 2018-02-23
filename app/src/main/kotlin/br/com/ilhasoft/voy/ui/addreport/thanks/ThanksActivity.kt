package br.com.ilhasoft.voy.ui.addreport.thanks

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle

import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityThanksBinding
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import br.com.ilhasoft.voy.ui.addreport.AddReportActivity
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.shared.helpers.ResourcesHelper

class ThanksActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, ThanksActivity::class.java)
    }

    private val preferences: Preferences by lazy { SharedPreferences(this) }

    private val binding: ActivityThanksBinding by lazy {
        DataBindingUtil.setContentView<ActivityThanksBinding>(this, R.layout.activity_thanks)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            newReport.setOnClickListener {
                startActivity(AddReportActivity.createIntent(this@ThanksActivity))
                finish()
            }
            drawableResId = getDrawableFromPreferences()
            close.setOnClickListener { finish() }
        }
    }

    private fun getDrawableFromPreferences(): Int {
        val position = preferences.getString(User.AVATAR).extractNumbers().toInt().minus(1)
        return ResourcesHelper.getAvatarsResources(this)[position]
    }


}
