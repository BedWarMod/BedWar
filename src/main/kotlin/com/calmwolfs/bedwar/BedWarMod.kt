package com.calmwolfs.bedwar

import com.calmwolfs.bedwar.data.MinecraftData
import com.calmwolfs.bedwar.data.RenderGuiData
import com.calmwolfs.bedwar.data.commands.Commands
import com.calmwolfs.bedwar.data.config.Features
import com.calmwolfs.bedwar.data.config.gui.ConfigManager
import com.calmwolfs.bedwar.data.config.gui.GuiEditorManager
import com.calmwolfs.bedwar.events.ModTickEvent
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
    guiFactory = "come.calmwolfs.bedwar.config.gui.ConfigGuiForgeInterop",
    version = "0.1",
)
object BedWarMod {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent?) {
        loadModule(this)

        loadModule(MinecraftData())
        loadModule(GuiEditorManager)
        loadModule(RenderGuiData())

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

    const val MODID = "bedwar"

    @JvmStatic
    val version: String get() = Loader.instance().indexedModList[MODID]!!.version

    @JvmStatic
    val feature: Features get() = configManager.features

    lateinit var configManager: ConfigManager

    private val logger: Logger = LogManager.getLogger("BedWar")

    private val modules: MutableList<Any> = ArrayList()
    private val globalJob: Job = Job(null)
    val coroutineScope = CoroutineScope(
        CoroutineName("BedWar") + SupervisorJob(globalJob)
    )

    fun consoleLog(message: String) {
        logger.log(Level.INFO, message)
    }

    var screenToOpen: GuiScreen? = null
    private var screenTicks = 0
}