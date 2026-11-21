package dev.thebjoredcraft.creative.listener

import dev.thebjoredcraft.creative.TEXT_COLOR
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object AsyncChatListener : Listener {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.GERMAN).withZone(
        ZoneId.of("Europe/Berlin")
    )

    @EventHandler
    fun onAsyncChat(event: AsyncChatEvent) {
        event.renderer { _, playerDisplayname, message, _ ->
            playerDisplayname
                .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                .append(message.color(TEXT_COLOR))
                .clickEvent(ClickEvent.runCommand("/teleport ${event.player.name}"))
                .hoverEvent(
                    Component.text(
                        dateTimeFormatter.format(LocalDateTime.now()),
                        NamedTextColor.YELLOW
                    )
                )
        }
    }
}