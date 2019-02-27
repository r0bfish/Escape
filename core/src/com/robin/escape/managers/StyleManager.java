package com.robin.escape.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class StyleManager {
    private String selectedStyle;
    private Preferences prefs;

    public StyleManager(){
        prefs = Gdx.app.getPreferences("GameProgress");
        selectedStyle = prefs.getString("style", "normal");
    }

    public String getStyle(){
        switch (selectedStyle){
            case "normal":
                return "normal";
            case "summer":
                return "summer";
            case "desert":
                return "desert";
        }
        return "normal";
    }
}
