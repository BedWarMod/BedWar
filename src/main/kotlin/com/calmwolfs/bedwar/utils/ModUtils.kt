package com.calmwolfs.bedwar.utils

import io.github.moulberry.moulconfig.observer.Property
import java.util.Timer
import java.util.TimerTask
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

    fun <T> Property<out T>.onToggle(observer: Runnable) {
        whenChanged { _, _ -> observer.run() }
    }
}