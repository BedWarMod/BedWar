package com.calmwolfs.bedwar.features.inventory

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.data.types.ModColours
import com.calmwolfs.bedwar.events.game.ModTooltipEvent
import com.calmwolfs.bedwar.events.gui.GuiContainerEvent
import com.calmwolfs.bedwar.events.inventory.InventoryCloseEvent
import com.calmwolfs.bedwar.events.inventory.InventoryFullyOpenedEvent
import com.calmwolfs.bedwar.events.inventory.OwnInventoryUpdateEvent
import com.calmwolfs.bedwar.events.inventory.SlotClickEvent
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.InventoryUtils
import com.calmwolfs.bedwar.utils.ItemRenderUtils.background
import com.calmwolfs.bedwar.utils.ItemUtils.getLore
import com.calmwolfs.bedwar.utils.computer.KeyboardUtils
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ShopInventoryOverlay {
    private val config get() = BedWarMod.feature.inventory.shopInventoryOverlay
    private var inInventory = false

    private val blockList = mutableListOf<Int>()
    private val affordableList = mutableListOf<Int>()

    @SubscribeEvent
    fun onInventoryClose(event: InventoryCloseEvent) {
        inInventory = false
        blockList.clear()
        affordableList.clear()
    }

    @SubscribeEvent
    fun onInventoryChange(event: OwnInventoryUpdateEvent) {
        if (!inInventory) return

        getSlotIndexes(InventoryUtils.getItemsInOpenChest())
    }

    @SubscribeEvent
    fun onInventoryOpen(event: InventoryFullyOpenedEvent) {
        inInventory = false
        if (!config.blockClicks && !config.hidePurchased && !config.showAffordable) return

        if (!BedwarsUtils.playingBedwars) return
        if (event.inventoryName !in InventoryUtils.shopNames) {
            return
        }

        getSlotIndexes(event.inventoryItems)
        inInventory = true
    }

    @SubscribeEvent
    fun onBackgroundDrawn(event: GuiContainerEvent.BackgroundDrawnEvent) {
        if (!inInventory) return
        if (event.gui !is GuiChest) return
        val guiChest = event.gui
        val chest = guiChest.inventorySlots as ContainerChest

        for (slot in chest.inventorySlots) {
            if (slot == null) continue
            if (slot.stack == null) continue

            if (config.showAffordable && slot.slotIndex in affordableList) {
                val opacity = config.opacityGreen
                val colour = ModColours.GREEN.addOpacity(opacity)
                slot.stack.background = colour.rgb
            } else if (config.hidePurchased && slot.slotIndex in blockList) {
                val opacity = config.opacityGrey
                val colour = ModColours.DARK_GREY.addOpacity(opacity)
                slot.stack.background = colour.rgb
            }
        }
    }

    @SubscribeEvent
    fun onSlotClick(event: SlotClickEvent) {
        if (!config.blockClicks || !inInventory) return
        if (KeyboardUtils.isShiftKeyDown()) return

        val slot = event.slot

        if (slot.slotIndex in blockList) {
            event.isCanceled = true
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onTooltip(event: ModTooltipEvent) {
        if (!inInventory) return

        val slot = event.slot

        if (slot.slotIndex in blockList) {
            event.toolTip.clear()
            event.toolTip.add("§eItem already purchased")
        }
    }

    private fun getSlotIndexes(inventory: Map<Int, ItemStack>) {
        blockList.clear()
        affordableList.clear()
        for (item in inventory) {
            val lore = item.value.getLore()
            if (lore.isNotEmpty()) {
                if (lore.last() == "§eClick to purchase!") {
                    affordableList.add(item.key)
                } else if (lore.last() in notClickable) {
                    blockList.add(item.key)
                }
            }
        }
    }

    private val notClickable = listOf(
        "§aUNLOCKED",
        "§aMAXED!",
        "§8⬇ §7Traps Queue"
    )
}