package dev.thebjoredcraft.creative.warps.config

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class WarpsConfig(
    val warps: MutableList<WarpConfig> = mutableListOf()
)
