package com.calmwolfs.bedwar

import com.calmwolfs.bedwar.commands.Commands
import com.calmwolfs.bedwar.config.Features
import com.calmwolfs.bedwar.config.gui.ConfigManager
import com.calmwolfs.bedwar.config.gui.GuiEditorManager
import com.calmwolfs.bedwar.data.RenderGuiData
import com.calmwolfs.bedwar.data.game.MinecraftData
import com.calmwolfs.bedwar.data.game.ScoreboardData
import com.calmwolfs.bedwar.events.ModTickEvent
import com.calmwolfs.bedwar.features.config.PauseButton
import com.calmwolfs.bedwar.utils.HypixelUtils
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(
    modid = BedWarMod.MODID,
    clientSideOnly = true,
    useMetadata = true,
    guiFactory = "com.calmwolfs.bedwar.config.gui.ConfigGuiForgeInterop",
    version = "0.1",
)
class BedWarMod {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent?) {
        loadModule(this)

        loadModule(GuiEditorManager)

        //utils
        loadModule(HypixelUtils)

        //data
        loadModule(MinecraftData())
        loadModule(RenderGuiData())
        loadModule(ScoreboardData)

        //features
        loadModule(PauseButton())

        Commands.init()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent?) {
        configManager = ConfigManager
        configManager.firstLoad()
        Runtime.getRuntime().addShutdownHook(Thread {
            configManager.saveConfig("shutdown-hook")
        })
    }

    private fun loadModule(obj: Any) {
        modules.add(obj)
        MinecraftForge.EVENT_BUS.register(obj)
    }

    @SubscribeEvent
    fun onTick(event: ModTickEvent) {
        if (screenToOpen != null) {
            screenTicks++
            if (screenTicks == 5) {
                Minecraft.getMinecraft().displayGuiScreen(screenToOpen)
                screenTicks = 0
                screenToOpen = null
            }
        }
    }

    companion object {
        const val MODID = "bedwar"

        @JvmStatic
        val version: String get() = Loader.instance().indexedModList[MODID]!!.version

        @JvmStatic
        val feature: Features get() = configManager.features

        lateinit var configManager: ConfigManager

        private val logger: Logger = LogManager.getLogger("BedWar")
        fun consoleLog(message: String) {
            logger.log(Level.INFO, message)
        }

        private val modules: MutableList<Any> = ArrayList()
        private val globalJob: Job = Job(null)

        val coroutineScope = CoroutineScope(
            CoroutineName("BedWar") + SupervisorJob(globalJob)
        )

        var screenToOpen: GuiScreen? = null
        private var screenTicks = 0
    }
}