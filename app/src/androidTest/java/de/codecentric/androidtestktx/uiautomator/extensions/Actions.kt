package de.codecentric.androidtestktx.uiautomator.extensions

import android.support.test.uiautomator.BySelector
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiObject
import android.support.test.uiautomator.Until
import de.codecentric.androidtestktx.seconds

/**
 * Moves the finger from the center of the screen to the top
 */
fun UiDevice.scrollUpALittleBit() {
  scrollUp(30)
}

fun UiDevice.scrollUp(percent: Int = 100) {
  val middleOfTheScreenHorizontally = displayWidth / 2
  val middleOfTheScreenVertically = displayHeight / 2
  val endY = middleOfTheScreenVertically - (middleOfTheScreenVertically * (percent.toDouble() / 100.00)).toInt()

  /**
   * some devices have emulated navigation buttons at the bottom of the screen, so we'll move start scroll (Y) to be
   * 10% of screen height above the bottom of the screen
   *   _________
   *  |         |
   *  |         |
   *  |    ^    |
   *  |    ^    |
   *  |    ^    |
   *  |    O    |
   *  |_________|
   *  |_________|
   */
  val startY = (middleOfTheScreenVertically - middleOfTheScreenVertically * 0.1).toInt()
  drag(middleOfTheScreenHorizontally, startY, middleOfTheScreenHorizontally, endY, 100)
}

/**
 * Moves the finger from the center of the screen to the bottom
 */
fun UiDevice.scrollDown() {
  val middleOfTheScreenHorizontally = displayWidth / 2
  val middleOfTheScreenVertically = displayHeight / 2
  drag(middleOfTheScreenHorizontally, middleOfTheScreenVertically, middleOfTheScreenHorizontally, displayHeight, 10)
}

infix fun UiDevice.scrollUntilVisible(uiObjectFun: () -> UiObject) {
  val uiObject = uiObjectFun()
  var counter = 5
  if (!uiObject.exists()) {
    do {
      scrollUp()
      --counter
    } while (!uiObject.exists() || counter == 0)
  }
}

fun UiObject.pressAndHold(durationMillis: Long = 2.seconds) {
  device.dragVertically(bounds, durationMillis)
}

fun UiObject.pressAndHoldScrollable(durationMillis: Long = 2.seconds) {
  device.dragHorizontally(bounds, durationMillis)
}

/**
 * Function waits to find and element with unexisting text. Effectively, function just wait to timeout.
 */
infix fun UiDevice.waitFor(milliseconds: Long) {
  viewByText { "xyzabc" }.waitForExists(milliseconds)
}

infix fun UiObject.waitToBeRemovedFor(milliseconds: Int) {
  waitUntilGone(milliseconds.toLong())
}

infix fun UiDevice.waitUntil(bySelector: BySelector) {
  wait(Until.hasObject(bySelector), 100000)
}

/**
 * Waiting for UIObject to become visible for 5 seconds.
 */
fun UiObject.waitToBecomeVisible() {
  waitToBecomeVisible(5.seconds)
}

infix fun UiObject.waitToBecomeVisible(milliseconds: Long) {
  waitForExists(milliseconds)
}