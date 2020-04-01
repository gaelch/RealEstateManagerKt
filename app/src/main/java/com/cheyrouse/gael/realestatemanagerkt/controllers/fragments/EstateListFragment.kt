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
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.DataInjection
import com.cheyrouse.gael.realestatemanagerkt.controllers.viewModel.PropertyViewModel
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.Prefs
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
    private val prefs: Prefs = Prefs.get(activity)


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

    private fun getPropertiesAndConfigureRecyclerView() {
        // Observe the model
        propertyViewModel.getAllProperty().observe(this, Observer { createList(it!!)})
    }

    // To init list of properties
    private fun createList(properties: List<Property>) {
        listProperty = properties
        // Data bind the recycler view
        estate_picture_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter =
                EstateListAdapter(properties) { property: Property -> onItemClicked(property) }
            estateListAdapter = adapter as EstateListAdapter
        }
        addItemDecoration()
        if(!prefs.isSearch){
            estate_picture_recycler_view.post {
                estate_picture_recycler_view.scrollToPosition(Utils.getPropertyPosition(activity, listProperty))
            }
        }else{
            prefs.storeIsSearch(false)
        }
    }

    private fun addItemDecoration() {
        estate_picture_recycler_view.addItemDecoration(
            ListPaddingDecoration(
                activity as Activity,
                resources.getDimension(R.dimen.my_value),
                0
            )
        )
    }

    private fun initViewModelFactory() {
        this.propertyViewModel = ViewModelProviders.of(this,
                activity?.applicationContext?.let {
                    DataInjection.Injection.provideViewModelFactory(
                        it
                    )
                })
            .get(PropertyViewModel::class.java)
    }


    private fun onItemClicked(property: Property) {
        mListener?.onFragmentListInteraction(property)
        if (prefs.lastItemClicked != -1) {
            estateListAdapter.notifyAdapter()
        }
    }

    interface OnFragmentInteractionListener {
        fun onFragmentListInteraction(property: Property)
    }

}
