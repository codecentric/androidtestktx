package de.codecentric.androidtestktx.uiautomator

import org.junit.Before

open class BaseAutomatorTest(private val preStartSettings: () -> Unit = {}) {

  @Before open fun setUp() {
    preStartSettings()
  }
}