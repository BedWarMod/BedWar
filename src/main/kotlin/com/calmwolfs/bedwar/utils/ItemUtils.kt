package com.calmwolfs.bedwar.utils

import net.minecraft.item.ItemStack

object ItemUtils {
    fun ItemStack.getLore(): List<String> {
        val tagCompound = this.tagCompound ?: return emptyList()
        val tagList = tagCompound.getCompoundTag("display").getTagList("Lore", 8)
        val list: MutableList<String> = ArrayList()
        for (i in 0 until tagList.tagCount()) {
            list.add(tagList.getStringTagAt(i))
        }
        return list
    }

    var ItemStack.name: String?
        get() = this.displayName
        set(value) {
            setStackDisplayName(value)
        }
}