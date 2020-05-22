package com.educatorapp.ui.fragments.educatorvideo

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.educatorapp.R
import com.educatorapp.application.App
import com.educatorapp.application.App.Companion.appContext
import com.educatorapp.databinding.FragmentEducatorVideosBinding
import com.educatorapp.model.Educator
import com.educatorapp.ui.adapter.EducatorListAdapter
import com.educatorapp.ui.adapter.EducatorVideoListAdapter
import com.educatorapp.ui.base.BaseFragment
import com.educatorapp.utils.constants.Constants
import com.educatorapp.utils.enums.State
import com.educatorapp.utils.network.isNetworkAvailable

class EducatorVideoFragment :
    BaseFragment<EducatorVideoViewModel, FragmentEducatorVideosBinding>(R.layout.fragment_educator_videos) {

    override val mViewModel: EducatorVideoViewModel by viewModels()
    private lateinit var mAdapter: EducatorVideoListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var selectedEducator = arguments?.getParcelable<Educator>("educator")

        if (isNetworkAvailable()) {
            mViewModel.getVideos(
                selectedEducator?.Id,
                selectedEducator?.subjectId
            )
        } else {
            loadFragment(App.appContext.getString(R.string.no_internet_connection), "")
        }

        /** Set observers*/
        setObservers()

        mAdapter = EducatorVideoListAdapter(EducatorVideoListAdapter.OnClickListener {
            /** Move to Educator Video Play fragment / Activity **/
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
                State.ERROR -> {
                    loadFragment(
                        App.appContext.getString(R.string.api_call_retry_message),
                        App.appContext.getString(R.string.api_call_retry_message_2)
                    )
                }
                State.NODATA -> {
                    loadFragment(appContext.getString(R.string.no_data_found_message), "")
                }
            }
        })

        /** Setup educator video list observer  */
        mViewModel.videos.observe(viewLifecycleOwner, Observer { entries ->
            mAdapter.setVideos(entries)
        })
    }

    private fun loadFragment(message_1: String, message_2: String) {
        showFragment(
            message_1,
            message_2
        )
    }
}