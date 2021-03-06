package com.cheyrouse.gael.realestatemanagerkt

import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.MortGageCalculatorFragment
import com.cheyrouse.gael.realestatemanagerkt.models.Address
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.TEXT_DATE
import com.cheyrouse.gael.realestatemanagerkt.utils.Converters
import com.cheyrouse.gael.realestatemanagerkt.utils.CreateEstateUtils
import com.cheyrouse.gael.realestatemanagerkt.utils.SearchUtils
import com.cheyrouse.gael.realestatemanagerkt.utils.Utils
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class UnitTest {

    @Test
    fun convertDollarToEuroTest(){
        assertEquals(8.0, Utils.convertDollarToEuro(10.0), 0.01)
    }

    @Test
    fun convertEuroToDollarTest(){
        assertEquals(12.0, Utils.convertEuroToDollar(10.0), 0.01)
    }

    @Test
    fun testTodayDateFormatter(){
        val dateFormat: DateFormat =
            SimpleDateFormat(TEXT_DATE, Locale.FRANCE)
        val dateStr = dateFormat.format(Date())
        assertEquals(dateStr, Utils.getTodayDate())
    }

    @Test
    fun getStringDateTest(){
        val dateStr = "04/04/2020"
        assertEquals(dateStr, Utils.getStringDate(2020, 4, 3))
    }

    @Test
    fun checkConvertersList(){
        val picture = Picture(0, "dressing", "content://media/external/images/media/5620")
        val picture2 = Picture(1, "room1", "content://media/external/images/media/5622")
        val listObj = listOf(picture, picture2)
        val converters = Converters()
        assertEquals(
            "[{\"id\":0,\"pictureName\":\"dressing\",\"picturePath\":\"content://media/external/images/media/5620\"}," +
                    "{\"id\":1,\"pictureName\":\"room1\",\"picturePath\":\"content://media/external/images/media/5622\"}]"
            ,converters.someObjectListToString(listObj))

        assertEquals(listObj, converters.stringToSomeObjectList("[{\"id\":0,\"pictureName\":\"dressing\",\"picturePath\":" +
                "\"content://media/external/images/media/5620\"},{\"id\":1,\"pictureName\":\"room1\"," +
                "\"picturePath\":\"content://media/external/images/media/5622\"}]"))
    }

    @Test
    fun getMonthlyPaymentToTest(){
        val interestRate = 1.0
        val  purchaseAmount = 10000.0
        val contribution = 100.0
        val duration = 2
        val calculatorFragment = MortGageCalculatorFragment.newInstance()
        assertEquals(416.81, calculatorFragment.getMonthlyPayment(interestRate,
            purchaseAmount,
            contribution,
            duration),
            0.1)
    }
    @Test
    fun getTotalLoanCalculatorTest(){
        val monthlyPayment = 416.81
        val duration = 2
        val calculatorFragment = MortGageCalculatorFragment.newInstance()
        assertEquals(10003.45, calculatorFragment.getTotalPayment(monthlyPayment, duration),0.1)
    }

    @Test
    fun setValuesInPropertyTest(){
        val address = Address(0, "75 PARK PLACE 8TH FLOOR   ", "",
            2, "NEW YORK", "NY 10007", "United States",
            40.7808, -73.9772, "")
        val propertyToCompare= Property(
            0, "Manor", "cute house", 200000.0, 300, 9,
            shops = false,
            trainStation = false,
            park = false,
            airport = false,
            subway = false,
            school = false,
            status = true,
            dateOfEntry = "16-03-2020",
            dateOfSale = "",
            realtor = "Julien",
            numOfBath = 2,
            numOfBed = 4,
            address = Address(0, "75 PARK PLACE 8TH FLOOR   ", "",
                2, "NEW YORK", "NY 10007", "United States",
                40.7808, -73.9772, ""),
            pictures = emptyList())
        val propertyToSend = Property(
            0, "Manor", "cute house", 200000.0, 300, 9,
            shops = false,
            trainStation = false,
            park = false,
            airport = false,
            subway = false,
            school = false,
            status = true,
            dateOfEntry = "16-03-2020",
            dateOfSale = "",
            realtor = "Julien",
            numOfBath = 2,
            numOfBed = 4,
            address = Address(0, "75 PARK PLACE 8TH FLOOR   ", "",
                2, "NEW YORK", "NY 10007", "United States",
                40.7808, -73.9772, ""),
            pictures = emptyList())

        val checkClass = CreateEstateUtils()
        val propertyToReturn: Property
        val pictures = arrayListOf<Picture>()
        propertyToReturn = checkClass.setValuesInProperty(40.7808, -73.9772,
            airport = false,
            school = false,
            subway = false,
            shops = false,
            trainStation = false,
            park = false,
            additionalAddress = "",
            pictures = pictures,
            address = address,
            description = "",
            entryDate = "16-03-2020",
            apartNumber = 2,
            sold = true,
            soldDate = "",
            property = propertyToSend
        )
        assertEquals(propertyToCompare, propertyToReturn)
    }

    @Test
    fun checkValuesToComposeAddress(){
        val checkClass = CreateEstateUtils()
        val city = "NEW YORK"
        val postalCode = "NY 10007"
        val addressToSend = Address(0, "75 PARK PLACE 8TH FLOOR", "",
            25, "", "", "United States", 0.0, 0.0, "")
        val addressToCompare = "75 PARK PLACE 8TH FLOOR+NEW YORKNY 10007"
        assertEquals(addressToCompare, checkClass.checksAddressElements(addressToSend, city, postalCode))
    }

    @Test
    fun makeQueryTest(){
        val queryToCompare = "SELECT * FROM Property WHERE type = 'Manor' AND livingSpace >= 35 AND livingSpace <= 935 " +
                "AND rooms >= 3 AND rooms <= 38 AND city = 'Livernon' AND postalCode = '46320' " +
                "AND country = 'France' AND school = 1 AND status = 1 AND price >= 238000 AND price <= 3357000"
        val searchUtils = SearchUtils()
        assertEquals(queryToCompare, searchUtils.makeQuery("Manor", 35, 935,
            3, 38, "Livernon", "46320", "France",
            shops = false,
            airport = false,
            park = false,
            subway = false,
            school = true,
            trainStation = false,
            sold = false,
            available = true,
            priceMin = 238000.0,
            priceMax = 3357000.0,
            bedRoomsMin = 0,
            bedRoomsMax = 0,
            entryDate = "",
            maxDate = "",
            realtorName = "",
            numberOfBath = 0,
            numberOfImages = 0
        ))
    }

    @Test
    fun getPropertyIdTest(){
        val propertyList = arrayListOf<Property>()
        Assert.assertNotNull(Utils.getPropertyId(propertyList))
    }

    @Test
    fun getPropertyPositionTest(){
        val propertyList = arrayListOf<Property>()
        Assert.assertNotNull(Utils.getPropertyPosition(propertyList))
    }
}
