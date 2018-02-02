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

        private const val EXTRA_URL = "extraUrl"

        @JvmStatic
        fun createIntent(context: Context, url: String): Intent {
            return Intent(context, VideoViewActivity::class.java)
                    .putExtra(EXTRA_URL, url)
        }
    }

    private val binding: ActivityVideoviewBinding by lazy {
        DataBindingUtil.setContentView<ActivityVideoviewBinding>(this, R.layout.activity_videoview)
    }

    private val mediaController: MediaController by lazy { MediaController(this) }

    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = intent.extras.getString(EXTRA_URL)
        showLoading()
        setupView()
    }


    private fun setupView() {
        binding.run {
            mediaController.setAnchorView(video)
            video.apply {
                setMediaController(mediaController)
                setVideoURI(Uri.parse(url))
                requestFocus()
                setOnPreparedListener {
                    dismissLoading()
                    video.start()
                }
            }
        }
    }

}