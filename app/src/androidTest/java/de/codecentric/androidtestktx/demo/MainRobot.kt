package de.codecentric.androidtestktx.demo

import android.content.Intent
import de.codecentric.androidtestktx.common.testContext
import de.codecentric.androidtestktx.demo.ui.MainActivity
import de.codecentric.androidtestktx.uiautomator.UiAutomatorRobot
import de.codecentric.androidtestktx.uiautomator.extensions.appStarts
import de.codecentric.androidtestktx.uiautomator.extensions.device
import de.codecentric.androidtestktx.uiautomator.extensions.itIsDisplayed
import de.codecentric.androidtestktx.uiautomator.extensions.verifyThat
import de.codecentric.androidtestktx.uiautomator.extensions.viewById
import de.codecentric.androidtestktx.uiautomator.extensions.waitUntil

fun mainRobot(fn: MainRobot.() -> Unit) = MainRobot().apply { fn() }

infix fun MainRobot.verifyThat(fn: MainRobotResult.() -> Unit) {
  val mainRobotResult = MainRobotResult()
  mainRobotResult.fn()
}

class MainRobot : UiAutomatorRobot<MainActivity>(MainActivity::class, false) {
  init {
    val intent = Intent(testContext, kClass.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    testContext.startActivity(intent)
    device waitUntil appStarts()
  }
}

class MainRobotResult {

  fun mainButtonIsDisplayed() {
    viewById { R.id.testComponent } verifyThat { itIsDisplayed() }
  }
}