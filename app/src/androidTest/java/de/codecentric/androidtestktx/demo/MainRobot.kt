package de.codecentric.androidtestktx.demo

import de.codecentric.androidtestktx.demo.ui.MainActivity
import de.codecentric.androidtestktx.espresso.extensions.itIsDisplayed
import de.codecentric.androidtestktx.espresso.extensions.verifyThat
import de.codecentric.androidtestktx.espresso.extensions.viewByText
import de.codecentric.androidtestktx.uiautomator.UiAutomatorRobot
import de.codecentric.androidtestktx.uiautomator.extensions.itIsDisplayed
import de.codecentric.androidtestktx.uiautomator.extensions.verifyThat
import de.codecentric.androidtestktx.uiautomator.extensions.viewById

fun withMainRobot(fn: MainRobot.() -> Unit) = MainRobot().apply { fn() }

infix fun MainRobot.verifyThat(fn: MainRobotResult.() -> Unit) {
  val mainRobotResult = MainRobotResult()
  mainRobotResult.fn()
}

class MainRobot : UiAutomatorRobot<MainActivity>(MainActivity::class, true) {
  fun openList() {
    val openListBtn = viewById { R.id.activityMainBtnOpenList }
    openListBtn.click()
  }
}

class MainRobotResult {

  fun mainButtonIsDisplayed() {
    val openListButton = viewById { R.id.activityMainBtnOpenList }
    openListButton verifyThat { itIsDisplayed() }
  }

  fun openListLabelIsDisplayed() {
    val openListButton = viewByText(R.string.open_list)
    openListButton verifyThat { itIsDisplayed() }
  }

  fun listScreenIsOpened() {
    MainActivity::class verifyThat { itIsDisplayed() }
  }
}