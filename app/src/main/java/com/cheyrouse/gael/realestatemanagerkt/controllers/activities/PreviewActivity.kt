package com.cheyrouse.gael.realestatemanagerkt.controllers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel

class PreviewActivity : AppCompatActivity() {

    private lateinit var propertyViewModel: PropertyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        deleteIfExist()
        Handler().postDelayed({
            launchMain()
        }, 800)
    }

    // Launch activity main
    private fun launchMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun deleteIfExist(){
        propertyViewModel = ViewModelProviders.of(
            this,
            this.let { DataInjection.Injection.provideViewModelFactory(it) }
        ).get(PropertyViewModel::class.java)
        Thread {
            propertyViewModel.deleteProperty(10001)
        }.start()
    }
}
