package com.calmwolfs.bedwar.utils.computer

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.data.commands.CopyErrorCommand
import com.calmwolfs.bedwar.utils.ModUtils
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import kotlin.time.Duration.Companion.milliseconds

object ClipboardUtils {
    private var lastClipboardAccessTime = SimpleTimeMark.farPast()

    private fun canAccessClibpard(): Boolean {
        val result = lastClipboardAccessTime.passedSince() > 10.milliseconds
        if (result) {
            lastClipboardAccessTime = SimpleTimeMark.now()
        }
        return result
    }

    private suspend fun getClipboard(): Clipboard? {
        val deferred = CompletableDeferred<Clipboard?>()
        if (canAccessClibpard()) {
            deferred.complete(Toolkit.getDefaultToolkit().systemClipboard)
        } else {
            ModUtils.runDelayed(5.milliseconds) {
                BedWarMod.coroutineScope.launch {
                    deferred.complete(getClipboard())
                }
            }
        }
        return deferred.await()
    }

    fun copyToClipboard(text: String, step: Int = 0) {
        BedWarMod.coroutineScope.launch {
            try {
                getClipboard()?.setContents(StringSelection(text), null)
            } catch (e: Exception) {
                if (step == 3) {
                    CopyErrorCommand.logError(e, "Error while trying to access the clipboard.")
                } else {
                    copyToClipboard(text, step + 1)
                }
            }
        }
    }
}