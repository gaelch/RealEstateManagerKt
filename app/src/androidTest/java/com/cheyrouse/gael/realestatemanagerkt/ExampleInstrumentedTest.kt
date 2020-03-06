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
import com.cheyrouse.gael.realestatemanagerkt.models.GeocodeInfo
import com.cheyrouse.gael.realestatemanagerkt.models.Property
import com.cheyrouse.gael.realestatemanagerkt.utils.RealEstateStream
import com.cheyrouse.gael.realestatemanagerkt.utils.Utils
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.security.AccessController.getContext


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)

class ExampleInstrumentedTest {

    // FOR DATA
    private var mContentResolver: ContentResolver? = null
    val AUTHORITY = "com.gael.openclassrooms.realestatemanager.provider"
    val TABLE_NAME = Property::class.java.simpleName
    val URI_PROPERTY = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    // DATA SET FOR TEST
    private val USER_ID: Long = 1

    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder<RealEstateDatabase>(
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
            ContentUris.withAppendedId(URI_PROPERTY, USER_ID), null, null, null, null
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
    private val address = "21 Avenue Jean Jaurès, 46100 Figeac France"


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
}
