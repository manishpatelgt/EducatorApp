package com.educatorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.educatorapp.databinding.CardViewSubjectItemBinding
import com.educatorapp.model.Subject

class SubjectListAdapter(val onClickListener: OnClickListener) :
    ListAdapter<Subject, SubjectViewHolder>(DIFF_CALLBACK) {

    private val mSubjects: MutableList<Subject> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder(CardViewSubjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = mSubjects.size

    fun setSubjects(subjectsList: List<Subject>) {
        clearAllSubjects()
        mSubjects.addAll(subjectsList)
        notifyDataSetChanged()
    }

    fun clearAllSubjects() {
        mSubjects.clear()
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int)  = holder.bind(mSubjects[position], onClickListener)

    class OnClickListener(val clickListener: (subject: Subject) -> Unit) {
        fun onClick(subject: Subject) = clickListener(subject)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Subject>() {
            override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean =
                oldItem.Id == newItem.Id

            override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean =
                oldItem == newItem

        }
    }
}