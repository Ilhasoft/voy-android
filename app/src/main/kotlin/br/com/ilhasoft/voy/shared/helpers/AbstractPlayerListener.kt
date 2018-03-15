package br.com.ilhasoft.voy.shared.helpers

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

/**
 * Created by felipe on 15/03/18.
 */
abstract class AbstractPlayerListener : Player.EventListener {

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {}

    override fun onTracksChanged(
        trackGroups: TrackGroupArray,
        trackSelections: TrackSelectionArray
    ) {}

    override fun onLoadingChanged(isLoading: Boolean) {}

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {}

    override fun onPlayerError(error: ExoPlaybackException) {}

    override fun onRepeatModeChanged(repeatMode: Int) {

    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

    }

    override fun onPositionDiscontinuity(reason: Int) {

    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

    }

    override fun onSeekProcessed() {

    }
}