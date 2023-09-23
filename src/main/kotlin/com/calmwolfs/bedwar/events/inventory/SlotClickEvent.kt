package com.calmwolfs.bedwar.events.inventory

import com.calmwolfs.bedwar.events.ModEvent
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Slot
import net.minecraftforge.fml.common.eventhandler.Cancelable

@Cancelable
class SlotClickEvent(
    val guiContainer: GuiContainer,
    val slot: Slot,
    val slotId: Int,
    var clickedButton: Int,
    var clickType: Int
) : ModEvent() {
    var usePickblock = false
    fun usePickblockInstead() {
        usePickblock = true
    }
}