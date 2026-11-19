package dev.thebjoredcraft.creative

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PluginMain::class.java)

class PluginMain : SuspendingJavaPlugin() {
}