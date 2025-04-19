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

@RunWith(AndroidJUnit4::class)
@LargeTest
class AlbumListTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun listAlbums() {
        // Ir al listado de albumers
        val albumNavigationButton = onView(withId(R.id.navigation_album))
        albumNavigationButton.perform(click())

        Thread.sleep(1500)

        val albumView = onView(withId(R.id.albums_recycler_view))

        // Verificar que la pantalla cargue sin problemas
        albumView.check(matches(isDisplayed()))

        // Verificamos que exista al menos un album cargado
        albumView.check(matches(hasDescendant(withId(R.id.album_cover_image))))
    }
}