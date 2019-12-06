package com.cheyrouse.gael.realestatemanagerkt.view

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.models.Address
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import kotlinx.android.synthetic.main.estate_list_item.view.*

class EstateListViewHolder (inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.estate_list_item, parent, false)) {
    private val mTypeView = itemView.estate_type
    private val mTownView = itemView.estate_town
    private var mPriceView = itemView.estate_price
    private var mImageView = itemView.estate_picture
    val glide: RequestManager = Glide.with(itemView)


    fun bind(property: Property, clickListener: (Property) -> Unit) {
        var address: Address
        mTypeView?.text = property.type.toString()
//        mTownView?.text = address.type
        mPriceView?.text = "$" + property.price.toString()
//
        mImageView?.let {
            glide.load(Uri.parse(property.pictures?.get(0)?.picturePath)).apply(RequestOptions().centerCrop()).into(
                it
            )
        }
        itemView.setOnClickListener { clickListener(property)}
    }

}