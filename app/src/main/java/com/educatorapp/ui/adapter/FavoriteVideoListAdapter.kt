package com.educatorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.educatorapp.databinding.CardViewFavoriteVideoBinding
import com.educatorapp.model.Video
import com.educatorapp.utils.extensions.loadUrl
import com.educatorapp.utils.states.FavState

class FavoriteVideoListAdapter(val onClickListener: OnClickListener) : ListAdapter<Video, FavoriteVideoViewHolder>(DIFF_CALLBACK) {

    private val mVideos: MutableList<Video> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = mVideos.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteVideoViewHolder {
        return FavoriteVideoViewHolder(
            CardViewFavoriteVideoBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    /*inner class FavoriteVideoViewHolder(private val binding: CardViewFavoriteVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            binding.textVideoTitle.text = video.title
            binding.thumbnailLogo.loadUrl(video.iconUrl)

            binding.favoriteBtn.setOnClickListener {
                onFavoriteClickListener.onFavClick(video)
            }

            binding.root.setOnClickListener {
                onClickListener.onClick(video)
            }
        }
    }*/

    override fun onBindViewHolder(holder: FavoriteVideoViewHolder, position: Int) =
        holder.bind(mVideos[position], onClickListener)

    interface OnClickListener {
        fun onClick(video: Video, favState: FavState)
    }

    fun setVideos(videoList: List<Video>) {
        clearAllVideos()
        mVideos.addAll(videoList)
        notifyDataSetChanged()
    }

    fun clearAllVideos() {
        mVideos.clear()
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
                oldItem.Id == newItem.Id

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
                oldItem == newItem

        }
    }
}