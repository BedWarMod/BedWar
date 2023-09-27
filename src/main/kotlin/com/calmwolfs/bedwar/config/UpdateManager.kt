package com.calmwolfs.bedwar.config

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.events.game.ModTickEvent
import com.calmwolfs.bedwar.config.features.AboutConfig
import com.calmwolfs.bedwar.config.update.ConfigVersionDisplay
import com.calmwolfs.bedwar.config.update.GuiOptionEditorUpdateCheck
import com.calmwolfs.bedwar.events.ConfigLoadEvent
import com.calmwolfs.bedwar.utils.ChatUtils
import com.calmwolfs.bedwar.utils.MinecraftExecutor
import com.calmwolfs.bedwar.utils.ModUtils.onToggle
import io.github.moulberry.moulconfig.processor.MoulConfigProcessor
import moe.nea.libautoupdate.*
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.concurrent.CompletableFuture

object UpdateManager {
    private var _activePromise: CompletableFuture<*>? = null
    private var activePromise: CompletableFuture<*>?
        get() = _activePromise
        set(value) {
            _activePromise?.cancel(true)
            _activePromise = value
        }

    var updateState: UpdateState = UpdateState.NONE
        private set

    fun getNextVersion(): String? {
        return potentialUpdate?.update?.versionNumber?.asString
    }

    @SubscribeEvent
    fun onConfigLoad(event: ConfigLoadEvent) {
        BedWarMod.feature.about.updateStream.onToggle {
            reset()
        }
    }

    @SubscribeEvent
    fun onTick(event: ModTickEvent) {
        Minecraft.getMinecraft().thePlayer ?: return
        MinecraftForge.EVENT_BUS.unregister(this)
        if (config.autoUpdates)
            checkUpdate()
    }

    fun getCurrentVersion(): String {
        return BedWarMod.version
    }

    fun injectConfigProcessor(processor: MoulConfigProcessor<*>) {
        processor.registerConfigEditor(ConfigVersionDisplay::class.java) { option, _ ->
            GuiOptionEditorUpdateCheck(option)
        }
    }

    private fun isPreRelease(): Boolean {
        return getCurrentVersion().contains("pre", ignoreCase = true)
    }

    private val config get() = BedWarMod.feature.about

    private fun reset() {
        updateState = UpdateState.NONE
        _activePromise = null
        potentialUpdate = null
        println("Reset update state")
    }

    fun checkUpdate() {
        if (updateState != UpdateState.NONE) {
            println("Trying to perform update check while another update is already in progress")
            return
        }
        println("Starting update check")
        var updateStream = config.updateStream.get()
        if (updateStream == AboutConfig.UpdateStream.RELEASES && isPreRelease()) {
            updateStream = AboutConfig.UpdateStream.PRE
        }
        activePromise = context.checkUpdate(updateStream.stream)
            .thenAcceptAsync({
                println("Update check completed")
                if (updateState != UpdateState.NONE) {
                    println("This appears to be the second update check. Ignoring this one")
                    return@thenAcceptAsync
                }
                potentialUpdate = it
                if (it.isUpdateAvailable) {
                    updateState = UpdateState.AVAILABLE
                    ChatUtils.clickableChat(
                        "§e[BedWar] §aBedWar Mod found a new update: ${it.update.versionName}. " +
                                "Go check §b/bw download update §afor more info.",
                        "bw download"
                    )
                }
            }, MinecraftExecutor.OnThread)
    }

    fun queueUpdate() {
        if (updateState != UpdateState.AVAILABLE) {
            println("Trying to enqueue an update while another one is already downloaded or none is present")
        }
        updateState = UpdateState.QUEUED
        activePromise = CompletableFuture.supplyAsync {
            println("Update download started")
            potentialUpdate!!.prepareUpdate()
        }.thenAcceptAsync({
            println("Update download completed, setting exit hook")
            updateState = UpdateState.DOWNLOADED
            potentialUpdate!!.executeUpdate()
        }, MinecraftExecutor.OnThread)
    }

    private val context = UpdateContext(
        UpdateSource.githubUpdateSource("BedWarMod", "BedWar"),
        UpdateTarget.deleteAndSaveInTheSameFolder(UpdateManager::class.java),
        CurrentVersion.ofTag(BedWarMod.version),
        BedWarMod.MODID,
    )

    init {
        context.cleanup()
    }

    enum class UpdateState {
        AVAILABLE,
        QUEUED,
        DOWNLOADED,
        NONE
    }

    private var potentialUpdate: PotentialUpdate? = null
}