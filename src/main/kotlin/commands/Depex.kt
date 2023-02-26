package commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import utils.KiyoshiAPI
import java.awt.Color

class Depex: ListenerAdapter() {

    private val api = KiyoshiAPI()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val password: String? = event.getOption("password", OptionMapping::getAsString)
        // optionally check for member information
        val user: Member? = event.getOption("user", OptionMapping::getAsMember)
        val role: Role? = event.getOption("role", OptionMapping::getAsRole)
        // make sure we handle the right command
        when (event.name) {
            "depex" -> {
                if(event.guild?.id.equals(api.getConfig("GUILDID"))){
                    if (api.isKiyoshi(event.member?.id.toString())) {
                        if (event.member?.canInteract(user!!)!!) {
                            if (password.equals(api.getConfig("PASSWORD"))) {
                                event.replyEmbeds(
                                    EmbedBuilder()
                                        .setTitle("Ungrant Permission, Depex")
                                        .setDescription("Depex ${user!!.asMention}...")
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
                                    if(user.roles.contains(role)){
                                        api.removeRole(
                                            event.jda,
                                            api.getConfig("GUILDID"),
                                            user,
                                            role!!.id
                                        )
                                        event.hook.editOriginalEmbeds(
                                            EmbedBuilder()
                                                .setTitle("Permission Ungranted, Depex")
                                                .setDescription("Depexed ${user.asMention}, they no longer have the ${role.name} role, removed by ${event.member?.asMention}.")
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
                                    } else {
                                        event.hook.editOriginalEmbeds(
                                            EmbedBuilder()
                                                .setTitle("Permission Ungranted, Depex")
                                                .setDescription("Depexed ${user.asMention}, they did not have the ${role?.name} role, removed by ${event.member?.asMention}.")
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
                                        AnsiConsole.systemInstall()
                                        println(ansi().fgBrightBlue().a("[").reset().a("Justin").fgBrightBlue().a("]").reset().a(" Depexed ${user.user.name}#${user.user.discriminator} (${user.id}) from ${role?.name} (${role?.id})").reset())
                                        AnsiConsole.systemInstall()
                                    }
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
                                    .setTitle("Ungrant Permission, Depex")
                                    .setDescription("You cannot depex someone with a higher role than you.")
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
                                .setTitle("Ungrant Permission, Depex")
                                .setDescription("You are not Kiyoshi.")
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