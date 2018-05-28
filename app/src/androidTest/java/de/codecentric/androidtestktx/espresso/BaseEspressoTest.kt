package de.codecentric.androidtestktx.espresso

import android.app.Activity
import android.support.test.rule.ActivityTestRule
import de.codecentric.androidtestktx.targetContext
import org.junit.Before
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
//
//    @JvmField
//    @Rule
//    val trampolineRule = TrampolineSchedulerRule()

  protected val injector: TestAppComponent by lazy { (targetContext.applicationContext as TestApp).testAppComponent }

  abstract fun inject()

  @Before
  fun initInjection() {
    inject()
  }

  fun run(apiMock: () -> Unit, test: () -> Unit) {
    apiMock()
    test()
  }
}

val defaultStartSetup = {
  //todo put some app related settings, like SharedPReferences cached values etc.
}