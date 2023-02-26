package commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import utils.KiyoshiAPI
import java.awt.Color

class Announce: ListenerAdapter() {

    private val api = KiyoshiAPI()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val announce: String? = event.getOption("announce", OptionMapping::getAsString)
        // make sure we handle the right command
        when (event.name) {
            "announce" -> {
                if(event.guild?.id.equals(api.getConfig("GUILDID"))){
                    event.replyEmbeds(
                        EmbedBuilder()
                            .setTitle("Announcement")
                            .setDescription("Sending announcement...")
                            .setImage(api.getConfig("SERVERIMAGE"))
                            .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                            .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                            .setTimestamp(event.timeCreated)
                            .build()
                    ).queue {
                        event.hook.editOriginalEmbeds(
                            EmbedBuilder()
                                .setTitle("Announcement")
                                .setDescription("Announcement sent!")
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .setTimestamp(event.timeCreated)
                                .build()
                        ).queue()
                    }
                    val everyone = api.getRoleByName(event.jda, api.getConfig("GUILDID"), "@everyone")
                    api.sendEmbedMessageInAnnounceChannel(
                        event.jda,
                        api.getConfig("ANNOUNCEMENTCHANNELID"),
                        "Announcement",
                        "${everyone.asMention} $announce",
                        "JustinTheWolf",
                        api.getConfig("WEBSITE"),
                        event.jda.selfUser.avatarUrl,
                        api.getTimestamp()
                    )
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