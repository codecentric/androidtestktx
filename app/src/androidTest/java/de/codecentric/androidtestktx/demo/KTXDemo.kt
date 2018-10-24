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
    withMainRobot {
      //doNothing
    } verifyThat {
      mainButtonIsDisplayed()
    }
  }

  @Test
  fun shouldFindViewByText() {
    withMainRobot {
      //do nothing
    } verifyThat {
      openListLabelIsDisplayed()
    }
  }

  @Test
  fun shouldOpenListScreen() {
    withMainRobot {
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