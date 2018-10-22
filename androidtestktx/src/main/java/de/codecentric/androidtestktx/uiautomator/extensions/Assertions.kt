package de.codecentric.androidtestktx.uiautomator.extensions

import androidx.test.uiautomator.UiObject
import de.codecentric.androidtestktx.common.assertFalse
import de.codecentric.androidtestktx.common.assertTrue


fun UiObject.isVisibleFor(milliseconds: Int) {
  device.waitFor(1000)
}

/**
 * Asserts whether the UIObject is displayed.
 */
fun UiObject.itIsDisplayed() {
  assertTrue(exists())
}

/**
 * Asserts whether the UIObject is not displayed on the screen
 */
fun UiObject.itIsNotDisplayed() {
  assertFalse(exists())
}

/**
 * Asserts whether the UIObject is enabled
 */
fun UiObject.itIsEnabled() {
  assertTrue(isEnabled)
}

/**
 * Asserts whether the UIObject is disabled
 */
fun UiObject.itIsNotEnabled() {
  assertFalse(isEnabled)
}

/**
 * Asserts whether the UIObject is checked
 */
fun UiObject.itIsChecked() {
  assertTrue(isChecked)
}

/**
 * Asserts whether the UIObject is unchecked
 */
fun UiObject.itIsUnchecked() {
  assertFalse(isChecked)
}