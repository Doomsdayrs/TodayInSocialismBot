package com.github.doomsdayrs.TodayInSocialism.support;

import com.github.doomsdayrs.TodayInSocialism.core.Config;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
public class Others {
    public static final Date date = new Date();
    static final Random gen = new Random();
    public static final DateFormat dateFormat = new SimpleDateFormat(" yyyy MM dd HH mmm ss");
    public static final DateFormat dateFormatForFile = new SimpleDateFormat(" yyyy-MM-dd-HH-mm");
    public static int commandsExecuted;

    public static String path;

    static {
        try {
            path = Config.getExecDir();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

