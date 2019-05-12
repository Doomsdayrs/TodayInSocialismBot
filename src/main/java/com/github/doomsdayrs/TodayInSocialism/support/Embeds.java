package com.github.doomsdayrs.TodayInSocialism.support;

import org.javacord.api.entity.message.embed.EmbedBuilder;

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
 * com.github.doomsdayrs.TodayInSocialism.support
 * 11 / 05 / 2019
 *
 * @author github.com/doomsdayrs
 */
public class Embeds {
    public static EmbedBuilder message(String message) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(message);
    }

    public static EmbedBuilder messageImage(String message, String url) {
        return new EmbedBuilder().setColor(Color.RED)
                .setDescription(message).setUrl(url);
    }
}
