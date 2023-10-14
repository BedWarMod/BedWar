package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.commands.CopyErrorCommand
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.zip.ZipInputStream

object RepoUtils {

    fun recursiveDelete(file: File) {
        if (file.isDirectory && !Files.isSymbolicLink(file.toPath())) {
            for (child in file.listFiles()) {
                recursiveDelete(child)
            }
        }
        file.delete()
    }

    fun unzipIgnoreFirstFolder(zipFilePath: String, destDir: String) {
        val dir = File(destDir)
        if (!dir.exists()) dir.mkdirs()
        val fis: FileInputStream

        val buffer = ByteArray(1024)
        try {
            fis = FileInputStream(zipFilePath)
            val zis = ZipInputStream(fis)
            var ze = zis.nextEntry
            while (ze != null) {
                if (!ze.isDirectory) {
                    var fileName = ze.name
                    fileName = fileName.substring(fileName.split("/").toTypedArray()[0].length + 1)
                    val newFile = File(destDir + File.separator + fileName)

                    File(newFile.parent).mkdirs()
                    if (!isInTree(dir, newFile)) {
                        throw RuntimeException(
                            "BedWarMod unexpected zip file, please report this on the GitHub."
                        )
                    }
                    val fos = FileOutputStream(newFile)
                    var len: Int
                    while (zis.read(buffer).also { len = it } > 0) {
                        fos.write(buffer, 0, len)
                    }
                    fos.close()
                }

                zis.closeEntry()
                ze = zis.nextEntry
            }

            zis.closeEntry()
            zis.close()
            fis.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Suppress("NAME_SHADOWING")
    @Throws(IOException::class)
    private fun isInTree(rootDirectory: File, file: File): Boolean {
        var rootDirectory = rootDirectory
        var file: File? = file
        file = file!!.canonicalFile
        rootDirectory = rootDirectory.canonicalFile
        while (file != null) {
            if (file == rootDirectory) return true
            file = file.parentFile
        }
        return false
    }

    fun <T> getConstant(repo: File, constant: String, gson: Gson, clazz: Class<T>?): T? {
        if (!repo.exists()) return null

        val jsonFile = File(repo, "constants/$constant.json")
        if (!jsonFile.isFile) {
            BedWarMod.repo.unsuccessfulConstants.add("Constant")
            CopyErrorCommand.logError(
                Error("File '$jsonFile' not found!"),
                "File in repo missing! ($jsonFile). Try §e/bwupdaterepo"
            )
            return null
        }
        BufferedReader(
            InputStreamReader(
                FileInputStream(jsonFile),
                StandardCharsets.UTF_8
            )
        ).use { reader ->
            return gson.fromJson(reader, clazz)
        }
    }
}