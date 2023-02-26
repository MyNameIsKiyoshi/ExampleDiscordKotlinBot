package commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.KiyoshiAPI
import java.awt.Color

class Help: ListenerAdapter() {

    private val api = KiyoshiAPI()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        // make sure we handle the right command
        when (event.name) {
            "help" -> {
                if(event.guild?.id.equals(api.getConfig("GUILDID"))){
                    event.replyEmbeds(
                        EmbedBuilder()
                            .setTitle("Justin The Wolf Help - Slash Commands List (1/1) 🍖")
                            .setDescription("Here is a list of all the commands you can use.")
                            .setImage(api.getConfig("SERVERIMAGE"))
                            .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                            .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                            .addField("Commands", "```yml\n" +
                                    "/help - Shows this message\n" +
                                    "/ping - Shows the bot's ping\n" +
                                    "/announce - Announcement command\n" +
                                    "/ban - Ban users\n" +
                                    "/pex - Pex users\n" +
                                    "/depex - Depex users\n" +
                                    "/getresourcelist - Get the avaliable resources\n" +
                                    "/getresource - Get Resource\n" +
                                    "/stopbot - Shutdown the bot\n" +
                                    "```", false)
                            .setTimestamp(event.timeCreated)
                            .build()
                    ).setActionRow(api.sendHelpButtons()).queue()
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
                    ).setActionRow(api.sendHelpButtons()).queue()
                }
            }
        }
    }

}