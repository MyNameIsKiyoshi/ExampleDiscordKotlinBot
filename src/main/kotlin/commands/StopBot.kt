package commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import utils.KiyoshiAPI
import java.awt.Color

class StopBot: ListenerAdapter() {

    private val api = KiyoshiAPI()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val password: String? = event.getOption("password", OptionMapping::getAsString)
        // make sure we handle the right command
        when (event.name) {
            "stopbot" -> {
                if(event.guild?.id.equals(api.getConfig("GUILDID"))){
                    if (api.isKiyoshi(event.member?.id.toString())) {
                        if (password.equals(api.getConfig("PASSWORD"))) {
                            event.replyEmbeds(
                                EmbedBuilder()
                                    .setTitle("Stop Bot")
                                    .setDescription("Shutting down...")
                                    .setImage(api.getConfig("SERVERIMAGE"))
                                    .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                    .setAuthor(
                                        event.jda.selfUser.name,
                                        api.getConfig("WEBSITE"),
                                        event.jda.selfUser.avatarUrl
                                    )
                                    .setTimestamp(event.timeCreated)
                                    .build()
                            ).queue {
                                api.stopTheBot(event.jda)
                            }
                        } else {
                            event.replyEmbeds(
                                EmbedBuilder()
                                    .setTitle("Error - Incorrect Password")
                                    .setDescription("The password is incorrect, please try again.")
                                    .setImage(api.getConfig("SERVERIMAGE"))
                                    .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                    .setAuthor(
                                        event.jda.selfUser.name,
                                        api.getConfig("WEBSITE"),
                                        event.jda.selfUser.avatarUrl
                                    )
                                    .setTimestamp(event.timeCreated)
                                    .build()
                            ).queue()
                        }
                    } else {
                        event.replyEmbeds(
                            EmbedBuilder()
                                .setTitle("Stop Bot")
                                .setDescription("You are not Kiyoshi, you cannot use this command.")
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .setAuthor(
                                    event.jda.selfUser.name,
                                    api.getConfig("WEBSITE"),
                                    event.jda.selfUser.avatarUrl
                                )
                                .setTimestamp(event.timeCreated)
                                .build()
                        ).queue()
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