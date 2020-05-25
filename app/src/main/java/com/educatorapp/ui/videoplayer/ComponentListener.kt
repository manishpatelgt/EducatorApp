package com.educatorapp.ui.videoplayer

import android.util.Log
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.audio.AudioRendererEventListener
import com.google.android.exoplayer2.decoder.DecoderCounters
import com.google.android.exoplayer2.video.VideoRendererEventListener

class ComponentListener : Player.EventListener, VideoRendererEventListener,
    AudioRendererEventListener, AnalyticsListener {
    private val TAG = "ComponentListener"

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        val stateString: String
        when (playbackState) {
            Player.STATE_IDLE -> stateString = "ExoPlayer.STATE_IDLE      -"
            Player.STATE_BUFFERING -> stateString = "ExoPlayer.STATE_BUFFERING -"
            Player.STATE_READY -> stateString = "ExoPlayer.STATE_READY     -"
            Player.STATE_ENDED -> stateString = "ExoPlayer.STATE_ENDED     -"
            else -> stateString = "UNKNOWN_STATE             -"
        }
        Log.d(TAG, "changed state to $stateString playWhenReady: $playWhenReady")
    }

    // Implementing VideoRendererEventListener.

    override fun onVideoEnabled(counters: DecoderCounters) {
        // Do nothing.
    }

    override fun onVideoDecoderInitialized(
        decoderName: String,
        initializedTimestampMs: Long,
        initializationDurationMs: Long
    ) {
        // Do nothing.
    }

    override fun onVideoInputFormatChanged(format: Format) {
        // Do nothing.
    }

    override fun onDroppedFrames(count: Int, elapsedMs: Long) {
        // Do nothing.
    }

    override fun onVideoSizeChanged(
        width: Int,
        height: Int,
        unappliedRotationDegrees: Int,
        pixelWidthHeightRatio: Float
    ) {
        // Do nothing.
    }

    override fun onVideoDisabled(counters: DecoderCounters) {
        // Do nothing.
    }

    // Implementing AudioRendererEventListener.

    override fun onAudioEnabled(counters: DecoderCounters) {
        // Do nothing.
    }

    override fun onAudioSessionId(audioSessionId: Int) {
        // Do nothing.
    }

    override fun onAudioDecoderInitialized(
        decoderName: String,
        initializedTimestampMs: Long,
        initializationDurationMs: Long
    ) {
        // Do nothing.
    }

    override fun onAudioInputFormatChanged(format: Format) {
        // Do nothing.
    }

    override fun onAudioSinkUnderrun(
        bufferSize: Int,
        bufferSizeMs: Long,
        elapsedSinceLastFeedMs: Long
    ) {
        // Do nothing.
    }

    override fun onAudioDisabled(counters: DecoderCounters) {
        // Do nothing.
    }

}

