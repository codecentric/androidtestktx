package de.codecentric.androidtestktx.espresso

import android.app.Activity
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import kotlin.reflect.KClass

abstract class BaseEspressoTest<A : Activity>(
  activityClass: KClass<A>,
  initialTouchMode: Boolean = false,
  launchActivity: Boolean = false,
  preStartSettings: () -> Unit = {}
) {

  @JvmField
  @Rule
  val activityRule: ActivityTestRule<A> = MockableTestRule(
    activityClass.java,
    initialTouchMode,
    launchActivity,
    preStartSettings = preStartSettings
  )

  fun run(apiMock: () -> Unit, test: () -> Unit) {
    apiMock()
    test()
  }
}

val defaultStartSetup = {
  //todo put some app related settings, like SharedPReferences cached values etc.
}