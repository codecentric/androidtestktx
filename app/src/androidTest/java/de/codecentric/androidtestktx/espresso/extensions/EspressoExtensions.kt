package de.codecentric.androidtestktx.espresso.extensions

import android.content.Intent
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import org.hamcrest.Matcher
import kotlin.reflect.KClass

fun <T : Any> activityByClass(clazz: KClass<T>): Matcher<Intent> = IntentMatchers.hasComponent(clazz.java.name)

/**
 * Assertion DSL
 */
infix fun Matcher<View>.verifySelectionByTextTo(textMatcher: () -> Matcher<Any>) =
  onData(textMatcher()).inAdapterView(this).isSelected()

infix fun Matcher<View>.verifyThat(func: () -> ViewAssertion) {
  onView(this).check(func())
}

infix fun Matcher<View>.verifyText(textFn: () -> String) {
  verifyThat { ViewAssertions.matches(withText(textFn())) }
}