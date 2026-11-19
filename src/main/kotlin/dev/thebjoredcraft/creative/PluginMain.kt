package dev.thebjoredcraft.creative

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.thebjoredcraft.creative.command.GameModeCommand
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PluginMain::class.java)
const val PREFIX = "aaaa"
const val COLOR = "color"

class PluginMain : SuspendingJavaPlugin() {
    override suspend fun onEnableAsync() {
        GameModeCommand.register()
    }
}