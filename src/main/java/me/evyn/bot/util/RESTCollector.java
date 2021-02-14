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

package me.evyn.bot.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RESTCollector {

    @SuppressWarnings("unchecked")
    public static List<String> getPrequelMemes() {
        try {
            URL url = new URL("https://www.reddit.com/r/prequelmemes.json?sort=top&t=week&limit=50");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // misc user agent to avoid 429
            conn.setRequestProperty("User-Agent", "Mozilla/5.0  Gecko/41.0 Firefox/41.0");

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);

            } else {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                JSONParser parse = new JSONParser();

                JSONObject jobj = (JSONObject) parse.parse(sb.toString());

                JSONObject data = (JSONObject) jobj.get("data");
                JSONArray children = (JSONArray) data.get("children");

                List<String> images = new ArrayList<>();

                for(Object child : children) {
                    JSONObject c = (JSONObject) child;
                    JSONObject childData = (JSONObject) c.get("data");
                    String imageUrl = (String) childData.getOrDefault("url_overridden_by_dest", null);

                    if (imageUrl != null) {
                        images.add(imageUrl);
                    }
                }

                return images;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
