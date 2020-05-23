package com.educatorapp.ui.fragments.videoplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import org.koin.android.viewmodel.ext.android.viewModel
import com.educatorapp.R
import com.educatorapp.application.App.Companion.appContext
import com.educatorapp.databinding.ActivityVideoPlayBinding
import com.educatorapp.model.Video
import com.educatorapp.ui.base.BaseActivity
import com.educatorapp.ui.videoplayer.ComponentListener
import com.educatorapp.utils.clients.VideoClient
import com.educatorapp.utils.extensions.isAtLeastAndroid6
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

class VideoPlayActivity : BaseActivity<VideoPlayViewModel, ActivityVideoPlayBinding>(),
    View.OnClickListener {

    override fun getViewBinding(): ActivityVideoPlayBinding =
        ActivityVideoPlayBinding.inflate(layoutInflater)

    // lazy inject MyViewModel
    override val mViewModel: VideoPlayViewModel by viewModel()

    private var playerView: PlayerView? = null
    private lateinit var player: SimpleExoPlayer
    var mediaUri = ""
    private var playbackPosition: Long = 0
    private var currentWindow: Int = 0
    private var playWhenReady = true

    private lateinit var video: Video

    private var componentListener: ComponentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        video = intent.getParcelableExtra(EXTRA)
        mediaUri = video.videoUrl

        setupUI()
    }

    private fun setupUI() {

        playerView = mViewBinding.videoView
        componentListener = ComponentListener()

        mViewBinding.txtVideoTitle.text = video.title
        mViewBinding.txtVideoInfo.text = video.description

        mViewModel.setVideoId(video.Id)

        mViewModel.selectedVideo.observe(this, Observer {
            if (it == null) {
                Log.e(TAG, "selectedVideo == null")
                /** video not found **/
                mViewBinding.likeBtn.isSelected = false
                mViewBinding.favoriteBtn.isSelected = false
            } else {
                /** video found so check isLike status **/
                mViewBinding.likeBtn.isSelected = it.isLike
                /** video found so check isFavorite status **/
                mViewBinding.favoriteBtn.isSelected = it.isFavorite
            }
        })

        /** button listeners **/
        mViewBinding.backBtn.setOnClickListener(this)
        mViewBinding.favoriteBtn.setOnClickListener(this)
        mViewBinding.likeBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_btn -> super.onBackPressed()
            R.id.like_btn -> {
                if (mViewBinding.likeBtn.isSelected) {
                    mViewBinding.likeBtn.isSelected = false
                    mViewModel.setVideoLike(video, false)
                    VideoClient.updateVideoLike(video.Id, false)
                } else {
                    mViewBinding.likeBtn.isSelected = true
                    mViewModel.setVideoLike(video, true)
                    VideoClient.updateVideoLike(video.Id, true)
                }
            }
            R.id.favorite_btn -> {
                if (mViewBinding.favoriteBtn.isSelected) {
                    mViewBinding.favoriteBtn.isSelected = false
                    mViewModel.setVideoFavorite(video, false)
                } else {
                    mViewBinding.favoriteBtn.isSelected = true
                    mViewModel.setVideoFavorite(video, true)
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        if (isAtLeastAndroid6()) {
            initializePlayer(mediaUri)
        }
    }

    public override fun onResume() {
        super.onResume()
        if (isAtLeastAndroid6() || !::player.isInitialized) {
            initializePlayer(mediaUri)
        }
    }

    public override fun onPause() {
        super.onPause()
        if (isAtLeastAndroid6() && ::player.isInitialized) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (isAtLeastAndroid6() && ::player.isInitialized) {
            releasePlayer()
        }
    }

    private fun initializePlayer(uri: String) {
        if (!::player.isInitialized) {
            // a factory to create an AdaptiveVideoTrackSelection
            val adaptiveTrackSelectionFactory = AdaptiveTrackSelection.Factory(BANDWIDTH_METER)
            player = ExoPlayerFactory.newSimpleInstance(
                this,
                DefaultRenderersFactory(this),
                DefaultTrackSelector(adaptiveTrackSelectionFactory),
                DefaultLoadControl()
            )
        }

        componentListener?.let {
            player.addListener(it)
        }
        playerView?.player = player

        player.playWhenReady = playWhenReady
        player.seekTo(currentWindow, playbackPosition)

        val mediaSource = buildMediaSource(Uri.parse(uri))
        player.prepare(mediaSource, true, false)
    }

    private fun releasePlayer() {
        playbackPosition = player.currentPosition
        currentWindow = player.currentWindowIndex
        playWhenReady = player.playWhenReady
        componentListener?.let {
            player.removeListener(it)
        }
        player.release()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun buildMediaSource(uri: Uri): MediaSource =
        ProgressiveMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-codelab"))
            .createMediaSource(uri)

    companion object {
        val TAG = VideoPlayActivity::class.simpleName
        private val BANDWIDTH_METER = DefaultBandwidthMeter()
        const val EXTRA = "Video"
        fun getIntent(video: Video) = Intent(appContext, VideoPlayActivity::class.java).apply {
            putExtra(EXTRA, video)
        }
    }

}