/*
 * MIT License
 *
 * Copyright (c) 2021 Evyn Price
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.evyn.barista.core;

import me.evyn.barista.core.listeners.MessageListener;
import me.evyn.barista.core.listeners.ReadyListener;
import me.evyn.barista.core.utils.Config;
import me.evyn.barista.core.utils.Datasource;
import net.dv8tion.jda.api.JDABuilder;
import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class Barista {
    public static void main(String[] args) throws LoginException, SQLException {
        // get connection and close to load Datasource
        Datasource.getConnection().close();

        JDABuilder builder = JDABuilder.createDefault(Config.API_TOKEN);

        // add listeners
        builder.addEventListeners(new ReadyListener());
        builder.addEventListeners(new MessageListener());

        builder.build();
    }
}
