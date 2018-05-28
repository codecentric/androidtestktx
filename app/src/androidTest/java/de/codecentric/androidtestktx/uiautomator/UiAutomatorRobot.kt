package de.codecentric.androidtestktx.uiautomator

import android.app.Activity
import android.content.Intent
import de.codecentric.androidtestktx.targetContext
import de.codecentric.androidtestktx.uiautomator.extensions.appStarts
import de.codecentric.androidtestktx.uiautomator.extensions.device
import de.codecentric.androidtestktx.uiautomator.extensions.viewByText
import kotlin.reflect.KClass

abstract class UiAutomatorRobot<T : Activity>(val kClass: KClass<T>, autoStartActivity: Boolean) {

  protected val injector: TestAppComponent by lazy { (targetContext.applicationContext as TestApp).testAppComponent }

  abstract fun inject()

  init {
    if (autoStartActivity) {
      startActivity()
    }
  }

  fun startActivity() {
    improveDeviceStartupConditions()

    val intent = Intent(targetContext, kClass.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    targetContext.startActivity(intent)
    device waitUntil appStarts()
  }

  private fun improveDeviceStartupConditions() {
    closeDialogWithButton("Cancel")
    closeDialogWithButton("Ok")
    closeDialogWithButton("Continue")
    closeDialogWithButton("Force close")
  }

  private fun closeDialogWithButton(buttonText: String) {
    val button = viewByText { buttonText }
    if (button.exists()) {
      button.click()
    }
  }
}