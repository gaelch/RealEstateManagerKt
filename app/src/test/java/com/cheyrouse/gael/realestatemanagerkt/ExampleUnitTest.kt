package com.cheyrouse.gael.realestatemanagerkt

import com.cheyrouse.gael.realestatemanagerkt.utils.Utils
import org.junit.Test

import org.junit.Assert.*

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
}
