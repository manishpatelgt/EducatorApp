package com.educatorapp.ui.fragments.educators

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
import com.educatorapp.databinding.FragmentEducatorsBinding
import com.educatorapp.model.Subject
import com.educatorapp.ui.adapter.EducatorListAdapter
import com.educatorapp.ui.base.BaseFragment
import com.educatorapp.utils.constants.Constants
import com.educatorapp.utils.enums.State
import com.educatorapp.utils.network.isNetworkAvailable

class EducatorsFragment :
    BaseFragment<EducatorsViewModel, FragmentEducatorsBinding>(R.layout.fragment_educators) {

    override val mViewModel: EducatorsViewModel by viewModels()
    private lateinit var mAdapter: EducatorListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var selectedSubject = arguments?.getParcelable<Subject>("subject")
        if (isNetworkAvailable()) {
            mViewModel.getEducators(selectedSubject?.Id)
        } else {
            loadFragment(App.appContext.getString(R.string.no_internet_connection), "")
        }

        /** Set observers*/
        setObservers()

        mAdapter = EducatorListAdapter(EducatorListAdapter.OnClickListener {
            /** Move to Educator Video list Fragment **/
            val title = " ${Constants.EDUCATORS} (${it.name})"
            val bundle = bundleOf("title" to title, "educator" to it)
            findNavController().navigate(R.id.active_educator_video_list_screen, bundle)
        })

        mViewBinding.educatorsList.apply {
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
                    loadFragment(appContext.getString(R.string.no_data_found_message_2), "")
                }
            }
        })

        /** Setup educator list observer  */
        mViewModel.educators.observe(viewLifecycleOwner, Observer { entries ->
            mAdapter.setEducators(entries)
        })

    }

    private fun loadFragment(message_1: String, message_2: String) {
        showFragment(
            message_1,
            message_2
        )
    }
}