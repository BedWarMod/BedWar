package com.calmwolfs.bedwar.config

import com.calmwolfs.bedwar.commands.CopyErrorCommand
import com.calmwolfs.bedwar.events.RepositoryReloadEvent
import com.calmwolfs.bedwar.features.notifications.ErrorNotifications
import com.calmwolfs.bedwar.utils.ChatUtils
import com.calmwolfs.bedwar.utils.ModUtils
import com.calmwolfs.bedwar.utils.RepoUtils
import com.google.gson.JsonObject
import net.minecraft.client.Minecraft
import org.apache.commons.io.FileUtils
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicBoolean

class RepoManager(private val configLocation: File) {
    private val gson get() = ConfigManager.gson
    private var latestRepoCommit: String? = null
    private val repoLocation: File = File(configLocation, "repo")

    val successfulConstants = mutableListOf<String>()
    val unsuccessfulConstants = mutableListOf<String>()

    fun loadRepoInformation() {
        atomicShouldManuallyReload.set(true)
        fetchRepository(false).thenRun(this::reloadRepository)
    }

    private val atomicShouldManuallyReload = AtomicBoolean(false)

    fun updateRepo() {
        atomicShouldManuallyReload.set(true)
        fetchRepository(true).thenRun { this.reloadRepository("Repo updated successfully :)") }
    }

    fun reloadRepo() {
        atomicShouldManuallyReload.set(true)
        reloadRepository("Repo loaded from local files successfully :)")
    }

    private fun fetchRepository(command: Boolean): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync {
            try {
                val currentCommitJSON: JsonObject? = getJsonFromFile(File(configLocation, "currentCommit.json"))
                latestRepoCommit = null
                try {
                    InputStreamReader(URL(getCommitApiUrl()).openStream())
                        .use { inReader ->
                            val commits: JsonObject = gson.fromJson(inReader, JsonObject::class.java)
                            latestRepoCommit = commits["sha"].asString
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (latestRepoCommit == null || latestRepoCommit!!.isEmpty()) return@supplyAsync false
                if (File(configLocation, "repo").exists()) {
                    if (currentCommitJSON != null && currentCommitJSON["sha"].asString == latestRepoCommit) {
                        if (unsuccessfulConstants.isEmpty()) {
                            if (command) {
                                ChatUtils.chat("§e[BedWar] §7The repo is already up to date!")
                                atomicShouldManuallyReload.set(false)
                            }
                            return@supplyAsync false
                        }
                    }
                }
                RepoUtils.recursiveDelete(repoLocation)
                repoLocation.mkdirs()
                val itemsZip = File(repoLocation, "bw-repo-main.zip")
                try {
                    itemsZip.createNewFile()
                } catch (e: IOException) {
                    return@supplyAsync false
                }
                val url = URL(getDownloadUrl(latestRepoCommit))
                val urlConnection = url.openConnection()
                urlConnection.connectTimeout = 15000
                urlConnection.readTimeout = 30000
                try {
                    urlConnection.getInputStream().use { `is` ->
                        FileUtils.copyInputStreamToFile(
                            `is`,
                            itemsZip
                        )
                    }
                } catch (e: IOException) {
                    Exception(
                        "Failed to download BedWar Mod Repo! Please report this issue on the GitHub!",
                        e
                    ).printStackTrace()
                    if (command) {
                        ModUtils.error("An error occurred while trying to reload the repo! See logs for more info.")
                    }
                    return@supplyAsync false
                }
                RepoUtils.unzipIgnoreFirstFolder(
                    itemsZip.absolutePath,
                    repoLocation.absolutePath
                )
                if (currentCommitJSON == null || currentCommitJSON["sha"].asString != latestRepoCommit) {
                    val newCurrentCommitJSON = JsonObject()
                    newCurrentCommitJSON.addProperty("sha", latestRepoCommit)
                    try {
                        writeJson(newCurrentCommitJSON, File(configLocation, "currentCommit.json"))
                    } catch (ignored: IOException) {
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            true
        }
    }

    private fun reloadRepository(answerMessage: String = ""): CompletableFuture<Void?> {
        val comp = CompletableFuture<Void?>()
        if (!atomicShouldManuallyReload.get()) return comp
        Minecraft.getMinecraft().addScheduledTask {
            try {
                successfulConstants.clear()
                unsuccessfulConstants.clear()
                RepositoryReloadEvent(repoLocation, gson).postAndCatch()
                comp.complete(null)
                if (unsuccessfulConstants.isNotEmpty()) {
                    ChatUtils.chat("§e[BedWar] error reloading repository")
                } else {
                    if (answerMessage.isNotEmpty()) {
                        ChatUtils.chat("§e[BedWar] §a$answerMessage")
                    }
                }
            } catch (e: java.lang.Exception) {
                CopyErrorCommand.logError(e, "Error reading repo data!")
            }
        }
        return comp
    }

    fun displayRepoStatus(joinEvent: Boolean) {
        if (joinEvent) {
            if (unsuccessfulConstants.isNotEmpty()) {
                ChatUtils.chat("§c[BedWar] §7Repo Issue! Features may not work please report this on the GitHub:")
                for (constant in unsuccessfulConstants) {
                    ChatUtils.chat("   §a- §7$constant")
                }
                ErrorNotifications.repoError()
            }
            return
        }
        if (unsuccessfulConstants.isEmpty() && successfulConstants.isNotEmpty()) {
            ChatUtils.chat("§a[BedWar] Repo working fine!")
            return
        }
        ChatUtils.chat("§a[BedWar] Successful Constants:")
        for (constant in successfulConstants) {
            ChatUtils.chat("   §a- §7$constant")
        }
        ChatUtils.chat("§c[BedWar] Unsuccessful Constants:")
        for (constant in unsuccessfulConstants) {
            ChatUtils.chat("   §a- §7$constant")
        }
    }

    private fun getJsonFromFile(file: File?): JsonObject? {
        try {
            BufferedReader(
                InputStreamReader(
                    FileInputStream(file),
                    StandardCharsets.UTF_8
                )
            ).use { reader ->
                return gson.fromJson(reader, JsonObject::class.java)
            }
        } catch (e: java.lang.Exception) {
            return null
        }
    }

    private fun getCommitApiUrl(): String {
        val repoUser = "BedWarMod"
        val repoName = "BedWar-Repo"
        val repoBranch = "main"
        return String.format("https://api.github.com/repos/%s/%s/commits/%s", repoUser, repoName, repoBranch)
    }

    private fun getDownloadUrl(commitId: String?): String {
        val repoUser = "BedWarMod"
        val repoName = "BedWar-Repo"
        return String.format("https://github.com/%s/%s/archive/%s.zip", repoUser, repoName, commitId)
    }

    @Throws(IOException::class)
    fun writeJson(json: JsonObject?, file: File) {
        file.createNewFile()
        BufferedWriter(
            OutputStreamWriter(
                FileOutputStream(file),
                StandardCharsets.UTF_8
            )
        ).use { writer -> writer.write(gson.toJson(json)) }
    }
}