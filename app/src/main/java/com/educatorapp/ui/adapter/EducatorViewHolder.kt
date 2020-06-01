package com.educatorapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.educatorapp.databinding.CardViewEducatorItemBinding
import com.educatorapp.model.Educator
import com.educatorapp.utils.extensions.loadUrl

class EducatorViewHolder(val binding: CardViewEducatorItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(educator: Educator, OnClickListener: EducatorListAdapter.OnClickListener? = null) {
        binding.textEducator.text = educator.name
        binding.educatorIcon.loadUrl(educator.iconUrl)
        binding.videoCount.text = "${educator.videosCount} Videos"
        binding.ratingBar.rating = educator.rating
        binding.ratingText.text = "${educator.rating}/5.0"

        binding.root.setOnClickListener {
            OnClickListener?.let {
                it.onClick(educator)
            }
        }
    }
}