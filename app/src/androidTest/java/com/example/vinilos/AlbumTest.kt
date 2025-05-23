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
class AlbumTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    private val firstAlbumName = "Buscando América"
    private val waitToLoadTime: Long = 1500L

    @Test
    fun listAlbums() {
        onView(withId(R.id.button_visitor)).perform(click())
        Thread.sleep(waitToLoadTime)

        // Ir al listado de albums
        val albumNavigationButton = onView(withId(R.id.navigation_album))
        albumNavigationButton.perform(click())

        Thread.sleep(waitToLoadTime)

        val albumView = onView(withId(R.id.albums_recycler_view))

        // Verificar que la pantalla cargue sin problemas
        albumView.check(matches(isDisplayed()))

        // Verificamos que exista al menos un album cargado
        albumView.check(matches(hasDescendant(withId(R.id.album_cover_image))))
    }

    @Test
    fun detailAlbum() {
        onView(withId(R.id.button_visitor)).perform(click())
        Thread.sleep(waitToLoadTime)

        // Ir al listado de albums
        val albumNavigationButton = onView(withId(R.id.navigation_album))
        albumNavigationButton.perform(click())

        Thread.sleep(waitToLoadTime)

        val albumView = onView(withId(R.id.albums_recycler_view))

        // Verificar que la pantalla cargue sin problemas
        albumView.check(matches(isDisplayed()))

        // Verificamos que exista al menos un album cargado
        albumView.check(matches(hasDescendant(withId(R.id.album_cover_image))))

        // Multiples albumes son cargados (por ende, comparten ID), obten todos y usa el primero
        onView(allOf(withId(R.id.album_name_text), withText(firstAlbumName))).perform(
            click()
        )

        Thread.sleep(waitToLoadTime) // Esperar que cargue la vista de detalle

        // Verificar que la vista de detalle muestre el nombre del álbum
        onView(withId(R.id.detail_album_title))
            .check(matches(withText(firstAlbumName)))

        // Verificar que la descripcion y el cover estan visibiles
        onView(withId(R.id.detail_album_description)).check(matches(isDisplayed()))
        onView(withId(R.id.detail_album_cover)).check(matches(isDisplayed()))
    }
}