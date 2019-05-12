package com.github.doomsdayrs.TodayInSocialism.support.SQL;

import com.github.doomsdayrs.TodayInSocialism.support.EventLoader;
import com.github.doomsdayrs.TodayInSocialism.support.UnsetChannelException;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

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
 * 09 / 05 / 2019
 *
 * @author github.com/doomsdayrs
 */
public class SQLControl {

    public static void storeServers(Collection<Server> servers) throws SQLException {
        for (Server server : servers) {
            System.out.println("Checking server: " + server.getId() + " " + server.getName());
            newServer(server.getId());
        }
        System.gc();
        System.out.println("Done!");
    }

    private static final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    public static boolean setChannel(long serverID, long channelID) throws SQLException, ParseException {
        Statement statement = SQL.connection.createStatement();
        if (statement.executeQuery("select count() from serverData where id = " + serverID).getInt("count()") == 1) {
            ResultSet resultSet = statement.executeQuery("select config from serverData where id = " + serverID);
            JSONObject jsonObject;
            if (resultSet.next()) {
                String jsonString = resultSet.getString("config");
                if (jsonString != null)
                    jsonObject = (JSONObject) new JSONParser().parse(jsonString);
                else jsonObject = new JSONObject();
                jsonObject.put("channel", channelID);
            } else {
                jsonObject = new JSONObject();
                jsonObject.put("channel", channelID);
                jsonObject.put("time", 0);
            }
            statement.executeUpdate("update serverData set config = '" + jsonObject.toJSONString() + "' where id = " + serverID);
            return true;
        }
        return false;
    }

    public static boolean setHour(long serverID, int hour) throws SQLException, ParseException, UnsetChannelException {
        Statement statement = SQL.connection.createStatement();
        if (statement.executeQuery("select count() from serverData where id = " + serverID).getInt("count()") == 1) {
            ResultSet resultSet = statement.executeQuery("select config from serverData where id = " + serverID);
            JSONObject jsonObject;
            if (resultSet.next()) {
                jsonObject = (JSONObject) new JSONParser().parse(resultSet.getString("config"));
                jsonObject.put("time", hour);
                statement.executeUpdate("update serverData set config = '" + jsonObject.toJSONString() + "' where id = " + serverID);
                return true;
            } else {
                throw new UnsetChannelException();
            }
        }
        return false;
    }

    private static void newServer(long id) throws SQLException {
        Statement statement = SQL.connection.createStatement();
        if ((statement.executeQuery("select count() from serverData where id = " + id).getInt("count()")) != 1) // Checks if the server row exists in serverData table
            statement.executeUpdate("insert into serverData (id,lastOut) values(" + id + ",0)"); // Inserts fresh Data
    }

    public static void announce(DiscordApi api) throws SQLException, ParseException {
        Statement statement = SQL.connection.createStatement();
        ResultSet set = statement.executeQuery("select id,config,lastOut from serverData");
        DateTime dateTime = new DateTime(DateTimeZone.UTC);

        ArrayList<String> messageQueue = new ArrayList<>();
        boolean events = false;
        for (int x = 0; x < EventLoader.jsonArray.size(); x++) {
            JSONObject event = (JSONObject) EventLoader.jsonArray.get(x);
            String[] date = event.get("date").toString().split(",");
            if (dateTime.getMonthOfYear() == Integer.parseInt(date[1])) {
                if (dateTime.getDayOfMonth() == Integer.parseInt(date[0])) {
                    if (!events) events = true;
                    String message = date[0] + "/" + date[1] + "/" + date[2];
                    message = message + "\n" + event.get("description").toString();
                    if (event.containsKey("importantLinks"))
                        message = message + "\n" + event.get("importantLinks").toString();
                    messageQueue.add(message);
                }
            }
        }

        while (set.next()) {
            System.out.println(set.getString("id"));


            //Continues if there are events
            if (events) {
                String configString = set.getString("config");
                if (configString != null) { //Checks to see if config isnt null
                    JSONObject jsonObject = (JSONObject) new JSONParser().parse(configString);
                    String time = jsonObject.get("time").toString();
                    if (time != null && !time.isEmpty()) {//Checks to see if time was set or not.
                        int hour = Integer.parseInt(time);
                        if (dateTime.getHourOfDay() == hour) {//Checks to see if it is time to announce for them
                            long currentTime = new Date().getTime();
                            long lastTime = set.getLong("lastOut");
                            if (currentTime >= lastTime + 86400000) {//Announces if they haven't been informed in 24 hours
                                statement.executeUpdate("updateServerData set lastOut = " + currentTime);
                                //Gets the channel
                                long channelID = Long.parseLong(jsonObject.get("channel").toString());
                                Optional<Channel> optionalChannel = api.getChannelById(channelID);
                                if (optionalChannel.isPresent()) {
                                    Channel channel = optionalChannel.get();
                                    Optional<TextChannel> optionalTextChannel = channel.asTextChannel();
                                    if (optionalTextChannel.isPresent()) {
                                        TextChannel textChannel = optionalTextChannel.get();
                                        //Announces the events of today
                                        //TODO Change this to be a different form of @everyone
                                        textChannel.sendMessage("@everyone");
                                        for (String string : messageQueue)
                                            textChannel.sendMessage(string);
                                    }
                                }
                            }
                        }
                    }

                }

            }
        }
    }
}
