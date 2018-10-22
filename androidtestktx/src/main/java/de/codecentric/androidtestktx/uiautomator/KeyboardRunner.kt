package de.codecentric.androidtestktx.uiautomator

import android.view.KeyEvent
import de.codecentric.androidtestktx.uiautomator.extensions.device

class KeyboardRunner {

  companion object {

    fun input(text: String, confirmInput: Boolean = false) {
      val keyboardRunner = KeyboardRunner()
      text.forEach { keyboardRunner.pressKeyCode(it) }
      if (confirmInput) {
        device.pressEnter()
      }
    }
  }

  private fun pressKeyCode(character: Char) {

    var isUpperCase = character.isUpperCase()
    val mappedCharacter = when (character) {
      '@' -> "AT"
      '.' -> "PERIOD"
      ',' -> "COMMA"
      '#' -> "POUND"
      '!' -> {
        isUpperCase = true
        "1"
      }
      '?' -> {
        isUpperCase = true
        "SLASH"
      }
      '+' -> "PLUS"
      '-' -> "MINUS"
      else -> character.toString()
    }

    val keyCode = KeyEvent.keyCodeFromString("KEYCODE_${mappedCharacter.toUpperCase()}")
    if (isUpperCase) {
      device.pressKeyCode(keyCode, KeyEvent.META_SHIFT_ON)
    } else {
      device.pressKeyCode(keyCode)
    }
  }
}