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
class ArtistTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    private val firstArtistName = "Queen"
    private val waitToLoadTime: Long = 1500L

    @Test
    fun listArtists() {
        onView(withId(R.id.button_visitor)).perform(click())
        Thread.sleep(waitToLoadTime)

        // Arrange
        val artistNavigationButton = onView(withId(R.id.navigation_artist))

        // Action
        artistNavigationButton.perform(click())
        Thread.sleep(waitToLoadTime)
        val artistView = onView(withId(R.id.artists_recycler_view))

        // Assert
        artistView.check(matches(isDisplayed()))
        artistView.check(matches(hasDescendant(withId(R.id.artist_cover_image))))
        artistView.check(matches(hasDescendant(allOf(
            withId(R.id.artist_name_text),
            withText(firstArtistName)
        ))))
    }

    @Test
    fun detailArtist() {
        onView(withId(R.id.button_visitor)).perform(click())
        Thread.sleep(waitToLoadTime)

        // Ir al listado de artistas
        val artistNavigationButton = onView(withId(R.id.navigation_artist))
        artistNavigationButton.perform(click())

        Thread.sleep(waitToLoadTime)

        val artistView = onView(withId(R.id.artists_recycler_view))

        // Verificar que la pantalla cargue sin problemas
        artistView.check(matches(isDisplayed()))

        // Verificamos que exista al menos un artista cargado
        artistView.check(matches(hasDescendant(withId(R.id.artist_cover_image))))

        // Multiples artistas son cargados (por ende, comparten ID), obten todos y usa el primero
        onView(allOf(withId(R.id.artist_name_text), withText(firstArtistName))).perform(
            click()
        )

        Thread.sleep(waitToLoadTime) // Esperar que cargue la vista de detalle

        // Verificar que la vista de detalle muestre el nombre del artista
        onView(withId(R.id.detail_artist_title))
            .check(matches(withText(firstArtistName)))

        // Verificar que la descripcion y el cover estan visibiles
        onView(withId(R.id.detail_artist_description)).check(matches(isDisplayed()))
        onView(withId(R.id.detail_artist_cover)).check(matches(isDisplayed()))
    }
}