package com.calmwolfs.bedwar.utils.computer

import com.calmwolfs.bedwar.events.game.ModKeyPressEvent
import com.calmwolfs.bedwar.events.game.ModTickEvent
import io.github.moulberry.moulconfig.internal.KeybindHelper
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

object KeyboardUtils {
    private var lastClickedMouseButton = -1

    fun isControlKeyDown() = isKeyHeld(Keyboard.KEY_LCONTROL) || isKeyHeld(Keyboard.KEY_RCONTROL)
    fun isShiftKeyDown() = isKeyHeld(Keyboard.KEY_LSHIFT) || isKeyHeld(Keyboard.KEY_RSHIFT)

    private fun isKeyHeld(keyCode: Int): Boolean {
        if (keyCode == 0) return false
        return if (keyCode < 0) {
            Mouse.isButtonDown(keyCode + 100)
        } else {
            KeybindHelper.isKeyDown(keyCode)
        }
    }

    @SubscribeEvent
    fun onTick(event: ModTickEvent) {
        if (Mouse.getEventButtonState() && Mouse.getEventButton() != -1) {
            val key = Mouse.getEventButton() - 100
            ModKeyPressEvent(key).postAndCatch()
            lastClickedMouseButton = key
            return
        }

        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() != 0) {
            val key = Keyboard.getEventKey()
            ModKeyPressEvent(key).postAndCatch()
            lastClickedMouseButton = -1
            return
        }

        if (Mouse.getEventButton() == -1 && lastClickedMouseButton != -1) {
            if (isKeyHeld(lastClickedMouseButton)) {
                ModKeyPressEvent(lastClickedMouseButton).postAndCatch()
                return
            }
            lastClickedMouseButton = -1
        }
    }
}