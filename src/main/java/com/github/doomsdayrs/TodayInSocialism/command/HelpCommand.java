package com.github.doomsdayrs.TodayInSocialism.command;

import com.github.doomsdayrs.TodayInSocialism.support.Logs;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.awt.*;

/**
 * This file is part of TodayInSocialismBot.
 * TodayInSocialismBot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Jikan4java.  If not, see <https://www.gnu.org/licenses/>.
 * ====================================================================
 * TodayInSocialismBot
 * com.github.doomsdayrs.TodayInSocialism.command
 * 11 / 05 / 2019
 *
 * @author github.com/doomsdayrs
 */
public class HelpCommand
        implements CommandExecutor {

    private final CommandHandler commandHandler;

    private final EmbedBuilder Help;


    public HelpCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        this.Help = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setThumbnail("https://i.imgur.com/K4vx0nO.png")
                .setTitle("Help menu")
                .setDescription(commandList().toString());
    }

    private StringBuilder commandList() {
        StringBuilder stringList = new StringBuilder();
        for (CommandHandler.SimpleCommand simpleCommand : this.commandHandler.getCommands())
            if (simpleCommand.getCommandAnnotation().showInHelpPage()) {
                stringList.append("\n");
                String[] aliases = simpleCommand.getCommandAnnotation().aliases();
                stringList.append("**");
                for (String alias : aliases) {
                    if (!simpleCommand.getCommandAnnotation().requiresMention()) {
                        stringList.append(this.commandHandler.getDefaultPrefix());
                    }
                    stringList.append(alias);
                    stringList.append(" | ");
                }
                stringList.deleteCharAt(stringList.lastIndexOf("|"));
                stringList.append(":** ");
                String description = simpleCommand.getCommandAnnotation().description();
                stringList.append(description);
            }
        return stringList;
    }

    @Command(aliases = {"help", "h", "?"}, description = "Shows this page")
    @SuppressWarnings("unused")
    public void onHelpCommand(TextChannel channel, User user) {
        Logs.logCommand(channel, user, "help");
        channel.sendMessage(this.Help);
    }
}

