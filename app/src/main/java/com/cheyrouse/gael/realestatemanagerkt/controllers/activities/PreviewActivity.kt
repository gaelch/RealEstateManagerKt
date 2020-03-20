package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.cheyrouse.gael.realestatemanagerkt.R
import java.util.*

class PreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        Handler().postDelayed({
            launchMain()
        }, 800)

    }

    private fun launchMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}
