package de.codecentric.androidtestktx.demo

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KTXDemo {

  @Before
  fun setUp() {
    Intents.init()
  }

  @Test
  fun shouldFindViewById() {
    mainRobot {
      //doNothing
    } verifyThat {
      mainButtonIsDisplayed()
    }
  }

  @Test
  fun shouldFindViewByText() {
    mainRobot {
      //do nothing
    } verifyThat {
      openListLabelIsDisplayed()
    }
  }

  @Test
  fun shouldOpenListScreen() {
    mainRobot {
      openList()
    } verifyThat {
      listScreenIsOpened()
    }
  }

  @After
  fun teardown() {
    Intents.release()
  }
}