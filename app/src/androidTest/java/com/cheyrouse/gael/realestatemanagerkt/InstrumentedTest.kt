package com.cheyrouse.gael.realestatemanagerkt

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cheyrouse.gael.realestatemanagerkt.database.RealEstateDatabase
import com.cheyrouse.gael.realestatemanagerkt.models.Address
import com.cheyrouse.gael.realestatemanagerkt.models.GeocodeInfo
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.*
import io.reactivex.observers.TestObserver
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)

class InstrumentedTest {

    // FOR DATA
    private var mContentResolver: ContentResolver? = null
    private val authority = "com.gael.openclassrooms.realestatemanager.provider"
    private val tableName = Property::class.java.simpleName
    private val uriProperty: Uri = Uri.parse("content://$authority/$tableName")

    // DATA SET FOR TEST
    private val userId: Long = 1

    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().context,
                RealEstateDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @Test
    fun getItemsWhenNoItemInserted() {
        val cursor: Cursor? = mContentResolver!!.query(
            ContentUris.withAppendedId(uriProperty, userId), null, null, null, null
        ).also {
            assertThat(it, notNullValue())
        }
        assertThat(cursor?.count, `is`(notNullValue()))
        cursor?.close()
    }

    @Test
    fun checkIfInternetIsAvailable() {
        assertEquals(true, Utils.isInternetAvailable(InstrumentationRegistry.getInstrumentation().context))
    }

    private val apiKey = BuildConfig.GoogleSecAPIKEY
    private val address = "4 Avenue Jean Jaurès, 46100 Figeac France"

    @Test
    fun streamGeocodeInfoTest(){
        val geocodeObservable = RealEstateStream().streamFetchGeocodeInfo(address,apiKey)
        val testObserver = TestObserver<GeocodeInfo>()
        geocodeObservable.subscribeWith(testObserver)
            .assertNoErrors()
            .assertNoTimeout()
            .awaitTerminalEvent()
        val geocodeInfo = testObserver.values()[0]
        assertEquals("OK", geocodeInfo.status)
    }

    @Test
    fun checkValueBeforeStorePropertyTest(){
        val address = Address(0, "75 PARK PLACE 8TH FLOOR   ", "",
            2, "NEW YORK", "NY 10007", "United States",
            40.7808, -73.9772, "")
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
            pictures = emptyList()
        )

        val checkClass = CreateEstateUtils()
        val propertyToReturn: Property
        propertyToReturn = checkClass.checkValueBeforeStoreProperty(InstrumentationRegistry.getInstrumentation().context, "Manor", 300,
            9, 4, 2, address,   200000.0,"Julien",
            "16-03-2020","", true, propertyToSend, "NEW YORK","NY 10007", "United States")
        Assert.assertEquals(propertyToSend, propertyToReturn)
    }

    @Test
    fun getPropertyIdTest(){
        val propertyList = arrayListOf<Property>()
        Assert.assertNotNull(Utils.getPropertyId(InstrumentationRegistry.getInstrumentation().context, propertyList))
    }

    @Test
    fun getPropertyPositionTest(){
        val propertyList = arrayListOf<Property>()
        Assert.assertNotNull(Utils.getPropertyPosition(InstrumentationRegistry.getInstrumentation().context, propertyList))
    }
}
