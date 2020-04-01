package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.*
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.DATA
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.FRAGMENT_DETAIL
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.FRAGMENT_LIST
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.FRAGMENT_MAP
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.FRAGMENT_MORT_GAGE
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.FRAGMENT_SEARCH
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.INTERNET
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.IS_DETAIL_CALLING_YOU
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.LIST_PROPERTY
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.LOCATION
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.OPEN_MAPS
import com.cheyrouse.gael.realestatemanagerkt.utils.Prefs
import com.cheyrouse.gael.realestatemanagerkt.utils.Utils
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    EstateListFragment.OnFragmentInteractionListener, SearchFragment.OnSearchFragmentListener,
    MapsFragment.OnMapsFragmentListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var propertiesList: List<Property>
    private var propertyId: Long = 0
    private var isDetail: Boolean = false
    private var isTablet: Boolean = false
    private lateinit var prefs: Prefs
    private var refreshCount: Int = 0
    private var isDisplaySearch = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkScreenOrientation()
        checkSelfPermissions()
        checkDeviceServices()
        configureToolbar()
        defineIsTablet()
        configureNavDrawer()
        configureNavView()
        getTheBundle()
    }

    // Check if screen is on portrait or landscape
    private fun checkScreenOrientation() {
        if (resources.getBoolean(R.bool.portrait_only)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    //Get intent bundle
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getTheBundle() {
        if (this.intent.extras != null) {
            propertiesList = intent.getParcelableArrayListExtra(LIST_PROPERTY)
            configureAndShowFragmentList(propertiesList)
            isDetail = intent.getBooleanExtra(IS_DETAIL_CALLING_YOU, false)
        } else {
            initViewModel()
        }
    }

    // Check if device is a tablet
    private fun defineIsTablet() {
        prefs = Prefs.get(this)
        if (activity_main_detail_frame_layout != null) {
            isTablet = true
        } else prefs.storeLastItemClicked(-1)
    }

    // Check permissions
    private fun checkSelfPermissions() {
        val permissions: ArrayList<String> = ArrayList()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_PHONE_STATE)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.CAMERA)
        }
        if (permissions.isNotEmpty()) {
            val askPermissionsList: Array<String> = arrayOf()
            val array = arrayOfNulls<String>(permissions.size)
            askPermissionsList to array
            ActivityCompat.requestPermissions(
                this,
                permissions.toArray(askPermissionsList),
                1
            )
        }
    }

    // To show alert dialog
    private fun checkDeviceServices() {
        if (!Utils.isInternetAvailable(this)) {
            showAlertDialog(INTERNET)
        }

        if (!Utils.isLocationEnabled(this)) {
            showAlertDialog(LOCATION)
        }
    }

    // View model initialisation
    private fun initViewModel() {
        val propertyViewModel: PropertyViewModel = ViewModelProviders.of(
            this,
            DataInjection.Injection.provideViewModelFactory(this)
        ).get(PropertyViewModel::class.java)
        propertyViewModel.getAllProperty()
            .observe(this, Observer { createDefaultList(it!!) })
    }

    // To init list of properties
    private fun createDefaultList(properties: List<Property>) {
        propertiesList = properties
        configureAndShowFragmentList(null)
        if (propertiesList.isEmpty()) {
            showAlertDialog(DATA)
        }
    }

    // Alert dialog dispatch and display
    private fun showAlertDialog(type: String) {
        var title = ""
        var text = ""
        var intent = Intent()
        lateinit var mAlertDialog: AlertDialog
        when (type) {
            LOCATION -> {
                title = resources.getString(R.string.gps_title)
                text = resources.getString(R.string.gps_text)
                intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            }
            INTERNET -> {
                title = resources.getString(R.string.internet_title)
                text = resources.getString(R.string.internet_text)
                intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            }
            DATA -> {
                title = resources.getString(R.string.welcome_title)
                text = resources.getString(R.string.welcome_text)
                intent = Intent(this, CreateEstateActivity::class.java)
            }
            OPEN_MAPS -> {
                title = resources.getString(R.string.open_maps_title)
                text = resources.getString(R.string.open_maps_text)
                intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            }
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(text)
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            startActivity(intent)
        }
        builder.setNegativeButton(android.R.string.no) { _, _ ->
            mAlertDialog.dismiss()
        }
        mAlertDialog = builder.show()
    }

    // Toolbar configuration
    private fun configureToolbar() {
        setSupportActionBar(toolbar)
    }

    // Navigation drawer configuration
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

    // Navigation view configuration
    private fun configureNavView() {
        val navigationView: NavigationView = nav_view
        navigationView.setNavigationItemSelectedListener(this)
    }

    // Toolbar menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Toolbar menu items
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

    // To open EditFragment
    private fun openEditFragment() {
        val intent = Intent(this, CreateEstateActivity::class.java)
        if (propertyId == 0L) {
            propertyId = propertiesList[0].id
        }
        intent.putExtra(DetailActivity.PROPERTY, propertyId)
        startActivity(intent)
    }

    // To launch CreateActivity
    private fun launchCreateActivity() {
        val intent = Intent(this, CreateEstateActivity::class.java)
        startActivity(intent)
    }

    // To launch Search
    private fun launchSearchFragment() {
        if (!isDisplaySearch) {
            if (isTablet) {
                launchFragment(FRAGMENT_SEARCH, 0, R.id.activity_main_100_frame_layout, null)
            } else {
                launchFragment(FRAGMENT_SEARCH, 0, R.id.activity_main_frame_layout, null)
            }
            isDisplaySearch = true
        }
    }

    // Check if device location is enable
    private fun checkIfLocationIsEnable() {
        if (Utils.isLocationEnabled(this)) {
            // Open search fragment
            if (isTablet) {
                launchFragment(FRAGMENT_MAP, 0, R.id.activity_main_100_frame_layout, null)
            } else {
                launchFragment(FRAGMENT_MAP, 0, R.id.activity_main_frame_layout, null)
            }
        } else {
            showAlertDialogLocation()
        }
    }

    // To display alert dialog location
    private fun showAlertDialogLocation() {
        showAlertDialog(OPEN_MAPS)
    }

    // Navigation drawer menu
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.activity_main_drawer_simulator -> {
                launchMortGageSimulator()
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
            R.id.activity_main_drawer_logout -> {
                showAlertDialogCloseApp()
            }
        }
        activity_main_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    // To launch Loan Simulator
    private fun launchMortGageSimulator() {
        launchFragment(FRAGMENT_MORT_GAGE, 0, R.id.activity_main_frame_layout, null)
    }

    // OnBackPressed function
    override fun onBackPressed() {
        if (activity_main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            activity_main_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (isTablet) {
                if (isDetail) {
                    isDetail = false
                    finish()
                } else checkBackStack(2)

            } else checkBackStack(1)
        }
        isDisplaySearch = false
    }

    // To manage fragment back stack
    private fun checkBackStack(i: Int) {
        if (supportFragmentManager.backStackEntryCount <= i) {
            showAlertDialogCloseApp()
        } else {
            if (refreshCount > 0) {
                while (supportFragmentManager.backStackEntryCount > 2) {
                    supportFragmentManager.popBackStackImmediate(
                        supportFragmentManager.backStackEntryCount - 1,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
                refreshCount = 0
            } else {
                super.onBackPressed()
            }
        }
    }

    // Display alert dialog to quit app
    private fun showAlertDialogCloseApp() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Do you want to quit the app?")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            prefs.storeLastItemClicked(0)
            finishAffinity()
        }
        builder.setNegativeButton(android.R.string.no) { _, _ -> }
        builder.show()
    }

    // Show List on phone or list and detail on tablet
    private fun configureAndShowFragmentList(it: List<Property>?) {
        val id: Long
        launchFragment(FRAGMENT_LIST, 0, R.id.activity_main_frame_layout, it)
        if (isTablet) {
            if (it != null) {
                id = it[0].id
                refreshCount = +1
            } else {
                id = Utils.getPropertyId(this, propertiesList)
            }
            launchFragment(FRAGMENT_DETAIL, id, R.id.activity_main_detail_frame_layout, it)
        }
    }

    // Show detail
    private fun configureAndShowFragmentDetail(property: Property) {
        if (isTablet) {
            val detailFragment = DetailEstateFragment.newInstance(property.id)
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_detail_frame_layout, detailFragment)
                .commit()
        } else {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.PROPERTY, property.id)
            startActivity(intent)
        }
    }

    private fun launchFragment(
        tag: String,
        propId: Long,
        frameLayout: Int,
        it: List<Property>?
    ) {
        lateinit var fragment: Fragment
        when (tag) {
            FRAGMENT_LIST -> fragment = EstateListFragment.newInstance(it)
            FRAGMENT_SEARCH -> fragment = SearchFragment.newInstance()
            FRAGMENT_MAP -> fragment = MapsFragment.newInstance()
            FRAGMENT_DETAIL -> fragment = DetailEstateFragment.newInstance(propId)
            FRAGMENT_MORT_GAGE -> fragment = MortGageCalculatorFragment.newInstance()
        }
        supportFragmentManager.beginTransaction()
            .replace(frameLayout, fragment)
            .addToBackStack(tag)
            .commit()
    }

    // ListFragment interface
    override fun onFragmentListInteraction(property: Property) {
        this.propertyId = property.id
        configureAndShowFragmentDetail(property)
    }

    // SearchFragment interface
    override fun onSearchInteraction(it: List<Property>) {
        onBackPressed()
        configureAndShowFragmentList(it)
        isDisplaySearch = false
    }

    // MapsFragment interface
    override fun onMapsInteraction(idProperty: Long) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.PROPERTY, idProperty)
        startActivity(intent)
        Handler().postDelayed({
            onBackPressed()
        }, 400)
    }
}

