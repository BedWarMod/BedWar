package com.calmwolfs.bedwar.data.commands

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.utils.ModUtils
import com.calmwolfs.bedwar.utils.StringUtils.removeColour
import com.calmwolfs.bedwar.utils.computer.ClipboardUtils
import com.calmwolfs.bedwar.utils.computer.KeyboardUtils
import com.google.common.cache.CacheBuilder
import net.minecraft.client.Minecraft
import java.util.*
import java.util.concurrent.TimeUnit

object CopyErrorCommand {
    private val errorMessages = mutableMapOf<String, String>()
    private val fullErrorMessages = mutableMapOf<String, String>()
    private var cache =
        CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build<Pair<String, Int>, Unit>()

    fun command(array: Array<String>) {
        if (array.size != 1) {
            ModUtils.chat("§cUse /bwcopyerror <error id> or just click on the error in chat!")
            return
        }

        val id = array[0]
        val fullError = KeyboardUtils.isControlKeyDown()
        val errorMessage = if (fullError) {
            fullErrorMessages[id]
        } else {
            errorMessages[id]
        }
        val name = if (fullError) "Full error" else "Error"
        ModUtils.chat(errorMessage?.let {
            ClipboardUtils.copyToClipboard(it)
            "§e[BedWar] $name copied into the clipboard, uhh do something with it"
        } ?: "§c[BedWar] Error id not found!")
    }

    fun logError(throwable: Throwable, message: String) {
        val error = Error(message, throwable)
        Minecraft.getMinecraft().thePlayer ?: throw error
        error.printStackTrace()

        val pair = if (throwable.stackTrace.isNotEmpty()) {
            throwable.stackTrace[0].let { it.fileName to it.lineNumber }
        } else message to 0
        if (cache.getIfPresent(pair) != null) return
        cache.put(pair, Unit)

        val fullStackTrace = throwable.getExactStackTrace(true).joinToString("\n")
        val stackTrace = throwable.getExactStackTrace(false).joinToString("\n").removeSpam()
        val randomId = UUID.randomUUID().toString()

        val rawMessage = message.removeColour()
        errorMessages[randomId] = "```\nBedWar Mod ${BedWarMod.version}: $rawMessage\n \n$stackTrace\n```"
        fullErrorMessages[randomId] =
            "```\nBedWar Mod ${BedWarMod.version}: $rawMessage\n(full stack trace)\n \n$fullStackTrace\n```"

        ModUtils.clickableChat(
            "§c[BedWar] ${BedWarMod.version}]: $message§c. Click here to copy the error into the clipboard.",
            "bwcopyerror $randomId"
        )
    }
}

private fun Throwable.getExactStackTrace(full: Boolean, parent: List<String> = emptyList()): List<String> = buildList {
    add("Caused by " + javaClass.name + ": $message")

    val breakAfter = listOf(
        "at net.minecraftforge.client.ClientCommandHandler.executeCommand(",
    )
    val replace = mapOf(
        "com.calmwolfs.bedwar" to "BW",
    )

    for (traceElement in stackTrace) {
        var text = "\tat $traceElement"
        if (!full) {
            if (text in parent) {
                println("broke at: $text")
                break
            }
        }
        if (!full) {
            for ((from, to) in replace) {
                text = text.replace(from, to)
            }
        }
        add(text)
        if (!full) {
            if (breakAfter.any { text.contains(it) }) {
                println("breakAfter: $text")
                break
            }
        }
    }

    cause?.let {
        addAll(it.getExactStackTrace(full, this))
    }
}

private fun String.removeSpam(): String {
    val ignored = listOf(
        "at io.netty.",
        "at net.minecraft.network.",
        "at net.minecraftforge.fml.common.network.handshake.",
        "at java.lang.Thread.run",
        "at com.google.gson.internal.",
        "at net.minecraftforge.fml.common.eventhandler.",
        "at java.util.concurrent.",
        "at sun.reflect.",
        "at net.minecraft.client.Minecraft.addScheduledTask(",
        "at java.lang.reflect.",
    )
    return split("\n").filter { line -> !ignored.any { line.contains(it) } }.joinToString("\n")
}