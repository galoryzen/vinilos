package com.example.vinilos

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Matchers.allOf

@RunWith(AndroidJUnit4::class)
@LargeTest
class CollectorTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    private val firstCollectorName = "Juan Perez"
    private val firstCollectorEmail = "juan.perez@example.com"
    private val waitToLoadTime: Long = 1500L

    @Test
    fun listCollectors() {
        
        // Arrange
        val collectorNavigationButton = onView(withId(R.id.navigation_collector))

        // Action
        collectorNavigationButton.perform(click())
        Thread.sleep(waitToLoadTime)
        val collectorView = onView(withId(R.id.collectors_recycler_view))

        // Assert
        collectorView.check(matches(isDisplayed()))
        collectorView.check(matches(hasDescendant(withId(R.id.collector_name_text))))
        collectorView.check(matches(hasDescendant(withId(R.id.collector_email_text))))
    }
}