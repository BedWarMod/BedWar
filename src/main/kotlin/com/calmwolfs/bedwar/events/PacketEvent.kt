package com.calmwolfs.bedwar.events

import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Cancelable

@Cancelable
abstract class PacketEvent : ModEvent() {
    abstract val direction: Direction
    abstract val packet: Packet<*>

    data class ReceiveEvent(override val packet: Packet<*>) : PacketEvent() {
        override val direction = Direction.INBOUND
    }

    data class SendEvent(override val packet: Packet<*>) : PacketEvent() {
        override val direction = Direction.OUTBOUND
    }

    enum class Direction {
        INBOUND, OUTBOUND
    }
}