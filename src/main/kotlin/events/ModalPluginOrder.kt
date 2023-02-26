package events

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole
import utils.KiyoshiAPI
import java.awt.Color

class ModalPluginOrder: EventListener {

    val api = KiyoshiAPI()

    // Start Minecraft Plugin Order Variables
    val minecraftpluginname = TextInput.create("minecraftpluginname", "Plugin Name 💬", TextInputStyle.SHORT)
        .setPlaceholder("The name of the plugin 💬")
        .setRequired(true)
        .setMinLength(4)
        .setMaxLength(100) // or setRequiredRange(10, 100)
        .build()

    val minecraftplugindescription = TextInput.create("minecraftplugindescription", "Plugin Description 📜", TextInputStyle.PARAGRAPH)
        .setPlaceholder("The description of the plugin 📜")
        .setRequired(true)
        .setMinLength(100)
        .setMaxLength(1000)
        .build()

    val minecraftpluginversion = TextInput.create("minecraftpluginversion", "Plugin Version 🔢", TextInputStyle.SHORT)
        .setPlaceholder("The version of the plugin ex: 1.19.3 🔢")
        .setRequired(true)
        .setMinLength(3)
        .setMaxLength(6)
        .build()

    val minecraftpluginusercontact = TextInput.create("minecraftpluginusercontact", "User Contact 🤵", TextInputStyle.SHORT)
        .setPlaceholder("Your contact ex: 📱 Phone(With Prefix) or 📧 Email")
        .setRequired(true)
        .setMinLength(8)
        .setMaxLength(30)
        .build()

    // End Minecraft Plugin Order Variables

    override fun onEvent(event: GenericEvent) {
        val modal: Modal = Modal.create("orderminecraftpluginmodal", "Order Plugin")
            .addActionRows(ActionRow.of(minecraftpluginname), ActionRow.of(minecraftplugindescription), ActionRow.of(minecraftpluginversion), ActionRow.of(minecraftpluginusercontact))
            .build()
        val owner = event.jda.getUserById(api.getConfig("OWNERID"))
        if(event is ButtonInteractionEvent) {
            // when button click
            if(event.componentId == "orderplugin") {
                event.replyModal(modal).queue ()
            }
        }
        if(event is ModalInteractionEvent) {
            val minecraftpluginname = event.getValue("minecraftpluginname") ?: return
            val minecraftplugindescription = event.getValue("minecraftplugindescription") ?: return
            val minecraftpluginversion = event.getValue("minecraftpluginversion") ?: return
            val minecraftpluginusercontact = event.getValue("minecraftpluginusercontact") ?: return
            if (event.modalId == "orderminecraftpluginmodal") {
                event.getValue("minecraftplugindescription") ?: return
                event.getValue("minecraftpluginversion") ?: return
                event.getValue("minecraftpluginusercontact") ?: return
                // send message after modal is closed
                owner!!.openPrivateChannel().queue { chat ->
                    chat.sendMessageEmbeds(
                        EmbedBuilder()
                            .setTitle("🪓 New Minecraft Plugin Order 📦")
                            .setDescription("You have a new plugin order, by ${event.user.asMention}")
                            .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                            .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                            .setImage(api.getConfig("SERVERIMAGE"))
                            .addField("Plugin Name 💬", minecraftpluginname.asString, true)
                            .addField("Plugin Description 📜", minecraftplugindescription.asString, true)
                            .addField("Plugin Version 🔢", minecraftpluginversion.asString, true)
                            .addField("User Contact 🤵", minecraftpluginusercontact.asString, true)
                            .setTimestamp(event.message!!.timeCreated)
                            .build()
                    ).setActionRow(api.sendHelpButtons()).queue {
                        // send message to log console
                        AnsiConsole.systemInstall()
                        println(Ansi.ansi().fgBrightBlue().a("[").reset().a("Justin | Minecraft Order").fgBrightBlue().a("]").reset().fgBrightGreen().a(" New Minecraft Plugin Order by ${event.interaction.user.asTag}").reset().a(" | ").fgBrightCyan().a("Plugin Name: ${minecraftpluginname.asString}").reset().a(" | ").fgBrightCyan().a("Plugin Description: ${minecraftplugindescription.asString}").reset().a(" | ").fgBrightCyan().a("Plugin Version: ${minecraftpluginversion.asString}").reset().a(" | ").fgBrightCyan().a("User Contact: ${minecraftpluginusercontact.asString}").reset())
                        AnsiConsole.systemInstall()
                    }
                }
                event.reply("👍 Thank you for your request, you will be contacted by Kiyoshi#1010 as soon as possible.\n\nInformations From The Developer (Minecraft Plugin)\n\n```Plugin Type: Minecraft```\n```Code Language: Kotlin```\n```Plugin Compiler: Gradle```\n```NMS Support: Avaliable```").setEphemeral(true).setActionRow(api.sendHelpButtons()).queue()
            }
        }
    }
}