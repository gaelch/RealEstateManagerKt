package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.view.DetailPictureAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_detail_estate.*


class DetailEstateFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var property: Property
    private var propertyId: Long = 0
    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var adapter: DetailPictureAdapter
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mListener: OnFragmentDetailListener? = null

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
        detail_lite_map.onCreate(savedInstanceState)
        detail_lite_map.getMapAsync(this)
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
        text_location_num_street.text = property.address?.address
        text_location_additional.text = property.address?.additionalAddress
        text_location_town.text = property.address?.city
        if (property.address?.apartmentNumber != 0) {
            text_location_num_type.text =
                resources.getString(R.string.nbr_of_apart, property.address?.apartmentNumber.toString())
        } else {
            text_location_num_type.visibility = View.GONE
        }
        text_location_country.text = property.address?.country
        text_location_cp.text = property.address?.postalCode
        configureRecyclerView()
        addMarkers()

    }

    @SuppressLint("WrongConstant")
    private fun configureRecyclerView() {
        detail_picture_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
            adapter = DetailPictureAdapter(property.pictures!!){position: Int -> onItemClicked(position)}
        }

    }

    private fun initViewModelFactory() {
        this.propertyViewModel = ViewModelProviders.of(this,
                activity?.applicationContext?.let {
                    DataInjection.Injection.provideViewModelFactory(
                        it) }).get(PropertyViewModel::class.java)
    }


    private fun updateAdapterWithDefaultValue(properties: List<Property>) {
        if (properties.isNotEmpty()) {
            property = properties[0]
            configureRecyclerView()
            initVars(property)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
        }
    }

    private fun addMarkers() {
        fusedLocationClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }!!
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        val myPlace =
            property.address?.lat?.let { property.address!!.lng?.let { it1 -> LatLng(it, it1) } }
        map.addMarker(myPlace?.let { MarkerOptions().position(it).title(property.type) })
        map.moveCamera(CameraUpdateFactory.newLatLng(myPlace))
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        mListener?.onDetailInteraction(property)
        return true
    }

    interface OnFragmentDetailListener {
        fun onDetailInteraction(property: Property)
    }

    private fun onItemClicked(position: Int) {
        Log.d("test", position.toString())
    }

}
