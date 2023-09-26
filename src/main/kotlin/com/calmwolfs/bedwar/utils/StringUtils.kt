package com.calmwolfs.bedwar.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtils {
    private val whiteSpaceResetPattern = "^(?:\\s|§r)*|(?:\\s|§r)*$".toPattern()
    private val resetPattern = "(?i)§R".toPattern()

    fun String.trimWhiteSpaceAndResets(): String = whiteSpaceResetPattern.matcher(this).replaceAll("")
    fun String.removeResets(): String = resetPattern.matcher(this).replaceAll("")

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

    fun String.toPlayerName(): String {
        val split = split(" ")
        return if (split.size > 1) {
            split[1].unformat()
        } else {
            split[0].unformat()
        }
    }

    inline fun <T> Pattern.matchMatcher(text: String, consumer: Matcher.() -> T) =
        matcher(text).let { if (it.matches()) consumer(it) else null }

    inline fun <T> Pattern.findMatcher(text: String, consumer: Matcher.() -> T) =
        matcher(text).let { if (it.find()) consumer(it) else null }

    fun optionalPlural(number: Int, singular: String, plural: String): String {
        return "$number " + if (number == 1) singular else plural
    }
}