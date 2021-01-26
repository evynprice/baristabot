package me.evyn.baristabot.data;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 * The Config class looks for a "config.json" file in the project directory and adds the fields to instance
 * variables. These can be accessed through several getter methods
 * @author Evyn
 */
public class Config {

    private String token;
    private String prefix;
    private String privilegedID;


    /**
     * The default constructor creates a new JSONParser object, and then tries to parse the contents of the file
     * The contents are then added to instance variables
     */
    public Config() {
        JSONParser parser = new JSONParser();
        try {
            // fetch jsonObject from config.json file
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("config.json"));

            // fetch token from jsonObject and add it to instance variable token
            this.token = (String) jsonObject.get("Token");

            // fetch prefix from jsonObject and add it to instance variable prefix
            this.prefix = (String) jsonObject.get("Prefix");

            // fetch privilegedID from jsonObject and add it to instance variable privilegedID
            this.privilegedID = (String) jsonObject.get("PrivilegedID");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return String This returns the bot token
     */
    public String getToken() {
        return this.token;
    }

    /**
     * @return String This returns the bot prefix
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * @return String This returns the privilegedID
     */
    public String getPrivilegedID() {
        return this.privilegedID;
    }
}
