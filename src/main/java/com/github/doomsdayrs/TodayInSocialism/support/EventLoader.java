package com.github.doomsdayrs.TodayInSocialism.support;

import com.github.doomsdayrs.TodayInSocialism.core.Config;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

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
 * 11 / 05 / 2019
 *
 * @author github.com/doomsdayrs
 */
public class EventLoader {
    public static JSONArray jsonArray = new JSONArray();

    private static final String url = "https://raw.githubusercontent.com/Doomsdayrs/TodayInSocialismBot/master/events.json";

    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter(new File("/home/doomsdayrs/IdeaProjects/TodayInSocialismBot/events.json"));
        fileWriter.write(jsonArray.toJSONString());
        fileWriter.flush();
    }

    public static void initialize() throws IOException, ParseException {
        downloadLatest();
    }

    public static void downloadLatest() throws IOException, ParseException {
        FileUtils.copyURLToFile(new URL(url), new File(Config.execDir + "Data/events.json"));
        jsonArray = (JSONArray) new JSONParser().parse(new FileReader(new File(Config.execDir + "Data/events.json")));
    }
}
