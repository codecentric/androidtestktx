package de.codecentric.androidtestktx.demo

import de.codecentric.androidtestktx.demo.ui.MainActivity
import de.codecentric.androidtestktx.uiautomator.UiAutomatorRobot
import de.codecentric.androidtestktx.uiautomator.extensions.itIsDisplayed
import de.codecentric.androidtestktx.uiautomator.extensions.verifyThat
import de.codecentric.androidtestktx.uiautomator.extensions.viewById

fun mainRobot(fn: MainRobot.() -> Unit) = MainRobot().apply { fn() }

infix fun MainRobot.verifyThat(fn: MainRobotResult.() -> Unit) {
  val mainRobotResult = MainRobotResult()
  mainRobotResult.fn()
}

class MainRobot : UiAutomatorRobot<MainActivity>(MainActivity::class, true)

class MainRobotResult {

  fun mainButtonIsDisplayed() {
    viewById { R.id.testComponent } verifyThat { itIsDisplayed() }
  }
}