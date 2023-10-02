package com.calmwolfs.bedwar.commands.testcommands

import com.calmwolfs.bedwar.data.types.toModVector
import com.calmwolfs.bedwar.utils.ChatUtils
import com.calmwolfs.bedwar.utils.EntityUtils
import com.calmwolfs.bedwar.utils.EntityUtils.baseMaxHealth
import com.calmwolfs.bedwar.utils.EntityUtils.getBlockInHand
import com.calmwolfs.bedwar.utils.EntityUtils.getSkinTexture
import com.calmwolfs.bedwar.utils.ItemUtils.getSkullTexture
import com.calmwolfs.bedwar.utils.LocationUtils
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import com.calmwolfs.bedwar.utils.computer.ClipboardUtils
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.monster.EntityMagmaCube
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

object CopyEntitiesCommand {
    fun command(args: Array<String>) {
        var searchRadius = 10
        if (args.size == 1) {
            searchRadius = args[0].toInt()
        }

        val start = LocationUtils.playerLocation()

        val resultList = mutableListOf<String>()
        var counter = 0

        for (entity in EntityUtils.getAllEntities()) {
            val position = entity.position
            val vec = position.toModVector()
            val distance = start.distance(vec)
            if (distance < searchRadius) {
                val simpleName = entity.javaClass.simpleName
                resultList.add("entity: $simpleName")
                val displayName = entity.displayName
                resultList.add("name: '" + entity.name + "'")
                resultList.add("displayName: '${displayName.formattedText}'")
                resultList.add("entityId: ${entity.entityId}")
                resultList.add("location data:")
                resultList.add("-  vec: $vec")
                resultList.add("-  distance: $distance")

                val rotationYaw = entity.rotationYaw
                val rotationPitch = entity.rotationPitch
                resultList.add("-  rotationYaw: $rotationYaw")
                resultList.add("-  rotationPitch: $rotationPitch")

                val riddenByEntity = entity.riddenByEntity
                resultList.add("riddenByEntity: $riddenByEntity")
                val ridingEntity = entity.ridingEntity
                resultList.add("ridingEntity: $ridingEntity")

                if (entity is EntityLivingBase) {
                    resultList.add("EntityLivingBase:")
                    val baseMaxHealth = entity.baseMaxHealth
                    val health = entity.health.toInt()
                    resultList.add("-  baseMaxHealth: $baseMaxHealth")
                    resultList.add("-  health: $health")
                }

                if (entity is EntityPlayer) {
                    val inventory = entity.inventory
                    if (inventory != null) {
                        resultList.add("armor:")
                        for ((i, itemStack) in inventory.armorInventory.withIndex()) {
                            val name = itemStack?.displayName ?: "null"
                            resultList.add("-  at: $i: $name")
                        }
                    }
                }

                when (entity) {
                    is EntityArmorStand -> {
                        resultList.add("EntityArmorStand:")
                        val headRotation = entity.headRotation.toModVector()
                        val bodyRotation = entity.bodyRotation.toModVector()
                        resultList.add("-  headRotation: $headRotation")
                        resultList.add("-  bodyRotation: $bodyRotation")

                        resultList.add("-  inventory:")
                        for ((id, stack) in entity.inventory.withIndex()) {
                            resultList.add("-  id $id ($stack)")
                            printItemStackData(stack, resultList)
                        }
                    }

                    is EntityEnderman -> {
                        resultList.add("EntityEnderman:")
                        val heldBlockState = entity.getBlockInHand()
                        resultList.add("-  heldBlockState: $heldBlockState")
                        if (heldBlockState != null) {
                            val block = heldBlockState.block
                            resultList.add("-  block: $block")
                        }
                    }

                    is EntityMagmaCube -> {
                        resultList.add("EntityMagmaCube:")
                        val squishFactor = entity.squishFactor
                        val slimeSize = entity.slimeSize
                        resultList.add("-  factor: $squishFactor")
                        resultList.add("-  slimeSize: $slimeSize")
                    }

                    is EntityItem -> {
                        resultList.add("EntityItem:")
                        val stack = entity.entityItem
                        val stackName = stack.displayName
                        val cleanName = stackName.unformat()
                        val itemEnchanted = stack.isItemEnchanted
                        val itemDamage = stack.itemDamage
                        val stackSize = stack.stackSize
                        val maxStackSize = stack.maxStackSize
                        resultList.add("-  name: '$stackName'")
                        resultList.add("-  cleanName: '$cleanName'")
                        resultList.add("-  itemEnchanted: '$itemEnchanted'")
                        resultList.add("-  itemDamage: '$itemDamage'")
                        resultList.add("-  stackSize: '$stackSize'")
                        resultList.add("-  maxStackSize: '$maxStackSize'")
                    }

                    is EntityOtherPlayerMP -> {
                        resultList.add("EntityOtherPlayerMP:")

                        val skinTexture = entity.getSkinTexture()
                        resultList.add("-  skin texture: $skinTexture")
                    }
                }
                resultList.add("")
                resultList.add("")
                counter++
            }
        }

        if (counter != 0) {
            val string = resultList.joinToString("\n")
            ClipboardUtils.copyToClipboard(string)
            ChatUtils.chat("§e[BedWar] $counter entities copied into the clipboard!")
        } else {
            ChatUtils.chat("§e[BedWar] No entities found in a search radius of $searchRadius!")
        }
    }

    private fun printItemStackData(stack: ItemStack?, resultList: MutableList<String>) {
        if (stack != null) {
            val skullTexture = stack.getSkullTexture()
            if (skullTexture != null) {
                resultList.add("-     skullTexture:")
                resultList.add("-     $skullTexture")
            }

            val stackName = stack.displayName
            val cleanName = stackName.unformat()
            val type = stack.javaClass.name
            resultList.add("-     name: '$stackName'")
            resultList.add("-     cleanName: '$cleanName'")
            resultList.add("-     type: $type")
        }
    }
}