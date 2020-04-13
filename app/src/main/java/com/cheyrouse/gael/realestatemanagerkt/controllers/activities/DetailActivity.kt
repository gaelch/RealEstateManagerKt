package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.RealEstateManagerApplication
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.*
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.IS_DETAIL_CALLING_YOU
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.LIST_PROPERTY
import com.cheyrouse.gael.realestatemanagerkt.utils.Utils
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class DetailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    SearchFragment.OnSearchFragmentListener, MapsFragment.OnMapsFragmentListener {

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
        configureAndShowFragmentDetail()
    }

    // Get data
    private fun getTheBundle() {
        propertyId = intent.getLongExtra(PROPERTY, 0)
    }

    // To configure Toolbar
    private fun configureToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Navigation drawer configuration
    private fun configureNavDrawer() {
        drawer = activity_detail_drawer_layout
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

    // Navigation view configuration
    private fun configureNavView() {
        val navigationView: NavigationView = nav_view
        navigationView.setNavigationItemSelectedListener(this)
    }

    // Toolbar option menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Toolbar menu actions
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
            R.id.menu_create -> {
                // Open create activity
                launchCreateActivity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // To launch SearchFragment
    private fun launchSearchFragment() {
        val searchFragment = SearchFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_detail_frame_layout, searchFragment)
            .addToBackStack("searchFragment")
            .commit()
    }


    // To check if location is enable
    private fun checkIfLocationIsEnable() {
        if (Utils.isLocationEnabled(this)) {
            // Open search fragment
            val mapsFragment = MapsFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_detail_frame_layout, mapsFragment)
//                .addToBackStack("mapsFragment")
                .commit()
        } else {
            showAlertDialog("location")
        }
    }

    // To display alertDialogs
    private fun showAlertDialog(s: String) {
        var title = ""
        var text = ""
        when (s) {
            "location" -> {
                title = resources.getString(R.string.gps_title)
                text = resources.getString(R.string.gps_text)
            }
            "closeApp" -> {
                title = resources.getString(R.string.close_app_title)
            }
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(text)
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            when (s) {
                "location" -> {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                "closeApp" -> {
                    RealEstateManagerApplication.setLastItemClicked(0)
                    finish()
                    moveTaskToBack(true)
                }
            }
        }
        builder.setNegativeButton(android.R.string.no) { _, _ -> }
        builder.show()
    }

    // Drawer menu items actions
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.activity_main_drawer_simulator -> {
                launchMortGageSimulator()
            }
            R.id.activity_main_drawer_create -> {
                launchCreateActivity()
            }
            R.id.activity_main_drawer_search -> {
                launchSearchFragment()
            }
            R.id.activity_main_drawer_prefs -> {
                // Open settings fragment
                launchSettingsFragment()
            }
            R.id.activity_main_drawer_logout -> {
                showAlertDialog("closeApp")
            }
        }
        activity_detail_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun launchSettingsFragment() {
        // Open settings fragment
        val settingsFragment = SettingsFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_detail_frame_layout, settingsFragment).commit()
    }

    // To launch CreateActivity
    private fun launchCreateActivity() {
        val intent = Intent(this, CreateEstateActivity::class.java)
        startActivity(intent)
    }

    // To launch MortGageSimulator
    private fun launchMortGageSimulator() {
        val mortGageCalculatorFragment = MortGageCalculatorFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_detail_frame_layout, mortGageCalculatorFragment)
            .commit()
    }

    override fun onBackPressed() {
        if (activity_detail_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            activity_detail_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount <= 1) {
                this.finish()
            } else {
                super.onBackPressed()
            }
        }
    }

    // To launch FragmentDetail
    private fun configureAndShowFragmentDetail() {
        val detailEstateFragment = DetailEstateFragment.newInstance(propertyId)
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_detail_frame_layout, detailEstateFragment).commit()
    }

    // Callback Search
    override fun onSearchInteraction(it: List<Property>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(LIST_PROPERTY, it as ArrayList)
        intent.putExtra(IS_DETAIL_CALLING_YOU, true)
        startActivity(intent)
    }

    // Callback Maps
    override fun onMapsInteraction(idProperty: Long) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(PROPERTY, idProperty)
        startActivity(intent)
        Handler().postDelayed({
            onBackPressed()
        }, 400)
    }
}
