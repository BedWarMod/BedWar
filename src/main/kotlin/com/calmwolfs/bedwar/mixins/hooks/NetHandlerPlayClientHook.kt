package com.calmwolfs.bedwar.mixins.hooks

import com.calmwolfs.bedwar.events.PacketEvent
import net.minecraft.network.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun onSendPacket(packet: Packet<*>, ci: CallbackInfo) {
    if (PacketEvent.SendEvent(packet).postAndCatch()) ci.cancel()
}