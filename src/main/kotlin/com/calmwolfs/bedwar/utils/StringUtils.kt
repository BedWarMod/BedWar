package com.calmwolfs.bedwar.utils

object StringUtils {
    fun String.unformat(): String {
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

    fun String.stripResets(): String {
        var message = this

        while (message.startsWith("§r")) {
            message = message.substring(2)
        }
        while (message.endsWith("§r")) {
            message = message.substring(0, message.length - 2)
        }
        return message
    }
}