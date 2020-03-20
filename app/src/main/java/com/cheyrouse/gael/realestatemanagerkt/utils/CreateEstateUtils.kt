package com.cheyrouse.gael.realestatemanagerkt.utils

import android.content.Context
import android.widget.Toast
import androidx.core.view.isVisible
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.models.Address
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import kotlinx.android.synthetic.main.activity_create_estate.*
import java.util.ArrayList

class CreateEstateUtils {

    fun checkValueBeforeStoreProperty(
        context: Context,
        typeOfProperty: String,
        surface: Int,
        numberOfRooms: Int,
        numberOfBed: Int,
        numberOfBath: Int,
        address: Address,
        price: Double,
        realtorName: String,
        entryDate: String,
        soldDate: String,
        sold: Boolean,
        property: Property,
        city: String,
        postalCode: String,
        country: String
    ): Property {
        val propertyToReturn = Property(
            0, "", "", 0.0, 0, 0,
            shops = false,
            trainStation = false,
            park = false,
            airport = false,
            subway = false,
            school = false,
            status = true,
            dateOfEntry = "",
            dateOfSale = "",
            realtor = "",
            numOfBath = 0,
            numOfBed = 0,
            address = null,
            pictures = emptyList()
        )

        if (typeOfProperty.isNotEmpty()) {
            property.type = typeOfProperty
            property.address = Address()
            if (price != 0.0) {
                property.price = price
                if (numberOfRooms != 0) {
                    property.rooms = numberOfRooms
                    if (numberOfBed != 0) {
                        property.numOfBed = numberOfBed
                        if (numberOfBath != 0) {
                            property.numOfBath = numberOfBath
                            if (surface != 0) {
                                property.livingSpace = surface
                                if (realtorName.isNotEmpty()) {
                                    property.realtor = realtorName
                                    if (address.address?.isNotEmpty()!!) {
                                        property.address = address
                                        if (city.isNotEmpty()) {
                                            property.address!!.city = city
                                            if (postalCode.isNotEmpty()) {
                                                property.address!!.postalCode = postalCode
                                                if (country.isNotEmpty()) {
                                                    property.address!!.country = country
                                                    if (sold) {
                                                        if (entryDate != "") {
                                                            property.dateOfEntry = entryDate
                                                            return property
                                                        } else showToast(
                                                            context,
                                                            context.resources.getString(R.string.entry_date_text)
                                                        )
                                                    } else if (!sold) {
                                                        if (soldDate != "") {
                                                            property.dateOfSale = soldDate
                                                            return property
                                                        } else showToast(
                                                            context,
                                                            context.resources.getString(R.string.sold_date_text)
                                                        )
                                                    }
                                                } else showToast(
                                                    context,
                                                    context.resources.getString(R.string.country_text)
                                                )

                                            } else showToast(
                                                context,
                                                context.resources.getString(R.string.postal_text)
                                            )

                                        } else showToast(
                                            context,
                                            context.resources.getString(R.string.city_text)
                                        )

                                    } else showToast(
                                        context,
                                        context.resources.getString(R.string.address_text)
                                    )

                                } else showToast(
                                    context,
                                    context.resources.getString(R.string.realtor_text)
                                )

                            } else showToast(
                                context,
                                context.resources.getString(R.string.space_text)
                            )

                        } else showToast(context, context.resources.getString(R.string.bath_text))

                    } else showToast(context, context.resources.getString(R.string.numBed_text))

                } else showToast(context, context.resources.getString(R.string.room_text))

            } else showToast(context, context.resources.getString(R.string.price_text))

        } else showToast(context, context.resources.getString(R.string.type_text))

        return propertyToReturn
    }

    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}