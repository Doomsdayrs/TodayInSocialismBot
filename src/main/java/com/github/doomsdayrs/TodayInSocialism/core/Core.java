package com.github.doomsdayrs.TodayInSocialism.core;

import com.github.doomsdayrs.TodayInSocialism.command.Commands;
import com.github.doomsdayrs.TodayInSocialism.support.Logs;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;

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
 * com.github.doomsdayrs.TodayInSocialism.core
 * 09 / 05 / 2019
 *
 * @author github.com/doomsdayrs
 */
//TODO Finish writing core class
public class Core {
    private static DiscordApi API;
    private static boolean debug = false;

    public static void main(String[] args) {
        for (String string : args) if (string.equalsIgnoreCase("-debug")) debug = true;

        System.out.println("\nMy version hun: " + Version.VersionNum);
        System.out.println("Change log: " + "\n==============================\n" + Version.changeLog);
        System.out.println("Hello " + System.getProperty("user.name") + "!");
        System.out.println("The OS is: " + System.getProperty("os.name") + "," + System.getProperty("os.version") + ", " + System.getProperty("os.arch"));
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("Current nano time " + System.nanoTime());
        System.out.println();

        //Config
        Config.confirmConfig();
        String[] config = Config.ReturnData();
        if (!debug)
            Logs.runLogging();


        //The bulk of the program
        API = new DiscordApiBuilder().setToken(config[0]).login().join();
        CommandHandler cmdHandler = new JavacordHandler(API);
        cmdHandler.setDefaultPrefix("s!");
        cmdHandler.registerCommand(new Commands(API));
        API.addServerJoinListener(event ->
        {
            event.getServer().getOwner().sendMessage("Thanks for inviting me to your server! " +
                    "\n I'm programed and maintained by doomsdayrs@gmail.com! " +
                    "\n This is my prefix ``" + cmdHandler.getDefaultPrefix() + "``");
        });
        System.out.println(API.createBotInvite(new PermissionsBuilder().setAllowed(PermissionType.SEND_MESSAGES, PermissionType.READ_MESSAGES, PermissionType.ATTACH_FILE, PermissionType.EMBED_LINKS).build()));

    }
}
