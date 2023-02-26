package commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import utils.KiyoshiAPI
import java.awt.Color


class GetResourceList: ListenerAdapter() {

    private val api = KiyoshiAPI()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        // make sure we handle the right command
        when (event.name) {
            "getresourcelist" -> {
                if(event.guild?.id.equals(api.getConfig("GUILDID"))){
                    event.replyEmbeds(
                        EmbedBuilder()
                            .setTitle("Resource List")
                            .setDescription("Getting resource list...")
                            .setImage(api.getConfig("SERVERIMAGE"))
                            .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                            .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                            .setTimestamp(event.timeCreated)
                            .build()
                    ).queue {
                        event.hook.editOriginalEmbeds(
                            EmbedBuilder()
                                .setTitle("Resource List")
                                .setDescription("Resources")
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .addField("Minecraft Kotlin Plugin (Repo/Depends)", api.codeMessage("/getresource kotlinmc", "yml"), true)
                                .addField("Minecraft Vault API (Repo/Depends/Code)", api.codeMessage("/getresource vaultmc", "yml"), true)
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