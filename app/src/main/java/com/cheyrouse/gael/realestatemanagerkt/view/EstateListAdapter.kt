package com.cheyrouse.gael.realestatemanagerkt.view

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.cheyrouse.gael.realestatemanagerkt.models.Property


class EstateListAdapter(private val list: List<Property>, private val clickListener: (Property) -> Unit)
    : RecyclerView.Adapter<EstateListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EstateListViewHolder(inflater, parent)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: EstateListViewHolder, position: Int) {
        val property: Property = list[position]
        holder.bind(property, clickListener, position)
//        notifyAdapter()
    }

    override fun getItemCount(): Int = list.size

    fun notifyAdapter(){
        notifyDataSetChanged()
    }

}