package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager

import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.view.EstateListAdapter
import kotlinx.android.synthetic.main.fragment_estate_list.*


class EstateListFragment : Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    private var db1: String = "12,456,230"
    private var db2: String = "33,400,300"
    private var db3: String = "15,505,000"
    private var db4: String = "22,006,230"
    private var db5: String = "42,400,030"
    private var db6: String = "30,000,230"
    private var db7: String = "25,006,230"
    private var db8: String = "56,400,030"

    private var str: String = "/storage/sdcard0/DCIM/Camera/IMG_20190831_110307.jp"
    private val uri = str.toUri()

    private val pictureList = listOf(Picture(0, "Lounge", uri, 0),
        Picture(1, "Lounge", uri, 0),
        Picture(2, "Lounge", uri, 0),
        Picture(3, "Lounge", uri, 0),
        Picture(4, "Lounge", uri, 0),
        Picture(5, "Lounge", uri, 0),
        Picture(6, "Lounge", uri, 0),
        Picture(7, "Lounge", uri, 0))
    private val pointInterestList = listOf(
        "school", "bus", "shops")
    private val propertiesList = listOf(
        Property(0, "Manor", "", db1, 300, 5,
            "2, rue Cavial 46100 Figeac", pointInterestList, true,  null, null, "Jake", pictureList),
        Property(1, "Loft", "", db2, 300, 5,
            "2, rue Cavial 46100 Figeac", pointInterestList, true,  null, null, "Jake", pictureList),
        Property(2, "Flat", "", db3, 300, 5,
            "2, rue Cavial 46100 Figeac", pointInterestList, true,  null, null, "Emmy", pictureList),
        Property(3, "House", "", db4, 300, 5,
            "2, rue Cavial 46100 Figeac", pointInterestList, true,  null, null, "Jennifer", pictureList),
        Property(4, "House", "", db5, 300, 5,
            "2, rue Cavial 46100 Figeac", pointInterestList, true,  null, null, "Billy", pictureList),
        Property(5, "Duplex", "", db6, 300, 5,
            "2, rue Cavial 46100 Figeac", pointInterestList, true,  null, null, "Emmy", pictureList),
        Property(6, "Loft", "", db7, 300, 5,
            "2, rue Cavial 46100 Figeac", pointInterestList, true,  null, null, "Jennifer", pictureList),
        Property(7, "House", "", db8, 300, 5,
            "2, rue Cavial 46100 Figeac", pointInterestList, true,  null, null, "Billy", pictureList)
    )

    companion object {

        fun newInstance(): EstateListFragment {
            return EstateListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_estate_list, container, false)

    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        estate_picture_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = EstateListAdapter(propertiesList) { property : Property -> onItemClicked(property) }
        }

    }

    private fun onItemClicked(property: Property) {
        mListener?.onFragmentInteraction(property)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(property: Property)
    }

}
