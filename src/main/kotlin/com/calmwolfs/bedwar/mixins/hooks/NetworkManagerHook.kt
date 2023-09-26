package com.calmwolfs.bedwar.mixins.hooks

import com.calmwolfs.bedwar.events.game.PacketEvent
import net.minecraft.network.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun onReceivePacket(packet: Packet<*>, ci: CallbackInfo) {
    if (PacketEvent.ReceiveEvent(packet).postAndCatch()) ci.cancel()
}