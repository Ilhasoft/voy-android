package br.com.ilhasoft.voy.ui.addreport.medias

import android.databinding.DataBindingUtil
import android.os.Bundle
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityAddMediasBinding
import br.com.ilhasoft.voy.ui.base.BaseActivity

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddMediasActivity : BaseActivity(), AddMediasContract {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityAddMediasBinding>(this@AddMediasActivity, R.layout.activity_add_medias)
    }

    private val presenter by lazy { AddMediasPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            this.presenter = this@AddMediasActivity.presenter
            toolbar?.apply {
                buttonBack.setOnClickListener { onBackPressed() }
            }
        }

        presenter.attachView(this)
    }
}