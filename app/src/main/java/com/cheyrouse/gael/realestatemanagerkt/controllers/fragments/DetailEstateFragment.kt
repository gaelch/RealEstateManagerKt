package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper

import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel
import com.cheyrouse.gael.realestatemanagerkt.models.Address
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.view.DetailPictureAdapter
import kotlinx.android.synthetic.main.fragment_detail_estate.*


class DetailEstateFragment : Fragment() {

    private lateinit var property: Property
    private var propertyId: Long = 0
    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var adapter: DetailPictureAdapter

    companion object {
        private const val ARG_PARAM = "property"
        fun newInstance(propertyId: Long): DetailEstateFragment {
            val fragment = DetailEstateFragment()
            val args = Bundle()
            args.putLong(ARG_PARAM, propertyId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_detail_estate, container, false)


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModelFactory()
        if (arguments != null) {
            propertyId = arguments?.getLong(ARG_PARAM)!!
            if (propertyId != 0L) {
                propertyViewModel.getProperty(propertyId).observe(this, Observer { property ->
                    property?.let { initVars(it) }
                })
            } else {
                propertyViewModel.getAllProperty()
                    .observe(this, Observer<List<Property>> { updateAdapterWithDefaultValue(it!!) })
            }
        }
    }

    private fun initVars(property: Property) {
        this.property = property
        tv_description_text.text = property.description
        text_surface.text = property.livingSpace.toString()
        text_nbr_of_rooms.text = property.rooms.toString()
        text_nbr_bathrooms.text = property.numOfBath.toString()
        text_nbr_bedrooms.text = property.numOfBed.toString()
//        text_location_num_street.text = property.address.
        text_location_town.text = property.address?.address
        configureRecyclerView()
    }

    @SuppressLint("WrongConstant")
    private fun configureRecyclerView() {
        detail_picture_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
            adapter = DetailPictureAdapter(property.pictures!!)
        }
    }

    private fun initViewModelFactory() {
        this.propertyViewModel = ViewModelProviders.of(this,
            activity?.applicationContext?.let { DataInjection.Injection.provideViewModelFactory(it) })
            .get(PropertyViewModel::class.java)
    }


    private fun updateAdapterWithDefaultValue(properties: List<Property>) {
        property = properties[0]
        configureRecyclerView()
    }


}
