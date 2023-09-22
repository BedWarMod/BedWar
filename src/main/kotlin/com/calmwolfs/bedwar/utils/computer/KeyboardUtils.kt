package com.calmwolfs.bedwar.utils.computer

import io.github.moulberry.moulconfig.internal.KeybindHelper
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

object KeyboardUtils {
    fun isControlKeyDown() = isKeyHeld(Keyboard.KEY_LCONTROL) || isKeyHeld(Keyboard.KEY_RCONTROL)
    fun isShiftKeyDown() = isKeyHeld(Keyboard.KEY_LSHIFT) || isKeyHeld(Keyboard.KEY_RSHIFT)

    fun isKeyHeld(keyCode: Int): Boolean {
        if (keyCode == 0) return false
        return if (keyCode < 0) {
            Mouse.isButtonDown(keyCode + 100)
        } else {
            KeybindHelper.isKeyDown(keyCode)
        }
    }
}