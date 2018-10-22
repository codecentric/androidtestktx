package de.codecentric.androidtestktx.common

import android.Manifest
import android.Manifest.permission
import android.app.Instrumentation
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import java.util.Locale

/**
 * Application context.
 */
val appContext: Context get() = InstrumentationRegistry.getInstrumentation().targetContext

/**
 * Instrumentation context
 */
val testContext: Context get() = InstrumentationRegistry.getInstrumentation().context

/**
 * AssetManager for androidTest environment
 */
val testAssets: AssetManager = testContext.assets

/**
 * Test runner instrumentation
 */
val instrumentation: Instrumentation get() = InstrumentationRegistry.getInstrumentation()

/**
 * Application package name
 */
val Context.originalPackageName: String get() = appContext.packageName.removeSuffix(".test")
val resource: Resources get() = appContext.resources

/**
 * Returns a application configuration with forced localization.
 */
fun Context.getConfigurationResources(locale: Locale): Resources {
  val configuration = Configuration(resources.configuration)
  configuration.setLocale(locale)
  return createConfigurationContext(configuration).resources
}

infix fun Resources.nameOf(viewId: Int): String = getResourceName(viewId)

/**
 * Gets a application configuration with forced [Locale.GERMAN].
 */
fun Context.getGermanConfiguration() = getConfigurationResources(Locale.GERMAN)

/**
 * Gets a application configuration with forced [Locale.ENGLISH].
 */
fun Context.getEnglishConfiguration() = getConfigurationResources(Locale.ENGLISH)

/**
 * Returns a string value from the resources, on a currently running localization, for the provided [stringResId].
 */
infix fun Context.stringOf(stringResId: Int) = getString(stringResId)

/**
 * Returns a string array value from the resources, on a currently running localization, for the provided [arrayResId]
 */
infix fun Context.stringArrayOf(arrayResId: Int) = resources.getStringArray(arrayResId)

/**
 * Returns a string array value from the resources, on a [Locale.ENGLISH] localization, for the provided [arrayResId]
 */
infix fun Context.englishArrayOf(arrayResId: Int) = getEnglishConfiguration().getStringArray(arrayResId)

/**
 * Returns a string value from the resources, on a [Locale.GERMAN] localization, for the provided [stringResId]
 */
infix fun Context.germanStringOf(stringResId: Int) = getGermanConfiguration().getString(stringResId)

/**
 * Returns a string value from the resources, on a [Locale.ENGLISH] localization, for the provided [stringResId]
 */
infix fun Context.englishStringOf(stringResId: Int) = getEnglishConfiguration().getString(stringResId)

/**
 * Returns a string value from the resources while applying integer substitutions for placeholders
 */
fun Context.stringFormatOf(stringResId: Int, vararg formatArgs: Any) = getString(stringResId, *formatArgs)

/**
 * Shared preferences sugar.
 */
inline fun Cache.applyChanges(executeAndApply: (Editor).() -> Unit) {
  val editor = edit()
  executeAndApply(editor)
  editor.commit()
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

/**
 * Checking whether the permission returned by [permission] lambda is allowed.
 *
 * @return TRUE if the permission is allowed. FALSE otherwise.
 */
fun isPermissionAllowed(permission: () -> String): Boolean {
  return ContextCompat.checkSelfPermission(appContext, permission()) == PermissionChecker.PERMISSION_GRANTED
}

/**
 * Checking whether the [Manifest.permission.ACCESS_FINE_LOCATION] is allowed.
 *
 * @return TRUE if the permission is allowed. FALSE otherwise.
 */
fun isLocationPermissionAllowed() =
  isPermissionAllowed { permission.ACCESS_FINE_LOCATION }

/**
 * Checking whether the [Manifest.permission.WRITE_EXTERNAL_STORAGE] is allowed.
 *
 * @return TRUE if the permission is allowed. FALSE otherwise.
 */
fun isStoragePermissionAllowed() =
  isPermissionAllowed { permission.WRITE_EXTERNAL_STORAGE }

fun assertTrue(value: Boolean) {
  Assert.assertEquals(true, value)
}

fun assertFalse(value: Boolean) {
  Assert.assertEquals(false, value)
}