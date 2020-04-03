package com.cheyrouse.gael.realestatemanagerkt.view

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.RealEstateManagerApplication
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import kotlinx.android.synthetic.main.estate_list_item.view.*
import java.text.NumberFormat
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EstateListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.estate_list_item, parent, false)) {

    private val mTypeView = itemView.estate_type
    private val mTownView = itemView.estate_town
    private var mPriceView = itemView.estate_price
    private var mImageView = itemView.estate_picture
    private var mSoldTextView = itemView.estate_sold
    private var constraint = itemView.constraint_item
    private val glide: RequestManager = Glide.with(itemView)
    private var index: Int = 0
    private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun bind(property: Property, clickListener: (Property) -> Unit, position: Int) {
        currencyFormat.maximumFractionDigits = 0
        index = RealEstateManagerApplication.getLastItemClicked()
        mTypeView?.text = property.type
        mTownView?.text = property.address?.city
        mPriceView?.text = currencyFormat.format(property.price)
        //get image from storage
        if (property.pictures != null && property.pictures!!.isNotEmpty()) {
            Log.e("test estate list vh", (property.pictures?.get(0)?.picturePath))
            mImageView?.let {
                glide.load(Uri.parse(property.pictures?.get(0)?.picturePath))
                    .apply(RequestOptions().centerCrop()).into(
                        it
                    )
            }
        }
        if (property.status == false) {
            mSoldTextView.visibility = View.VISIBLE
        }
        itemView.setOnClickListener {
            if(index!=-1) RealEstateManagerApplication.setLastItemClicked(position)
            clickListener(property)
        }
        if(index!=-1)setItemColor(position)
    }

    //To set item and view color
    private fun setItemColor(position: Int) {
        if (index == position) {
            constraint.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.colorRed
                )
            )
            mPriceView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
        } else {
            constraint.setBackgroundColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.white
                )
            )
            mPriceView.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.colorPrimaryDark
                )
            )
        }
    }

}