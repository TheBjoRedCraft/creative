package dev.thebjoredcraft.creative.warps.config

import dev.thebjoredcraft.creative.plugin
import dev.thebjoredcraft.creative.warps.WarpRegistry
import dev.thebjoredcraft.railway.config.RailwayConfigFormat
import dev.thebjoredcraft.railway.config.manager.ConfigManager

object WarpConfigManager {
    val configManager by lazy {
        ConfigManager.create(
            plugin.dataPath.resolve("warps.yml"), RailwayConfigFormat.YAML,
            WarpsConfig()
        )
    }

    fun loadAll() {
        configManager.load()
        configManager.value.warps.forEach {
            WarpRegistry.register(it.name, it.location(), it.creator)
        }
    }

    fun saveAll() {
        val localWarps = WarpRegistry.all().map { WarpConfig.of(it) }

        configManager.update {
            WarpsConfig(localWarps.toMutableList())
        }

        configManager.save()
    }
}