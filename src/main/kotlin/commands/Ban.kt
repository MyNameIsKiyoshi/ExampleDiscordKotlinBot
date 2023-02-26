package commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import utils.KiyoshiAPI
import java.awt.Color
import java.util.concurrent.TimeUnit

class Ban: ListenerAdapter() {
    
    private val api = KiyoshiAPI()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        // make sure we handle the right command
        when (event.name) {
            "ban" -> {
                if(event.guild?.id.equals(api.getConfig("GUILDID"))){
                    // double check permissions, don't trust discord on this!
                    if (!event.member?.hasPermission(Permission.BAN_MEMBERS)!!) {
                        event.replyEmbeds(
                            EmbedBuilder()
                                .setTitle("Error")
                                .setDescription("You do not have permission to ban members!")
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                .setTimestamp(event.timeCreated)
                                .build()
                        ).setEphemeral(true).queue()
                    }
                    val target: User? = event.getOption("user", OptionMapping::getAsUser)
                    // optionally check for member information
                    val member: Member? = event.getOption("user", OptionMapping::getAsMember)
                    if (!event.member!!.canInteract(member!!)) {
                        event.replyEmbeds(
                            EmbedBuilder()
                                .setTitle("Error")
                                .setDescription("You cannot ban this user!")
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                .setTimestamp(event.timeCreated)
                                .build()
                        ).setEphemeral(true).queue()
                    }
                    // Before starting our ban request, tell the user we received the command
                    // This sends a "Bot is thinking..." message which is later edited once we finished
                    event.deferReply().queue()
                    val reason: String? = event.getOption("reason", OptionMapping::getAsString)
                    var action: AuditableRestAction<Void?> = event.guild!!.ban(target!!, 0, TimeUnit.MINUTES) // Start building our ban request
                    if (reason != null) // reason is optional
                        action = action.reason(reason) // set the reason for the ban in the audit logs and ban log
                    action.queue({
                        // Edit the thinking message with our response on success
                        event.hook.editOriginalEmbeds(
                            EmbedBuilder()
                                .setTitle("Success")
                                .setDescription("Successfully banned ${target.asTag}!")
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                .setTimestamp(event.timeCreated)
                                .build()
                        ).queue()
                        AnsiConsole.systemInstall()
                        println(ansi().fgBrightBlue().a("[").reset().a("Justin").fgBrightBlue().a("]").reset().fgBrightGreen().a(" Successfully banned ${target.asTag} ").reset().a("By: ${event.user.asTag}").fgBrightBlue().a("[").reset().a(event.user.name).fgBrightBlue().a("]").reset())
                        AnsiConsole.systemInstall()
                    }) { error ->
                        // Tell the user we encountered some error
                        event.hook.editOriginalEmbeds(
                            EmbedBuilder()
                                .setTitle("Error")
                                .setDescription("An error occurred while banning ${target.asTag}!")
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                .setTimestamp(event.timeCreated)
                                .build()
                        ).queue()
                        error.printStackTrace()
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