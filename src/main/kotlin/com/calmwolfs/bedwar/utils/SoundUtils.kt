package com.calmwolfs.bedwar.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.audio.ISound
import net.minecraft.client.audio.PositionedSound
import net.minecraft.client.audio.SoundCategory
import net.minecraft.util.ResourceLocation

object SoundUtils {
    private val beepSound by lazy { createSound("random.orb", 1f) }

    private fun ISound.playSound() {
        Minecraft.getMinecraft().addScheduledTask {
            val gameSettings = Minecraft.getMinecraft().gameSettings
            val oldLevel = gameSettings.getSoundLevel(SoundCategory.PLAYERS)
            gameSettings.setSoundLevel(SoundCategory.PLAYERS, 1f)
            try {
                Minecraft.getMinecraft().soundHandler.playSound(this)
            } catch (e: Exception) {
                if (e is IllegalArgumentException) {
                    e.message?.let {
                        if (it.startsWith("value already present:")) {
                            println("BedWar sound error: $it")
                            return@addScheduledTask
                        }
                    }
                }
                e.printStackTrace()
            } finally {
                gameSettings.setSoundLevel(SoundCategory.PLAYERS, oldLevel)
            }
        }
    }

    private fun createSound(name: String, pitch: Float): ISound {
        val sound: ISound = object : PositionedSound(ResourceLocation(name)) {
            init {
                volume = 50f
                repeat = false
                repeatDelay = 0
                attenuationType = ISound.AttenuationType.NONE
                this.pitch = pitch
            }
        }
        return sound
    }

    fun playBeepSound() {
        beepSound.playSound()
    }
}