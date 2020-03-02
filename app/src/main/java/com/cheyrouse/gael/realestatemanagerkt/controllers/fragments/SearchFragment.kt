package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.activities.MainActivity
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel
import com.cheyrouse.gael.realestatemanagerkt.database.dao.PropertyDao
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.listOfSearchTypes
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.listOfTypes
import com.cheyrouse.gael.realestatemanagerkt.utils.Utils
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import kotlin.text.toDoubleOrNull as toDoubleOrNull1


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var mListener: OnSearchFragmentListener? = null
    private lateinit var propertyViewModel: PropertyViewModel
    private var typeOfProperty: String = "ALL"
    private var roomMin: Int = 0
    private var roomMax: Int = 40
    private var surfaceMin: Int = 0
    private var surfaceMax: Int = 1000
    private var priceMin: Double = 0.0
    private var priceMax: Double = 0.0
    private var bedRoomsMin: Int = 0
    private var bedRoomsMax: Int = 20
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    private var entryDate: String = ""
    private var soldDate: String = ""
    private var city: String = ""
    private var postalCode: String = ""
    private var country: String = ""
    private var sold: Boolean = false
    private var available: Boolean = false
    private var airport: Boolean = false
    private var school: Boolean = false
    private var subway: Boolean = false
    private var shops: Boolean = false
    private var trainStation: Boolean = false
    private var park: Boolean = false
    private var numberOfBath: Int = 0
    private var realtorName: String = ""

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSearchFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        configureSeekBarSurface()
        configureTypes()
        configureSeekBarNumbRooms()
        configureEditTown()
        configureSeekBarPrice()
        configurePostalCode()
        configureCountry()
        configureCheckBox()
        configureSeekBarBeds()
        configureDateEntry()
        configureDateSold()
        configureRealtorName()
        configureNbrBathrooms()
        configureButtonSearch()
    }

    private fun initViewModel() {
        propertyViewModel = ViewModelProviders.of(
            this,
            context?.let { DataInjection.Injection.provideViewModelFactory(it) }
        ).get(PropertyViewModel::class.java)
    }

    private fun configureSeekBarSurface() {
        seekBar_surface.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            tvSurfaceResultMin.text = minValue.toString()
            tvSurfaceResultMax.text = maxValue.toString()
        }

    }

    private fun configureNbrBathrooms() {
        edit_bath.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val nbrBathStr: String = edit_bath.text.toString()
                if (nbrBathStr.isNotEmpty()) {
                    numberOfBath = nbrBathStr.toInt()
                }
            }
        })
    }

    private fun configureRealtorName() {
        edt_realtor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                realtorName = edt_realtor.text.toString()
            }
        })
    }

    private fun configureDateSold() {
        picker_sold.setOnClickListener {
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        /* Display Selected date in TextView */
                        picker_sold.text = Utils.getStringDate(year, dayOfMonth, monthOfYear)
                        soldDate = picker_sold.text.toString()
                    },
                    year,
                    month,
                    day
                )
            }
            dpd?.show()
        }
    }

    private fun configureDateEntry() {
        picker_entry.text = Utils.initPickers()
        picker_entry.setOnClickListener {
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in TextView
                        picker_entry.text = Utils.getStringDate(year, dayOfMonth, monthOfYear)
                        entryDate = picker_entry.text.toString()
                    },
                    year,
                    month,
                    day
                )
            }
            dpd?.show()
        }
    }

    private fun configureSeekBarBeds() {
        seekBar_nbr_bed.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            bedroom_min.text = minValue.toString()
            bedroom_max.text = maxValue.toString()
        }
    }

    private fun configureCheckBox() {
        check_airport.setOnClickListener { airport = true }
        check_school.setOnClickListener { school = true }
        check_shops.setOnClickListener { shops = true }
        check_subway.setOnClickListener { subway = true }
        check_train_station.setOnClickListener { trainStation = true }
        check_park.setOnClickListener { park = true }
        check_available.setOnClickListener {
            available = check_available.isChecked
            check_sold.isChecked = false
        }
        check_sold.setOnClickListener {
            check_available.isChecked = false
            sold = check_sold.isChecked
        }
    }

    private fun configureCountry() {
        edt_country.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                country = edt_country.text.toString()
            }
        })
    }

    private fun configurePostalCode() {
        edit_postl_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                postalCode = edit_postl_code.text.toString()
            }
        })
    }

    private fun configureSeekBarPrice() {
        seekBar_price.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            price_min.text = minValue.toString()
            price_max.text = maxValue.toString()
        }
    }

    private fun configureEditTown() {
        edit_town.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                city = edit_town.text.toString()
            }
        })
    }

    private fun configureSeekBarNumbRooms() {
        seekBar_nbr_rooms.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            room_min.text = minValue.toString()
            room_max.text = maxValue.toString()
        }
    }

    private fun configureTypes() {
        type_spinner!!.onItemSelectedListener = this
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, listOfSearchTypes) }
        // Set layout to use when the list of choices appear
        aa?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        type_spinner!!.adapter = aa
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        typeOfProperty = listOfSearchTypes[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
//        typeOfProperty = listOfItems[0]
    }

    private fun configureButtonSearch() {
        button_search.setOnClickListener {
            getEntryValues()
        }
    }

    private fun getEntryValues() {
        surfaceMin = Integer.decode(tvSurfaceResultMin.text.toString())
        surfaceMax = Integer.decode(tvSurfaceResultMax.text.toString())
        roomMin = Integer.decode(room_min.text.toString())
        roomMax = Integer.decode(room_max.text.toString())
        priceMin = price_min.text.toString().toDouble()
        priceMax = price_max.text.toString().toDouble()
        bedRoomsMin = Integer.decode(bedroom_min.text.toString())
        bedRoomsMax = Integer.decode(bedroom_max.text.toString())

        makeSearchQuery()
    }

    // --- Make Search -- //
    private fun makeSearchQuery() {
        var query = "SELECT * FROM Property"
//        var query = "UPDATE * FROM Property SET visibility = false "
        val args = arrayListOf<Any>()
        var containsAnd = false
        var containsEqual = 0

        //Type
        if (typeOfProperty.isNotEmpty() && typeOfProperty != "ALL") {
            query += " WHERE type = :$typeOfProperty"
            args.add(typeOfProperty)
            containsAnd = true
            containsEqual += 1
        }
        //LivingSpace min
        if (surfaceMin != 0){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "livingSpace >= :$surfaceMin"
            args.add(surfaceMin)
        }
        //LivingSpace max
        if (surfaceMax != 0){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "livingSpace <= :$surfaceMax"
            args.add(surfaceMax)
        }
        //Number of min Rooms
        if (roomMin != 0){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query +=  "rooms >= :$roomMin"
            args.add(roomMin)
        }
////        //Number of max Rooms
        if (roomMax != 0){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query +="rooms <= :$roomMax"
            args.add(roomMax)
        }
        //City
        if (city != ""){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "city = :$city"
            args.add(city)
        }
//        //Postal code
        if (postalCode != ""){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "postalCode = :$postalCode"
            args.add(postalCode)
        }
//        //Country
        if (country != ""){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "country = :$country"
            args.add(country)
        }
        //Points of interest
        if ((shops)){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "shops = :$shops"
            args.add(shops)
        }
        if ((airport)){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query +=  "airport = :$airport"
            args.add(airport)
        }
        if ((park)){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "park = :$park"
            args.add(park)
        }
        if ((subway)){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "subway = :$subway"
            args.add(subway)
        }
        if ((sold)){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "status = :$sold"
            args.add(sold)
        }
        if ((school)){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query +=  "school = :$school"
            args.add(school)
        }
        if ((trainStation)){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query +=  "trainStation = :$trainStation"
            args.add(trainStation)
        }
        //Status
        if ((sold)){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query +=  "status = :false"
            args.add("false")
        }
        if ((available)){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query +=  "status = :$available"
            args.add(available)
        }
        //Price min
        if (priceMin != 0.0){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "price >= ${priceMin.toInt()}"
            args.add(priceMin.toInt())
        }
        //Price max
        if (priceMax != 0.0){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "price <= :${priceMax.toInt()}"
            args.add(priceMax.toInt())
        }
        //Number of min Bedrooms
//        if (bedRoomsMin != 0){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += "Property.numOfBed >= :$bedRoomsMin"
//            args.add(bedRoomsMin)
//        }
        //Number of max Bedrooms
        if (bedRoomsMax != 0){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "numOfBed <= :$bedRoomsMax"
            args.add(bedRoomsMax)
        }
//        //Entry date
        if (entryDate.isNotEmpty()){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "dateOfEntry >= :$entryDate"
            args.add(entryDate)
        }
//        //Sold date
        if (soldDate.isNotEmpty()){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "dateOfSale <= :$soldDate"
            args.add(soldDate)
        }
//        //Realtor name
        if (realtorName != ""){
            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
            query += "realtor = :$realtorName"
            args.add(realtorName)
        }
//        //Number of Bathrooms
        if (numberOfBath != 0){
            query += if (containsAnd) " AND " else " WHERE "
            query += "numOfBath = :$numberOfBath"
            args.add(numberOfBath)
        }
//        //LivingSpace min
//        if (surfaceMin != 0){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2)"livingSpace >= :$surfaceMin"  else "Property.livingSpace >= :$surfaceMin"+ "=?"; containsEqual += 1
//            args.add(surfaceMin)
//        }
//        //LivingSpace max
//        if (surfaceMax != 0){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "livingSpace <= :$surfaceMax" else "livingSpace <= :$surfaceMax"+ "=?" ; containsEqual += 1
//            args.add(surfaceMax)
//        }
//        //Number of min Rooms
//        if (roomMin != 0){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "rooms >= :$roomMin" else "rooms >= :$roomMin"+ "=?"; containsEqual += 1
//            args.add(roomMin)
//        }
//        //Number of max Rooms
//        if (roomMax != 0){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "rooms <= :$roomMax" else "rooms <= :$roomMax"+ "=?"; containsEqual += 1
//            args.add(roomMax)
//        }
//        //City
//        if (city != ""){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "city = :$city" else "city = :$city" + "=?" ; containsEqual += 1
//            args.add(city)
//        }
////        //Postal code
//        if (postalCode != ""){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "postalCode = :$postalCode" else "postalCode = :$postalCode" + "=?" ; containsEqual += 1
//            args.add(postalCode)
//        }
////        //Country
//        if (country != ""){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "country = :$country" else "country = :$country" + "=?" ; containsEqual += 1
//            args.add(country)
//        }
//        //Points of interest
//        if ((shops)){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "shops = :$shops" else "shops = :$shops"+ "=?"; containsEqual += 1
//            args.add(shops)
//        }
//        if ((airport)){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2)  "airport = :$airport" else "airport = :$airport" + "=?"; containsEqual += 1
//            args.add(airport)
//        }
//        if ((park)){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "park = :$park" else "park = :$park" + "=?"; containsEqual += 1
//            args.add(park)
//        }
//        if ((subway)){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "subway = :$subway" else "subway = :$subway"+ "=?"; containsEqual += 1
//            args.add(subway)
//        }
//        if ((sold)){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "status = :$sold"  else "status = :$sold"+ "=?"; containsEqual += 1
//            args.add(sold)
//        }
//        if ((school)){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "school = :$school"  else "school = :$school"+ "=?"; containsEqual += 1
//            args.add(school)
//        }
//        if ((trainStation)){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "trainStation = :$trainStation"  else "trainStation = :$trainStation"+ "=?"; containsEqual += 1
//            args.add(trainStation)
//        }
//        //Status
//        if ((sold)){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "status = :$sold" else "status = :$sold" + "=?"; containsEqual += 1
//            args.add(sold)
//        }
//        if ((available)){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "status = :$available" else "status = :$available" + " =?" ; containsEqual += 1
//            args.add(available)
//        }
//        //Price min
//        if (priceMin != 0.0){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "Property.price >= $priceMin" else "Property.price >= $priceMin" + " =?" ; containsEqual += 1
//            args.add(priceMin)
//        }
//        //Price max
//        if (priceMax != 0.0){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "Property.price <= :${priceMax.toInt()}" else "Property.price <= :${priceMax.toInt()}" + " =?" ; containsEqual += 1
////            args.add(priceMax)
//        }
//        //Number of min Bedrooms
//        if (bedRoomsMin != 0){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "Property.numOfBed >= :$bedRoomsMin" else "Property.numOfBed >= :$bedRoomsMin" + " =?" ; containsEqual += 1
//            args.add(bedRoomsMin)
//        }
////        //Number of max Bedrooms
//        if (bedRoomsMax != 0){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "Property.numOfBed <= :$bedRoomsMax" else "Property.numOfBed <= :$bedRoomsMax" + " =?" ; containsEqual += 1
//            args.add(bedRoomsMax)
//        }
////        //Entry date
//        if (entryDate.isNotEmpty()){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += "Property.dateOfEntry >= ?"
//            args.add(entryDate)
//        }
////        //Sold date
//        if (soldDate.isNotEmpty()){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += "Property.dateOfSale <= ?"
//            args.add(soldDate)
//        }
////        //Realtor name
//        if (realtorName != ""){
//            query += if (containsAnd) " AND " else " WHERE "; containsAnd = true
//            query += if (containsEqual<2) "Property.realtor = :$realtorName" else "Property.realtor = :$realtorName" + "=?"; containsEqual += 1
//            args.add(realtorName)
//        }
////        //Number of Bathrooms
//        if (numberOfBath != 0){
//            query += if (containsAnd) " AND " else " WHERE "
//            query += if (containsEqual<2) "Property.numOfBath = :$numberOfBath" else "Property.numOfBath = :$numberOfBath"; containsEqual += 1
//            args.add(numberOfBath)
//        }

        propertyViewModel.getPropertyByArgs(query, args).observe(this, Observer<List<Property>> {
            if (it!!.isNotEmpty()) {
                getResult(it)
            } else {
                Toast.makeText(
                    this.context!!,
                    resources.getString(R.string.result_is_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun getResult(it: List<Property>) {
        mListener?.onSearchInteraction(it)
        Log.e(resources.getString(R.string.test_query), it.toString())
    }

    interface OnSearchFragmentListener {
        fun onSearchInteraction(it: List<Property>)
    }
}
