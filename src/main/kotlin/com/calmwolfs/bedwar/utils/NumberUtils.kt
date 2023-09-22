package com.calmwolfs.bedwar.utils

object NumberUtils {
    fun Float.round(decimals: Int): Float {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        val result = kotlin.math.round(this * multiplier) / multiplier
        val a = result.toString()
        val b = toString()
        return if (a.length > b.length) this else result.toFloat()
    }
}