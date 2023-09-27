package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.data.jsonobjects.ExperienceJson
import com.calmwolfs.bedwar.events.RepositoryReloadEvent
import com.calmwolfs.bedwar.utils.NumberUtils.addSeparators
import com.calmwolfs.bedwar.utils.NumberUtils.round
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.floor

object BedwarsStarUtils {
    private var easyLevelCount = -1
    private var easyLevelExp = listOf<Int>()
    private var easyLevelExpTotal = -1
    private var expPerLevel = -1
    private var expPerPrestige = -1
    private var levelsPerPrestige = -1

    @SubscribeEvent
    fun onRepoReload(event: RepositoryReloadEvent) {
        try {
            val data = event.getConstant<ExperienceJson>("Experience") ?: return
            easyLevelCount = data.easyLevelCount
            easyLevelExp = data.easyLevelExp
            easyLevelExpTotal = data.easyLevelExpTotal
            expPerLevel = data.expPerLevel
            expPerPrestige = data.expPerPrestige
            levelsPerPrestige = data.levelsPerPrestige
            BedWarMod.repo.successfulConstants.add("Experience")
        } catch (e: Exception) {
            ModUtils.error("Error in repository reload event (Experience)")
            BedWarMod.repo.unsuccessfulConstants.add("Experience")
        }
    }

    fun testExperience(args: Array<String>) {
        if (args.isEmpty()) return
        val amount = args[0]
        try {
            val intValue = amount.toInt()
             getStarForExp(intValue, true)
        } catch (_: NumberFormatException) {
            ModUtils.warning("[BedWar] ${args[0]} is not a valid integer")
        }
    }

    fun getStarForExp(experience: Int, test: Boolean = false): Double {
        if (test) ChatUtils.chat("\nInput exp: $experience")
        var remainingExp = experience.toDouble()
        var level = 0.0
        val prestiges = (floor(remainingExp / expPerPrestige)).toInt()
        remainingExp -= expPerPrestige * prestiges
        level += prestiges * levelsPerPrestige
        if (test) ChatUtils.chat("prestiges: $prestiges level: $level remaining exp: ${remainingExp.addSeparators()}")

        if (remainingExp > easyLevelExpTotal) {
            level += easyLevelCount
            remainingExp -= easyLevelExpTotal
            if (test) ChatUtils.chat("more than easyXp! | Remaining exp: ${remainingExp.addSeparators()}")

            while (remainingExp > expPerLevel) {
                level++
                remainingExp -= expPerLevel
            }
            level += remainingExp / expPerLevel
        } else {
            if (test) ChatUtils.chat("less than easyXp! | Remaining exp: ${remainingExp.addSeparators()}")
            for (expAmount in easyLevelExp) {
                if (remainingExp > expAmount) {
                    if (test) ChatUtils.chat("added level needed: ${expAmount.addSeparators()} had: ${remainingExp.addSeparators()}")
                    level++
                    remainingExp -= expAmount
                } else {
                    if (test) ChatUtils.chat("didnt add level needed: ${expAmount.addSeparators()} had: ${remainingExp.addSeparators()}")
                    level += remainingExp / expAmount
                    remainingExp = 0.0
                }
            }
        }

        if (test) ChatUtils.chat("Final level: $level\n")
        return level.round(2)
    }
}