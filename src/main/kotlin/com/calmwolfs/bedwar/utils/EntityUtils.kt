package com.calmwolfs.bedwar.utils

import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.player.EntityPlayer

object EntityUtils {
    fun getAllEntities(): Sequence<Entity> = Minecraft.getMinecraft()?.theWorld?.loadedEntityList?.let {
        if (Minecraft.getMinecraft().isCallingFromMinecraftThread) it else it.toMutableList()
    }?.asSequence() ?: emptySequence()

    val EntityLivingBase.baseMaxHealth: Int
        get() = this.getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue.toInt()

    fun EntityEnderman.getBlockInHand(): IBlockState? = heldBlockState

    fun EntityPlayer.getSkinTexture(): String? {
        val gameProfile = gameProfile ?: return null

        return gameProfile.properties.entries()
            .filter { it.key == "textures" }
            .map { it.value }
            .firstOrNull { it.name == "textures" }
            ?.value
    }
}