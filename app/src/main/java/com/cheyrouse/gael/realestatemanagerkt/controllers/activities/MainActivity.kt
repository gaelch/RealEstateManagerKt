package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.DetailEstateFragment
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.EstateListFragment
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, EstateListFragment.OnFragmentInteractionListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    // juste pour test
    private var db1: String = "12,456,230"
    private var db2: String = "33,400,300"
    private var db3: String = "15,505,000"
    private var db4: String = "22,006,230"
    private var db5: String = "42,400,030"
    private var db6: String = "30,000,230"
    private var db7: String = "25,006,230"
    private var db8: String = "56,400,030"

    private val pictureList = listOf(
        Picture(0, "Lounge", "/storage/sdcard0/DCIM/Camera/IMG_20190831_110307.jpg", 0),
        Picture(1, "Lounge", Environment.getExternalStoragePublicDirectory("/storage/sdcard0/DCIM/Camera/IMG_20190831_110307.jpg").path, 0),
        Picture(2, "Lounge", Environment.DIRECTORY_DCIM + "/Camera/IMG_20190831_110307.jpg", 0),
        Picture(3, "Lounge", Environment.DIRECTORY_DCIM + "/Camera/IMG_20190831_110307.jpg", 0),
        Picture(4, "Lounge", Environment.DIRECTORY_DCIM + "/Camera/IMG_20190831_110307.jpg", 0),
        Picture(5, "Lounge", Environment.DIRECTORY_DCIM + "/Camera/IMG_20190831_110307.jpg", 0),
        Picture(6, "Lounge", Environment.DIRECTORY_DCIM + "/Camera/IMG_20190831_110307.jpg", 0),
        Picture(7, "Lounge", Environment.DIRECTORY_DCIM + "/Camera/IMG_20190831_110307.jpg", 0)
    )
    private val propertiesList = listOf(
        Property(0, "Manor", "", db1, 300, 5, 2, 2, true,  null, null, "Jake", pictureList),
        Property(1, "Loft", "", db2, 300, 5, 2, 2, true,  null, null, "Jake", pictureList),
        Property(2, "Flat", "", db3, 300, 5, 2, 2, true,  null, null, "Emmy", pictureList),
        Property(3, "House", "", db4, 300, 5, 2, 2, true,  null, null, "Jennifer", pictureList),
        Property(4, "House", "", db5, 300, 5, 2, 2, true,  null, null, "Billy", pictureList),
        Property(5, "Duplex", "", db6, 300, 5, 2, 2, true,  null, null, "Emmy", pictureList),
        Property(6, "Loft", "", db7, 300, 5, 2, 2, true,  null, null, "Jennifer", pictureList),
        Property(7, "House", "", db8, 300, 5, 2, 2, true,  null, null, "Billy", pictureList))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureToolbar()
        configureNavDrawer()
        configureNavView()
        configureAndShowFragmentList()
    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun configureNavDrawer() {
        drawer = activity_main_drawer_layout
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
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
            R.id.menu_search -> {
                // Open search fragment
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.menu_edit-> {
                // Open edit fragment
                Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.menu_create -> {
                // Open create fragment
                Toast.makeText(this, "create", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.activity_main_drawer_simulator -> {
                Toast.makeText(this, "simulator", Toast.LENGTH_SHORT).show()
            }
            R.id.activity_main_drawer_create -> {
                Toast.makeText(this, "create", Toast.LENGTH_SHORT).show()
            }
            R.id.activity_main_drawer_edit -> {
                Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show()
            }
            R.id.activity_main_drawer_search -> {
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show()
            }
        }
        activity_main_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (activity_main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            activity_main_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun configureAndShowFragmentList() {
        val listFragment = EstateListFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_main_frame_layout, listFragment)
            .addToBackStack("listFragment")
            .commit()

        if(activity_detail_frame_layout != null) {
            val detailFragment = DetailEstateFragment.newInstance(propertiesList[0])
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_detail_frame_layout, detailFragment)
                .addToBackStack("detailFragment")
                .commit()
        }
    }
    override fun onFragmentInteraction(property: Property) {
        Toast.makeText(this, "Clicked: ${property.type}", Toast.LENGTH_LONG).show()
        configureAndShowFragmentDetail(property)
    }

    private fun configureAndShowFragmentDetail(property: Property) {
        val detailFragment = DetailEstateFragment.newInstance(property)
        if(activity_detail_frame_layout != null) {
            supportFragmentManager.beginTransaction()
            .add(R.id.activity_detail_frame_layout, detailFragment)
            .addToBackStack("detailFragment")
            .commit()
        }else{
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.PROPERTY, property)
            startActivity(intent)
        }
    }
}
