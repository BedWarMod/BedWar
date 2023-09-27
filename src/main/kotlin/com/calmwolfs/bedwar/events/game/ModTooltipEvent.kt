package com.calmwolfs.bedwar.events.game

import com.calmwolfs.bedwar.events.ModEvent
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class ModTooltipEvent(val slot: Slot, val itemStack: ItemStack, var toolTip: MutableList<String>) : ModEvent()