package com.cheyrouse.gael.realestatemanagerkt.view

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import kotlinx.android.synthetic.main.estate_list_item.view.*

class DetailPictureViewHolder (inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.detail_picture_item, parent, false)) {
    private val mNameView = itemView.estate_type
    private val mImageView = itemView.estate_picture
    private val glide: RequestManager = Glide.with(itemView)


    fun bind(picture: Picture) {
        mNameView?.text = picture.pictureName.toString()
        mNameView?.setBackgroundColor(80808080)
        mImageView?.let {
            glide.load(Uri.parse(picture.picturePath)).apply(RequestOptions().centerCrop()).into(
                it
            )
        }
    }
}