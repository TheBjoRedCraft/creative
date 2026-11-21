package dev.thebjoredcraft.creative

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.thebjoredcraft.creative.command.ClearCommand
import dev.thebjoredcraft.creative.command.GameModeCommand
import dev.thebjoredcraft.creative.listener.BlockUpdateListener
import dev.thebjoredcraft.creative.listener.TabSuggestionListener
import dev.thebjoredcraft.creative.warps.config.WarpConfigManager
import dev.thebjoredcraft.creative.whitelist.WhitelistListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PluginMain::class.java)
const val COLOR = "<#6eff94>"
const val PREFIX = "<dark_gray>»$COLOR Creative <dark_gray>|<white>"

class PluginMain : SuspendingJavaPlugin() {
    override suspend fun onEnableAsync() {
        GameModeCommand.register()
        ClearCommand.register()

        Bukkit.getPluginManager().registerEvents(BlockUpdateListener, this)
        Bukkit.getPluginManager().registerEvents(TabSuggestionListener, this)
        Bukkit.getPluginManager().registerEvents(WhitelistListener, this)

        WarpConfigManager.loadAll()
    }

    override suspend fun onDisableAsync() {
        WarpConfigManager.saveAll()
    }
}