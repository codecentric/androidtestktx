package de.codecentric.androidtestktx.espresso

import android.app.Activity
import androidx.test.rule.ActivityTestRule

class MockableTestRule<A : Activity>(
  clazz: Class<A>, initialTouchMode: Boolean = false,
  launchActivity: Boolean = false, private val preStartSettings: () -> Unit = {}
) : ActivityTestRule<A>(
  clazz, initialTouchMode,
  launchActivity
) {

  override fun beforeActivityLaunched() {
    super.beforeActivityLaunched()
    preStartSettings()
  }
}