package de.codecentric.androidtestktx.espresso.extensions

import android.content.Intent
import android.support.annotation.IdRes
import android.support.test.espresso.DataInteraction
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers

fun DataInteraction.isSelected(): ViewInteraction = check(ViewAssertions.matches(ViewMatchers.isSelected()))

fun isDisplayed(): ViewAssertion = ViewAssertions.matches(ViewMatchers.isDisplayed())

fun isNotDisplayed(): ViewAssertion = ViewAssertions.doesNotExist()

fun isEnabled(): ViewAssertion = ViewAssertions.matches(ViewMatchers.isEnabled())

fun isDisabled(): ViewAssertion = ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled()))

fun isChildOf(@IdRes parentId: Int) = ViewAssertions.matches(
  ViewMatchers.withParent(
    viewById(parentId)
  )
)

fun isChildOf(parent: Matcher<View>) = ViewAssertions.matches(ViewMatchers.withParent(parent))

fun isDescendantOf(parent: Matcher<View>): ViewAssertion = ViewAssertions.matches(ViewMatchers.isDescendantOfA(parent))

fun Matcher<Intent>.isDisplayed() {
  Intents.intended(this)
}