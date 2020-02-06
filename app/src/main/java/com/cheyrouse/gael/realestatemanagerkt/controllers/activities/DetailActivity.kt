package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.DetailEstateFragment
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.MapsFragment
import com.cheyrouse.gael.realestatemanagerkt.utils.Utils
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class DetailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var propertyId: Long = 0

    companion object {
        const val PROPERTY = "property"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        configureToolbar()
        configureNavDrawer()
        configureNavView()
        getTheBundle()
        configureAndShowFragmentList()
    }

    private fun getTheBundle() {
        propertyId = intent.getLongExtra(PROPERTY, 0)
    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun configureNavDrawer() {
        drawer = activity_detail_drawer_layout
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
            R.id.menu_map-> {
                checkIfLocationIsEnable()
                return true
            }

            R.id.menu_search -> {
                // Open search fragment
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.menu_edit-> {
                // Open edit fragment
                val intent = Intent(this, CreateEstateActivity::class.java)
                intent.putExtra(PROPERTY, propertyId)
                startActivity(intent)
                return true
            }
            R.id.menu_create -> {
                // Open create activity
                val intent = Intent(this, CreateEstateActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkIfLocationIsEnable() {
        if(Utils.isLocationEnabled(this)){
            // Open search fragment
            val mapsFragment = MapsFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_detail_frame_layout, mapsFragment)
                .addToBackStack("mapsFragment")
                .commit()
        }else{
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Location Alert")
        builder.setMessage("To open map, enable location please.")
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->

        }
        builder.show()
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
        activity_detail_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (activity_detail_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            activity_detail_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun configureAndShowFragmentList() {
        val detailEstateFragment = DetailEstateFragment.newInstance(propertyId)
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_detail_frame_layout, detailEstateFragment).commit()
    }
}
