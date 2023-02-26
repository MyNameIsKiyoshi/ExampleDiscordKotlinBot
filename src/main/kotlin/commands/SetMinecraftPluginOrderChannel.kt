package commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.KiyoshiAPI
import java.awt.Color


class SetMinecraftPluginOrderChannel: ListenerAdapter() {

    private val api = KiyoshiAPI()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        // make sure we handle the right command
        when (event.name) {
            "setminecraftpluginorderchannel" -> {
                if(event.guild?.id.equals(api.getConfig("GUILDID"))){
                    event.replyEmbeds(
                        EmbedBuilder()
                            .setTitle("Set Order Channel")
                            .setDescription("Setting order channel...")
                            .setImage(api.getConfig("SERVERIMAGE"))
                            .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                            .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                            .setTimestamp(event.timeCreated)
                            .build()
                    ).queue {
                        event.hook.editOriginalEmbeds(
                            EmbedBuilder()
                                .setTitle("Order Minecraft Plugin 📦")
                                .setDescription("Click the button below to order a plugin.")
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .setTimestamp(event.timeCreated)
                                .build()
                        ).setActionRow(api.sendOrderPluginbutton()).queue()
                    }
                } else {
                    event.replyEmbeds(
                        EmbedBuilder()
                            .setTitle("Error")
                            .setDescription("You are not in the Kiyoshi server, you cannot use this command.")
                            .setImage(api.getConfig("SERVERIMAGE"))
                            .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                            .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                            .setTimestamp(event.timeCreated)
                            .build()
                    ).queue()
                }
            }
        }
    }
}