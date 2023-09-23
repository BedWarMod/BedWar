package com.calmwolfs.bedwar.data.types

import net.minecraft.item.ItemStack

class Inventory(
    val windowId: Int,
    val title: String,
    val slotCount: Int,
    val items: MutableMap<Int, ItemStack> = mutableMapOf(),
)