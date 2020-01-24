package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.view.EstateListAdapter
import kotlinx.android.synthetic.main.fragment_estate_list.*


class EstateListFragment : Fragment() {

    private lateinit var propertyViewModel: PropertyViewModel
    private var mListener: OnFragmentInteractionListener? = null


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
        initViewModelFactory()
        getPropertiesAndConfigureRecyclerView()
    }

    private fun getPropertiesAndConfigureRecyclerView() {
        // Observe the model
        propertyViewModel.getAllProperty().observe(this, Observer{ properties->
            // Data bind the recycler view
            estate_picture_recycler_view.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = EstateListAdapter(properties) { property : Property -> onItemClicked(property) }
            }
        })
    }

    private fun initViewModelFactory() {
        this.propertyViewModel = ViewModelProviders.of(this,
            activity?.applicationContext?.let { DataInjection.Injection.provideViewModelFactory(it) }).get(PropertyViewModel::class.java)
    }

    private fun onItemClicked(property: Property) {
        mListener?.onFragmentInteraction(property)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(property: Property)
    }

}
