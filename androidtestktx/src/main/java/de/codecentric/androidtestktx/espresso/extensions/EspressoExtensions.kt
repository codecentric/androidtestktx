package de.codecentric.androidtestktx.espresso.extensions

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import kotlin.reflect.KClass

/**
 * Asserts whether the ListView with [textMatcher] is selected for a current adapter [Matcher]
 */
infix fun Matcher<View>.verifySelectionByTextTo(textMatcher: () -> Matcher<Any>) =
  onData(textMatcher()).inAdapterView(this).isSelected()

/**
 * Asserts the view with current [Matcher] with provided [ViewAssertion] from lambda.
 */
infix fun Matcher<View>.verifyThat(func: () -> ViewAssertion) {
  onView(this).check(func())
}

/**
 * Asserts the intent with current KClass with provided [Matcher] from lambda
 */
infix fun <T : Activity> KClass<T>.verifyThat(fn: Matcher<Intent>.() -> Unit) {
  fn(IntentMatchers.hasComponent(this.java.name))
}

/**
 * Asserts whether the view with current [Matcher] matches the text returned from a [textFn] lambda
 */
infix fun Matcher<View>.verifyText(textFn: () -> String) {
  verifyThat { ViewAssertions.matches(withText(textFn())) }
}