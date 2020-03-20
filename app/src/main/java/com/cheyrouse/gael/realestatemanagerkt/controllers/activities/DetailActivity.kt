package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.DetailEstateFragment
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.MapsFragment
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.MortGageCalculatorFragment
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.SearchFragment
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.IS_DETAIL_CALLING_YOU
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.LIST_PROPERTY
import com.cheyrouse.gael.realestatemanagerkt.utils.Prefs
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

    private fun getTheBundle() {
        propertyId = intent.getLongExtra(PROPERTY, 0)
    }

    private fun configureToolbar() {
        setSupportActionBar(toolbar)
    }

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
                launchCreateActivity(propertyId)
                return true
            }
            R.id.menu_create -> {
                // Open create activity
                launchCreateActivity(null)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchSearchFragment() {
        val searchFragment = SearchFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_detail_frame_layout, searchFragment)
            .addToBackStack("searchFragment")
            .commit()
    }


    private fun checkIfLocationIsEnable() {
        if (Utils.isLocationEnabled(this)) {
            // Open search fragment
            val mapsFragment = MapsFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_detail_frame_layout, mapsFragment)
//                .addToBackStack("mapsFragment")
                .commit()
        } else {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Location Alert")
        builder.setMessage("To open map, enable location please.")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        builder.setNegativeButton(android.R.string.no) { _, _ ->

        }
        builder.show()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.activity_main_drawer_simulator -> {
                launchMortGageSimulator()
            }
            R.id.activity_main_drawer_create -> {
                launchCreateActivity(null)
            }
            R.id.activity_main_drawer_edit -> {
                launchCreateActivity(propertyId)
            }
            R.id.activity_main_drawer_search -> {
                launchSearchFragment()
            }
            R.id.activity_main_drawer_logout -> {
                showAlertDialogCloseApp()
            }
        }
        activity_detail_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showAlertDialogCloseApp() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Do you want to quit the app?")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            val prefs: Prefs = Prefs.get(this)
            prefs.storeLastItemClicked(0)
            finish()
            moveTaskToBack(true);}
        builder.setNegativeButton(android.R.string.no) { _, _ -> }
        builder.show()
    }

    private fun launchCreateActivity(id: Long?) {
        val intent = Intent(this, CreateEstateActivity::class.java)
        if (id != null) {
            intent.putExtra(PROPERTY, propertyId)
        }
        startActivity(intent)
    }

    private fun launchMortGageSimulator() {
        val mortGageCalculatorFragment = MortGageCalculatorFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_detail_frame_layout, mortGageCalculatorFragment)
//            .addToBackStack("mortGageCalculatorFragment")
            .commit()
    }

    override fun onBackPressed() {
        if (activity_detail_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            activity_detail_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount <= 1) {
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun configureAndShowFragmentDetail() {
        val detailEstateFragment = DetailEstateFragment.newInstance(propertyId)
        supportFragmentManager.beginTransaction()
            .add(R.id.activity_detail_frame_layout, detailEstateFragment).commit()
    }

    override fun onSearchInteraction(it: List<Property>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(LIST_PROPERTY, it as ArrayList)
        intent.putExtra(IS_DETAIL_CALLING_YOU, true)
        startActivity(intent)
    }

    override fun onMapsInteraction(idProperty: Long) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(PROPERTY, idProperty)
        startActivity(intent)
        Handler().postDelayed({
            onBackPressed()
        }, 400)
    }
}
