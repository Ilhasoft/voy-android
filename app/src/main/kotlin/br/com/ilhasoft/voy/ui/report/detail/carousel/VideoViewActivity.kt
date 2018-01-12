package br.com.ilhasoft.voy.ui.report.detail.carousel

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityVideoviewBinding
import br.com.ilhasoft.voy.ui.base.BaseActivity

/**
 * Created by geral on 04/01/18.
 */
class VideoViewActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent = Intent(context, VideoViewActivity::class.java)
    }

    private val binding: ActivityVideoviewBinding by lazy {
        DataBindingUtil.setContentView<ActivityVideoviewBinding>(this, R.layout.activity_videoview)
    }

    private val mediaController: MediaController by lazy { MediaController(this) }

    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = intent.extras.getString("url")
        showLoading()
        setupView()
    }


    private fun setupView() {
        binding.run {
            mediaController.setAnchorView(video)
            video.setMediaController(mediaController)
            video.setVideoURI(Uri.parse(url))
            video.requestFocus()
            video.setOnPreparedListener {
                dismissLoading()
                video.start()
            }
        }
    }

}