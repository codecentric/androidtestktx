package de.codecentric.androidtestktx.espresso

import android.app.Activity
import android.content.Intent
import android.support.test.rule.ActivityTestRule

open class EspressoRobot<T : Activity>(val activityTestRule: ActivityTestRule<T>, autoStart: Boolean) {
  init {
    if (autoStart) {
      launchActivity()
    }
  }

  fun launchActivity() {
    activityTestRule.launchActivity(Intent())
  }
}