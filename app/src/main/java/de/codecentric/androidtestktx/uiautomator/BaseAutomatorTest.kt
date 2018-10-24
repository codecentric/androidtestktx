package com.daimler.mbpro.android.core.uiautomator

open class BaseAutomatorTest(private val preStartSettings: () -> Unit = {}) {

  @Before open fun setUp() {
    preStartSettings()
  }

  fun startApp() {
    viewByText { targetContext stringOf R.string.all_continue }.click()

  }
}