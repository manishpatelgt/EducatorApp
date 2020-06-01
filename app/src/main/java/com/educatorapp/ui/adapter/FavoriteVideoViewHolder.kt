package com.educatorapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.educatorapp.databinding.CardViewFavoriteVideoBinding
import com.educatorapp.model.Video
import com.educatorapp.utils.extensions.loadUrl
import com.educatorapp.utils.states.FavState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteVideoViewHolder(private val binding: CardViewFavoriteVideoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(video: Video, OnClickListener: FavoriteVideoListAdapter.OnClickListener? = null) {
        binding.textVideoTitle.text = video.title
        binding.thumbnailLogo.loadUrl(video.iconUrl)

        binding.favoriteBtn.setOnClickListener {
            OnClickListener?.let {
                it.onClick(video, FavState.FavRemove)
            }
        }

        binding.root.setOnClickListener {
            OnClickListener?.let {
                it.onClick(video,FavState.FavClick)
            }
        }
    }
}