package de.codecentric.androidtestktx.demo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KTXDemo {

  @Test
  fun shouldFindViewById() {
    mainRobot {
      //doNothing
    } verifyThat {
      mainButtonIsDisplayed()
    }
  }
}