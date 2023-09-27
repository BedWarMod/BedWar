package com.calmwolfs.bedwar.mixins.hooks

import com.calmwolfs.bedwar.events.gui.RenderSlotOverlays
import net.minecraft.item.ItemStack

fun renderItemReturn(stack: ItemStack, x: Int, y: Int) {
    RenderSlotOverlays(stack, x, y).postAndCatch()
}