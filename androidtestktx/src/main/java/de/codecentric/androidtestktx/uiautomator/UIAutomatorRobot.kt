package de.codecentric.androidtestktx.uiautomator

import android.app.Activity
import android.content.Intent
import de.codecentric.androidtestktx.common.appContext
import de.codecentric.androidtestktx.uiautomator.extensions.appStarts
import de.codecentric.androidtestktx.uiautomator.extensions.device
import de.codecentric.androidtestktx.uiautomator.extensions.viewByText
import de.codecentric.androidtestktx.uiautomator.extensions.waitUntil
import kotlin.reflect.KClass

abstract class UIAutomatorRobot<T : Activity>(val kClass: KClass<T>, autoStartActivity: Boolean) {

  init {
    if (autoStartActivity) {
      startActivity()
    }
  }

  fun startActivity() {
    improveDeviceStartupConditions()

    val intent = Intent(appContext, kClass.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    appContext.startActivity(intent)
    device waitUntil appStarts()
  }

  private fun improveDeviceStartupConditions() {
    closeDialogWithButton("Cancel")
    closeDialogWithButton("Ok")
    closeDialogWithButton("Continue")
    closeDialogWithButton("Force close")
  }

  private fun closeDialogWithButton(buttonText: String) {
    val button = viewByText(buttonText)
    if (button.exists()) {
      button.click()
    }
  }
}