package com.calmwolfs.bedwar.utils.computer

import com.calmwolfs.bedwar.utils.ModUtils
import java.awt.Desktop
import java.io.IOException
import java.net.URI

object WebUtils {
    @JvmStatic
    fun openBrowser(url: String) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(URI(url))
            } catch (e: IOException) {
                e.printStackTrace()
                ModUtils.error("[BedWar] Error opening website: $url!")
            }
        } else {
            ClipboardUtils.copyToClipboard(url)
            ModUtils.warning("[BedWar] Web browser is not supported! Copied url to clipboard.")
        }
    }
}