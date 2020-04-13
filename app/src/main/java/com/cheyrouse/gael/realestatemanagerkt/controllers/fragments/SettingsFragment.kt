package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.utils.Prefs
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    private var device: Boolean = false
    private lateinit var prefs: Prefs

    companion object {

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPrefs()
        initCheckBoxDevice()
        configureValidBtn()
    }


    private fun getPrefs() {
        prefs = Prefs.get(activity)
        device = prefs.foreignCurrency
    }

    private fun initCheckBoxDevice() {
        if(device){
            euros_box.isChecked = true
            dollars_box.isChecked = false
        }else{
            dollars_box.isChecked = true
            euros_box.isChecked = false
        }
        dollars_box.setOnCheckedChangeListener { _, b ->
            if (b) {
                dollars_box.isChecked = true
                euros_box.isChecked = false
                device = false
            }
        }
        euros_box.setOnCheckedChangeListener { _, b ->
            if (b) {
                euros_box.isChecked = true
                dollars_box.isChecked = false
                device = true
            }
        }
    }

    private fun configureValidBtn() {
        valid_device_btn.setOnClickListener {
            prefs.storeForeignCurrency(device)
            activity?.onBackPressed()
        }
    }

}
