package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cheyrouse.gael.realestatemanagerkt.R


class EstateListFragment : Fragment() {

    companion object {

        fun newInstance(): EstateListFragment {
            return EstateListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estate_list, container, false)
    }


}
