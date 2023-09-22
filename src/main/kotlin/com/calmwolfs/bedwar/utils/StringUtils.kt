package com.calmwolfs.bedwar.utils

object StringUtils {
    fun String.removeColour(): String {
        val builder = StringBuilder()

        var counter = 0
        while (counter < this.length) {
            if (this[counter] == '§') {
                counter += 2
            } else {
                builder.append(this[counter])
                counter++
            }
        }

        return builder.toString()
    }
}