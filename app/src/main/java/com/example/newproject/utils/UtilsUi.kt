package com.example.newproject.utils

import android.graphics.Color

class UtilsUi {

    companion object{

        fun darker(color: Int, factor: Double): Int {
            val a = Color.alpha(color)
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            return Color.argb(a, Math.max((r * factor).toInt(), 0), Math.max((g * factor).toInt(), 0), Math.max((b * factor).toInt(), 0))
        }

        fun light(color: Int, factor: Double): Int {
            val a = Color.alpha(color)
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            return Color.argb(a, Math.max((r * factor).toInt(), 0), Math.max((g * factor).toInt(), 0), Math.max((b * factor).toInt(), 0))
        }

    }


}