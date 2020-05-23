package com.educatorapp.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.educatorapp.R
import com.educatorapp.databinding.FragmentNoDataBinding
import com.educatorapp.utils.extensions.gone

class NoDataFoundFragment : Fragment(R.layout.fragment_no_data) {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var fragmentNoDataBinding: FragmentNoDataBinding? = null

    private var message_1 = ""
    private var message_2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /** message details **/
        arguments?.let {
            message_1 = it.getString(ARG_MESSAGE_1, "")
            message_2 = it.getString(ARG_MESSAGE_2, "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNoDataBinding.bind(view)
        fragmentNoDataBinding = binding

        /** set message to textviews **/
        binding.txtMessage1.text = message_1
        binding.txtMessage2.text = message_2

        if (message_2.isNullOrEmpty()) {
            binding.txtMessage2.gone()
        }

    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        fragmentNoDataBinding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_MESSAGE_1 = "message_1"
        private const val ARG_MESSAGE_2 = "message_2"

        fun create(message_1: String, message_2: String) = NoDataFoundFragment().apply {
            arguments = bundleOf(ARG_MESSAGE_1 to message_1, ARG_MESSAGE_2 to message_2)
        }
    }
}