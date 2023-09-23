package com.calmwolfs.bedwar.features.inventory

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.data.game.OtherInventoryData
import com.calmwolfs.bedwar.events.gui.GuiRenderEvent
import com.calmwolfs.bedwar.events.inventory.InventoryCloseEvent
import com.calmwolfs.bedwar.events.inventory.InventoryFullyOpenedEvent
import com.calmwolfs.bedwar.events.inventory.OwnInventoryUpdateEvent
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.InventoryUtils
import com.calmwolfs.bedwar.utils.ListUtils.addAsSingletonList
import com.calmwolfs.bedwar.utils.gui.GuiElementUtils.renderStringsAndItems
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ResourceOverlay {
    private val config get() = BedWarMod.feature.inventory.resourceOverlay
    private var display = emptyList<List<Any>>()

    private val ownResources = mutableMapOf<ItemStack, Int>()
    private val enderChestResources = mutableMapOf<ItemStack, Int>()

    private var inInventory = false

    @SubscribeEvent
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (inInventory && OtherInventoryData.currentInventory?.items != null) {
            println("was in echest last: ${event.inventoryName == " Ender Chest"}")
            val otherInventoryItems = OtherInventoryData.currentInventory!!.items

            for (trackedItem in trackedItems) {
                val count = InventoryUtils.countItemsInInventory(otherInventoryItems) { it.item == trackedItem.item }
                enderChestResources[trackedItem] = count
            }
        }

        inInventory = false
    }

    @SubscribeEvent
    fun onInventoryOpen(event: InventoryFullyOpenedEvent) {
        inInventory = false

        if (!BedwarsUtils.inBedwarsGame) return
        if (event.inventoryName != "Ender Chest") {
            return
        }

        inInventory = true
        display = drawResourceDisplay()
    }

    @SubscribeEvent
    fun onInventoryChange(event: OwnInventoryUpdateEvent) {
        if (!BedwarsUtils.inBedwarsGame) return

        for (trackedItem in trackedItems) {
            val count = InventoryUtils.countItemsInLowerInventory { it.item == trackedItem.item }
            ownResources[trackedItem] = count
        }

        if (inInventory && OtherInventoryData.currentInventory?.items != null) {
            val otherInventoryItems = OtherInventoryData.currentInventory!!.items

            for (trackedItem in trackedItems) {
                val count = InventoryUtils.countItemsInInventory(otherInventoryItems) { it.item == trackedItem.item }
                enderChestResources[trackedItem] = count
            }
        }

        display = drawResourceDisplay()
    }

    @SubscribeEvent
    fun onRenderOverlay(event: GuiRenderEvent.GuiOverlayRenderEvent) {
        if (!config.enabled) return
        if (!BedwarsUtils.inBedwarsGame) return

        config.position.renderStringsAndItems(display, posLabel = "Resource Overlay")
    }

    private fun drawResourceDisplay() = buildList<List<Any>> {
        addAsSingletonList("§e§lResources")
        trackedItems.forEach { trackedItem ->
            val inventoryCount = ownResources[trackedItem] ?: 0
            val enderChestCount = enderChestResources[trackedItem] ?: 0
            val total = inventoryCount + enderChestCount

            val line = if (config.showTotal) "§a$inventoryCount §7+ §a$enderChestCount §7(§a$total§7)" else "§a$inventoryCount §7+ §a$enderChestCount"
            add(listOf(trackedItem, line))
        }
    }

    private val trackedItems = listOf(
        ItemStack(Items.iron_ingot),
        ItemStack(Items.gold_ingot),
        ItemStack(Items.diamond),
        ItemStack(Items.emerald)
    )
}