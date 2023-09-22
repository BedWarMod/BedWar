package com.calmwolfs.bedwar.events

open class GuiRenderEvent : ModEvent() {
    class ChestGuiOverlayRenderEvent : GuiRenderEvent()
    class GuiOverlayRenderEvent : GuiRenderEvent()
}