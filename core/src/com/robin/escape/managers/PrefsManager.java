package com.robin.escape.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.HashMap;

public class PrefsManager {
    private Preferences prefs;
    private HashMap<String, Boolean> unlockedMap;

    public PrefsManager(){
        prefs = Gdx.app.getPreferences("GameProgress");
        unlockedMap = new HashMap<>();
    }
}
