package br.com.ilhasoft.voy.ui.report.detail.carousel

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityVideoviewBinding
import br.com.ilhasoft.voy.shared.helpers.AbstractPlayerListener
import br.com.ilhasoft.voy.ui.base.BaseActivity
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util

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
    private var player: SimpleExoPlayer? = null
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = intent.extras.getString(EXTRA_URL)
        showLoading()
        setupView()
    }

    override fun onResume() {
        super.onResume()
        playVideo()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    private fun setupView() {
        player = setupPlayer()
        setupPlayerView()
    }

    private fun setupPlayerView() {
        binding.playerView.player = player
        player?.prepare(createVideoSource())
    }

    private fun setupPlayer(): SimpleExoPlayer {
        val trackSelector = createTrack()
        val loadControl = DefaultLoadControl()

        val player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl)
        player.addListener(object: AbstractPlayerListener() {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (Player.STATE_READY == playbackState) dismissLoading()
            }
        })
        player.playWhenReady = true

        player.seekTo(0)
        return player
    }

    private fun createTrack(): TrackSelector {
        val bandwidthMeter = DefaultBandwidthMeter()

        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        return DefaultTrackSelector(videoTrackSelectionFactory)
    }

    private fun createVideoSource(): MediaSource {
        return ExtractorMediaSource.Factory(buildDataSourceFactory(true))
            .createMediaSource(Uri.parse(url), null, null)
    }

    private fun buildDataSourceFactory(bandwidthMeter: DefaultBandwidthMeter?): DataSource.Factory {
        return DefaultDataSourceFactory(
            this, bandwidthMeter,
            buildHttpDataSourceFactory(bandwidthMeter)
        )
    }

    private fun buildHttpDataSourceFactory(bandwidthMeter: DefaultBandwidthMeter?): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(
            Util.getUserAgent(this, getString(R.string.app_name)),
            bandwidthMeter
        )
    }

    private fun buildDataSourceFactory(useBandwidthMeter: Boolean): DataSource.Factory {
        return buildDataSourceFactory(if (useBandwidthMeter) DefaultBandwidthMeter() else null)
    }

    private fun releasePlayer() {
        player?.release()
        player = null
        binding.playerView.setPlayer(null)
    }

    private fun playVideo() {
        if (player == null) {
            player = setupPlayer()
            setupPlayerView()
        }
    }
}