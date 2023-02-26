import commands.*
import events.*
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import utils.KiyoshiAPI
import java.util.*

fun main() {
    val api = KiyoshiAPI()
    api.createResourcesFolder()

    val justinBuilder = JDABuilder.createDefault(api.getEnv("TOKEN"), Collections.emptyList())

    fun configureMemoryUsage(justinBuilder: JDABuilder){
        // Disable cache for member activities (streaming/games/spotify)
        justinBuilder.disableCache(
            CacheFlag.MEMBER_OVERRIDES,
            CacheFlag.ACTIVITY,
            CacheFlag.EMOJI,
            CacheFlag.STICKER,
            CacheFlag.SCHEDULED_EVENTS,
            CacheFlag.VOICE_STATE
            )

        // Only cache members who are either in a voice channel or owner of the guild
        justinBuilder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))

        // Disable member chunking on startup
        justinBuilder.setChunkingFilter(ChunkingFilter.NONE)

        // Enable member chunking for voice states
        justinBuilder.enableIntents(
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.MESSAGE_CONTENT
        )

        // Disable presence updates and typing events
        justinBuilder.disableIntents(
            GatewayIntent.GUILD_PRESENCES,
            GatewayIntent.GUILD_MESSAGE_TYPING
        )

        // Consider guilds with more than 50 members as "large".
        // Large guilds will only provide online members in their setup and thus reduce bandwidth if chunking is disabled.
        justinBuilder.setLargeThreshold(50)
    }

    fun components(){
        // Enable the bulk delete event
        justinBuilder.setBulkDeleteSplittingEnabled(false)
    }

    fun events(){
        justinBuilder.addEventListeners(
            Ready(),
            ErrorJustin(),
            OwnerTag(),
            ModalPluginOrder()
        )
    }

    fun commands(){
        justinBuilder.addEventListeners(
            Ping(),
            Ban(),
            StopBot(),
            Pex(),
            Depex(),
            Announce(),
            GetResource(),
            GetResourceList(),
            Help(),
            UserInfo(),
            SetMinecraftPluginOrderChannel()
        )
    }

    fun build(){
        configureMemoryUsage(justinBuilder)
        components()
        events()
        commands()
        // Sets the global command list to the provided commands (removing all others)
        justinBuilder.build().awaitReady().updateCommands().addCommands(
            Commands.slash("ping", "Calculate ping of the bot"),
            Commands.slash("ban", "Ban a user from the server")
                .setDefaultPermissions(
                    DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS)) // only usable with ban permissions
                    .setGuildOnly(true) // Ban command only works inside a guild
                    .addOption(OptionType.USER, "user", "The user to ban", true) // required option of type user (target to ban)
                    .addOption(OptionType.STRING, "reason", "The ban reason"), // optional reason
            Commands.slash("stopbot", "Stop the bot")
                .setGuildOnly(true) // Stop command only works inside a guild
                .setDefaultPermissions(
                    DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)) // only usable with admin permissions
                .addOption(OptionType.STRING, "password", "Password For Shutdown", true),
            Commands.slash("pex", "Give a user a permission")
                .setGuildOnly(true)
                .setDefaultPermissions(
                    DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addOption(OptionType.USER, "user", "The user to give the permission", true)
                .addOption(OptionType.ROLE, "role", "The role to give the permission", true)
                .addOption(OptionType.STRING, "password", "Password For Pex", true),
            Commands.slash("depex", "Remove a permission from a user")
                .setGuildOnly(true)
                .setDefaultPermissions(
                    DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addOption(OptionType.USER, "user", "The user to revoke the permission", true)
                .addOption(OptionType.ROLE, "role", "The role to revoke the permission", true)
                .addOption(OptionType.STRING, "password", "Password For Depex", true),
            Commands.slash("announce", "Announce something")
                .setGuildOnly(true)
                .setDefaultPermissions(
                    DefaultMemberPermissions.enabledFor(Permission.MANAGE_CHANNEL))
                .addOption(OptionType.STRING, "announce", "The message to announce", true),
            Commands.slash("getresource", "Get a resource from the KiyoshiCenter")
                .setGuildOnly(true)
                .addOption(OptionType.STRING, "resource", "Do /getresourcelist to see all Resources", true)
                .setDefaultPermissions(
                    DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND)),
            Commands.slash("getresourcelist", "Get a list of all resources from the KiyoshiCenter")
                .setGuildOnly(true)
                .setDefaultPermissions(
                    DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND)),
            Commands.slash("help", "Show the help menu 🍖")
                .setGuildOnly(true)
                .setDefaultPermissions(
                    DefaultMemberPermissions.enabledFor(Permission.MESSAGE_SEND)),
            Commands.slash("userinfo", "Get info about a user")
                .setGuildOnly(true)
                .setDefaultPermissions(
                    DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addOption(OptionType.USER, "user", "The user to get info about", true),
            Commands.slash("setminecraftpluginorderchannel", "Set the minecraft plugin order channel")
                .setGuildOnly(true)
                .setDefaultPermissions(
                    DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        ).queue()
    }
    build()
}