package de.codecentric.androidtestktx.uiautomator.extensions

import android.app.Instrumentation
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import de.codecentric.androidtestktx.common.appContext
import de.codecentric.androidtestktx.common.bottomPoint
import de.codecentric.androidtestktx.common.instrumentation
import de.codecentric.androidtestktx.common.leftPoint
import de.codecentric.androidtestktx.common.nameOf
import de.codecentric.androidtestktx.common.originalPackageName
import de.codecentric.androidtestktx.common.resource
import de.codecentric.androidtestktx.common.seconds
import de.codecentric.androidtestktx.common.stringOf
import de.codecentric.androidtestktx.common.toSeconds
import org.junit.Assert
import kotlin.reflect.KClass


/**
 * UIDevice property initialized by singleton [UiDevice.getInstance] method, for the running Instrumentation.
 */
val device: UiDevice get() = UiDevice.getInstance(instrumentation)

/**
 * Starts the app, without activity definition. Activity defined as default in AndroidManifest is started.
 */
fun Instrumentation.startApp() {
  val intent = context.packageManager.getLaunchIntentForPackage(targetContext.originalPackageName)
  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
  context.startActivity(intent)
}

/**
 * Selector used for finding the UIObject's by R.id value
 */
fun byIdSelector(viewId: Int): UiSelector = UiSelector().resourceId(resource nameOf viewId)

/**
 * Selector used for finding the UIObject's by text value fetched from the string resources.
 * Strings are resolved on currently active localization.
 */
fun byTextSelector(@StringRes stringResId: Int): UiSelector = UiSelector().text(appContext stringOf stringResId)

/**
 * Selector used for checking whether the app is shown
 */
fun appStarts(): BySelector = By.pkg(appContext.originalPackageName).depth(0)

/**
 * UIDevice extension function, which is getting the UIObject by utilizing the [byIdSelector] with id value provided by [viewId] lambda
 */
infix fun UiDevice.objectById(viewId: () -> Int): UiObject = findObject(
  byIdSelector(
    viewId()
  )
)

/**
 * UIDevice extension function, which is getting the UIObject by utilizing the [byTextSelector] with text value provided by [stringResId] lambda
 */
infix fun UiDevice.objectByTextResource(@IdRes stringResId: () -> Int): UiObject = findObject(
  byTextSelector(
    stringResId()
  )
)

/**
 * UIDevice extension function, which is getting the UIObject by selecting the component with text value provided by [text] lambda
 */
infix fun UiDevice.objectByText(text: () -> String): UiObject = findObject(UiSelector().text(text()))

/**
 * UIDevice extension function, which is getting the UIObject by selecting the component that contains text value provided by [text] lambda
 */
infix fun UiDevice.objectContainingText(text: () -> String): UiObject = findObject(UiSelector().textContains(text()))

/**
 * UIDevice extension function, which is getting the UIObject by selecting the component with [KClass] type provided by [kClass] lambda
 */
infix fun <K : View> UiDevice.objectByComponentType(kClass: () -> KClass<K>): UiObject =
  findObject(UiSelector().className(kClass().java))

/**
 * UIObject extension function, which is getting the descendant of current UIObject by utilizing the [byIdSelector] with [viewId] value.
 *
 * @throws UiObjectNotFoundException, if the descendant is not found.
 */
infix fun UiObject.descendantById(@IdRes viewId: Int): UiObject {
  var child: UiObject? = null
  if (childCount > 0) {
    child = getChild(byIdSelector(viewId))
    if (!child.exists()) {
      for (childIndex in 0 until childCount) {
        val indexedChild = getChild(UiSelector().index(childIndex))
        child = try {
          indexedChild.descendantById(viewId)
        } catch (e: UiObjectNotFoundException) {
          null
        }

        if (child != null) {
          break
        }
      }
    }
  }

  return if (child != null) {
    child
  } else {
    throw UiObjectNotFoundException("UiObject not found as descendant for id $viewId")
  }
}

/**
 * UIObject extension function which sugars the assertion.
 *
 */
infix fun UiObject.verifyThat(assertFun: (UiObject).() -> Unit) {
  assertFun(this)
}

/**
 * UIObject extension function which sugars the text assertions
 */
infix fun UiObject.verifyTextMatchingTo(assertTextFun: (UiObject).() -> String) {
  Assert.assertEquals(text, assertTextFun())
}

fun UiDevice.dragVertically(rect: Rect, durationMillis: Long = 2.seconds) {
  val length = rect.height()
  device.dragUp(rect.bottomPoint(), length, durationMillis)
}

fun UiDevice.dragHorizontally(rect: Rect, durationMillis: Long = 2.seconds) {
  val length = rect.width()
  device.dragRight(rect.leftPoint(), length, durationMillis)
}

fun UiDevice.dragUp(bottomPoint: Point, length: Int, durationMillis: Long = 2.seconds) {
  val stepsInOneSecond = 200
  val adjustedY = bottomPoint.y - 1
  val adjustedLength = length - 2
  drag(
    bottomPoint.x,
    adjustedY,
    bottomPoint.x,
    adjustedY - adjustedLength,
    durationMillis.toSeconds() * stepsInOneSecond
  )
}

fun UiDevice.dragRight(bottomPoint: Point, length: Int, durationMillis: Long = 2.seconds) {
  val stepsInOneSecond = 200
  val adjustedX = bottomPoint.x + 1
  val adjustedLength = length - 2
  drag(
    adjustedX,
    bottomPoint.y,
    adjustedX + adjustedLength,
    bottomPoint.y,
    durationMillis.toSeconds() * stepsInOneSecond
  )
}