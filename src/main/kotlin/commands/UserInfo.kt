package commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import utils.KiyoshiAPI
import java.awt.Color

class UserInfo: ListenerAdapter() {

    private val api = KiyoshiAPI()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val user: Member? = event.getOption("user", OptionMapping::getAsMember)
        // make sure we handle the right command
        when (event.name) {
            "userinfo" -> {
                if(event.guild?.id.equals(api.getConfig("GUILDID"))){
                    event.replyEmbeds(
                        EmbedBuilder()
                            .setTitle("User Information")
                            .setDescription("Calculating user information...")
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
                        event.hook.editOriginalEmbeds(
                            EmbedBuilder()
                                .setTitle("User Information")
                                .setDescription("Information about ${user!!.asMention}")
                                .addField("User ID", user.id, true)
                                .addField("User Name", user.user.name, true)
                                .addField("User Discriminator", user.user.discriminator, true)
                                .addField("User Mention", user.asMention, true)
                                .addField("User Avatar", user.user.avatarUrl!!, true)
                                .addField("User Creation Time", user.timeCreated.toString(), true)
                                .addField("User Join Time", user.timeJoined.toString(), true)
                                .addField("User Roles", user.roles.toString(), true)
                                .addField("User Permissions", user.permissions.toString(), true)
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