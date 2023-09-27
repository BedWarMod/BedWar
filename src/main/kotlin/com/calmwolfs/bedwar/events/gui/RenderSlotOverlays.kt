package com.calmwolfs.bedwar.events.gui

import com.calmwolfs.bedwar.events.ModEvent
import net.minecraft.item.ItemStack

class RenderSlotOverlays(val stack: ItemStack?, val x: Int, val y: Int) : ModEvent()