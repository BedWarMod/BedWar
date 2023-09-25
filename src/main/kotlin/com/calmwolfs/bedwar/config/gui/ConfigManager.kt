package com.calmwolfs.bedwar.config.gui

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.commands.CopyErrorCommand
import com.calmwolfs.bedwar.config.Features
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.moulberry.moulconfig.observer.PropertyTypeAdapterFactory
import io.github.moulberry.moulconfig.processor.BuiltinMoulConfigGuis
import io.github.moulberry.moulconfig.processor.ConfigProcessorDriver
import io.github.moulberry.moulconfig.processor.MoulConfigProcessor
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*
import kotlin.concurrent.fixedRateTimer

object ConfigManager {
    val gson = GsonBuilder().setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .serializeSpecialFloatingPointValues()
        .registerTypeAdapterFactory(PropertyTypeAdapterFactory())
        .registerTypeAdapter(UUID::class.java, object : TypeAdapter<UUID>() {
            override fun write(out: JsonWriter, value: UUID) {
                out.value(value.toString())
            }

            override fun read(reader: JsonReader): UUID {
                return UUID.fromString(reader.nextString())
            }
        }.nullSafe())
        .enableComplexMapKeySerialization()
        .create()

    lateinit var features: Features

    var configDirectory = File("config/bedwar")
    private var configFile: File? = null
    lateinit var processor: MoulConfigProcessor<Features>

    fun firstLoad() {
        if (ConfigManager::features.isInitialized) {
            println("Loading config despite config being already loaded?")
        }
        configDirectory.mkdir()

        configFile = File(configDirectory, "config.json")

        println("Trying to load config from $configFile")

        if (configFile!!.exists()) {
            try {
                val builder = StringBuilder()

                println("load-config-now")
                BufferedReader(FileReader(configFile!!)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        builder.append(line).append('\n')
                    }
                }
                val configJson = gson.fromJson(builder.toString(), JsonObject::class.java)
                features = gson.fromJson(configJson, Features::class.java)

                println("Loaded config from file")
            } catch (error: Exception) {
                error.printStackTrace()
                val backupFile = configFile!!.resolveSibling("config-${System.currentTimeMillis()}-backup.json")
                println("Exception while reading $configFile. Will load blank config and save backup to $backupFile")
                println("Exception was $error")
                try {
                    configFile!!.copyTo(backupFile)
                } catch (e: Exception) {
                    println("Could not create backup for config file")
                    e.printStackTrace()
                }
            }
        }

        if (!ConfigManager::features.isInitialized) {
            println("Creating blank config and saving to file")
            features = Features()
            saveConfig("blank config")
        }

        fixedRateTimer(name = "bedwar-config-auto-save", period = 60_000L, initialDelay = 60_000L) {
            try {
                saveConfig("auto-save-60s")
            } catch (error: Throwable) {
                CopyErrorCommand.logError(error, "Error auto-saving config!")
            }
        }

        val features = BedWarMod.feature
        processor = MoulConfigProcessor(BedWarMod.feature)
        BuiltinMoulConfigGuis.addProcessors(processor)
        // todo auto-update
//        UpdateManager.injectConfigProcessor(processor)
        ConfigProcessorDriver.processConfig(
            features.javaClass,
            features,
            processor
        )
    }

    fun saveConfig(reason: String) {
        println("saveConfig: $reason")
        val file = configFile ?: throw Error("Can not save config, configFile is null!")
        try {
            println("Saving config file")
            file.parentFile.mkdirs()
            val unit = file.parentFile.resolve("config.json.write")
            unit.createNewFile()
            BufferedWriter(OutputStreamWriter(FileOutputStream(unit), StandardCharsets.UTF_8)).use { writer ->
                writer.write(gson.toJson(BedWarMod.feature))
            }

            Files.move(
                unit.toPath(),
                file.toPath(),
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
            )
        } catch (e: IOException) {
            println("Could not save config file to $file")
            e.printStackTrace()
        }
    }
}