package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.data.game.OwnInventoryData
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.item.ItemStack

object InventoryUtils {
    fun countItemsInLowerInventory(predicate: (ItemStack) -> Boolean): Int {
        return OwnInventoryData.inventoryItems.filter { predicate(it) }.sumOf { it.stackSize }
    }

    fun countItemsInOpenInventory(predicate: (ItemStack) -> Boolean): Int {
        return getItemsInOpenChest().filter { predicate(it) }.sumOf { it.stackSize }
    }

    private fun getItemsInOpenChest(): MutableList<ItemStack> {
        val list = mutableListOf<ItemStack>()
        val guiChest = Minecraft.getMinecraft().currentScreen as? GuiChest ?: return list

        val inventorySlots = guiChest.inventorySlots.inventorySlots
        val skipAt = inventorySlots.size - 9 * 4
        var i = 0
        for (slot in inventorySlots) {
            val stack = slot.stack
            if (stack != null) {
                list.add(stack)
            }
            i++
            if (i == skipAt) break
        }

        return list
    }
}