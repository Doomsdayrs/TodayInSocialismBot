package com.github.doomsdayrs.TodayInSocialism.command;

import com.github.doomsdayrs.TodayInSocialism.core.Version;
import com.github.doomsdayrs.TodayInSocialism.support.Logs;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.user.User;

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
    public Commands(DiscordApi api){
        this.api = api;
    }

    @Command(aliases = {"info"}, description = "Gets bot info")
    public void onInfoCommand(TextChannel channel, User user) {
        Logs.logCommand(channel, user, "info");
        channel.sendMessage("I'm TodayInSocialism news bot. \nMy current version is: " + Version.VersionNum + "\n My current changelog is: " + Version.changeLog);
    }

    @Command(aliases = {"invite"}, description = "Gets bot invite")
    public void onInviteCommand(TextChannel channel, User user) {
        Logs.logCommand(channel, user, "info");
        channel.sendMessage(api.createBotInvite(new PermissionsBuilder().setAllowed(PermissionType.SEND_MESSAGES,PermissionType.READ_MESSAGES,PermissionType.ATTACH_FILE,PermissionType.EMBED_LINKS).build()));
    }

}
