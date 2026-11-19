package dev.thebjoredcraft.creative.warps

import org.bukkit.Location

data class Warp(
    val name: String,
    val location: Location,
    val creator: String
)
