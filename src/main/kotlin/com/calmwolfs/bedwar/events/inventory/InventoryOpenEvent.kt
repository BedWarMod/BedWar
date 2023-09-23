package com.calmwolfs.bedwar.events.inventory

import com.calmwolfs.bedwar.data.types.Inventory
import com.calmwolfs.bedwar.events.ModEvent
import net.minecraft.item.ItemStack

open class InventoryOpenEvent(inventory: Inventory) : ModEvent() {
    val inventoryId: Int by lazy { inventory.windowId }
    val inventoryName: String by lazy { inventory.title }
    val inventorySize: Int by lazy { inventory.slotCount }
    val inventoryItems: Map<Int, ItemStack> by lazy { inventory.items }
}

class InventoryFullyOpenedEvent(inventory: Inventory) : InventoryOpenEvent(inventory)

class InventoryUpdatedEvent(inventory: Inventory) : InventoryOpenEvent(inventory)