package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.DetailEstateFragment
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class DetailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var property: Property

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
        property = intent.getParcelableExtra(PROPERTY)
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
        val detailEstateFragment = DetailEstateFragment.newInstance(property)
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_detail_frame_layout, detailEstateFragment).commit()
    }
}
