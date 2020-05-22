package com.educatorapp.utils.bindingAdapters

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.educatorapp.model.Educator
import com.educatorapp.model.Subject
import com.educatorapp.model.Video
import com.educatorapp.ui.adapter.EducatorListAdapter
import com.educatorapp.ui.adapter.EducatorVideoListAdapter
import com.educatorapp.ui.adapter.SubjectListAdapter
import com.educatorapp.utils.extensions.loadUrl


/** Load image from the network or cache with placeholder and error images */
@BindingAdapter("imageLoad")
fun loadImage(imageView: AppCompatImageView, url: String?) {
    url?.let {
        imageView.loadUrl(url)
    }
}

@BindingAdapter("subjectListData")
fun bindSubjectListData(recyclerView: RecyclerView, data: List<Subject>) {
    val adapter = recyclerView.adapter as SubjectListAdapter
    adapter.submitList(data)
}

@BindingAdapter("educatorListData")
fun bindEducatorListData(recyclerView: RecyclerView, data: List<Educator>) {
    val adapter = recyclerView.adapter as EducatorListAdapter
    adapter.submitList(data)
}

@BindingAdapter("educatorVideoListData")
fun bindEducatorVideoListData(recyclerView: RecyclerView, data: List<Video>) {
    val adapter = recyclerView.adapter as EducatorVideoListAdapter
    adapter.submitList(data)
}

@BindingAdapter("setRatingText")
fun setRatingText(view: TextView, rating: Float) {
    view.text = "$rating/5.0"
}