package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.data.game.OwnInventoryData
import net.minecraft.item.ItemStack

object InventoryUtils {
    fun countItemsInLowerInventory(predicate: (ItemStack) -> Boolean): Int {
        return OwnInventoryData.inventoryItems.filter { predicate(it) }.sumOf { it.stackSize }
    }

    fun countItemsInInventory(itemsMap: Map<Int, ItemStack>, predicate: (ItemStack) -> Boolean): Int {
        return itemsMap.values.filter { predicate(it) }.sumOf { it.stackSize }
    }
}