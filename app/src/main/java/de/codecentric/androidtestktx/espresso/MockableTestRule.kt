package com.daimler.mbpro.android.core.espresso

import android.app.Activity

/**
 * Created by Bajic Dusko (www.bajicdusko.com) on 08.11.17.
 */
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