package com.calmwolfs.bedwar.data.types

import com.calmwolfs.bedwar.utils.computer.SimpleTimeMark
import net.minecraft.item.ItemStack

data class Notification(
    val notificationLines: List<String>,
    val notificationItem: ItemStack?,
    val startTime: SimpleTimeMark,
    var endTime: SimpleTimeMark
)