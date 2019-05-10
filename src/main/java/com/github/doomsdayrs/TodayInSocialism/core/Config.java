package com.github.doomsdayrs.TodayInSocialism.core;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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

class Config {
    private static final String configFile = "data.json";
    private static String execDir;
    static {
        try {
            execDir = getExecDir();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static final String[] names = {"token"};

    /**
     * Looks for a config.properties config file in the application root directory.
     * Will create one if none was found
     */
    static void confirmConfig() {
        File config = new File(execDir + configFile);
        System.out.println("\n" + execDir + configFile + " exists: " + config.exists());
        if (!config.exists()) {
            try (FileWriter fileWriter = new FileWriter(config)) {

                JSONObject jsonObject = new JSONObject();

                for (String name : names) jsonObject.put(name, "");

                System.out.println("\nCreating new config file");
                System.out.println(jsonObject.toJSONString());
                if (config.canWrite())
                    fileWriter.write(jsonObject.toJSONString());
                else {
                    System.out.println("Cannot write");
                    System.exit(1);
                }
                fileWriter.flush();
                System.out.println("Please fill out file fully");
                System.exit(0);
            } catch (IOException e) {
                System.out.println("\n(Within confirmConfig IOException) IO Error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Config exists, Confirming legitimacy. . .");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = (JSONObject) new JSONParser().parse(new FileReader(execDir + configFile));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                System.out.println("Invalid config, Deleting");
                if (!new File(execDir + configFile).delete())
                    System.out.println("I can't even delete it smh");
                System.exit(1);
            }
            if (!jsonObject.containsKey(names[0])){
                System.out.println("Missing key: " + names[0]);
                System.exit(1);
            }
        }
    }

    static String[] ReturnData() {
        String[] string = new String[names.length];
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(execDir + configFile));
            for (int x = 0; x < names.length; x++)
                string[x] = jsonObject.get(names[x]).toString();
        } catch (IOException ex) {
            System.out.println("\n(Within ReturnData IOException) IO Error occurred: ");
            ex.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * Returns the execution directory.
     * When the application is run from a .jar file this will remove .jar from its path
     *
     * @return the execution directory
     */
    private static String getExecDir() throws UnsupportedEncodingException {
        String decodedPath;
        String runtimePath = Core.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        System.out.println("\nGetting Execution direction...");
        System.out.println("Execution directory raw: " + runtimePath);

        decodedPath = URLDecoder.decode(runtimePath, StandardCharsets.UTF_8.toString());
        if (decodedPath.toLowerCase().endsWith(".jar")) {
            System.out.println("Application was called from jar file");
            decodedPath = decodedPath.substring(0, decodedPath.lastIndexOf("/") + 1);
        }
        String output = "";

        if (System.getProperty("os.name").equals("Windows 10")) {
            output = decodedPath.substring(1);
        } else if (System.getProperty("os.name").equals("Linux")) {
            output = decodedPath;
        }
        System.out.println("Final Execution Directory: " + output);
        return output;
    }

}

