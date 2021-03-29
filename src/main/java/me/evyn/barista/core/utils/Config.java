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

package me.evyn.barista.core.utils;

import com.moandjiezana.toml.Toml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    public static final String API_TOKEN;
    public static final String DEFAULT_PREFIX;
    public static final Boolean DEFAULT_EMBEDS_ENABLED;
    public static final String[] DEFAULT_MAINTAINERS;
    public static final Boolean DATASOURCE_ENABLED;
    public static final String DATASOURCE_TYPE;
    public static final String DATASOURCE_IP;
    public static final String DATASOURCE_PORT;
    public static final String DATASOURCE_DATABASE;
    public static final String DATASOURCE_USER;
    public static final String DATASOURCE_PASSWORD;

    static {
        String apiToken = null;
        String defaultPrefix = null;
        Boolean defaultEmbedsEnabled = null;
        List<String> defaultMaintainers = null;
        Boolean datasourceEnabled = null;
        String datasourceType = null;
        String datasourceIP = null;
        String datasourcePort = null;
        String datasourceDatabase = null;
        String datasourceUser = null;
        String datasourcePassword = null;
        try (InputStream stream = new FileInputStream("config.toml")) {
            Toml toml = new Toml().read(stream);
            apiToken = toml.getString("API.token");
            defaultPrefix = toml.getString("Defaults.prefix");
            defaultEmbedsEnabled = toml.getBoolean("Defaults.embeds_enabled");
            defaultMaintainers = toml.getList("Defaults.maintainer_ids");
            datasourceEnabled = toml.getBoolean("Datasource.enabled");
            datasourceType = toml.getString("Datasource.type");
            datasourceIP = toml.getString("Datasource.ip");
            datasourcePort = toml.getString("Datasource.port");
            datasourceDatabase = toml.getString("Datasource.database");
            datasourceUser = toml.getString("Datasource.user");
            datasourcePassword = toml.getString("Datasource.password");
        } catch (Exception e) {
            // attempt to read system environment variables for Docker environment
            apiToken = System.getenv("API_TOKEN");
            defaultPrefix = System.getenv("DEFAULT_PREFIX");

            if (System.getenv("DEFAULT_EMBEDS_ENABLED").equals("true")) {
                defaultEmbedsEnabled = true;
            } else if (System.getenv("DEFAULT_EMBEDS_ENABLED").equals("false")) {
                defaultEmbedsEnabled = false;
            }

            String maintainersRaw = System.getenv("DEFAULT_MAINTAINERS");
            defaultMaintainers = Arrays.asList(maintainersRaw.split(","));
        } finally {

            API_TOKEN = apiToken;
            DEFAULT_PREFIX = defaultPrefix;
            DEFAULT_EMBEDS_ENABLED = defaultEmbedsEnabled;
            DEFAULT_MAINTAINERS = defaultMaintainers.toArray(new String[0]);
            DATASOURCE_ENABLED = datasourceEnabled;
            DATASOURCE_TYPE = datasourceType;
            DATASOURCE_IP = datasourceIP;
            DATASOURCE_PORT = datasourcePort;
            DATASOURCE_DATABASE = datasourceDatabase;
            DATASOURCE_USER = datasourceUser;
            DATASOURCE_PASSWORD = datasourcePassword;

            // check if configurations are correct

            if (API_TOKEN == null || !API_TOKEN.matches(".{59,}")) {
                LOGGER.error("Bot token must be at least 59 characters in length");
                System.exit(0);
            }

            if (DEFAULT_PREFIX == null || !DEFAULT_PREFIX.matches(".{1,100}")) {
                LOGGER.error("The default global prefix must be between 1 and 100 characters");
                System.exit(0);
            }

            if (DEFAULT_EMBEDS_ENABLED == null) {
                LOGGER.error("The default embeds should be true or false");
                System.exit(0);
            }

            if (DATASOURCE_ENABLED == null) {
                LOGGER.error("Datasource.enabled should be true or false");
            }

            List<String> validSources = Arrays.asList("MySQL", "mysql", "MYSQL", "SQLite", "sqlite", "SQLITE");
            if (DATASOURCE_ENABLED == true && !validSources.contains(DATASOURCE_TYPE)) {
                LOGGER.error("Datasource is enabled but type is invalid");
                System.exit(0);
            }

            // send logs if configuration was loaded

            LOGGER.info("Config loaded");
        }
    }
}
