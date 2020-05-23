package com.educatorapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.educatorapp.databinding.CardViewFavoriteVideoBinding
import com.educatorapp.model.Video
import com.educatorapp.utils.extensions.loadUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Manish Patel on 5/23/2020.
 */
class FavoriteVideoViewHolder(private val binding: CardViewFavoriteVideoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val coroutineScope = CoroutineScope(Dispatchers.IO)
    fun bind(video: Video, OnClickListener: FavoriteVideoListAdapter.OnClickListener? = null) {
        binding.textVideoTitle.text = video.title
        binding.thumbnailLogo.loadUrl(video.iconUrl)

        binding.favoriteBtn.setOnClickListener {
            coroutineScope.launch {
            }
        }

        binding.root.setOnClickListener {
            OnClickListener?.let {
                it.onClick(video)
            }
        }
    }
}