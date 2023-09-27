package com.calmwolfs.bedwar.features.inventory

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.events.inventory.InventoryCloseEvent
import com.calmwolfs.bedwar.events.inventory.InventoryFullyOpenedEvent
import com.calmwolfs.bedwar.events.inventory.SlotClickEvent
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.InventoryUtils
import com.calmwolfs.bedwar.utils.computer.KeyboardUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ShopMiddleClick {
    private val config get() = BedWarMod.feature.inventory
    private var inInventory = false

    @SubscribeEvent
    fun onInventoryClose(event: InventoryCloseEvent) {
        inInventory = false
    }

    @SubscribeEvent
    fun onInventoryOpen(event: InventoryFullyOpenedEvent) {
        inInventory = false

        if (!BedwarsUtils.playingBedwars) return
        if (event.inventoryName !in InventoryUtils.shopNames) {
            return
        }

        inInventory = true
    }

    @SubscribeEvent
    fun onSlotClick(event: SlotClickEvent) {
        if (!config.shopMiddleClick || !inInventory) return
        if (KeyboardUtils.isShiftKeyDown()) return
        event.usePickblockInstead()
    }
}