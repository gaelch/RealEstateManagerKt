package com.cheyrouse.gael.realestatemanagerkt.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.cheyrouse.gael.realestatemanagerkt.models.Property

class DetailPictureAdapter (private val list: List<Picture>)
    : RecyclerView.Adapter<DetailPictureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailPictureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DetailPictureViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DetailPictureViewHolder, position: Int) {
        val picture: Picture = list[position]
        holder.bind(picture)
    }

    override fun getItemCount(): Int = list.size
}