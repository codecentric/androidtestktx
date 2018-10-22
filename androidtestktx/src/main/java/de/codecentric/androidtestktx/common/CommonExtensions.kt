package de.codecentric.androidtestktx.common

import android.graphics.Point
import android.graphics.Rect
import java.util.Random


const val EMPTY_STRING = ""

fun Rect.bottomPoint(): Point {
  val x = right - ((right - left) / 2)
  val y = bottom
  return Point(x, y)
}

fun Rect.leftPoint(): Point {
  val x = left
  val y = bottom - ((bottom - top) / 2)
  return Point(x, y)
}

fun ClosedRange<Int>.random() = Random().nextInt((endInclusive + 1) - start) + start

val Int.seconds: Long get() = (this * 1000).toLong()
val Int.second: Long get() = (this * 1000).toLong()
val Int.minutes: Long get() = this.seconds * 60
val Int.minute: Long get() = this.seconds * 60
fun Long.toSeconds(): Int = (this / 1000).toInt()