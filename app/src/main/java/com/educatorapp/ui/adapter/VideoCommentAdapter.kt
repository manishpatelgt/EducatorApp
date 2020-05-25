package com.educatorapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.educatorapp.R
import com.educatorapp.databinding.CardViewVideoCommentBinding
import com.educatorapp.model.VideoComment
import com.educatorapp.utils.TimeHelper
import com.educatorapp.utils.extensions.loadUrl
import com.educatorapp.utils.states.CommentState

/**
 * Created by Manish Patel on 5/25/2020.
 */
class VideoCommentAdapter(val onClickListener: OnClickListener) :
    ListAdapter<VideoComment, VideoCommentAdapter.VideoCommentViewHolder>(DIFF_CALLBACK) {

    private val comments: MutableList<VideoComment> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoCommentViewHolder {
        return VideoCommentViewHolder(
            CardViewVideoCommentBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    inner class VideoCommentViewHolder(private val binding: CardViewVideoCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(videoComment: VideoComment) {
            binding.textUser.text = videoComment.displayName
            binding.textComment.text = videoComment.comment
            binding.textDatetime.text =
                videoComment.createdAt?.let { TimeHelper.getStringToDate(it) }
            videoComment.photoUrl?.let { binding.userIcon.loadUrl(it) }

            binding.iconEdit.setOnClickListener {
                val popupMenu: PopupMenu = PopupMenu(binding.root.context, binding.iconEdit)
                popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_edit -> {
                            onClickListener.onClick(videoComment, CommentState.EditComment)
                        }
                        R.id.action_delete -> {
                            onClickListener.onClick(videoComment, CommentState.DeleteComment)
                        }
                    }
                    true
                }
                popupMenu.show()
            }
        }
    }

    interface OnClickListener {
        fun onClick(videoComment: VideoComment, commentState: CommentState)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount() = comments.size

    fun setComments(list: List<VideoComment>) {
        clearList()
        comments.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        comments.clear()
    }

    override fun onBindViewHolder(holder: VideoCommentViewHolder, position: Int) =
        holder.bind(comments[position])

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoComment>() {
            override fun areItemsTheSame(oldItem: VideoComment, newItem: VideoComment): Boolean =
                oldItem.Id == newItem.Id

            override fun areContentsTheSame(oldItem: VideoComment, newItem: VideoComment): Boolean =
                oldItem == newItem

        }
    }
}