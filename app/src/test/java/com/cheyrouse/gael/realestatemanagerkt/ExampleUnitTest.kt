package com.cheyrouse.gael.realestatemanagerkt

import com.cheyrouse.gael.realestatemanagerkt.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun convertDollarToEuroTest(){
        assertEquals(8, Utils.convertDollarToEuro(10))
    }

    @Test
    fun convertEuroToDollarTest(){
        assertEquals(12, Utils.convertEuroToDollar(10))
    }

    @Test
    fun testTodayDateFormatter(){
        val dateFormat: DateFormat =
            SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val dateStr = dateFormat.format(Date())
        assertEquals(dateStr, Utils.getTodayDate())
    }

    @Test
    fun getStringDateTest(){
        val dateStr = "04-04-2020"
        assertEquals(dateStr, Utils.getStringDate(2020, 4, 3))
    }
}
