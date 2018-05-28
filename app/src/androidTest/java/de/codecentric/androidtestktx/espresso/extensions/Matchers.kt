package de.codecentric.androidtestktx.espresso.extensions

import android.support.annotation.StringRes
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import de.codecentric.androidtestktx.stringOf
import de.codecentric.androidtestktx.targetContext
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

fun viewById(viewId: Int): Matcher<View> = ViewMatchers.withId(viewId)

fun viewByText(@StringRes stringResId: Int): Matcher<View> = viewByText(
  targetContext stringOf stringResId
)

fun viewByText(text: String): Matcher<View> = ViewMatchers.withText(text)

fun firstVisibleItemById(viewId: Int): Matcher<View> = firstVisibleItem(viewById(viewId))
fun firstVisibleItemByText(text: String): Matcher<View> = firstVisibleItem(viewByText(text))

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