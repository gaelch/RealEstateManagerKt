package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.*
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.Utils
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    EstateListFragment.OnFragmentInteractionListener, SearchFragment.OnSearchFragmentListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var propertiesList: List<Property>
    private var propertyId: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkSelfPermissions()
        configureToolbar()
        initViewModel()
        configureNavDrawer()
        configureNavView()
        configureAndShowFragmentList(null)
    }

    private fun checkSelfPermissions() {
        val permissions: ArrayList<String> = ArrayList()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_PHONE_STATE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }
        if (permissions.isNotEmpty()) {
            val askPermissionsList: Array<String> = arrayOf()
            val array = arrayOfNulls<String>(permissions.size)
            askPermissionsList to array
            ActivityCompat.requestPermissions(
                this,
                permissions.toArray<String>(askPermissionsList),
                1
            )
        }
    }

    private fun initViewModel() {
        val propertyViewModel: PropertyViewModel = ViewModelProviders.of(
            this,
            DataInjection.Injection.provideViewModelFactory(this)
        ).get(PropertyViewModel::class.java)
        propertyViewModel.getAllProperty()
            .observe(this, Observer { createDefaultList(it!!) })
    }

    private fun createDefaultList(properties: List<Property>) {
        propertiesList = properties
        if(propertiesList.isEmpty()){
           showAlertDialogWelcome()
        }
    }

    private fun showAlertDialogWelcome() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Welcome to Real Estate Manager ! ")
        builder.setMessage("To start using the app, please register a property")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            val intent = Intent(this, CreateEstateActivity::class.java)
            startActivity(intent)
        }
        builder.show()
    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun configureNavDrawer() {
        drawer = activity_main_drawer_layout
        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
    }

    private fun configureNavView() {
        val navigationView: NavigationView = nav_view
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_map -> {
                checkIfLocationIsEnable()
                return true
            }
            R.id.menu_search -> {
                // Open search fragment
                launchSearchFragment()
                return true
            }
            R.id.menu_edit -> {
                // Open edit fragment
                openEditFragment()
                return true
            }
            R.id.menu_create -> {
                // Open create activity
                launchCreateActivity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openEditFragment() {
        val intent = Intent(this, CreateEstateActivity::class.java)
        if (propertyId == 0L) {
            propertyId = propertiesList[0].id
        }
        intent.putExtra(DetailActivity.PROPERTY, propertyId)
        startActivity(intent)
    }

    private fun launchCreateActivity() {
        val intent = Intent(this, CreateEstateActivity::class.java)
        startActivity(intent)
    }

    private fun launchSearchFragment() {
        val searchFragment = SearchFragment.newInstance()
        if (activity_detail_frame_layout != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_100_frame_layout, searchFragment)
                .addToBackStack("mapsFragment")
                .commit()
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_frame_layout, searchFragment)
                .addToBackStack("mapsFragment")
                .commit()
        }
    }

    private fun checkIfLocationIsEnable() {
        if (Utils.isLocationEnabled(this)) {
            // Open search fragment
            if (activity_detail_frame_layout != null) {
                val mapsFragment = MapsFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_100_frame_layout, mapsFragment)
                    .addToBackStack("mapsFragment")
                    .commit()
            }else{
                val mapsFragment = MapsFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_frame_layout, mapsFragment)
                    .addToBackStack("mapsFragment")
                    .commit()
            }

        } else {
            showAlertDialogLocation()
        }
    }

    private fun showAlertDialogLocation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Location Alert")
        builder.setMessage("To open map, enable location please.")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        builder.setNegativeButton(android.R.string.no) { _, _ -> }
        builder.show()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.activity_main_drawer_simulator -> {
                val mortGageCalculatorFragment = MortGageCalculatorFragment.newInstance()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_frame_layout, mortGageCalculatorFragment)
                    .addToBackStack("detailFragment")
                    .commit()
            }
            R.id.activity_main_drawer_create -> {
                // Open create activity
                launchCreateActivity()
            }
            R.id.activity_main_drawer_edit -> {
                // Open edit fragment
                openEditFragment()
            }
            R.id.activity_main_drawer_search -> {
                // Open search fragment
                launchSearchFragment()
            }
        }
        activity_main_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (activity_main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            activity_main_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount == 1) {
                showAlertDialogCloseApp()
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun showAlertDialogCloseApp() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Do you want to quit the app?")
        builder.setPositiveButton(android.R.string.yes) { _, _ -> finish() }
        builder.setNegativeButton(android.R.string.no) { _, _ -> }
        builder.show()
    }

    private fun configureAndShowFragmentList(it: List<Property>?) {
        val listFragment = EstateListFragment.newInstance(it)
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_frame_layout, listFragment)
            .addToBackStack("listFragment")
            .commit()

        if (activity_detail_frame_layout != null) {
            val detailFragment = DetailEstateFragment.newInstance(0)
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_detail_frame_layout, detailFragment)
                .addToBackStack("detailFragment")
                .commit()
        }
    }

    override fun onFragmentInteraction(property: Property) {
        Toast.makeText(this, "Clicked: ${property.type}", Toast.LENGTH_LONG).show()
        this.propertyId = property.id
        configureAndShowFragmentDetail(property)
    }

    private fun configureAndShowFragmentDetail(property: Property) {
        val detailFragment = DetailEstateFragment.newInstance(property.id)
        if (activity_detail_frame_layout != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_detail_frame_layout, detailFragment)
                .addToBackStack("detailFragment")
                .commit()
        } else {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.PROPERTY, property.id)
            startActivity(intent)
        }
    }

    override fun onSearchInteraction(it: List<Property>) {
        onBackPressed()
        configureAndShowFragmentList(it)
//        val listFragment = EstateListFragment.newInstance(it)
//        if (activity_detail_frame_layout != null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.activity_main_100_frame_layout, listFragment)
//                .addToBackStack("listFragment")
//                .commit()
//        }else{
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.activity_main_frame_layout, listFragment)
//                .addToBackStack("listFragment")
//                .commit()
//        }
    }
}

