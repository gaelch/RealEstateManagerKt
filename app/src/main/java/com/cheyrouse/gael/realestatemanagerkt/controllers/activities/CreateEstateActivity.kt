package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.cheyrouse.gael.realestatemanagerkt.R
import kotlinx.android.synthetic.main.activity_create_estate.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel
import com.cheyrouse.gael.realestatemanagerkt.models.Address
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant
import com.cheyrouse.gael.realestatemanagerkt.view.DetailPictureAdapter
import kotlinx.android.synthetic.main.picture_title_dialogue.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class CreateEstateActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    // 1 - FOR DATA
    private var propertyId: Long = 0
    private lateinit var propertyViewModel: PropertyViewModel
    private var listOfItems = Constant.ConstantVal.listOfItems
    private lateinit var typeOfProperty: String
    private var surface: Int = 0
    private var numbreOfRooms: Int = 0
    private lateinit var address: Address
    private lateinit var description: String
    private var price: String = ""
    private lateinit var realtorName: String
    private lateinit var entryDate: String
    private lateinit var soldDate: String
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    private var sold: Boolean = false
    private var pictures = arrayListOf<Picture>()
    private lateinit var property: Property
    private var airport: Boolean = false
    private var school: Boolean = false
    private var subway: Boolean = false
    private var shops: Boolean = false
    private var trainStation: Boolean = false
    private var park: Boolean = false
    var image_uri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_estate)
        initViewModelFactory()
        configureToolbar()
        getTheBundle()
        configureSpinner()
        configureSurface()
        configureNumRooms()
        configureAddress()
        configureDescription()
        configurePrice()
        configureRealtorName()
        configureDatePickerEntry()
        configureDatePickerSold()
        configureCheckBoxClick()
        configureButtons()
        configureButtonValidate()
    }

    private fun initViewModelFactory() {
        this.propertyViewModel = ViewModelProviders.of(
            this,
            DataInjection.Injection.provideViewModelFactory(this)
        ).get(PropertyViewModel::class.java)
    }

    private fun getTheBundle() {
        propertyId = intent.getLongExtra(DetailActivity.PROPERTY, 0)
        if (propertyId != 0L) {
            getSelectProperty()
        } else {
            property = Property()
        }
    }

    private fun getSelectProperty() {
        propertyViewModel.getProperty(propertyId).observe(this, Observer { property ->
            property?.let { initVars(it) }
        })
    }

    private fun initVars(property: Property) {
        this.property = property
        typeOfProperty = property.type
        if (property.livingSpace != null) surface = property.livingSpace!!
        if (property.rooms != null) numbreOfRooms = property.rooms!!
//        if (property.address != null) address = property.address!!   ------->>>> à remplacer par l'objet ADDRESS
        if (property.description != null) description = property.description!!
        if (property.price != null) price = property.price!!
        if (property.realtor != null) realtorName = property.realtor!!
        if (property.dateOfEntry != null) entryDate = property.dateOfEntry!!
        if (property.dateOfSale != null) soldDate = property.dateOfSale!!
        if (property.status != null) sold = property.status!!
//        if (property.pictures != null) pictures = property.pictures as ArrayList<Picture>  -------->>>> à remplacer pas PICTURE
        if (property.airport != null) this.airport = property.airport!!
        if (property.subway != null) this.subway = property.subway!!
        if (property.school != null) this.school = property.school!!
        if (property.shops != null) this.shops = property.shops!!
        if (property.trainStation != null) this.trainStation = property.trainStation!!
        if (property.park != null) this.park = property.park!!
        if(property.pictures!=null) this.pictures = property.pictures as ArrayList<Picture>
        initWidgets()
    }

    private fun initWidgets() {
        configureRecyclerView()
        val spinnerPosition: Int = listOfItems.indexOf(typeOfProperty)
        type_spinner.setSelection(spinnerPosition)
        if (property.livingSpace != null) edit_surface.setText(surface.toString())
        if (property.rooms != null) edit_nbr_rooms.setText(numbreOfRooms.toString())
        if (property.address != null) {
            edit_address.setText(property.address!!.address)
        } else {
            address = Address()
        }
        if (property.realtor != null) edit_realtor.setText(realtorName)
        if (property.description != null) edit_description.setText(description)
        if (property.price != null) picker_price.setText(price)
        if (property.dateOfSale != null) picker_sold_date.text = soldDate
        if (property.dateOfEntry != null) picker_entry_date.text = entryDate
        initCheckbox()
        if (property.status != null && property.status == true) {
            checkbox_available.isChecked = true
        } else {
            checkbox_sold.isChecked = true
        }
        configureRecyclerView()
    }


    private fun initCheckbox() {
        if (airport) {
            checkbox_airport.isChecked = true
        }
        if (school) {
            checkbox_school.isChecked = true
        }
        if (shops) {
            checkbox_shops.isChecked = true
        }
        if (subway) {
            checkbox_subway.isChecked = true
        }
        if (trainStation) {
            checkbox_train_station.isChecked = true
        }
        if (park) {
            checkbox_park.isChecked = true
        }

    }

    private fun configureButtons() {
        button_gallery.setOnClickListener {
            checkPermissionToReadStorage()
        }
        button_take_picture.setOnClickListener {
            checkPermissionToAccessCamera()
        }
    }

    @SuppressLint("WrongConstant")
    private fun configureRecyclerView() {
        create_picture_recycler_view.apply {
            layoutManager = LinearLayoutManager(applicationContext, OrientationHelper.HORIZONTAL, false)
            adapter = DetailPictureAdapter(pictures)
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
                price = priceStr
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
                address = Address()
                address.address = edit_address.text.toString()
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

    private fun configureCheckBoxClick() {
        checkbox_airport.setOnClickListener { airport = true }
        checkbox_school.setOnClickListener { school = true }
        checkbox_shops.setOnClickListener { shops = true }
        checkbox_subway.setOnClickListener { subway = true }
        checkbox_train_station.setOnClickListener { trainStation = true }
        checkbox_park.setOnClickListener { park = true }
        checkbox_available.setOnClickListener {
            sold = true
            checkbox_sold.isChecked = false
        }
        checkbox_sold.setOnClickListener {
            sold = false
            checkbox_available.isChecked = false
        }
    }

//    override fun onClick(p0: View?) {
//        p0 as CheckBox
//        var isChecked: Boolean = p0.isChecked
//        when (p0.id) {
//
//            R.id.checkbox_airport ->
//                if (isChecked) {
//                    airport = true
//                }
//            R.id.checkbox_school ->
//                if (isChecked) {
//                    school = true
//                }
//            R.id.checkbox_shops ->
//                if (isChecked) {
//                    shops = true
//                }
//            R.id.checkbox_subway ->
//                if (isChecked) {
//                    subway = true
//                }
//            R.id.checkbox_train_station ->
//                if (isChecked) {
//                    trainStation = true
//                }
//            R.id.checkbox_park ->
//                if (isChecked) {
//                    park = true
//                }
//            // Status
//            R.id.checkbox_available ->
//                if (isChecked) {
//                    sold = true
//                    checkbox_sold.isChecked = false
//                }
//            R.id.checkbox_sold ->
//                if (isChecked) {
//                    sold = false
//                    checkbox_available.isChecked = false
//                }
//        }
//    }

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

    private fun checkPermissionToAccessCamera() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this,
                "sorry but you did not grant permission to the application to access the camera",
                Toast.LENGTH_LONG
            ).show()
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, CAMERA_PICK_CODE)
    }

    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000
        //camera pick code
        private const val CAMERA_PICK_CODE = 100
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
            if (data != null) {
                image_uri = data.data
                image_uri?.let { showCustomDialog(it) }
                Log.e("test path uri", data.data.toString())
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_PICK_CODE) {
            Log.e("test path uri", image_uri.toString())
//            imagetest.setImageURI(image_uri)
//            takePicture()
            if (image_uri != null) {
                image_uri?.let { showCustomDialog(it) }
                Log.e("test path uri", image_uri.toString())
            }
        }
    }

    private fun showCustomDialog(image_uri: Uri) {
        var picture: Picture? = null
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
            picture = Picture(0, name, image_uri.toString(), propertyId)
            if (picture != null) {
                this.pictures.add(picture!!)
            }
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

    private fun configureButtonValidate() {
        button_validate.setOnClickListener {
            if (typeOfProperty.isNotEmpty()) property.type = typeOfProperty
            if (description.isNotEmpty()) property.description = description
            if (price.isNotEmpty()) property.price = price
            if (numbreOfRooms != 0) property.rooms = numbreOfRooms
            if (surface != 0) property.livingSpace = surface
            property.airport = airport
            property.park = park
            property.school = school
            property.subway = subway
            property.shops = shops
            property.trainStation = trainStation
            property.status = sold
            property.dateOfEntry = entryDate
            property.dateOfSale = soldDate
            if (realtorName.isNotEmpty()) property.realtor = realtorName
            property.address = Address()
            if (address.address?.isNotEmpty()!!) property.address = address
            if (pictures.size != 0) property.pictures = pictures

            propertyViewModel.createProperty(property)
            Toast.makeText(applicationContext, "Property registered", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}