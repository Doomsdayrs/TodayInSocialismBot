package com.github.doomsdayrs.TodayInSocialism.support.SQL;

import com.github.doomsdayrs.TodayInSocialism.support.Others;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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
 * 27 / March / 2019
 *
 * @author github.com/doomsdayrs
 */
public class SQL {
    static Connection connection;

    public static void initialize() throws ClassNotFoundException, SQLException {
        if (!Files.exists(Paths.get(Others.path + "Data"))) {
            System.out.println("Data folder does not exist, creating");
            try {
                Files.createDirectory(Paths.get(Others.path + "Data"));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to make Data folder");
                System.exit(0);
            }
        }
        if (!Files.exists(Paths.get(Others.path + "Data/backup"))) {
            System.out.println("Data/backup folder does not exist, creating");
            try {
                Files.createDirectory(Paths.get(Others.path + "Data/backup"));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to make Data/backup folder");
                System.exit(0);
            }
        }
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + Others.config[1] + Others.config[2]);
        Statement statement = connection.createStatement();
        statement.executeUpdate("create table if not exists \"serverData\" (`id` text primary key, 'config' text,'lastOut' text)");
    }

    public static void toRam() throws IOException {
        if (new File(Others.path + "Data/" + Others.config[2]).exists())
            Files.copy(new File(Others.path + "Data/" + Others.config[2]).toPath(), new File(Others.config[1] + Others.config[2]).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void toHard() throws IOException {
        if (new File(Others.config[1] + Others.config[2]).exists())
            Files.copy(new File(Others.config[1] + Others.config[2]).toPath(), new File(Others.path + "Data/" + Others.config[2]).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void backup() throws IOException {
        File backupJSON = new File(Others.path + "Data/backup/data.json");
        if (!backupJSON.exists()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", new Date().getTime());
            FileWriter fileWriter = new FileWriter(backupJSON);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
        }
        JSONObject data = null;
        try {
            data = (JSONObject) new JSONParser().parse(new FileReader(backupJSON));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (data != null) {

            if (!data.containsKey("count"))
                data.put("count", 0);
            int count = Integer.parseInt(data.get("count").toString());
            count++;
            if (count > 10) {
                new File(Others.path + "Data/backup/database" + (count - 10) + ".db").delete();
                new File(Others.path + "Data/backup/databaseRAM" + (count - 10) + ".db").delete();
            }
            File db = new File(Others.path + "Data/backup/database" + (count) + ".db");
            File dbRam = new File(Others.path + "Data/backup/databaseRAM" + (count) + ".db");
            data.put("count", count);
            FileWriter fileWriter = new FileWriter(backupJSON);
            fileWriter.write(data.toJSONString());
            fileWriter.flush();

            if (new File(Others.path + "Data/database.db").exists())
                Files.copy(new File(Others.path + "Data/" + Others.config[2]).toPath(), db.toPath());
            if (new File(Others.config[1] + "database.db").exists())
                Files.copy(new File(Others.config[1] + Others.config[2]).toPath(), dbRam.toPath());
        }
    }
}

