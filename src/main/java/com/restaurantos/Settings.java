package com.restaurantos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class Settings {
    private static final Logger logger = LogManager.getLogger(Settings.class.getName());

    protected static double winWidthDefault = 1200;
    protected static double winHeightDefault = 675;
    protected static double winWidth = winWidthDefault;
    protected static double winHeight = winHeightDefault;
    protected static boolean winCanResize = true;
    protected static boolean winIsMaximize = false;

    public static void loadSettings(){
        logger.log(Level.INFO, "Load Settings Started");
        String read = Files.readString("\\App\\settings.txt");

        if (!read.equals("")) {
            try {
                JSONObject settingsObj = new JSONObject(read);
                JSONObject windowObj = settingsObj.getJSONObject("window");
                winWidth = windowObj.getDouble("winWidth");
                winHeight = windowObj.getDouble("winHeight");
                winCanResize = windowObj.getBoolean("winCanResize");
                winIsMaximize = windowObj.getBoolean("winIsMaximize");
                Main.useDarkMode = windowObj.getBoolean("useDarkMode");

                logger.log(Level.INFO, "Load Settings Done");
            }catch (org.json.JSONException e){
                saveSettings();
                logger.log(Level.ERROR, "Wrong Settings File -> Creating New");
            }
        }
    }
    public static void saveSettings(){
        logger.log(Level.INFO, "Save Settings Started");
        JSONObject settingsObj = new JSONObject();

        JSONObject windowObj = new JSONObject();
        windowObj.put("winWidth", winWidth);
        windowObj.put("winHeight", winHeight);
        windowObj.put("winWidthDefault", winWidthDefault);
        windowObj.put("winHeightDefault", winHeightDefault);
        windowObj.put("winCanResize", winCanResize);
        windowObj.put("winIsMaximize", winIsMaximize);
        windowObj.put("useDarkMode", Main.useDarkMode);

        settingsObj.put("window", windowObj);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString( settingsObj.toString());
        String enhanceJson = gson.toJson(je);

        Files.writeString("\\App\\settings.txt", enhanceJson);
        logger.log(Level.INFO, "Save Settings Done");
    }

}
