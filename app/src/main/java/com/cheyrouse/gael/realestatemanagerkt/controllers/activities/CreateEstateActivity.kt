package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import com.cheyrouse.gael.realestatemanagerkt.R
import kotlinx.android.synthetic.main.activity_create_estate.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.view.DetailPictureAdapter
import kotlinx.android.synthetic.main.picture_title_dialogue.view.*
import kotlin.collections.ArrayList

class CreateEstateActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {

    private var listOfItems =
        arrayOf("Manor", "House", "Castle", "Flat", "Loft", "Apartment", "Duplex")
    private lateinit var typeOfProperty: String
    private var surface: Int = 0
    private var numbreOfRooms: Int = 0
    private lateinit var address: String
    private lateinit var description: String
    private var price: Int = 0
    private lateinit var realtorName: String
    private lateinit var entryDate: String
    private lateinit var soldDate: String
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    private var pointsOfInterestlist = arrayListOf<String>()
    private var sold: Boolean = false
    private var pictures = arrayListOf<Picture>()
    private lateinit var adapter: DetailPictureAdapter
    private lateinit var property: Property


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_estate)
        configureToolbar()
        configureRecyclerView()
        configureSpinner()
        configureSurface()
        configureNumRooms()
        configureAddress()
        configureDescription()
        configurePrice()
        configureRealtorName()
        configureDatePickerEntry()
        configureDatePickerSold()
        configureButtons()
        getTheBundle()
    }

    private fun getTheBundle() {
        if(intent.getParcelableExtra<Property>(DetailActivity.PROPERTY) != null){
            property = intent.getParcelableExtra(DetailActivity.PROPERTY)!!
            initVars()
        }
    }

    private fun initVars() {
        typeOfProperty = property.type!!
        surface = property.livingSpace!!
        numbreOfRooms = property.rooms!!
        address = property.address!!
        description = property.description!!
        price = property.price!!.toInt()
        realtorName =  property.realtor!!
        entryDate = property.dateOfEntry!!
        soldDate = property.dateOfSale!!
        pointsOfInterestlist = property.pointsOfInterest as ArrayList<String>
        sold = property.status!!
        pictures = property.pictures as ArrayList<Picture>
        initWidgets()
    }

    private fun initWidgets() {
        val spinnerPosition: Int = listOfItems.indexOf(typeOfProperty)
        type_spinner.setSelection(spinnerPosition)
        edit_surface.setText(surface)
        edit_nbr_rooms.setText(numbreOfRooms)
        edit_address.setText(address)
        edit_realtor.setText(realtorName)
        edit_description.setText(description)
        picker_price.setText(price)
        picker_sold_date.text = soldDate
        picker_entry_date.text = entryDate
        for (i in pointsOfInterestlist){
            if(i.equals("airport")){
                checkbox_airport.isChecked
            }
            if(i.equals("school")){
                checkbox_airport.isChecked
            }
            if(i.equals("shops")){
                checkbox_airport.isChecked
            }
            if(i.equals("subway")){
                checkbox_airport.isChecked
            }
            if(i.equals("station")){
                checkbox_airport.isChecked
            }
            if(i.equals("parc")){
                checkbox_airport.isChecked
            }
        }
        configureRecyclerView()
    }

    private fun configureButtons() {
        button_gallery.setOnClickListener {
            checkPermissionToReadStorage()
        }
        button_take_picture.setOnClickListener(View.OnClickListener {

        })
    }


    @SuppressLint("WrongConstant")
    private fun configureRecyclerView() {
        create_picture_recycler_view.apply {
            layoutManager =
                LinearLayoutManager(applicationContext, OrientationHelper.HORIZONTAL, false)
            this.adapter = pictures.let { DetailPictureAdapter(pictures) }
        }
    }

    private fun configureDatePickerSold() {
        picker_sold_date.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    picker_sold_date.text = "" + dayOfMonth + " " + month + ", " + year
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        soldDate = picker_entry_date.text.toString()
    }

    private fun configureDatePickerEntry() {
        picker_entry_date.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    picker_entry_date.text = "" + dayOfMonth + " " + month + ", " + year
                },
                year,
                month,
                day
            )
            dpd.show()
        }
        entryDate = picker_entry_date.text.toString()
    }

    private fun configureRealtorName() {
        edit_realtor.addTextChangedListener(object : TextWatcher {

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
                realtorName = edit_realtor.text.toString()
            }
        })

    }

    private fun configurePrice() {
        picker_price.addTextChangedListener(object : TextWatcher {

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
                val priceStr: String = picker_price.text.toString()
                price = priceStr.toInt()
            }
        })

    }

    private fun configureDescription() {
        edit_description.addTextChangedListener(object : TextWatcher {

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
                description = edit_description.text.toString()
            }
        })
    }

    private fun configureAddress() {
        edit_address.addTextChangedListener(object : TextWatcher {

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
                address = edit_address.text.toString()
            }
        })
    }

    private fun configureNumRooms() {
        edit_nbr_rooms.addTextChangedListener(object : TextWatcher {

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
                val nbrRoomsStr: String = edit_nbr_rooms.text.toString()
                numbreOfRooms = nbrRoomsStr.toInt()
            }
        })
    }

    private fun configureSurface() {
        edit_surface.addTextChangedListener(object : TextWatcher {

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
                val surfaceStr: String = edit_surface.text.toString()
                surface = surfaceStr.toInt()
            }
        })
    }


    private fun configureToolbar() {
        setSupportActionBar(toolbar)
//        toolbar.title = resources.getString(R.string.create_title)
    }

    private fun configureSpinner() {
        type_spinner!!.onItemSelectedListener = this
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOfItems)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        type_spinner!!.adapter = aa
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
       //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        typeOfProperty = listOfItems[position]
    }

    override fun onClick(view: View?) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                // Point of interest
                R.id.checkbox_airport -> {
                    if (checked) {
                        pointsOfInterestlist.add("airport")

                    } else {
                        pointsOfInterestlist.remove("airport")
                    }
                }
                R.id.checkbox_school -> {
                    if (checked) {
                        pointsOfInterestlist.add("school")
                    } else {
                        pointsOfInterestlist.remove("airport")
                    }
                }
                R.id.checkbox_shops -> {
                    if (checked) {
                        pointsOfInterestlist.add("shops")
                    } else {
                        pointsOfInterestlist.remove("shops")
                    }
                }
                R.id.checkbox_subway -> {
                    if (checked) {
                        pointsOfInterestlist.add("subway")
                    } else {
                        pointsOfInterestlist.remove("airport")
                    }
                }
                R.id.checkbox_train_station -> {
                    if (checked) {
                        pointsOfInterestlist.add("station")
                    } else {
                        pointsOfInterestlist.remove("station")
                    }
                }
                R.id.checkbox_parc -> {
                    if (checked) {
                        pointsOfInterestlist.add("parc")
                    } else {
                        pointsOfInterestlist.remove("parc")
                    }
                }
                // Status
                R.id.checkbox_available -> {
                    if (checked) {
                        sold = false
                        checkbox_sold.isChecked.not()
                    } else {
                        // I'm lactose intolerant
                    }
                }
                R.id.checkbox_sold -> {
                    if (checked) {
                        sold = true
                        checkbox_available.isChecked.not()
                    } else {
                        // I'm lactose intolerant
                    }
                }
            }
        }
    }

    private fun checkPermissionToReadStorage() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this,
                "sorry but you did not grant permission to the application to access the image gallery",
                Toast.LENGTH_LONG
            ).show()
        } else {
            pickImageFromGallery()
        }
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            showCustomDialog(data)
        }
    }

    private fun showCustomDialog(data: Intent?) {
        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.picture_title_dialogue, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Login Form")
        //show dialog
        val mAlertDialog = mBuilder.show()
        //login button click of custom layout
        mDialogView.dialogLoginBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            //get text from EditTexts of custom layout
            val name = mDialogView.dialogNameEt.text.toString()
            var picture = Picture(0, name, data?.data, 0)
            this.pictures.add(picture)
            updateUi()
        }
        //cancel button click of custom layout
        mDialogView.dialogCancelBtn.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    private fun updateUi() {
        configureRecyclerView()
//        this.adapter.notifyDataSetChanged()
    }
}
