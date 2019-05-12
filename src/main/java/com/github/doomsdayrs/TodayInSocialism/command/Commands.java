package com.github.doomsdayrs.TodayInSocialism.command;

import com.github.doomsdayrs.TodayInSocialism.core.Core;
import com.github.doomsdayrs.TodayInSocialism.core.Version;
import com.github.doomsdayrs.TodayInSocialism.support.Embeds;
import com.github.doomsdayrs.TodayInSocialism.support.EventLoader;
import com.github.doomsdayrs.TodayInSocialism.support.Logs;
import com.github.doomsdayrs.TodayInSocialism.support.SQL.SQL;
import com.github.doomsdayrs.TodayInSocialism.support.SQL.SQLControl;
import com.github.doomsdayrs.TodayInSocialism.support.UnsetChannelException;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

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
 * 09 / 05 / 2019
 *
 * @author github.com/doomsdayrs
 */
//TODO Write commands to configure bot on a certain server
public class Commands implements CommandExecutor {

    private DiscordApi api;

    public Commands(DiscordApi api) {
        this.api = api;
    }

    @Command(aliases = {"info"}, description = "Gets bot info")
    public void onInfoCommand(TextChannel channel, User user) {
        Logs.logCommand(channel, user, "info");
        channel.sendMessage("I'm TodayInSocialism news bot. \nMy current version is: " + Version.VersionNum + "\n My current changelog is: " + Version.changeLog);
    }

    @Command(aliases = {"invite"}, description = "Gets bot invite")
    public void onInviteCommand(TextChannel channel, User user) {
        Logs.logCommand(channel, user, "invite");
        channel.sendMessage(api.createBotInvite(new PermissionsBuilder().setAllowed(PermissionType.SEND_MESSAGES, PermissionType.READ_MESSAGES, PermissionType.ATTACH_FILE, PermissionType.EMBED_LINKS).build()));
    }

    @Command(aliases = {"Shutdown"}, showInHelpPage = false)
    public void onShutDownCommand(TextChannel channel, User user) {
        Logs.logCommand(channel, user, "Shutdown");
        String ID = user.getIdAsString();

        if (ID.equals("244481558831038464")) {
            System.out.println("User " + user.getDiscriminatedName() + " has shutdown the bot");
            channel.sendMessage(Embeds.messageImage("Goodnight for now", "https://i.imgur.com/mwGf0kK.gif"));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.exit(0);
        } else {
            System.out.println("(" + ID + ") " + user.getDiscriminatedName() + " Attempted to shutdown bot without being owner");
            channel.sendMessage(Embeds.message("You aren't my creator silly!"));
        }
    }

    @Command(aliases = {"store"}, showInHelpPage = false)
    public void onStoreCommand(TextChannel channel, User user) {
        Logs.logCommand(channel, user, "store");
        String ID = user.getIdAsString();

        if (ID.equals("244481558831038464")) {
            try {
                SQL.toHard();
                channel.sendMessage(Embeds.message("Stored database"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            channel.sendMessage(Embeds.message("You aren't my creator silly!"));
        }
    }

    @Command(aliases = {"backup"}, showInHelpPage = false)
    public void onBackupCommand(TextChannel channel, User user) {
        Logs.logCommand(channel, user, "backup");
        String ID = user.getIdAsString();

        if (ID.equals("244481558831038464")) {
            try {
                SQL.backup();
                channel.sendMessage(Embeds.message("Backed up database"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            channel.sendMessage(Embeds.message("You aren't my creator silly!"));
        }
    }

    @Command(aliases = {"dl"}, showInHelpPage = false)
    public void onDLCommand(TextChannel channel, User user) {
        Logs.logCommand(channel, user, "dl");
        String ID = user.getIdAsString();
        if (ID.equals("244481558831038464")) {
            try {
                EventLoader.downloadLatest();
                System.out.println("Downloaded latest");
                channel.sendMessage(Embeds.message("Downloaded latest"));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

        } else {
            channel.sendMessage(Embeds.message("You aren't my creator silly!"));
        }
    }

    @Command(aliases = {"restartThread", "rt"}, showInHelpPage = false)
    public void onRestartThreadCommand(TextChannel channel, User user) {
        Logs.logCommand(channel, user, "restartThread");
        String ID = user.getIdAsString();
        if (ID.equals("244481558831038464")) {
            Core.resetEvent();
        } else {
            channel.sendMessage(Embeds.message("You aren't my creator silly!"));
        }
    }


    @Command(aliases = {"tA"}, showInHelpPage = false)
    public void onTAommand(TextChannel channel, User user) {
        Logs.logCommand(channel, user, "tA");
        String ID = user.getIdAsString();
        if (ID.equals("244481558831038464")) {
            try {
                SQLControl.announce(api);
            } catch (ParseException | SQLException e) {
                e.printStackTrace();
            }

        } else {
            channel.sendMessage(Embeds.message("You aren't my creator silly!"));
        }
    }

    @Command(aliases = {"setChannel", "sc"}, description = "sets the channel for the bot to use, FIRST THING TO DO")
    public void onSetChannelCommand(TextChannel channel, Server server, User user) {
        Logs.logCommand(channel, user, "info");
        if (server != null) {
            try {
                if (SQLControl.setChannel(server.getId(), channel.getId()))
                    channel.sendMessage(Embeds.message("Set properly"));
                else channel.sendMessage(Embeds.message("Not set"));
            } catch (SQLException | ParseException e) {
                System.out.println("Something went wrong");
                channel.sendMessage(Embeds.message("Something went wrong!!!"));
            }
        } else channel.sendMessage(Embeds.message("Not une server"));
    }

    @Command(aliases = {"setTime", "st"}, description = "sets the time to announce events, 24 hour based on UTC")
    public void onSetTimeCommand(TextChannel channel, Server server, User user, String command, String timeString) {
        Logs.logCommand(channel, user, "info");
        if (server != null) {
            try {
                int time = Integer.parseInt(timeString.replace(command, ""));
                if (time < 24 && time > -1) {
                    try {
                        if (SQLControl.setHour(server.getId(), time))
                            channel.sendMessage(Embeds.message("Set properly"));
                        else channel.sendMessage(Embeds.message("Not set"));
                    } catch (SQLException | ParseException e) {
                        System.out.println("Something went wrong");
                        channel.sendMessage(Embeds.message("Something went wrong!!!"));
                    } catch (UnsetChannelException e) {
                        channel.sendMessage(Embeds.message("Please set the channel first fools!"));
                    }
                } else channel.sendMessage(Embeds.message("Time provided is out of bounds!"));
            } catch (NumberFormatException e) {
                channel.sendMessage(Embeds.message("That is not an hour of the day"));
            }
        } else channel.sendMessage(Embeds.message("Not une server"));
    }
}
