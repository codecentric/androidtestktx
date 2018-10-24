package com.daimler.mbpro.android.core.espresso

import android.app.Activity
import android.content.Intent

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