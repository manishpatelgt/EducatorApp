package com.educatorapp.ui.fragments.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.educatorapp.R
import com.educatorapp.databinding.FragmentFavoriteBinding
import com.educatorapp.model.Video
import com.educatorapp.ui.adapter.FavoriteVideoListAdapter
import com.educatorapp.ui.base.BaseFragment
import com.educatorapp.ui.videoplayer.VideoPlayActivity
import com.educatorapp.ui.main.MainViewModel
import com.educatorapp.utils.constants.Constants
import com.educatorapp.utils.states.FavState
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFragment :
    BaseFragment<FavoriteViewModel, FragmentFavoriteBinding>(R.layout.fragment_favorite),
    FavoriteVideoListAdapter.OnClickListener {

    // lazy inject MyViewModel
    override val mViewModel: FavoriteViewModel by viewModel()
    private lateinit var mAdapter: FavoriteVideoListAdapter
    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Set fragment state in shared view model */
        sharedViewModel.setFragmentStateHolder(Constants.FRAGMENT_FAVORITE)

        /** Set observers*/
        setObservers()
        mAdapter = FavoriteVideoListAdapter(this)

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
                showFragment(
                    getString(R.string.favorite_video_list_empty_message),
                    ""
                )
            } else {
                mAdapter.setVideos(it)
            }
        })
    }

    override fun onClick(video: Video, favState: FavState) {
        when (favState) {
            is FavState.FavRemove -> {
                mViewModel.removeVideoFavorite(video)
                sharedViewModel.setToastMessage(getString(R.string.favorite_removed_message))
            }
            is FavState.FavClick -> {
                /** Move to Educator Video Play fragment **/
                requireActivity()?.let {
                    startActivity(VideoPlayActivity.getIntent(video))
                }
            }
        }

    }

}