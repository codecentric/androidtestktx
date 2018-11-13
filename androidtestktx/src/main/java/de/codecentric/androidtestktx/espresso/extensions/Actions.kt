package de.codecentric.androidtestktx.espresso.extensions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import de.codecentric.androidtestktx.common.seconds
import org.hamcrest.Matcher
import org.hamcrest.Matchers.instanceOf
import kotlin.reflect.KClass

/**
 * Extension function for performing a [ViewActions.click] on current [Matcher].
 */
fun Matcher<View>.click(): ViewInteraction = Espresso.onView(this).perform(ViewActions.click())

/**
 * Sugar syntax for [ViewActions.click]
 */
val click: ViewAction get() = ViewActions.click()

/**
 * Extension function for performing a [ViewActions.longClick] on current [Matcher].
 */
fun Matcher<View>.longClick(): ViewInteraction = Espresso.onView(this).perform(ViewActions.longClick())

/**
 * Extension function for performing a [ViewActions.typeText] on current [Matcher]
 */
fun Matcher<View>.typeText(textToInput: String): ViewInteraction =
  Espresso.onView(this).perform(ViewActions.typeText(textToInput))

/**
 * Sugar syntax for [ViewActions.typeText]
 */
fun typeText(text: String): ViewAction = ViewActions.typeText(text)

/**
 * Extension function for performing a [ViewActions.clearText] on current [Matcher]
 */
fun Matcher<View>.clearText(): ViewInteraction = Espresso.onView(this).perform(ViewActions.clearText())

/**
 * Sugar syntax for [ViewActions.clearText]
 */
val clearText: ViewAction = ViewActions.clearText()

/**
 * Extension function for performing a [ViewActions.replaceText] on current [Matcher]
 */
fun Matcher<View>.replaceText(textToInput: String): ViewInteraction =
    Espresso.onView(this).perform(ViewActions.replaceText(textToInput))

/**
 * Sugar syntax for [ViewActions.replaceText]
 */
fun replaceText(textToInput: String): ViewAction = ViewActions.replaceText(textToInput)

/**
 * Extension function for performing a [ViewActions.swipeUp] on current [Matcher].
 */
fun Matcher<View>.scroll(): ViewInteraction = Espresso.onView(this).perform(ViewActions.swipeUp())

/**
 * Extension function for performing a scrollUp on a current [Matcher].
 * Scrolling is implemented by repeating a [ViewActions.swipeUp] for a 20 times.
 */
fun Matcher<View>.scrollUp() {
  var swipes = 0
  do {
    Thread.sleep(500)
    Espresso.onView(this).perform(ViewActions.swipeUp())
    swipes++
  } while (swipes < 30)
}

/**
 * Extension function for selection of ListView item on a current [Matcher].
 *
 * @param textMatcher matcher for a specific list item.
 */
infix fun Matcher<View>.selectListItemByText(textMatcher: () -> Matcher<out Any>): ViewInteraction =
  Espresso.onData(textMatcher()).inAdapterView(this).perform(ViewActions.click())

/**
 * Effectively sleeps a current [Thread] for [sleepDuration] in milliseconds.
 */
fun waitFor(sleepDuration: Long = 5.seconds) {
  Thread.sleep(sleepDuration)
}

/**
 * Extension function which scrolls a currently matched RecyclerView.
 */
fun <VH : RecyclerView.ViewHolder> Matcher<View>.scrollToPosition(position: Int) {
  onView(this).perform(RecyclerViewActions.scrollToPosition<VH>(position))
}

/**
 * Extension function which clicks on ViewHolder at the specific [position] on a currently matched RecyclerView.
 *
 * @param position of the item in the [RecyclerView]
 */
fun <VH : RecyclerView.ViewHolder> Matcher<View>.clickOnPosition(position: Int) {
  onView(this).perform(RecyclerViewActions.actionOnItemAtPosition<VH>(position, ViewActions.click()))
}

/**
 * Function which clicks at the specific [position] inside generic View's adapter.
 *
 * @param position of the item in the generic type adapter
 * @param className is a class name of items in adapter
 * @param adapterViewId is used when we have multiple adapters with objects of same type on one screen, and we need
 *                      to differentiate those views. It's actually id of a view which contains adapter.
 */
fun <T : Any> Matcher<View>.clickAtPosition(position: Int, className: KClass<T>, adapterViewId: Int) {
  onData(instanceOf(className.java)).inAdapterView(withId(adapterViewId)).atPosition(position)
    ?.let { it.perform(ViewActions.click()) } ?: throw Exception("DataInteraction not found.")
}