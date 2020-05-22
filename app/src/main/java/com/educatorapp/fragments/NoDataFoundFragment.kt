package com.educatorapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.educatorapp.R
import com.educatorapp.utils.extensions.gone
import kotlinx.android.synthetic.main.fragment_no_data.*

class NoDataFoundFragment : Fragment() {

    private var message_1 = ""
    private var message_2 = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_no_data, container, false)
        return root
    }

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

        /** set message to textviews **/
        txt_message_1.text = message_1
        txt_message_2.text = message_2

        if (message_2.isNullOrEmpty()) {
            txt_message_2.gone()
        }

    }

    companion object {
        private const val ARG_MESSAGE_1 = "message_1"
        private const val ARG_MESSAGE_2 = "message_2"

        fun create(message_1: String, message_2: String) = NoDataFoundFragment().apply {
            arguments = bundleOf(ARG_MESSAGE_1 to message_1, ARG_MESSAGE_2 to message_2)
        }
    }
}