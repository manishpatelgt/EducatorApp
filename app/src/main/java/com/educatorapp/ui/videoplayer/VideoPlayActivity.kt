package com.educatorapp.ui.videoplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import com.educatorapp.R
import com.educatorapp.application.App.Companion.appContext
import com.educatorapp.databinding.ActivityVideoPlayBinding
import com.educatorapp.model.Video
import com.educatorapp.model.VideoComment
import com.educatorapp.ui.adapter.VideoCommentAdapter
import com.educatorapp.ui.base.BaseActivity
import com.educatorapp.utils.clients.VideoClient
import com.educatorapp.utils.enums.State
import com.educatorapp.utils.extensions.gone
import com.educatorapp.utils.extensions.isAtLeastAndroid6
import com.educatorapp.utils.extensions.recyclerDivider
import com.educatorapp.utils.extensions.visible
import com.educatorapp.utils.network.isNetworkAvailable
import com.educatorapp.utils.states.CommentState
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
    View.OnClickListener, VideoCommentAdapter.OnClickListener {

    override fun getViewBinding(): ActivityVideoPlayBinding =
        ActivityVideoPlayBinding.inflate(layoutInflater)

    // lazy inject MyViewModel
    override val mViewModel: VideoPlayViewModel by viewModel()
    private lateinit var mAdapter: VideoCommentAdapter

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

        /** get video comment **/
        mViewModel.getVideoComments(video.key)

        mAdapter = VideoCommentAdapter(this)

        mViewBinding.commentsList.apply {
            addItemDecoration(recyclerDivider())
            layoutManager = LinearLayoutManager(this@VideoPlayActivity)
            setHasFixedSize(true)
            adapter = mAdapter
        }

        setObservers()

        /** button listeners **/
        mViewBinding.backBtn.setOnClickListener(this)
        mViewBinding.favoriteBtn.setOnClickListener(this)
        mViewBinding.likeBtn.setOnClickListener(this)
        mViewBinding.sendBtn.setOnClickListener(this)
    }

    private fun setObservers() {

        mViewModel.videoComments.observe(this, Observer {
            mAdapter.setComments(it)
        })

        mViewModel.selectedVideo.observe(this, Observer {
            if (it == null) {
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

        /** Set observer for a Status */
        mViewModel.status.observe(this, Observer {
            when (it) {
                State.LOADING -> mViewBinding.progress.visible()
                State.ERROR -> {
                    showFragment(
                        appContext.getString(R.string.api_call_retry_message),
                        appContext.getString(R.string.api_call_retry_message_2)
                    )
                }
                State.NOINTERNET -> {
                    mViewBinding.progress.gone()
                    showFragment(appContext.getString(R.string.no_internet_connection), "")
                }
                State.NODATA -> {
                    mViewBinding.progress.gone()
                    showFragment(appContext.getString(R.string.no_data_found_message_4), "")
                }
                State.DONE -> {
                    mViewBinding.progress.gone()
                }
            }
        })

        mViewModel.isSubmitted.observe(this, Observer {
            if (it) {
                showProgress(false)
                resetUI()
                /** get video comment **/
                mViewModel.getVideoComments(video.key)
                mViewModel.resetSubmit()
            }
        })
    }

    fun resetUI() {
        mViewBinding.editComment.text.clear()
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
            R.id.send_btn -> {
                val comment = mViewBinding.editComment.text.toString().trim()

                if (comment.isNullOrEmpty()) {
                    showToastMessage(getString(R.string.comment_hint))
                    return
                }

                if (!isNetworkAvailable()) {
                    showToastMessage(getString(R.string.no_internet_connection))
                    return
                }

                showProgress(true)

                mViewModel.submitComment(
                    comment,
                    video.key,
                    video.Id,
                    video.subjectId,
                    video.educatorId
                )
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

    override fun onClick(videoComment: VideoComment, commentState: CommentState) {
        when (commentState) {
            is CommentState.EditComment -> {

            }
            is CommentState.DeleteComment -> {
                //Delete comment from firebase
                mViewModel.deleteComment(videoComment, video.key)
            }
        }

    }

}