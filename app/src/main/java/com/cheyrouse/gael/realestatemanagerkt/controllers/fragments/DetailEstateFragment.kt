package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper

import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.view.DetailPictureAdapter
import kotlinx.android.synthetic.main.fragment_detail_estate.*


class DetailEstateFragment : Fragment() {

    private lateinit var property: Property

    companion object {
        private const val ARG_PARAM = "property"
        fun newInstance(bdlProperty: Property): DetailEstateFragment {
            val fragment = DetailEstateFragment()
            val args = Bundle()
            args.putParcelable(ARG_PARAM, bdlProperty)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_detail_estate, container, false)


    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detail_picture_recycler_view.apply {
            if (arguments != null) {
                property = arguments?.getParcelable(ARG_PARAM)!!
            }
            layoutManager = LinearLayoutManager(activity, OrientationHelper.HORIZONTAL, false)
            adapter = property.pictures?.let { DetailPictureAdapter(it) }
        }
    }

}
