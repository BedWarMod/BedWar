package com.calmwolfs.bedwar.events.gui

import com.calmwolfs.bedwar.events.ModEvent

open class GuiRenderEvent : ModEvent() {
    class ChestGuiOverlayRenderEvent : GuiRenderEvent()
    class GuiOverlayRenderEvent : GuiRenderEvent()
}