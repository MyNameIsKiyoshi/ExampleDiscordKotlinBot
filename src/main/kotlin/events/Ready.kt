package events

import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.EventListener
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import utils.KiyoshiAPI


class Ready: EventListener {

    private val api = KiyoshiAPI()

    override fun onEvent(event: GenericEvent) {
        if (event is ReadyEvent){
            event.jda.presence.setStatus(OnlineStatus.DO_NOT_DISTURB)
            AnsiConsole.systemInstall()
            println(ansi().fgBrightBlue().a("[").reset().a("Justin").fgBrightBlue().a("]").reset().fgBrightGreen().a(" Ready").reset().fgBrightBlue().a(" | ").reset().fgBrightCyan().a("Logged in as ${event.jda.selfUser.asTag}").reset())
            AnsiConsole.systemInstall()
            api.autoActivity(event.jda)
            api.preventLag()
            for (command in api.getAllCommands(event.jda)){
                println(ansi().fgBrightBlue().a("[").reset().a("Justin").fgBrightBlue().a("]").reset().fgBrightGreen().a(" Command").reset().fgBrightBlue().a(" | ").reset().fgBrightCyan().a("Registered command: ${command.name}").reset())
            }
            api.sendEmbedMessageInChannel(
                event.jda,
                api.getConfig("LOGCHANNELID"),
                "Online",
                "I'm ready!",
                "JustinTheWolf",
                api.getConfig("WEBSITE"),
                event.jda.selfUser.avatarUrl,
                api.getTimestamp()
            )
        }
    }
}