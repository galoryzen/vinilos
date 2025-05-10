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
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.containsString

@RunWith(AndroidJUnit4::class)
@LargeTest
class CollectorTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    private val firstCollectorName = "Manolo Bellon"
    private val firstCollectorEmail = "manollo@caracol.com.co"
    private val firstCollectorPhone = "3502457896"
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

    @Test
    fun detailCollector() {
        // Obtener el boton del menu de navegacion de coleccionistas
        val collectorNavigationButton = onView(withId(R.id.navigation_collector))

        // Presionar el boton y espear a que carga la vista de los coleccionistas
        collectorNavigationButton.perform(click())
        Thread.sleep(waitToLoadTime)
        val collectorView = onView(withId(R.id.collectors_recycler_view))

        // Validar que exista el listado
        collectorView.check(matches(isDisplayed()))
        collectorView.check(matches(hasDescendant(withId(R.id.collector_name_text))))

        // Multiples coleccionistas son cargados (por ende, comparten ID), obten todos y usa el primero
        onView(allOf(withId(R.id.collector_name_text), withText(firstCollectorName))).perform(
            click()
        )

        Thread.sleep(waitToLoadTime) // Esperar que cargue la vista de detalle

        // Verificar que la vista de detalle muestre el nombre del coleccionista
        onView(withId(R.id.detail_collector_name))
            .check(matches(withText(firstCollectorName)))

        onView(withId(R.id.detail_collector_email))
            .check(matches(withText(containsString(firstCollectorEmail))))

        onView(withId(R.id.detail_collector_phone))
            .check(matches(withText(containsString(firstCollectorPhone))))

        val albumsView = onView(withId(R.id.detail_collector_albums_recycled_view))
        albumsView.check(matches(hasDescendant(withId(R.id.album_name_text))))

    }
}