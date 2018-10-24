package com.daimler.mbpro.android.core.uiautomator.extensions

import android.app.Instrumentation
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import kotlin.reflect.KClass

val device: UiDevice get() = UiDevice.getInstance(instrumentation)

infix fun Resources.nameOf(viewId: Int) = getResourceName(viewId)

fun Instrumentation.startApp() {
  val intent = context.packageManager.getLaunchIntentForPackage(targetContext.originalPackageName)
  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
  context.startActivity(intent)
}

infix fun UiDevice.objectById(viewId: () -> Int) = findObject(
  byIdSelector(viewId())
)

fun byIdSelector(viewId: Int): UiSelector = UiSelector().resourceId(resource nameOf viewId)

fun byTextSelector(@StringRes stringResId: Int): UiSelector = UiSelector().text(targetContext stringOf stringResId)

infix fun UiDevice.objectByTextResource(stringResId: () -> Int) = findObject(
  UiSelector().text(targetContext stringOf stringResId())
)

infix fun UiDevice.objectByText(text: () -> String) = findObject(
  UiSelector().text(text())
)

infix fun <K : View> UiDevice.objectByComponentType(kclass: () -> KClass<K>): UiObject =
  findObject(UiSelector().className(kclass().java))

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

fun appStarts() = By.pkg(targetContext.originalPackageName)

infix fun UiObject.verifyThat(assertFun: (UiObject).() -> Unit) {
  assertFun(this)
}

infix fun UiObject.verifyTextMatchingTo(assertTextFun: (UiObject).() -> String) {
  Assert.assertEquals(text, assertTextFun())
}

fun collectionById(viewId: () -> Int): UiCollection = UiCollection(byIdSelector(viewId()))
fun <W : View> collectionByClassName(kClass: () -> KClass<W>): UiCollection =
  UiCollection(UiSelector().className(kClass().java))

inline fun <W : View> UiCollection.forEach(containerKClass: KClass<W>, fn: (UiObject) -> Boolean) {
//    val selector = UiSelector().clickable(true)
  val selector = UiSelector().className(containerKClass.java)

  @Suppress("LoopToCallChain")
  for (itemIndex in 0 until this.childCount) {
    val child = this.getChild(selector.index(itemIndex))

    if (child.exists()) {
      device.scrollUpALittleBit()
      val shouldContinue = fn(child)
      if (!shouldContinue) {
        break
      }
    }
  }
}

fun UiDevice.dragVertically(rect: Rect, durationMillis: Long = 2.seconds) {
  val length = rect.height()
  device.dragUp(rect.bottomPoint(), length, durationMillis)
}

fun UiDevice.dragHorizontally(rect: Rect, durationMillis: Long = 2.seconds) {
  val length = rect.width()
  device.dragRight(rect.leftPoint(), length, durationMillis)
}

fun Rect.bottomPoint(): Point {
  val x = right - ((right - left) / 2)
  val y = bottom
  return Point(x, y)
}

fun Rect.leftPoint(): Point {
  val x = left
  val y = bottom - ((bottom - top) / 2)
  return Point(x, y)
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