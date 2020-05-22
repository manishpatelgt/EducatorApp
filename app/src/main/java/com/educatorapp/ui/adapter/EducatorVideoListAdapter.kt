package com.educatorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.educatorapp.databinding.CardViewEducatorVideoBinding
import com.educatorapp.model.Educator
import com.educatorapp.model.Subject
import com.educatorapp.model.Video

class EducatorVideoListAdapter(val onClickListener: OnClickListener) :
    ListAdapter<Video, VideoViewHolder>(DIFF_CALLBACK) {

    private val videos: MutableList<Video> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = videos.size

    fun setVideos(list: List<Video>) {
        clearList()
        videos.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        videos.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(CardViewEducatorVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) =
        holder.bind(videos[position], onClickListener)

    class OnClickListener(val clickListener: (video: Video) -> Unit) {
        fun onClick(video: Video) = clickListener(video)
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