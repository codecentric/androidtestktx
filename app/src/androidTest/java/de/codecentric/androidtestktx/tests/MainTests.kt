package de.codecentric.androidtestktx.tests

import android.content.Intent
import de.codecentric.androidtestktx.R
import de.codecentric.androidtestktx.targetContext
import de.codecentric.androidtestktx.ui.MainActivity
import de.codecentric.androidtestktx.uiautomator.extensions.appStarts
import de.codecentric.androidtestktx.uiautomator.extensions.device
import de.codecentric.androidtestktx.uiautomator.extensions.itIsDisplayed
import de.codecentric.androidtestktx.uiautomator.extensions.verifyThat
import de.codecentric.androidtestktx.uiautomator.extensions.viewById
import de.codecentric.androidtestktx.uiautomator.extensions.waitUntil
import org.junit.Test

class MainTests {

  @Test
  fun mainButtonShouldBeVisible() {
    mainRobot {
      //do nothing
    } verifyThat {
      mainButtonIsDisplayed()
    }
  }
}

fun mainRobot(fn: MainRobot.() -> Unit) = MainRobot().apply { fn() }

infix fun MainRobot.verifyThat(fn: MainRobotResult.() -> Unit) {
  val mainRobotResult = MainRobotResult()
  mainRobotResult.fn()
}

class MainRobot {

  init {
    val intent = Intent(targetContext, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    targetContext.startActivity(intent)
    device waitUntil appStarts()
  }
}

class MainRobotResult {
  private val mainButton = viewById { R.id.mainButton }

  fun mainButtonIsDisplayed() {
    mainButton verifyThat { itIsDisplayed() }
  }
}