package com.calmwolfs.bedwar.utils

import java.util.*
import kotlin.time.Duration

object ModUtils {
    fun warning(message: String) {
        ChatUtils.internalChat("§cWarning! $message")
    }

    fun error(message: String) {
        println("error: '$message'")
        ChatUtils.internalChat("§c$message")
    }

    fun runDelayed(duration: Duration, runnable: () -> Unit) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                runnable()
            }
        }, duration.inWholeMilliseconds)
    }
}