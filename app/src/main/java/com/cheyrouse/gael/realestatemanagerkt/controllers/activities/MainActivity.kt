package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.DetailEstateFragment
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.EstateListFragment
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, EstateListFragment.OnFragmentInteractionListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
//    private var str: String = "/storage/sdcard0/DCIM/Camera/IMG_20190831_110307.jp"
    private lateinit var propertiesList: List<Property>
    private var propertyId: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureToolbar()
        initViewModel()
        configureNavDrawer()
        configureNavView()
        configureAndShowFragmentList()
    }

    private fun initViewModel() {
        val propertyViewModel: PropertyViewModel = ViewModelProviders.of(
            this,
            DataInjection.Injection.provideViewModelFactory(this)
        ).get(PropertyViewModel::class.java)
        propertyViewModel.getAllProperty().observe(this, Observer<List<Property>>{createDefaultList(it!!)})
    }

    private fun createDefaultList(properties:List<Property>) {
        propertiesList = properties
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
                val intent = Intent(this, CreateEstateActivity::class.java)
                if(propertyId==0L){
                      propertyId = propertiesList[0].id
                }
                intent.putExtra(DetailActivity.PROPERTY, propertyId)
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
            val detailFragment = DetailEstateFragment.newInstance(0)
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_detail_frame_layout, detailFragment)
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
        if(activity_detail_frame_layout != null) {
            supportFragmentManager.beginTransaction()
            .add(R.id.activity_detail_frame_layout, detailFragment)
            .addToBackStack("detailFragment")
            .commit()
        }else{
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.PROPERTY, property.id)
            startActivity(intent)
        }
    }
}

