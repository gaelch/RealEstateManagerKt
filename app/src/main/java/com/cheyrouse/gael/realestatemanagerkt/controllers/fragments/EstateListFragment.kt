package com.cheyrouse.gael.realestatemanagerkt.controllers.fragments


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.RealEstateManagerApplication
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.Utils
import com.cheyrouse.gael.realestatemanagerkt.view.EstateListAdapter
import com.cheyrouse.gael.realestatemanagerkt.view.ListPaddingDecoration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_estate_list.*


class EstateListFragment : Fragment() {

    private lateinit var propertyViewModel: PropertyViewModel
    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var estateListAdapter: EstateListAdapter
    private var listProperty: List<Property>? = null


    companion object {
        private const val ARG_PARAM = "any"
        fun newInstance(list: List<Property>?): EstateListFragment {
            return EstateListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, Gson().toJson(list))
                }
            }
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
        getTheBundleIfExist()
    }

    // Get intent data
    private fun getTheBundleIfExist() {
        if (arguments != null) {
            listProperty = Gson().fromJson(
                arguments?.getString(ARG_PARAM),
                object : TypeToken<List<Property>>() {}.type
            )
            if (listProperty != null) {
                setListInRecyclerView()
            } else {
                initViewModelFactory()
                getPropertiesAndConfigureRecyclerView()
            }
        } else {
            initViewModelFactory()
            getPropertiesAndConfigureRecyclerView()
        }
    }

    // Configure recycler view
    private fun setListInRecyclerView() {
        estate_picture_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listProperty?.let {
                EstateListAdapter(it) { property: Property ->
                    onItemClicked(property)
                }
            }
            estateListAdapter = adapter as EstateListAdapter
        }
        addItemDecoration()
    }

    // Get properties
    private fun getPropertiesAndConfigureRecyclerView() {
        // Observe the model
        propertyViewModel.getAllProperty().observe(this, Observer { displayList(it!!)})
    }

    // To set list of properties in recyclerView
    private fun displayList(properties: List<Property>) {
        listProperty = properties
        // Data bind the recycler view
        estate_picture_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter =
                EstateListAdapter(properties) { property: Property -> onItemClicked(property) }
            estateListAdapter = adapter as EstateListAdapter
        }
        addItemDecoration()
        if(!RealEstateManagerApplication.isSearch()){
            estate_picture_recycler_view.post {
                estate_picture_recycler_view.scrollToPosition(Utils.getPropertyPosition(listProperty))
            }
        }else{
            RealEstateManagerApplication.setSearchCalls(false)
        }
    }

    // To add decorations to recyclerView items
    private fun addItemDecoration() {
        estate_picture_recycler_view.addItemDecoration(
            ListPaddingDecoration(
                activity as Activity,
                resources.getDimension(R.dimen.my_value),
                0
            )
        )
    }

    //Init viewModel
    private fun initViewModelFactory() {
        this.propertyViewModel = ViewModelProviders.of(this,
                activity?.applicationContext?.let {
                    DataInjection.Injection.provideViewModelFactory(
                        it
                    )
                })
            .get(PropertyViewModel::class.java)
    }

    // Items listener
    private fun onItemClicked(property: Property) {
        mListener?.onFragmentListInteraction(property)
        if (RealEstateManagerApplication.getLastItemClicked()!= -1) {
            estateListAdapter.notifyAdapter()
        }
    }

    // ListFragment callback
    interface OnFragmentInteractionListener {
        fun onFragmentListInteraction(property: Property)
    }

}
