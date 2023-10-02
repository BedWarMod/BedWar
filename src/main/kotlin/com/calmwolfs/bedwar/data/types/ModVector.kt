package com.calmwolfs.bedwar.data.types

import net.minecraft.entity.Entity
import net.minecraft.util.BlockPos
import net.minecraft.util.Rotations
import kotlin.math.pow

data class ModVector(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(x: Float, y: Float, z: Float) : this(x.toDouble(), y.toDouble(), z.toDouble())


    fun distance(other: ModVector): Double = distanceSq(other).pow(0.5)

    fun distanceSq(other: ModVector): Double {
        val dx = (other.x - x)
        val dy = (other.y - y)
        val dz = (other.z - z)
        return (dx * dx + dy * dy + dz * dz)
    }
}


fun Rotations.toModVector(): ModVector = ModVector(x, y, z)
fun BlockPos.toModVector(): ModVector = ModVector(x, y, z)
fun Entity.getModVector(): ModVector = ModVector(posX, posY, posZ)