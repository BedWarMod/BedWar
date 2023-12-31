package com.calmwolfs.bedwar.commands.testcommands

import com.calmwolfs.bedwar.data.game.ScoreboardData
import com.calmwolfs.bedwar.utils.ChatUtils
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import com.calmwolfs.bedwar.utils.computer.ClipboardUtils

object CopyScoreboardCommand {
    fun command(args: Array<String>) {
        val resultList = mutableListOf<String>()
        val noColour = args.size == 1 && args[0] == "true"
        resultList.add("Header:")
        resultList.add(if (noColour) ScoreboardData.getObjective().unformat() else ScoreboardData.getObjective())
        resultList.add("")

        for (line in ScoreboardData.getScoreboard()) {
            val scoreboardLine = if (noColour) line.unformat() else line
            resultList.add("'$scoreboardLine'")
        }

        val string = resultList.joinToString("\n")
        ClipboardUtils.copyToClipboard(string)
        ChatUtils.chat("§e[BedWar] scoreboard copied into your clipboard!")
    }
}