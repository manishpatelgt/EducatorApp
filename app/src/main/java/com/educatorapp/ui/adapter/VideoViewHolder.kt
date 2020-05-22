package com.educatorapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.educatorapp.databinding.CardViewEducatorVideoBinding
import com.educatorapp.model.Video
import com.educatorapp.utils.extensions.loadUrl

/**
 * Created by Manish Patel on 5/22/2020.
 */
class VideoViewHolder(val binding: CardViewEducatorVideoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(video: Video, OnClickListener: EducatorVideoListAdapter.OnClickListener? = null) {
        binding.textVideoTitle.text = video.title
        binding.thumbnailLogo.loadUrl(video.iconUrl)
        binding.likeText.text = "${video.totalLikes}"

        binding.root.setOnClickListener {
            OnClickListener?.let {
                it.onClick(video)
            }
        }
    }
}