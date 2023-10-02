package com.calmwolfs.bedwar.data.game

import com.calmwolfs.bedwar.events.game.ModTickEvent
import com.calmwolfs.bedwar.events.game.PacketEvent
import com.calmwolfs.bedwar.events.inventory.OwnInventoryUpdateEvent
import com.calmwolfs.bedwar.utils.BedwarsUtils
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.S2FPacketSetSlot
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object OwnInventoryData {
    var inventoryItems = listOf<ItemStack>()

    @SubscribeEvent
    fun onChatPacket(event: PacketEvent.ReceiveEvent) {
        val packet = event.packet
        if (Minecraft.getMinecraft().thePlayer == null) return

        if (packet !is S2FPacketSetSlot) return
        val windowId = packet.func_149175_c()
        if (windowId == 0) {
            val invItems = getItemsInOwnInventory()
            if (invItems != inventoryItems) {
                inventoryItems = invItems
                OwnInventoryUpdateEvent().postAndCatch()
            }
        }
    }

    @SubscribeEvent
    fun onTick(event: ModTickEvent) {
        if (!BedwarsUtils.inBedwarsArea && !event.repeatSeconds(1)) return
        val invItems = getItemsInOwnInventory()
        if (invItems != inventoryItems) {
            inventoryItems = invItems
            OwnInventoryUpdateEvent().postAndCatch()
        }
    }

    private fun getItemsInOwnInventory() = Minecraft.getMinecraft().thePlayer.inventory.mainInventory.filterNotNull()
}