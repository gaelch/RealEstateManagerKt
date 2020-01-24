package com.cheyrouse.gael.realestatemanagerkt.view

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import kotlinx.android.synthetic.main.detail_picture_item.view.*
import kotlinx.android.synthetic.main.estate_list_item.view.*
import java.io.File

class DetailPictureViewHolder (inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.detail_picture_item, parent, false)) {
    private val mNameView = itemView.description_pic_text
    private val mImageView = itemView.picture_detail
    private val glide: RequestManager = Glide.with(itemView)


    fun bind(picture: Picture) {
        mNameView?.text = picture.pictureName.toString()
        mNameView?.setBackgroundColor(80808080)
        Log.e("test detail picture vh", (picture.picturePath))
        mImageView?.let {
            glide.load(Uri.parse(picture.picturePath)).apply(RequestOptions().centerCrop()).into(
                it
            )
        }
    }
}