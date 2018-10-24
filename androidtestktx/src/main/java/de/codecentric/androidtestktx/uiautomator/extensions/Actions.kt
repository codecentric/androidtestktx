package de.codecentric.androidtestktx.uiautomator.extensions

import android.Manifest.permission
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.Until
import de.codecentric.androidtestktx.common.appContext
import de.codecentric.androidtestktx.common.instrumentation
import de.codecentric.androidtestktx.common.originalPackageName
import de.codecentric.androidtestktx.common.seconds

@Suppress("ClassName")
object click

@Suppress("ClassName")
object typeText {
  lateinit var text: String
  operator fun invoke(text: String): typeText {
    this.text = text
    return this
  }
}

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
   *  |    x    |
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
  drag(middleOfTheScreenHorizontally, middleOfTheScreenVertically, middleOfTheScreenHorizontally, displayHeight, 100)
}

/**
 * Customized [UiObject.swipeLeft] function to look a bit cleaner in code
 */
fun UiObject.leftSwipe(steps: Int = 100) {
  this.swipeLeft(steps)
}

/**
 * Simulates long click, the higher steps, longer it takes
 * For 100 steps, the swipe will take about 1/2 second to complete
 */
fun UiObject.hold(steps: Int = 100) {
  val centerX = this.bounds.centerX()
  val centerY = this.bounds.centerY()
  device.swipe(centerX, centerY, centerX, centerY, steps)
}

/**
 * Moves finger 15% from the top of the screen to the bottom of the screen, this simulates
 * scroll to the top of the screen. We're using this value of 15% because we must add toolbar height
 * to our equation.
 */
fun UiDevice.scrollToTheTopOfScreen() {
  val verticalAxisStart = (displayHeight * 0.15).toInt()
  drag(0, verticalAxisStart, 0, displayHeight, 10)
}

fun UiDevice.swipeDown() {
  val middleOfTheScreenHorizontally = displayWidth / 2
  val middleOfTheScreenVertically = displayHeight / 2
  swipe(middleOfTheScreenHorizontally, middleOfTheScreenVertically, middleOfTheScreenHorizontally, displayHeight, 100)
}

/**
 * Scrolling the content on the device until the object returned from [uiObjectFun] becomes visible or
 * scroll takes more than 5 scrolls.
 */
infix fun UiDevice.scrollUntilVisible(uiObjectFun: () -> UiObject) {
  val uiObject = uiObjectFun()
  var counter = 5
  if (!uiObject.exists()) {
    do {
      scrollUp()
      --counter
    } while (!uiObject.exists() && counter > 0)
  }
}

/**
 * Trying to emulate press and hold by executing the drag vertically inside UIObject bounds
 */
fun UiObject.pressAndHold(durationMillis: Long = 2.seconds) {
  device.dragVertically(bounds, durationMillis)
}

/**
 * Trying to emulate press and hold in vertically scrollable view by executing the horizontal drag inside UIObject bounds.
 */
fun UiObject.pressAndHoldScrollable(durationMillis: Long = 2.seconds) {
  device.dragHorizontally(bounds, durationMillis)
}

/**
 * Function waits to find and element with unexisting text. Effectively, function just wait to timeout.
 */
infix fun UiDevice.waitFor(milliseconds: Long) {
  viewByText { "xyzabc" }.waitForExists(milliseconds)
}

/**
 * Waiting until the UIObject is removed from the screen or [timeOutMillis] timeout
 */
infix fun UiObject.waitToBeRemovedFor(timeOutMillis: Int) {
  waitUntilGone(timeOutMillis.toLong())
}

/**
 * Waiting until the object according to [bySelector] selection is found. Wait timeout after 10 seconds.
 */
infix fun UiDevice.waitUntil(bySelector: BySelector) {
  wait(Until.hasObject(bySelector), 10000)
}

/**
 * Waiting for UIObject to become visible for 5 seconds.
 */
fun UiObject.waitToBecomeVisible() {
  waitToBecomeVisible(5.seconds)
}

/**
 * Waiting for UIObject to become visible. Wait timeout in [timeOutMillis]
 */
infix fun UiObject.waitToBecomeVisible(timeOutMillis: Long) {
  if (!waitForExists(timeOutMillis)) {
    throw UiObjectNotFoundException("Timeout: ${timeOutMillis.toDouble() / 1000}s. ${this.selector}")
  }
}

/**
 * Waits for the object to become visible.
 *
 * @return TRUE if the object is found, FALSE otherwise.
 */
fun UiObject.waitToBecomeVisibleOtherwiseContinue(timeOutMillis: Long = 5.seconds) = waitForExists(timeOutMillis)

/**
 * Executes a number of Actions in order to open the device Settings screen and disables
 * all permissions on a device level for this app.
 *
 * Function assumes that the device has a English localization.
 */
fun denyAppPermissions() {
  revokePermissionCommand(permission.ACCESS_FINE_LOCATION)
  revokePermissionCommand(permission.WRITE_EXTERNAL_STORAGE)
  revokePermissionCommand(permission.GET_ACCOUNTS)
}

fun revokePermissionCommand(permission: String) =
  execShell("pm revoke ${appContext.originalPackageName} $permission")

fun execShell(command: String) {
  instrumentation.uiAutomation.executeShellCommand(command)
}