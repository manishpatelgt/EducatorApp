package com.educatorapp.ui.fragments.favorites

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.educatorapp.R
import com.educatorapp.application.App
import com.educatorapp.databinding.FragmentFavoriteBinding
import com.educatorapp.model.Video
import com.educatorapp.ui.adapter.FavoriteVideoListAdapter
import com.educatorapp.ui.base.BaseFragment
import com.educatorapp.ui.fragments.videoplayer.VideoPlayActivity
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFragment :
    BaseFragment<FavoriteViewModel, FragmentFavoriteBinding>(R.layout.fragment_favorite),
    FavoriteVideoListAdapter.OnClickListener, FavoriteVideoListAdapter.OnFavoriteClickListener {

    // lazy inject MyViewModel
    override val mViewModel: FavoriteViewModel by viewModel()
    private lateinit var mAdapter: FavoriteVideoListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Set observers*/
        setObservers()
        mAdapter = FavoriteVideoListAdapter(this, this)

        /*mAdapter = FavoriteVideoListAdapter(FavoriteVideoListAdapter.OnClickListener { video ->
            /** Move to Educator Video Play fragment **/
            requireActivity()?.let {
                startActivity(VideoPlayActivity.getIntent(video))
            }
        })*/

        mViewBinding.favoriteVideoList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = mAdapter
        }
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

    override fun onClick(video: Video) {
        /** Move to Educator Video Play fragment **/
        requireActivity()?.let {
            startActivity(VideoPlayActivity.getIntent(video))
        }
    }

    override fun onFavClick(video: Video) {
        mViewModel.removeVideoFavorite(video)
    }

}