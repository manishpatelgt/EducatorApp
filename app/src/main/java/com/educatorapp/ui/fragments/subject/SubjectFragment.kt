package com.educatorapp.ui.fragments.subject

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.educatorapp.R
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.educatorapp.application.App.Companion.appContext
import com.educatorapp.databinding.FragmentSubjectBinding
import com.educatorapp.ui.adapter.SubjectListAdapter
import com.educatorapp.ui.base.BaseFragment
import com.educatorapp.utils.constants.Constants
import com.educatorapp.utils.network.isNetworkAvailable
import com.educatorapp.utils.enums.State.ERROR

class SubjectFragment :
    BaseFragment<SubjectViewModel, FragmentSubjectBinding>(R.layout.fragment_subject) {

    override val mViewModel: SubjectViewModel by viewModels()
    private lateinit var mAdapter: SubjectListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isNetworkAvailable()) {
            mViewModel.getSubjects()
        } else {
            loadFragment(appContext.getString(R.string.no_internet_connection), "")
        }

        /** Set observers*/
        setObservers()

        mAdapter = SubjectListAdapter(SubjectListAdapter.OnClickListener {
            /** Move to Educator list Fragment **/
            val title = " ${Constants.EDUCATORS} (${it.title})"
            val bundle = bundleOf("title" to title, "subject" to it)
            findNavController().navigate(R.id.action_to_educator_list_screen, bundle)
        })

        mViewBinding.subjectList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    private fun setObservers() {

        /** Set observer for a Status */
        mViewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                ERROR -> {
                    loadFragment(
                        appContext.getString(R.string.api_call_retry_message),
                        appContext.getString(R.string.api_call_retry_message_2)
                    )
                }
            }
        })

        /** Setup subject list observer  */
        mViewModel.subjects.observe(viewLifecycleOwner, Observer { entries ->
            mAdapter.setSubjects(entries)
        })
    }

    private fun loadFragment(message_1: String, message_2: String) {
        showFragment(
            message_1,
            message_2
        )
    }
}