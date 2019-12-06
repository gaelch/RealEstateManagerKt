package com.cheyrouse.gael.realestatemanagerkt.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.cheyrouse.gael.realestatemanagerkt.models.Property

class EstateListAdapter (private val list: List<Property>, private val clickListener: (Property) -> Unit)
    : RecyclerView.Adapter<EstateListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EstateListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: EstateListViewHolder, position: Int) {
        val property: Property = list[position]
        holder.bind(property, clickListener)

    }

    override fun getItemCount(): Int = list.size

}