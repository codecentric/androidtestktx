package de.codecentric.androidtestktx.demo

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KTXUIAutomatorDemo {

  @Before
  fun setUp() {
    Intents.init()
  }

  @Test
  fun shouldFindViewById() {
    withMainUIAutomatorRobot {
      //doNothing
    } verifyThat {
      mainButtonIsDisplayed()
    }
  }

  @Test
  fun shouldFindViewByText() {
    withMainUIAutomatorRobot {
      //do nothing
    } verifyThat {
      openListLabelIsDisplayed()
    }
  }

  @Test
  fun shouldOpenListScreen() {
    withMainUIAutomatorRobot {
      openList()
    } verifyThat {
      listScreenIsOpened()
    }
  }

  @Test
  fun shouldTypeIntoTheField(){
    withMainUIAutomatorRobot {
      inputText()
    } verifyThat {
      textIsEnteredCorrectly()
    }
  }

  @Test
  fun shouldClearTheTextFromInputField(){
    withMainUIAutomatorRobot {
      inputText()
    } andThen {
      clearText()
    } verifyThat {
      textIsClearedInTheInputField()
    }
  }

  @Test
  fun shouldReplaceTheTextInTheInputField(){
    withMainUIAutomatorRobot {
      inputText()
    } andThen {
      replaceTextInField()
    } verifyThat {
      textIsEnteredCorrectly(REPLACE_TEXT)
    }
  }

  @After
  fun teardown() {
    Intents.release()
  }
}