package com.simplyfire.komoottesttask.feature.photolist

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.simplyfire.komoottesttask.R
import com.simplyfire.komoottesttask.utils.inflateAdapterView

private val diffUtilCallback =  object: DiffUtil.ItemCallback<DisplayablePhoto>() {
    override fun areItemsTheSame(oldItem: DisplayablePhoto, newItem: DisplayablePhoto): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DisplayablePhoto, newItem: DisplayablePhoto): Boolean {
        return oldItem == newItem
    }

}

class PhotoAdapter : ListAdapter<DisplayablePhoto, PhotoAdapter.PhotoViewHolder>(diffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(parent.inflateAdapterView(R.layout.photo_item_view))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PhotoViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private val imageView = v.findViewById<ImageView>(R.id.imageView)

        fun bind(photo: DisplayablePhoto) {
            Glide.with(v).load(photo.url).into(imageView)
        }
    }
}