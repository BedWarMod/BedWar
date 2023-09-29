package com.calmwolfs.bedwar.commands.testcommands

import com.calmwolfs.bedwar.data.game.TablistData
import com.calmwolfs.bedwar.utils.ChatUtils
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import com.calmwolfs.bedwar.utils.computer.ClipboardUtils

object CopyTablistCommand {
    fun command(args: Array<String>) {
        val resultList = mutableListOf<String>()
        val noColour = args.size == 1 && args[0] == "true"

        for (line in TablistData.getTablist()) {
            val tabListLine = if (noColour) line.unformat() else line
            if (tabListLine != "") resultList.add("'$tabListLine'")
        }

        val header = if (noColour) TablistData.getHeader().unformat() else TablistData.getHeader()
        val footer = if (noColour) TablistData.getFooter().unformat() else TablistData.getFooter()

        val string = "Header:\n\n$header\n\nBody:\n\n${resultList.joinToString("\n")}\n\nFooter:\n\n$footer"
        ClipboardUtils.copyToClipboard(string)
        ChatUtils.chat("§e[BedWar] tablist copied into your clipboard!")
    }
}