package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cheyrouse.gael.realestatemanagerkt.R


class DetailEstateFragment : Fragment() {

    companion object {

        fun newInstance(): DetailEstateFragment {
            return DetailEstateFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_estate, container, false)
    }


}
