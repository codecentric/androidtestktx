package de.codecentric.androidtestktx

import android.Manifest
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.provider.Settings
import android.support.test.InstrumentationRegistry
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.util.Log
import com.google.gson.Gson
import de.codecentric.androidtestktx.uiautomator.extensions.device
import de.codecentric.androidtestktx.uiautomator.extensions.viewByText
import org.junit.Assert
import java.util.Locale


const val EMPTY_STRING = ""

val targetContext: Context get() = InstrumentationRegistry.getTargetContext()
val Context.originalPackageName: String get() = targetContext.packageName.removeSuffix(".test")
val resource: Resources get() = targetContext.resources
val instrumentation: Instrumentation get() = InstrumentationRegistry.getInstrumentation()

fun Context.getConfigurationResources(locale: Locale): Resources {
  val configuration = Configuration(resources.configuration)
  configuration.setLocale(locale)
  return createConfigurationContext(configuration).resources
}

fun Context.getGermanConfiguration() = getConfigurationResources(Locale.GERMAN)
fun Context.getEnglishConfiguration() = getConfigurationResources(Locale.ENGLISH)

fun Any.toJson() = Gson().toJson(this)

fun Any.log(textFn: () -> String) {
  Log.d(javaClass.simpleName, textFn())
}

infix fun Context.stringOf(stringResId: Int) = getString(stringResId)
infix fun Context.stringArrayOf(arrayResId: Int) = resources.getStringArray(arrayResId)
infix fun Context.englishArrayOf(arrayResId: Int) = getEnglishConfiguration().getStringArray(arrayResId)
infix fun Context.germanStringOf(stringResId: Int) = getGermanConfiguration().getString(stringResId)
infix fun Context.englishStringOf(stringResId: Int) = getEnglishConfiguration().getString(stringResId)

typealias Cache = SharedPreferences
typealias Editor = SharedPreferences.Editor

inline fun Cache.applyChanges(executeAndApply: (Editor).() -> Unit) {
  val editor = edit()
  executeAndApply(editor)
  editor.apply()
}

inline fun Cache.save(valueKey: () -> Pair<Any, String>) {
  val (value, key) = valueKey()
  applyChanges {
    when (value) {
      is String -> putString(key, value)
      is Int -> putInt(key, value)
      is Float -> putFloat(key, value)
      is Boolean -> putBoolean(key, value)
    }
  }
}

inline fun Cache.remove(value: () -> String) {
  applyChanges {
    remove(value())
  }
}

fun isPermissionAllowed(permission: () -> String): Boolean {
  return ContextCompat.checkSelfPermission(targetContext, permission()) == PermissionChecker.PERMISSION_GRANTED
}

fun isLocationPermissionAllowed() = isPermissionAllowed { Manifest.permission.ACCESS_FINE_LOCATION }
fun isStoragePermissionAllowed() = isPermissionAllowed { Manifest.permission.WRITE_EXTERNAL_STORAGE }

fun denyAppPermissions() {
  val intent = Intent()
  intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
  intent.addCategory(Intent.CATEGORY_DEFAULT)
  intent.data = Uri.parse("package:" + targetContext.originalPackageName)
  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
  intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
  targetContext.startActivity(intent)

  val permissionsOption = viewByText { "Permissions" }
  permissionsOption.waitForExists(2.seconds)
  if (permissionsOption.exists()) {
    permissionsOption.click()
  } else {
    device.pressBack()
    return
  }

  val locationPermission = viewByText { "Location" }
  val storagePermission = viewByText { "Storage" }
  locationPermission.waitForExists(2.seconds)

  if (locationPermission.exists()) {
    if (isLocationPermissionAllowed()) {
      locationPermission.click()
    }
    if (isStoragePermissionAllowed()) {
      storagePermission.click()
    }
  }

  device.pressBack()
  device.pressBack()
}

fun assertTrue(value: Boolean) {
  Assert.assertEquals(true, value)
}

fun assertFalse(value: Boolean) {
  Assert.assertEquals(false, value)
}


val Int.seconds: Long get() = (this * 1000).toLong()
val Int.minutes: Long get() = this.seconds * 60
fun Long.toSeconds(): Int = (this / 1000).toInt()