package de.codecentric.androidtestktx.uiautomator.extensions

import android.view.View
import androidx.test.uiautomator.UiObject
import kotlin.reflect.KClass

/**
 * Returns a UIObject by viewId value provided from [viewId] id.
 */
fun viewById(viewId: Int): UiObject = device.objectById(viewId)

/**
 * Returns a UIObject by text value provided from string resources for [stringResId] id.
 * String result is fetched according to currently active localization on the device.
 */
fun viewByTextResource(stringResId: Int): UiObject = device.objectByTextResource(stringResId)

/**
 * Returns a UIObject by text value provided by [text] argument.
 */
fun viewByText(text: String): UiObject = device.objectByText(text)

/**
 * Returns a UIObject containing text value provided by [text] lambda.
 */
fun viewContainingText(text: String): UiObject = device.objectContainingText(text)

/**
 * Returns a UIObject by type of the View provided by [kClass] lambda
 */
fun <K : View> viewByComponentType(kClass: () -> KClass<K>): UiObject = device.objectByComponentType { kClass() }