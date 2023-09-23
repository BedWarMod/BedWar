package com.calmwolfs.bedwar.commands.testcommands

import com.calmwolfs.bedwar.data.game.TablistData
import com.calmwolfs.bedwar.utils.ModUtils
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import com.calmwolfs.bedwar.utils.computer.ClipboardUtils

object CopyTablistCommand {
    fun command(args: Array<String>) {
        val resultList = mutableListOf<String>()
        val noColor = args.size == 1 && args[0] == "true"

        for (line in TablistData.tablist) {
            val tabListLine = if (noColor) line.unformat() else line
            if (tabListLine != "") resultList.add("'$tabListLine'")
        }

        val header = if (noColor) TablistData.header.unformat() else TablistData.header
        val footer = if (noColor) TablistData.footer.unformat() else TablistData.footer

        val string = "Header:\n\n$header\n\nBody:\n\n${resultList.joinToString("\n")}\n\nFooter:\n\n$footer"
        ClipboardUtils.copyToClipboard(string)
        ModUtils.chat("§e[BedWar] tablist copied into your clipboard!")
    }
}