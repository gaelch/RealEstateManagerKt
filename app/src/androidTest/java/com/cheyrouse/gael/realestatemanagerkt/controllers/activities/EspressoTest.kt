package com.cheyrouse.gael.realestatemanagerkt.controllers.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.cheyrouse.gael.realestatemanagerkt.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class EspressoTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(PreviewActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_PHONE_STATE"
        )

    @Test
    fun espressoTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(800)

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

        val appCompatButton = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.buttonPanel),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton.perform(scrollTo(), click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

        val appCompatEditText = onView(
            allOf(
                withId(R.id.edit_surface),
                childAtPosition(
                    allOf(
                        withId(R.id.constraint_detail),
                        childAtPosition(
                            withId(R.id.scrollView),
                            0
                        )
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("500"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.edit_nbr_rooms),
                childAtPosition(
                    allOf(
                        withId(R.id.constraint_detail),
                        childAtPosition(
                            withId(R.id.scrollView),
                            0
                        )
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("10"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.edit_nbr_bed),
                childAtPosition(
                    allOf(
                        withId(R.id.constraint_detail),
                        childAtPosition(
                            withId(R.id.scrollView),
                            0
                        )
                    ),
                    11
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("8"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.edit_nbr_bath),
                childAtPosition(
                    allOf(
                        withId(R.id.constraint_detail),
                        childAtPosition(
                            withId(R.id.scrollView),
                            0
                        )
                    ),
                    13
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("5"), closeSoftKeyboard())

        val appCompatTextView = onView(
            allOf(
                withId(R.id.picker_entry_date), withText("01-04-2020"),
                childAtPosition(
                    allOf(
                        withId(R.id.constraint_detail),
                        childAtPosition(
                            withId(R.id.scrollView),
                            0
                        )
                    ),
                    49
                ),
                isDisplayed()
            )
        )
        appCompatTextView.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
