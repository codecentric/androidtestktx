package de.codecentric.androidtestktx.espresso.extensions

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Matcher

fun Matcher<View>.click(): ViewInteraction = Espresso.onView(this).perform(ViewActions.click())

fun Matcher<View>.longClick(): ViewInteraction = Espresso.onView(this).perform(ViewActions.longClick())

fun Matcher<View>.scroll(): ViewInteraction = Espresso.onView(this).perform(ViewActions.swipeUp())

fun Matcher<View>.scrollUp() {
  var swipes = 0
  do {
    Thread.sleep(500)
    Espresso.onView(this).perform(ViewActions.swipeUp())
    swipes++
  } while (swipes < 20)
}

infix fun Matcher<View>.selectListItemByText(textMatcher: () -> Matcher<Any>): ViewInteraction =
  Espresso.onData(textMatcher()).inAdapterView(this).perform(ViewActions.click())

fun waitFor(millis: Long) {
  Thread.sleep(millis)
}

fun <VH : RecyclerView.ViewHolder> Matcher<View>.scrollToPosition(position: Int) {
  onView(this).perform(RecyclerViewActions.scrollToPosition<VH>(position))
}

fun <VH : RecyclerView.ViewHolder> Matcher<View>.clickOnPosition(position: Int) {
  onView(this).perform(RecyclerViewActions.actionOnItemAtPosition<VH>(position, ViewActions.click()))
}