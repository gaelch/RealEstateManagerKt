package com.cheyrouse.gael.realestatemanagerkt

import com.cheyrouse.gael.realestatemanagerkt.controllers.fragments.MortGageCalculatorFragment
import com.cheyrouse.gael.realestatemanagerkt.models.Picture
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.TEXT_DATE
import com.cheyrouse.gael.realestatemanagerkt.utils.Converters
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
        val dateStr = "04-04-2020"
        assertEquals(dateStr, Utils.getStringDate(2020, 4, 3))
    }

    @Test
    fun checkConvertersList(){
        val picture = Picture(0, "dressing", "content://media/external/images/media/5620")
        val picture2 = Picture(1, "room1", "content://media/external/images/media/5622")
        val listObj = listOf<Picture>(picture, picture2)
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
}
