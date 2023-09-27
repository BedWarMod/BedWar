package com.calmwolfs.bedwar.data.types

import java.awt.Color

enum class ModColours(private var chatColourCode: Char, private val colour: Color) {
    BLACK('0', Color(0, 0, 0)),
    DARK_BLUE('1', Color(0, 0, 170)),
    DARK_GREEN('2', Color(0, 170, 0)),
    DARK_AQUA('3', Color(0, 170, 170)),
    DARK_RED('4', Color(170, 0, 0)),
    DARK_PURPLE('5', Color(170, 0, 170)),
    GOLD('6', Color(255, 170, 0)),
    GREY('7', Color(170, 170, 170)),
    DARK_GREY('8', Color(85, 85, 85)),
    BLUE('9', Color(85, 85, 255)),
    GREEN('a', Color(85, 255, 85)),
    AQUA('b', Color(85, 255, 255)),
    RED('c', Color(255, 85, 85)),
    LIGHT_PURPLE('d', Color(255, 85, 255)),
    YELLOW('e', Color(255, 255, 85)),
    WHITE('f', Color(255, 255, 255)),
    ;

    fun getChatColour(): String = "§$chatColourCode"

    fun toColour(): Color = colour

    fun addOpacity(opacity: Int): Color {
        val colour = toColour()
        val red = colour.red
        val green = colour.green
        val blue = colour.blue
        return Color(red, green, blue, opacity)
    }
}