package de.codecentric.androidtestktx.espresso.extensions

import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.matcher.ViewMatchers
import de.codecentric.androidtestktx.common.appContext
import de.codecentric.androidtestktx.common.stringOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

/**
 * Returns a [Matcher] for a given [viewId]
 */
fun viewById(viewId: Int): Matcher<View> = ViewMatchers.withId(viewId)

/**
 * Returns a [Matcher] for a string value fetched from a resource for [stringResId]. Value returned from a resource
 * is localized on a currently running localization.
 */
fun viewByText(@StringRes stringResId: Int): Matcher<View> =
  viewByText(appContext stringOf stringResId)

/**
 * Returns a [Matcher] for a given [text]
 */
fun viewByText(text: String): Matcher<View> = ViewMatchers.withText(text)

/**
 * Returns a [Matcher] for a first visible item by id.
 */
fun firstVisibleItemById(viewId: Int): Matcher<View> = firstVisibleItem(
  viewById(viewId)
)

/**
 * Returns a [Matcher] for a first visible item by text.
 */
fun firstVisibleItemByText(text: String): Matcher<View> = firstVisibleItem(
  viewByText(text)
)

fun firstVisibleItem(matcher: Matcher<View>): Matcher<View> {
  return object : TypeSafeMatcher<View>() {

    var found = false

    override fun describeTo(description: Description?) {
      description?.appendText("Matcher first ($matcher).")
    }

    override fun matchesSafely(item: View?): Boolean {
      return if (!found && allOf(matcher, ViewMatchers.isCompletelyDisplayed()).matches(item)) {
        found = true
        true
      } else {
        false
      }
    }
  }
}