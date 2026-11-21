package dev.thebjoredcraft.creative.listener

import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object WhitelistListener : Listener {
    @EventHandler
    fun onWhitelistVerify(event: ProfileWhitelistVerifyEvent) {
        if (event.isWhitelistEnabled && !event.isWhitelisted && !event.isOp) {
            event.kickMessage(
                Component.text(
                    "You are prohibited from joining this server.",
                    NamedTextColor.RED
                )
            )
        }
    }
}