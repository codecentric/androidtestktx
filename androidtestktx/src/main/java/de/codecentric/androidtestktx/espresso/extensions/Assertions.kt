package de.codecentric.androidtestktx.espresso.extensions

import android.content.Intent
import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.DataInteraction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers


/**
 * [DataInteraction] extension function which matches whether the matched item is selected.
 */
fun DataInteraction.isSelected(): ViewInteraction = check(ViewAssertions.matches(ViewMatchers.isSelected()))

/**
 * [ViewMatchers.isDisplayed] sugar.
 */
fun itIsDisplayed(): ViewAssertion = ViewAssertions.matches(ViewMatchers.isDisplayed())

/**
 * [ViewAssertions.doesNotExist] sugar.
 */
fun isNotDisplayed(): ViewAssertion = ViewAssertions.doesNotExist()

/**
 * [ViewMatchers.isEnabled] sugar.
 */
fun isEnabled(): ViewAssertion = ViewAssertions.matches(ViewMatchers.isEnabled())

/**
 * [ViewMatchers.isEnabled] negation sugar.
 */
fun isDisabled(): ViewAssertion = ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled()))

/**
 *  [ViewMatchers.withParent] sugar.
 *
 *  @param parentId Id of the parent view we would like to check against.
 */
fun isChildOf(@IdRes parentId: Int): ViewAssertion =
  isChildOf(viewById(parentId))

/**
 *  [ViewMatchers.withParent] sugar.
 *
 *  @param parent Matcher
 */
fun isChildOf(parent: Matcher<View>): ViewAssertion = ViewAssertions.matches(ViewMatchers.withParent(parent))

/**
 *  [ViewMatchers.isDescendantOfA] sugar. Descendant does not have to be a direct child.
 *
 *  @param parent Matcher
 */
fun isDescendantOf(parent: Matcher<View>): ViewAssertion = ViewAssertions.matches(ViewMatchers.isDescendantOfA(parent))

/**
 * Extension function on a current [Matcher] which checks the Intent of current matcher.
 */
fun Matcher<Intent>.itIsDisplayed() {
  Intents.intended(this)
}