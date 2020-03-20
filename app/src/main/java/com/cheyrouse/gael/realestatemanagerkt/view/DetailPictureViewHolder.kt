package com.cheyrouse.gael.realestatemanagerkt.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import kotlinx.android.synthetic.main.detail_picture_item.view.*


class DetailPictureViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.detail_picture_item, parent, false)) {
    private val mNameView = itemView.description_pic_text
    private val mImageView = itemView.picture_detail
    private val glide: RequestManager = Glide.with(itemView)

    fun bind(picture: Picture, clickListener: (Int) -> Unit) {
        mNameView?.text = picture.pictureName.toString()
        mImageView?.let {
            glide.load(Uri.parse(picture.picturePath)).apply(RequestOptions().centerCrop()).into(
                it
            )
        }
        itemView.setOnClickListener {
            val position:Int = adapterPosition
           clickListener(position)
        }
    }
}