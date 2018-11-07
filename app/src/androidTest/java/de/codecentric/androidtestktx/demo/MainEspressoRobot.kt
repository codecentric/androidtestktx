package de.codecentric.androidtestktx.demo

import de.codecentric.androidtestktx.demo.ui.MainActivity
import de.codecentric.androidtestktx.espresso.EspressoRobot
import de.codecentric.androidtestktx.espresso.MockableTestRule
import de.codecentric.androidtestktx.espresso.extensions.*

fun withMainEspressoRobot(fn: MainUIAutomatorRobot.() -> Unit) = MainUIAutomatorRobot().apply { fn() }

infix fun MainEspressoRobot.andThen(fn: MainEspressoRobot.() -> Unit): MainEspressoRobot {
  fn()
  return this
}

infix fun MainEspressoRobot.verifyThat(fn: MainEspressoRobotResult.() -> Unit) {
  val mainRobotResult = MainEspressoRobotResult()
  mainRobotResult.fn()
}

class MainEspressoRobot : EspressoRobot<MainActivity>(MockableTestRule(MainActivity::class.java), true) {
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

class MainEspressoRobotResult {

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