package com.educatorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.educatorapp.databinding.CardViewFavoriteVideoBinding
import com.educatorapp.model.VideoEntity

class FavoriteVideoListAdapter(val onClickListener: OnClickListener) :
    ListAdapter<VideoEntity, FavoriteVideoListAdapter.VideoViewHolder>(DIFF_CALLBACK) {

    private val mVideos: MutableList<VideoEntity> = mutableListOf()

    inner class VideoViewHolder(private var binding: CardViewFavoriteVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: VideoEntity) {
            binding.video = video
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(CardViewFavoriteVideoBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = getItem(position)
        holder.itemView.setOnClickListener {
            video?.let {
                onClickListener.onClick(it)
            }
        }
        holder.bind(video)
    }

    class OnClickListener(val clickListener: (video: VideoEntity) -> Unit) {
        fun onClick(video: VideoEntity) = clickListener(video)
    }

    fun setVideos(videoList: List<VideoEntity>) {
        clearAllVideos()
        mVideos.addAll(videoList)
        notifyDataSetChanged()
    }

    fun clearAllVideos() {
        mVideos.clear()
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoEntity>() {
            override fun areItemsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean =
                oldItem.Id == newItem.Id

            override fun areContentsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean =
                oldItem == newItem

        }
    }
}