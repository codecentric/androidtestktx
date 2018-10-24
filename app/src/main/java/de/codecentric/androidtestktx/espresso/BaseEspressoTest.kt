package com.daimler.mbpro.android.core.espresso

import android.app.Activity
import android.util.Log
import java.util.Locale
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
  val TAG = "defaultStartSetup"

  preferences.locale = Locale(Constants.DEFAULT_LOCALE)
  Log.d(TAG, "Setting Locale to ${preferences.locale}")

  preferences.isOnboardingCompleted = true
  Log.d(TAG, "Setting isOnboardingCompleted to ${preferences.isOnboardingCompleted}")

  preferences.isEulaAccepted = true
  Log.d(TAG, "Setting isEulaAccepted to ${preferences.isEulaAccepted}")

  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_MOCKED_MODE to true")
  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_PUSH_NOTIFICATION to true")
  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_USAGE_DATA_ANALYSIS to true")
  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_LOCATION to true")
  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_DRIVE_MODE to true")
  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_STORAGE to true")

  cache.save { true to Constants.PREFS_FIELD_PERMISSION_MOCKED_MODE }
  cache.save { true to Constants.PREFS_FIELD_PERMISSION_PUSH_NOTIFICATION }
  cache.save { true to Constants.PREFS_FIELD_PERMISSION_USAGE_DATA_ANALYSIS }
  cache.save { true to Constants.PREFS_FIELD_PERMISSION_LOCATION }
  cache.save { true to Constants.PREFS_FIELD_PERMISSION_DRIVE_MODE }
  cache.save { true to Constants.PREFS_FIELD_PERMISSION_STORAGE }
}

val onBoardingDefaultSetup = {
  defaultStartSetup()
  val TAG = "onBoardingDefaultSetup"

  preferences.isOnboardingCompleted = false
  Log.d(TAG, "Setting isOnboardingCompleted to ${preferences.isOnboardingCompleted}")

  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_MOCKED_MODE to true")
  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_PUSH_NOTIFICATION to false")
  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_USAGE_DATA_ANALYSIS to false")
  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_LOCATION to false")
  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_DRIVE_MODE to false")
  Log.d(TAG, "Setting PREFS_FIELD_PERMISSION_STORAGE to false")

  cache.save { false to Constants.PREFS_FIELD_PERMISSION_MOCKED_MODE }
  cache.save { false to Constants.PREFS_FIELD_PERMISSION_PUSH_NOTIFICATION }
  cache.save { false to Constants.PREFS_FIELD_PERMISSION_USAGE_DATA_ANALYSIS }
  cache.save { false to Constants.PREFS_FIELD_PERMISSION_LOCATION }
  cache.save { false to Constants.PREFS_FIELD_PERMISSION_DRIVE_MODE }
  cache.save { false to Constants.PREFS_FIELD_PERMISSION_STORAGE }
}

fun turnOfMockedData() {
  cache.save { false to Constants.PREFS_FIELD_PERMISSION_MOCKED_MODE }
}