package com.educatorapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.educatorapp.databinding.CardViewSubjectItemBinding
import com.educatorapp.model.Subject
import com.educatorapp.utils.extensions.loadUrl

class SubjectViewHolder(val binding: CardViewSubjectItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(subject: Subject, OnClickListener: SubjectListAdapter.OnClickListener? = null) {
        binding.textSubject.text = subject.title
        binding.subjectIcon.loadUrl(subject.iconUrl)

        binding.root.setOnClickListener {
            OnClickListener?.let {
                it.onClick(subject)
            }
        }
    }
}