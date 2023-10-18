package com.calmwolfs.bedwar

import com.calmwolfs.bedwar.commands.Commands
import com.calmwolfs.bedwar.config.ConfigManager
import com.calmwolfs.bedwar.config.Features
import com.calmwolfs.bedwar.config.PlayerData
import com.calmwolfs.bedwar.config.RepoManager
import com.calmwolfs.bedwar.config.UpdateManager
import com.calmwolfs.bedwar.config.gui.GuiEditorManager
import com.calmwolfs.bedwar.data.RenderGuiData
import com.calmwolfs.bedwar.data.game.ChatManager
import com.calmwolfs.bedwar.data.game.MinecraftData
import com.calmwolfs.bedwar.data.game.OtherInventoryData
import com.calmwolfs.bedwar.data.game.OwnInventoryData
import com.calmwolfs.bedwar.data.game.ScoreboardData
import com.calmwolfs.bedwar.data.game.TablistData
import com.calmwolfs.bedwar.data.game.TooltipData
import com.calmwolfs.bedwar.events.BedwarsEventManager
import com.calmwolfs.bedwar.events.game.ModTickEvent
import com.calmwolfs.bedwar.features.chat.ChatMentions
import com.calmwolfs.bedwar.features.chat.ChatStatDisplay
import com.calmwolfs.bedwar.features.chat.CopyChat
import com.calmwolfs.bedwar.features.chat.PlayerChatClick
import com.calmwolfs.bedwar.features.config.PauseButton
import com.calmwolfs.bedwar.features.inventory.ResourceOverlay
import com.calmwolfs.bedwar.features.inventory.ShopInventoryOverlay
import com.calmwolfs.bedwar.features.inventory.ShopMiddleClick
import com.calmwolfs.bedwar.features.notifications.GameNotifications
import com.calmwolfs.bedwar.features.notifications.PartyNotifications
import com.calmwolfs.bedwar.features.party.PartyGameStats
import com.calmwolfs.bedwar.features.session.SessionDisplay
import com.calmwolfs.bedwar.features.session.TrapDisplay
import com.calmwolfs.bedwar.features.team.TeamStatus
import com.calmwolfs.bedwar.utils.ApiUtils
import com.calmwolfs.bedwar.utils.BedwarsStarUtils
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.ChatCompleteUtils
import com.calmwolfs.bedwar.utils.HypixelUtils
import com.calmwolfs.bedwar.utils.ItemRenderUtils
import com.calmwolfs.bedwar.utils.PartyUtils
import com.calmwolfs.bedwar.utils.computer.KeyboardUtils
import com.calmwolfs.bedwar.utils.gui.NotificationUtils
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
    version = "0.1.5.Pre.1",
)
class BedWarMod {
    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent?) {
        loadModule(this)

        //other
        loadModule(BedwarsEventManager)
        loadModule(ChatManager)
        loadModule(GuiEditorManager)
        loadModule(PlayerData)
        loadModule(UpdateManager)

        //utils
        loadModule(ApiUtils)
        loadModule(BedwarsStarUtils)
        loadModule(BedwarsUtils)
        loadModule(ChatCompleteUtils)
        loadModule(HypixelUtils)
        loadModule(ItemRenderUtils)
        loadModule(KeyboardUtils)
        loadModule(NotificationUtils)
        loadModule(PartyUtils)

        //data
        loadModule(MinecraftData())
        loadModule(OtherInventoryData)
        loadModule(OwnInventoryData)
        loadModule(RenderGuiData())
        loadModule(ScoreboardData)
        loadModule(TablistData)
        loadModule(TooltipData())

        //features
        loadModule(ChatMentions())
        loadModule(ChatStatDisplay)
        loadModule(CopyChat())
        loadModule(GameNotifications)
        loadModule(ShopInventoryOverlay())
        loadModule(TeamStatus)
        loadModule(TrapDisplay())
        loadModule(PartyGameStats)
        loadModule(PartyNotifications)
        loadModule(PauseButton())
        loadModule(PlayerChatClick())
        loadModule(ResourceOverlay())
        loadModule(SessionDisplay)
        loadModule(ShopMiddleClick())

        Commands.init()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent?) {
        configManager = ConfigManager
        configManager.firstLoad()
        Runtime.getRuntime().addShutdownHook(Thread {
            configManager.saveConfig("shutdown-hook")
        })
        repo = RepoManager(configManager.configDirectory)
        try {
            repo.loadRepoInformation()
        } catch (e: Exception) {
            Exception("Error reading repo data", e).printStackTrace()
        }
    }

    private fun loadModule(obj: Any) {
        modules.add(obj)
        MinecraftForge.EVENT_BUS.register(obj)
    }

    @SubscribeEvent
    fun onTick(event: ModTickEvent) {
        if (screenToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(screenToOpen)
            screenToOpen = null
        }
    }

    companion object {
        const val MODID = "bedwar"

        @JvmStatic
        val version: String get() = Loader.instance().indexedModList[MODID]!!.version

        @JvmStatic
        val feature: Features get() = configManager.features

        lateinit var repo: RepoManager
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
    }
}