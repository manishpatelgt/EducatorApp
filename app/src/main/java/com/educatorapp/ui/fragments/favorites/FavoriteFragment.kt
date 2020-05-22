package com.educatorapp.ui.fragments.favorites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.educatorapp.R
import com.educatorapp.application.App
import com.educatorapp.databinding.FragmentFavoriteBinding
import com.educatorapp.model.Video
import com.educatorapp.ui.adapter.FavoriteVideoListAdapter
import com.educatorapp.ui.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFragment :
    BaseFragment<FavoriteViewModel, FragmentFavoriteBinding>(R.layout.fragment_favorite) {

    // lazy inject MyViewModel
    override val mViewModel: FavoriteViewModel by viewModel()
    private lateinit var mAdapter: FavoriteVideoListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewBinding.apply {
            lifecycleOwner = lifecycleOwner
            viewModel = mViewModel
        }

        /** Set observers*/
        setObservers()
        mAdapter =
            FavoriteVideoListAdapter(FavoriteVideoListAdapter.OnClickListener { videoEntity ->
                /** Move to Educator Video Play fragment **/
                requireActivity()?.let {
                    val video = Video(
                        videoEntity.Id,
                        videoEntity.title,
                        videoEntity.description,
                        videoEntity.iconUrl,
                        videoEntity.videoUrl,
                        videoEntity.totalComment,
                        videoEntity.totalLikes,
                        videoEntity.subjectId,
                        videoEntity.educatorId,
                        videoEntity.rating
                    )
                    //startActivity(VideoPlayActivity.getIntent(video))
                }
            })

        /*mViewBinding.favoriteVideoList.adapter =
            FavoriteVideoListAdapter(FavoriteVideoListAdapter.OnClickListener { videoEntity ->
                /** Move to Educator Video Play fragment **/
                requireActivity()?.let {
                    val video = Video(
                        videoEntity.Id,
                        videoEntity.title,
                        videoEntity.description,
                        videoEntity.iconUrl,
                        videoEntity.videoUrl,
                        videoEntity.totalComment,
                        videoEntity.totalLikes,
                        videoEntity.subjectId,
                        videoEntity.educatorId,
                        videoEntity.rating
                    )
                }
                //startActivity(VideoPlayActivity.getIntent(video))
            })*/
    }

    private fun setObservers() {
        mViewModel.favoriteVideoList.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                mAdapter.setVideos(emptyList())
                loadFragment(
                    App.appContext.getString(R.string.favorite_video_list_empty_message),
                    ""
                )
            } else {
                mAdapter.setVideos(it)
            }
        })
    }

    private fun loadFragment(message_1: String, message_2: String) {
        showFragment(
            message_1,
            message_2
        )
    }
}