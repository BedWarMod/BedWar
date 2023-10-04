package com.calmwolfs.bedwar.utils

import java.text.NumberFormat

object NumberUtils {
    fun Number.addSeparators(): String = NumberFormat.getNumberInstance().format(this)
    
    fun Float.round(decimals: Int): Float {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        val result = kotlin.math.round(this * multiplier) / multiplier
        val a = result.toString()
        val b = toString()
        return if (a.length > b.length) this else result.toFloat()
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        val result = kotlin.math.round(this * multiplier) / multiplier
        val a = result.toString()
        val b = toString()
        return if (a.length > b.length) this else result
    }

    fun getRatio(good: Int, bad: Int): Double {
        val first = good.toDouble()
        val second = if (bad == 0) 1.0 else bad.toDouble()

        return first / second
    }
}