package com.daimler.mbpro.android.core.espresso.extensions

import android.view.View

/**
 * Created by bajicdusko on 12/02/2018.
 */

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