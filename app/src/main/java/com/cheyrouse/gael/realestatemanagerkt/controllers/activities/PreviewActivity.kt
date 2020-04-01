package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.utils.Prefs

class PreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        checkScreenOrientation()
        Handler().postDelayed({
            launchMain()
        }, 800)
    }

    private fun launchMain() {
        val prefs: Prefs = Prefs.get(this)
        prefs.storeLastItemClicked(0)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun checkScreenOrientation() {
        if (resources.getBoolean(R.bool.portrait_only)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
