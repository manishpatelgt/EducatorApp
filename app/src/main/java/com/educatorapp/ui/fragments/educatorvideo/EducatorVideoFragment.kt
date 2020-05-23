package com.educatorapp.ui.fragments.educatorvideo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.educatorapp.R
import com.educatorapp.application.App
import com.educatorapp.application.App.Companion.appContext
import com.educatorapp.databinding.FragmentEducatorVideosBinding
import com.educatorapp.model.Educator
import com.educatorapp.ui.adapter.EducatorVideoListAdapter
import com.educatorapp.ui.base.BaseFragment
import com.educatorapp.ui.fragments.videoplayer.VideoPlayActivity
import com.educatorapp.utils.enums.State
import com.educatorapp.utils.extensions.gone
import com.educatorapp.utils.extensions.visible

class EducatorVideoFragment :
    BaseFragment<EducatorVideoViewModel, FragmentEducatorVideosBinding>(R.layout.fragment_educator_videos) {

    override val mViewModel: EducatorVideoViewModel by viewModels()
    private lateinit var mAdapter: EducatorVideoListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var selectedEducator = arguments?.getParcelable<Educator>("educator")

        /** get video list based on educator and subject **/
        mViewModel.getVideos(
            selectedEducator?.Id,
            selectedEducator?.subjectId
        )

        /** Set observers*/
        setObservers()

        mAdapter = EducatorVideoListAdapter(EducatorVideoListAdapter.OnClickListener { video ->
            /** Move to Educator Video Play fragment / Activity **/
            requireActivity()?.let {
                startActivity(VideoPlayActivity.getIntent(video))
            }
        })

        mViewBinding.educatorVideoList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    private fun setObservers() {

        /** Set observer for a Status */
        mViewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                State.LOADING -> mViewBinding.progress.visible()
                State.ERROR -> {
                    loadFragment(
                        appContext.getString(R.string.api_call_retry_message),
                        appContext.getString(R.string.api_call_retry_message_2)
                    )
                }
                State.NOINTERNET -> {
                    mViewBinding.progress.gone()
                    loadFragment(appContext.getString(R.string.no_internet_connection), "")
                }
                State.NODATA -> {
                    mViewBinding.progress.gone()
                    loadFragment(appContext.getString(R.string.no_data_found_message), "")
                }
                State.DONE -> {
                    mViewBinding.progress.gone()
                }
            }
        })

        /** Setup educator video list observer  */
        mViewModel.videos.observe(viewLifecycleOwner, Observer { entries ->
            mAdapter.setVideos(entries)
        })
    }
}