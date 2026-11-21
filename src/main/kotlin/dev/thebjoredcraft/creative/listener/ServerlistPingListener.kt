package dev.thebjoredcraft.creative.listener

import dev.thebjoredcraft.creative.COLOR
import dev.thebjoredcraft.creative.miniMessage
import dev.thebjoredcraft.creative.plugin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

object ServerlistPingListener : Listener {
    @EventHandler
    fun onServerPing(event: ServerListPingEvent) {
        event.motd(
            miniMessage.deserialize(
                "                   <red>ᴀ ᴘʀɪᴠᴀᴛᴇ ѕᴇʀᴠᴇʀ\n" +
                        "             <gray>Running creative v${plugin.description.version} - [${COLOR}${Bukkit.getMinecraftVersion()}<gray>]"
            )
        )

        event.maxPlayers = 500
    }
}