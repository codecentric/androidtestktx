package de.codecentric.androidtestktx.uiautomator.extensions

import android.support.test.uiautomator.UiObject
import android.view.View
import kotlin.reflect.KClass

fun viewById(viewId: () -> Int): UiObject = device.objectById { viewId() }

fun viewByTextResource(stringResId: () -> Int): UiObject = device.objectByTextResource { stringResId() }

fun viewByText(string: () -> String): UiObject = device.objectByText { string() }

fun <K : View> viewByComponentType(kclass: () -> KClass<K>): UiObject = device.objectByComponentType { kclass() }