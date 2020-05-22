package com.educatorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.educatorapp.databinding.CardViewEducatorItemBinding
import com.educatorapp.model.Educator

class EducatorListAdapter(val onClickListener: OnClickListener) :
    ListAdapter<Educator, EducatorViewHolder>(DIFF_CALLBACK) {

    private val educators: MutableList<Educator> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = educators.size

    fun setEducators(list: List<Educator>) {
        clearList()
        educators.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        educators.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducatorViewHolder {
        return EducatorViewHolder(CardViewEducatorItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: EducatorViewHolder, position: Int) =
        holder.bind(educators[position], onClickListener)

    class OnClickListener(val clickListener: (educator: Educator) -> Unit) {
        fun onClick(educator: Educator) = clickListener(educator)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Educator>() {
            override fun areItemsTheSame(oldItem: Educator, newItem: Educator): Boolean =
                oldItem.Id == newItem.Id

            override fun areContentsTheSame(oldItem: Educator, newItem: Educator): Boolean =
                oldItem == newItem

        }
    }

}