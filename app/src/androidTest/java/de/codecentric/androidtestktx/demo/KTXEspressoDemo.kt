package de.codecentric.androidtestktx.demo

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KTXEspressoDemo {

  @Before
  fun setUp() {
    Intents.init()
  }

  @Test
  fun shouldFindViewById() {
    withMainEspressoRobot {
      //doNothing
    } verifyThat {
      mainButtonIsDisplayed()
    }
  }

  @Test
  fun shouldFindViewByText() {
    withMainEspressoRobot {
      //do nothing
    } verifyThat {
      openListLabelIsDisplayed()
    }
  }

  @Test
  fun shouldOpenListScreen() {
    withMainEspressoRobot {
      openList()
    } verifyThat {
      listScreenIsOpened()
    }
  }

  @Test
  fun shouldTypeIntoTheField(){
    withMainEspressoRobot {
      inputText()
    } verifyThat {
      textIsEnteredCorrectly()
    }
  }

  @Test
  fun shouldClearTheTextFromInputField(){
    withMainEspressoRobot {
      inputText()
    } andThen {
      clearText()
    } verifyThat {
      textIsClearedInTheInputField()
    }
  }

  @Test
  fun shouldReplaceTheTextInTheInputField(){
    withMainEspressoRobot {
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