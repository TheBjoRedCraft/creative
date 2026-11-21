package dev.thebjoredcraft.creative.listener

import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.status.server.WrapperStatusServerResponse
import dev.thebjoredcraft.creative.COLOR
import dev.thebjoredcraft.creative.miniMessage
import dev.thebjoredcraft.creative.plugin
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit

object BrandPacketListener : PacketListenerAbstract() {
    override fun onPacketSend(event: PacketSendEvent) {
        if (event.packetType != PacketType.Status.Server.RESPONSE) {
            return
        }

        val packet = WrapperStatusServerResponse(event)
        val component = packet.component

        component.getAsJsonObject("version")
            .addProperty("name",
                LegacyComponentSerializer.legacySection()
                    .serialize(miniMessage.deserialize("${COLOR}Creative ${plugin.description.version} @MCv${Bukkit.getMinecraftVersion()}"))
            )

        packet.component = component
    }
}