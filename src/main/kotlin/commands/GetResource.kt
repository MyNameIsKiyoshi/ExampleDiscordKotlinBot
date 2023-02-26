package commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import utils.KiyoshiAPI
import java.awt.Color

class GetResource: ListenerAdapter() {

    private val api = KiyoshiAPI()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val resource: String? = event.getOption("resource", OptionMapping::getAsString)
        // make sure we handle the right command
        when (event.name) {
            "getresource" -> {
                if(event.guild?.id.equals(api.getConfig("GUILDID"))) {
                    if (resource == "kotlinmc") {
                        event.replyEmbeds(
                            EmbedBuilder()
                                .setTitle("- Minecraft Kotlin Plugin - ")
                                .setDescription("Getting kotlin resource...")
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                .setTimestamp(event.timeCreated)
                                .build()
                        ).queue {
                            event.hook.editOriginalEmbeds(
                                EmbedBuilder()
                                    .setTitle("- Minecraft Kotlin Plugin - ")
                                    .setDescription("Code successfully generated!\nFrom https://dev.kiyoshi.space\n\nYou can get an example here https://github.com/MyNameIsKiyoshi/KotlinExamplePlugin/")
                                    .setImage(api.getConfig("SERVERIMAGE"))
                                    .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                    .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                    .addField("repositories", api.codeMessage(kotlinPluginMinecraftRepositories(), "kt"), false)
                                    .addField("dependencies", api.codeMessage(kotlinPluginMinecraftDependencies(), "kt"), false)
                                    .setTimestamp(event.timeCreated)
                                    .build()
                            ).queue()
                            AnsiConsole.systemInstall()
                            println(ansi().fgBrightBlue().a("[").reset().a("Justin").fgBrightBlue().a("]").reset().fgBrightGreen().a(" Successfully generated kotlin plugin code, for ").reset().fgBrightRed().a(event.user.name).reset().fgBrightGreen().a(" in ").reset().fgBrightRed().a(event.guild!!.name).reset().fgBrightGreen().a("!").reset())
                            AnsiConsole.systemInstall()
                        }
                    } else if (resource == "vaultmc"){
                        event.replyEmbeds(
                            EmbedBuilder()
                                .setTitle("- Minecraft Vault API - ")
                                .setDescription("Getting vault resource...")
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                .setTimestamp(event.timeCreated)
                                .build()
                        ).queue {
                            event.hook.editOriginalEmbeds(
                                EmbedBuilder()
                                    .setTitle("- Minecraft Vault API - ", "https://nexus.kiyoshi.space")
                                    .setDescription("Code successfully generated!\nFrom https://dev.kiyoshi.space\n\nYou can find my APIs on https://nexus.kiyoshi.space")
                                    .setImage(api.getConfig("SERVERIMAGE"))
                                    .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                    .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                    .addField("repositories", api.codeMessage(minecraftVaultAPIRespitories(), "gradle"), false)
                                    .addField("dependencies", api.codeMessage(minecraftVaultAPIDependencies(), "gradle"), false)
                                    .addField("setupcode", api.codeMessage(minecraftVaultAPISetupCode(), "kt"), false)
                                    .addField("checkonenable", api.codeMessage(minecraftVaultAPICheckIf(), "kt"), false)
                                    .setTimestamp(event.timeCreated)
                                    .build()
                            ).queue()
                            AnsiConsole.systemInstall()
                            println(ansi().fgBrightBlue().a("[").reset().a("Justin").fgBrightBlue().a("]").reset().fgBrightGreen().a(" Successfully generated kotlin plugin code, for ").reset().fgBrightRed().a(event.user.name).reset().fgBrightGreen().a(" in ").reset().fgBrightRed().a(event.guild!!.name).reset().fgBrightGreen().a("!").reset())
                            AnsiConsole.systemInstall()
                        }
                    } else {
                        event.replyEmbeds(
                            EmbedBuilder()
                                .setTitle("Error")
                                .setDescription("Command /$resource not found, do /getresourcelist to see all resources.")
                                .setImage(api.getConfig("SERVERIMAGE"))
                                .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                                .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                                .setTimestamp(event.timeCreated)
                                .build()
                        ).queue()
                    }
                } else {
                    event.replyEmbeds(
                        EmbedBuilder()
                            .setTitle("Error")
                            .setDescription("You are not in the Kiyoshi server, you cannot use this command.")
                            .setColor(Color.decode(api.getConfig("JUSTINCOLOR")))
                            .setAuthor(event.jda.selfUser.name, api.getConfig("WEBSITE"), event.jda.selfUser.avatarUrl)
                            .setTimestamp(event.timeCreated)
                            .build()
                    ).queue()
                }
            }
        }
    }

    private fun kotlinPluginMinecraftRepositories(): String{
        return "maven {\n" +
                "        url = 'https://hub.spigotmc.org/nexus/content/repositories/snapshots/'\n" +
                "\n" +
                "        // As of Gradle 5.1, you can limit this to only those\n" +
                "        // dependencies you expect from it\n" +
                "        content {\n" +
                "            includeGroup 'org.bukkit'\n" +
                "            includeGroup 'org.spigotmc'\n" +
                "        }\n" +
                "    }\n" +
                "    /*\n" +
                "    As Spigot-API depends on the BungeeCord ChatComponent-API,\n" +
                "    we need to add the Sonatype OSS repository, as Gradle,\n" +
                "    in comparison to maven, doesn't want to understand the ~/.m2\n" +
                "    directory unless added using mavenLocal(). Maven usually just gets\n" +
                "    it from there, as most people have run the BuildTools at least once.\n" +
                "    This is therefore not needed if you're using the full Spigot/CraftBukkit,\n" +
                "    or if you're using the Bukkit API.\n" +
                "    */\n" +
                "    maven { url = 'https://oss.sonatype.org/content/repositories/snapshots' }\n" +
                "    maven { url = 'https://oss.sonatype.org/content/repositories/central' }\n" +
                "    mavenLocal()\n" +
                "    mavenCentral() // This is needed for CraftBukkit and Spigot. "
    }

    private fun kotlinPluginMinecraftDependencies(): String{
        return "compileOnly 'org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT'\n" +
                "    compileOnly 'org.spigotmc:spigot:1.19.2-R0.1-SNAPSHOT' \n"
    }

    public fun minecraftVaultAPIRespitories(): String {
        return "maven { url 'https://jitpack.io' }"
    }

    public fun minecraftVaultAPIDependencies(): String {
        return "compileOnly 'com.github.MilkBowl:VaultAPI:1.7'"
    }

    public fun minecraftVaultAPISetupCode(): String {
        return "private fun setupEconomy(): Boolean {\n" +
                "        if (server.pluginManager.getPlugin(\"Vault\") == null) {\n" +
                "            return false\n" +
                "        }\n" +
                "        val rsp = server.servicesManager.getRegistration(\n" +
                "            Economy::class.java\n" +
                "        ) ?: return false\n" +
                "        economy = rsp.provider\n" +
                "        return economy != null\n" +
                "    }"
    }

    public fun minecraftVaultAPICheckIf(): String {
        return "\n" +
                "# For Vault And Logger You Can Use KiyoshiAPI From https://nexus.kiyoshi.space\n" +
                "var log = LoggerAPI()\n" +
                "  if (!setupEconomy() ) {\n" +
                "            log.log.severe(String.format(\"[%s] - Disabled due to no Vault dependency found!\", description.name));\n" +
                "            server.pluginManager.disablePlugin(this);\n" +
                "            return;\n" +
                "        }"
    }

}