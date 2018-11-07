package de.codecentric.androidtestktx.demo

import de.codecentric.androidtestktx.demo.ui.MainActivity
import de.codecentric.androidtestktx.espresso.extensions.itIsDisplayed
import de.codecentric.androidtestktx.espresso.extensions.verifyThat
import de.codecentric.androidtestktx.uiautomator.UIAutomatorRobot
import de.codecentric.androidtestktx.uiautomator.extensions.*

fun withMainUIAutomatorRobot(fn: MainUIAutomatorRobot.() -> Unit) = MainUIAutomatorRobot().apply { fn() }

infix fun MainUIAutomatorRobot.andThen(fn: MainUIAutomatorRobot.() -> Unit): MainUIAutomatorRobot {
  fn()
  return this
}

infix fun MainUIAutomatorRobot.verifyThat(fn: MainUIAutomatorRobotResult.() -> Unit) {
  val mainRobotResult = MainUIAutomatorRobotResult()
  mainRobotResult.fn()
}

class MainUIAutomatorRobot : UIAutomatorRobot<MainActivity>(MainActivity::class, true) {
  fun openList() {
    click on button(R.id.activityMainBtnOpenList)
  }

  fun inputText(inputText: String = INPUT_TEXT) {
    typeText(inputText) into text(R.id.activityMainEditText)
  }

  fun replaceTextInField(inputText: String = REPLACE_TEXT) {
    replaceText(inputText) on text(R.id.activityMainEditText)
  }

  fun clearText() {
    clearText from text(R.id.activityMainEditText)
  }
}

class MainUIAutomatorRobotResult {

  fun mainButtonIsDisplayed() {
    val openListButton = viewById(R.id.activityMainBtnOpenList)
    openListButton verifyThat { itIsDisplayed() }
  }

  fun openListLabelIsDisplayed() {
    val openListButton = viewByText("OPEN LIST")
    openListButton verifyThat { itIsDisplayed() }
  }

  fun listScreenIsOpened() {
    MainActivity::class verifyThat { itIsDisplayed() }
  }

  fun textIsEnteredCorrectly(textToCheck: String = INPUT_TEXT) {
    val filledEditText = viewByText(textToCheck)
    filledEditText verifyThat { itIsDisplayed() }
  }

  fun textIsClearedInTheInputField() {
    val filledEditText = viewById(R.id.activityMainEditText)
    filledEditText verifyThat { textFieldIsEmpty() }
  }
}