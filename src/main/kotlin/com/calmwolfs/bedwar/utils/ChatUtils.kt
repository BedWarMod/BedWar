package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.mixins.transformers.AccessorChatComponentText
import com.calmwolfs.bedwar.utils.StringUtils.matchMatcher
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import java.util.function.Predicate

object ChatUtils {
    private val playerChatPattern = ".*§[f7]: .*".toPattern()
    private val chatUsernamePattern = "^(?:\\[\\d+] )?(?:\\S )?(?:\\[\\w.+] )?(?<username>\\w+)(?: \\[.+?])?\$".toPattern()

    fun String.getPlayerName(): String {
        if (!playerChatPattern.matcher(this).matches()) return "-"

        var username = this.unformat().split(":")[0]

        if (username.contains(">")) {
            username = username.substring(username.indexOf('>') + 1).trim()
        }
        username = username.removePrefix("From ")
        username = username.removePrefix("To ")

        chatUsernamePattern.matchMatcher(username) {
            return group("username")
        }
        return "-"
    }

    private fun modifyFirstChatComponent(chatComponent: IChatComponent, action: Predicate<IChatComponent>): Boolean {
        if (action.test(chatComponent)) {
            return true
        }
        for (sibling in chatComponent.siblings) {
            if (modifyFirstChatComponent(sibling, action)) {
                return true
            }
        }
        return false
    }

    fun replaceFirstChatText(chatComponent: IChatComponent, toReplace: String, replacement: String): IChatComponent {
        modifyFirstChatComponent(chatComponent) { component ->
            if (component is ChatComponentText) {
                component as AccessorChatComponentText
                if (component.bedwar_text().contains(toReplace)) {
                    component.bedwar_setText(component.bedwar_text().replace(toReplace, replacement))
                    return@modifyFirstChatComponent true
                }
                return@modifyFirstChatComponent false
            }
            return@modifyFirstChatComponent false
        }
        return chatComponent
    }
}