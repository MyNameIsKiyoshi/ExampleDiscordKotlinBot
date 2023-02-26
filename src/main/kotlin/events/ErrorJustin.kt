package events

import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.events.ExceptionEvent
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import utils.KiyoshiAPI

class ErrorJustin: EventListener {

    private val api = KiyoshiAPI()

    override fun onEvent(event: GenericEvent) {
        if (event is ExceptionEvent){
            AnsiConsole.systemInstall()
            println(ansi().fgBrightBlue().a("[").reset().a("Justin").fgBrightBlue().a("]").reset().fgBrightRed().a(" Error").reset().fgBrightBlue().a(" | ").reset().fgBrightCyan().a("Error: ${event.cause}").reset())
            AnsiConsole.systemInstall()
            // when give a error send message
            api.sendEmbedMessageInChannel(
                event.jda,
                api.getConfig("LOGCHANNELID"),
                "Error",
                "I have a error: ${event.cause}",
                "JustinTheWolf",
                api.getConfig("WEBSITE"),
                event.jda.selfUser.avatarUrl,
                api.getTimestamp()
            )
        }
    }
}