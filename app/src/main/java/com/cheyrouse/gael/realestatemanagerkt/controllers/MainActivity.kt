package com.cheyrouse.gael.realestatemanagerkt.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.cheyrouse.gael.realestatemanagerkt.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureToolbar()
    }

    private fun configureToolbar() {

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
                return true
            }
            R.id.menu_edit-> {
                // Open edit fragment
                return true
            }
            R.id.menu_create -> {
                // Open create fragment
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
