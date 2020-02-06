package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.activities.DetailActivity
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel
import com.cheyrouse.gael.realestatemanagerkt.models.GeocodeInfo
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.RealEstateStream
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_maps.*

/**
 * A simple [Fragment] subclass.
 */
class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var propertiesList: List<Property>
    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var disposable: Disposable
    private lateinit var geoCodeList: MutableList<GeocodeInfo>
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geoLocation: GeocodeInfo
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var marker: Marker
    private var initPosition: Boolean = false

    companion object {
        fun newInstance(): MapsFragment {
            return MapsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocationClient()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val map =
            childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment?
        map?.getMapAsync(this)
        initViewModel()
    }

    private fun getLocationClient() {
        fusedLocationClient =
            activity?.let { LocationServices.getFusedLocationProviderClient(it) }!!
    }

    private fun initViewModel() {
        propertyViewModel = ViewModelProviders.of(
            this,
            context?.let { DataInjection.Injection.provideViewModelFactory(it) }
        ).get(PropertyViewModel::class.java)
        propertyViewModel.getAllProperty()
            .observe(this, Observer<List<Property>> { createDefaultList(it!!) })
    }

    private fun createDefaultList(properties: List<Property>) {
        geoCodeList = arrayListOf()
        propertiesList = properties
        for (p: Property in properties) {
            if (p.address?.lat != 0.0 && p.address?.lng != 0.0) {
                addMarker(p, p.address?.lat!!, p.address?.lng!!)
            } else {
                executeRequestToGetAddresses(p)
            }
        }
    }

    private fun executeRequestToGetAddresses(property: Property) {
        val addressStr =
            property.address?.address + "+" + property.address?.city + property.address?.postalCode /*+ "+" + property.address?.additionalAddress +"+"+ property.address?.apartmentNumber +" "+ property.address?.sector*/
        Log.e("test address", addressStr)
        val realEstateStream = RealEstateStream()
        disposable = realEstateStream.streamFetchGeocodeInfo(
            addressStr,
            "AIzaSyCCjy4vvQ8_U_3qELz9v_W2hKApVg3Nbws"
        )
            .subscribeWith(object : DisposableObserver<GeocodeInfo?>() {
                override fun onNext(t: GeocodeInfo) {
                    geoLocation = t
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                    geoCodeList.add(geoLocation)
                    val lat: Double = geoLocation.results?.get(0)?.geometry?.location?.lat!!
                    val lng: Double = geoLocation.results?.get(0)?.geometry?.location?.lng!!
                    addMarker(property, lat, lng)
                    storeLocation(property, lat, lng)
                }
            })
    }

    private fun storeLocation(property: Property, lat: Double, lng: Double) {
        property.address?.lat = lat
        property.address?.lng = lng
        propertyViewModel.updateProperty(property)
    }

    private fun addMarker(property: Property, lat: Double, lng: Double) {
        //        val lat: Double? = geo.results?.get(0)?.geometry?.location?.lat
//        val lng: Double? = geo.results?.get(0)?.geometry?.location?.lng
        val latLon = LatLng(lat, lng)
        /*marker =  */map.addMarker(
            MarkerOptions().position(latLon).title(property.type)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )
//        marker.showInfoWindow()
        map.moveCamera(CameraUpdateFactory.newLatLng(latLon))
//        map.addMarker(MarkerOptions().position(myPlace).title("My Favorite City"))
        gotoMyLocation()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
        }
    }

    private fun gotoMyLocation() {
        requestNewLocationData()
    }

    private fun getLocation(location: Location) {
        if(initPosition)marker.remove()
        val latLon: LatLng
        val latitude: Double?
        val longitude: Double?
        latitude = location.latitude
        longitude = location.longitude
        latLon = latitude.let { longitude.let { it1 -> LatLng(it, it1) } }
        marker = map.addMarker(latLon.let {
            MarkerOptions().position(it).title("My Favorite City").title("Me")
        })
        map.setOnMarkerClickListener(this)
        if(!initPosition){
            map.moveCamera(CameraUpdateFactory.newLatLng(latLon))
            map.animateCamera(CameraUpdateFactory.zoomTo(15.0F), 2000, null)
            map.uiSettings.isZoomControlsEnabled = true
            map.setOnMarkerClickListener(this)
            initPosition = true
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        progressBar.visibility = View.VISIBLE

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    getLocation(location)
                    progressBar.visibility = View.GONE
                }
            }
        }
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = (30 * 1000)
        mLocationRequest.fastestInterval = 1000
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        p0?.let { getPropertyId(it) }
        return false
    }

    private fun getPropertyId(marker: Marker) {
        for (p: Property in propertiesList) {
            val latLng: LatLng =
                p.address?.lat?.let { p.address!!.lng?.let { it1 -> LatLng(it, it1) } }!!
            if (marker.position == latLng) {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.PROPERTY, p.id)
                startActivity(intent)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mLocationCallback)
    }
}