package events

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import utils.KiyoshiAPI
import java.awt.Color

class OwnerTag: EventListener {
    val api = KiyoshiAPI()
    override fun onEvent(event: GenericEvent) {
        if (event is MessageReceivedEvent) {
            // when mention owner
            val owner = api.getUserById(event.jda, api.getConfig("OWNERID"))
            if(owner != null){
                if(!(event.author.isBot)){
                    if (event.message.mentions.isMentioned(owner)) {
                        // reply message
                        event.channel.sendMessageEmbeds(
                            EmbedBuilder()
                                .setTitle("Owner Tagged")
                                .setDescription("Sending message to owner...")
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setTimestamp(api.getTimestamp())
                                .build()
                        ).queue {
                            event.channel.editMessageEmbedsById(
                                it.id, EmbedBuilder()
                                    .setTitle("Owner Tagged")
                                    .setDescription("My owner is ${owner.asTag}")
                                    .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                    .setImage(api.getConfig("SERVERIMAGE"))
                                    .setFooter("I have sent your message him. He will reply soon.", owner.avatarUrl)
                                    .setTimestamp(api.getTimestamp())
                                    .build()
                            ).queue()
                        }
                        // send private message to owner when mentioned
                        owner.openPrivateChannel().queue { chat ->
                            chat.sendMessageEmbeds(
                                EmbedBuilder()
                                    .setTitle("You have been mentioned!")
                                    .setDescription("You have been mentioned in ${event.guild.name}\n by ${event.author.asMention}\n in ${event.channel.asMention}")
                                    .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                    .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                    .setImage(api.getConfig("SERVERIMAGE"))
                                    .addField("Message", event.message.contentRaw, true)
                                    .setTimestamp(event.message.timeCreated)
                                    .build()
                            ).queue {
                                // send message to log console
                                AnsiConsole.systemInstall()
                                println(ansi().fgBrightBlue().a("[").reset().a("Justin").fgBrightBlue().a("]").reset().fgBrightGreen().a(" Owner Tagged").reset().fgBrightBlue().a(" | ").reset().fgBrightCyan().a("Sent message to owner").reset())
                                println(ansi().fgBrightBlue().a("[").reset().a("Justin").fgBrightBlue().a("]").reset().fgBrightYellow().a(" Await").fgBrightBlue().a(" | ").reset().fgBrightCyan().a("Await: Owner Tag").reset())
                                AnsiConsole.systemInstall()
                            }
                        }
                    }
                }
            } else {
                AnsiConsole.systemInstall()
                println(ansi().fgBrightBlue().a("[").reset().a("Justin").fgBrightBlue().a("]").reset().fgBrightYellow().a(" Await").fgBrightBlue().a(" | ").reset().fgBrightCyan().a("Await: Owner Tag").reset())
                AnsiConsole.systemInstall()
            }
        }
    }
}