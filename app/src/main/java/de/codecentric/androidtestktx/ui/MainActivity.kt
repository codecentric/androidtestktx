package de.codecentric.androidtestktx.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.codecentric.androidtestktx.R.layout

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
  }
}
