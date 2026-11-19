package dev.thebjoredcraft.creative.warps.config

import dev.thebjoredcraft.creative.warps.Warp
import org.bukkit.Bukkit
import org.bukkit.Location
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class WarpConfig(
    val name: String,
    val location: String,
    val creator: String
) {
    fun location(): Location {
        val parts = location.split(",")
        val world = Bukkit.getWorld(parts[0])
            ?: error("World '${parts[0]}' not found for warp '$name'.")
        val x = parts[1].toDouble()
        val y = parts[2].toDouble()
        val z = parts[3].toDouble()
        val yaw = parts[4].toFloat()
        val pitch = parts[5].toFloat()

        return Location(world, x, y, z, yaw, pitch)
    }

    companion object {
        fun of(warp: Warp): WarpConfig {
            val location = warp.location
            val locationString =
                "${location.world?.name},${location.x},${location.y},${location.z},${location.yaw},${location.pitch}"
            return WarpConfig(warp.name, locationString, warp.creator)
        }
    }
}
