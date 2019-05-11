package com.github.doomsdayrs.TodayInSocialism.support;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
 * com.github.doomsdayrs.TodayInSocialism.support
 * 10 / 05 / 2019
 *
 * @author github.com/doomsdayrs
 */

    public class Logs {

        public static void runLogging() {
            System.out.println("\nConsole output will be now stored in Logs");
            PrintStream out = null;
            //Makes directory for logs

            if (!Files.exists(Paths.get(Others.path + "Logs"))) {
                System.out.println("Logs folder does not exist, creating");
                try {
                    Files.createDirectory(Paths.get(Others.path + "Logs"));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Failed to make Logs folder");
                    System.exit(0);
                }
            }


            try {
                if (System.getProperty("os.name").equals("Windows 10")) {
                    System.out.println("Console will be outputed via Windows 10 Method");
                    out = new PrintStream(new FileOutputStream(Others.path + "\\Logs\\" + "DoomsBotLog " + Others.dateFormatForFile.format(Others.date) + ".txt"));
                } else if (System.getProperty("os.name").equals("Linux")) {
                    System.out.println("Console will be outputed via Linux Method");
                    out = new PrintStream(new FileOutputStream(Others.path + "Logs/" + "DoomsBotLog " + Others.dateFormatForFile.format(Others.date) + ".txt"));
                }
                System.out.println("\nLet's get that main program running hun:");
            } catch (FileNotFoundException e) {
                System.out.println("EEKKK!! Something went wrong in the logger, please send the log to my creator!");
                e.printStackTrace();
            }

            System.setOut(out);
            System.setErr(out);
        }

        public static void logCommand(TextChannel channel, User user, String command) {
            Others.commandsExecuted++;
            if (channel.asServerChannel().isPresent()) {
                Server server = channel.asServerChannel().get().getServer();
                System.out.println("\n>>" + command + " command by " + user.getDiscriminatedName() + "(" + user.getId() + ")  at |" + Others.dateFormat.format(Others.date) + "| on " + server.getName() + "(" + server.getId() + ")");
            } else if (channel.asPrivateChannel().isPresent()) {
                System.out.println("\n>>" + command + " command by " + user.getDiscriminatedName() + "(" + user.getId() + ")  at |" + Others.dateFormat.format(Others.date) + "| in direct message");
            } else
                System.out.println("\n>>" + command + " command by " + user.getDiscriminatedName() + "(" + user.getId() + ")  at |" + Others.dateFormat.format(Others.date) + "|");
        }
    }

