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

class Pex: ListenerAdapter() {

    private val api = KiyoshiAPI()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val password: String? = event.getOption("password", OptionMapping::getAsString)
        // optionally check for member information
        val user: Member? = event.getOption("user", OptionMapping::getAsMember)
        val role: Role? = event.getOption("role", OptionMapping::getAsRole)
        // make sure we handle the right command
        when (event.name) {
            "pex" -> {
                if(event.guild?.id.equals(api.getConfig("GUILDID"))){
                    if (api.isKiyoshi(event.member?.id.toString())) {
                        if (event.member?.canInteract(user!!)!!) {
                            if (password.equals(api.getConfig("PASSWORD"))) {
                                event.replyEmbeds(
                                    EmbedBuilder()
                                        .setTitle("Grant Permission, Pex")
                                        .setDescription("Pexing ${user!!.asMention}...")
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
                                    if(!user.roles.contains(role)){
                                        api.addRole(
                                            event.jda,
                                            api.getConfig("GUILDID"),
                                            user,
                                            role!!.id
                                        )
                                        event.hook.editOriginalEmbeds(
                                            EmbedBuilder()
                                                .setTitle("Permission Granted, Pex")
                                                .setDescription("Pexed ${user.asMention}, they now have the ${role.name} role, granted by ${event.member?.asMention}.")
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
                                        println(ansi().fgBrightBlue().a("[").reset().a("Justin").fgBrightBlue().a("]").reset().a(" Pexed ${user.user.name}#${user.user.discriminator} with the ${role.name} role, granted by ${event.member?.user?.name}#${event.member?.user?.discriminator}").reset())
                                        AnsiConsole.systemInstall()
                                    } else {
                                        event.hook.editOriginalEmbeds(
                                            EmbedBuilder()
                                                .setTitle("Permission Already Granted, Pex")
                                                .setDescription("Pexed ${user.asMention}, they already have the ${role?.name} role, granted by ${event.member?.asMention}.")
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
                                    .setTitle("Grant Permission, Pex")
                                    .setDescription("You cannot pex this user.")
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
                                .setTitle("Grant Permission, Pex")
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