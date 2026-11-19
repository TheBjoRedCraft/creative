package dev.thebjoredcraft.creative.warps

import org.bukkit.Location

object WarpRegistry {
    private val warps = mutableMapOf<String, Warp>()

    fun register(name: String, location: Location, creator: String) {
        warps[name] = Warp(name, location, creator)
    }

    fun unregister(name: String) = warps.remove(name)
    fun getWarp(name: String) = warps[name]
    fun all() = warps.values.toList()
}