package com.simplyfire.komoottesttask.feature.photolist

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.simplyfire.komoottesttask.core.utils.inflateAdapterView


private val diffUtilCallback =  object: DiffUtil.ItemCallback<DisplayablePhoto>() {
    override fun areItemsTheSame(oldItem: DisplayablePhoto, newItem: DisplayablePhoto): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DisplayablePhoto, newItem: DisplayablePhoto): Boolean {
        return oldItem == newItem
    }

}

class PhotoAdapter : ListAdapter<DisplayablePhoto, PhotoAdapter.PhotoViewHolder>(diffUtilCallback) {
    private val glideOptions = RequestOptions()
        .fitCenter()
        .placeholder(com.simplyfire.komoottesttask.R.drawable.loading)
        .error(com.simplyfire.komoottesttask.R.drawable.img_error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .dontAnimate()
        .dontTransform()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(parent.inflateAdapterView(com.simplyfire.komoottesttask.R.layout.photo_item_view))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhotoViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private val imageView = v.findViewById<ImageView>(com.simplyfire.komoottesttask.R.id.imageView)

        fun bind(photo: DisplayablePhoto) {
            Glide.with(v).load(photo.url).apply(glideOptions).into(imageView)
        }
    }
}