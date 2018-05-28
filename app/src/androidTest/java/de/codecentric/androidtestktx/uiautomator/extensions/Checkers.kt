package de.codecentric.androidtestktx.uiautomator.extensions

import android.support.test.uiautomator.UiObject
import de.codecentric.androidtestktx.assertFalse
import de.codecentric.androidtestktx.assertTrue

//TODO Write a function for isSelected


fun UiObject.isVisibleFor(milliseconds: Int) {
  device.waitFor(1000)
}

fun UiObject.itIsDisplayed() {
  assertTrue(exists())
}

fun UiObject.itIsNotDisplayed() {
  assertFalse(exists())
}

fun UiObject.itIsEnabled() {
  assertTrue(isEnabled)
}

fun UiObject.itIsNotEnabled() {
  assertFalse(isEnabled)
}

fun UiObject.itIsChecked() {
  assertTrue(isChecked)
}

fun UiObject.itIsUnchecked() {
  assertFalse(isChecked)
}