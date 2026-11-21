package dev.thebjoredcraft.creative.listener

import com.github.shynixn.mccoroutine.folia.launch
import dev.thebjoredcraft.creative.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerLoadEvent

object ServerLoadListener : Listener {
    @EventHandler
    fun onServerLoad(event: ServerLoadEvent) {
        plugin.launch {
            plugin.afterEnableAsync()
        }
    }
}