package com.calmwolfs.bedwar.data.game

import com.calmwolfs.bedwar.events.game.ModTooltipEvent
import net.minecraft.inventory.Slot
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class TooltipData {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onTooltip(event: ItemTooltipEvent) {
        val toolTip = event.toolTip ?: return
        val slot = lastSlot ?: return
        ModTooltipEvent(slot, event.itemStack, toolTip).postAndCatch()
    }

    companion object {
        var lastSlot: Slot? = null
    }
}