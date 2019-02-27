package com.robin.escape.utilities;

import java.util.HashMap;
import java.util.Map;

public class LevelHelper {
    private static Map<String, String> map;

    static {
        map = new HashMap<>();
        map.put("levels/level1.json", "levels/level2.json");
        map.put("levels/level2.json", "levels/level3.json");
        map.put("levels/level3.json", "levels/level4.json");
        map.put("levels/level4.json", "levels/level5.json");
        map.put("levels/level5.json", "levels/level6.json");
        map.put("levels/level6.json", "levels/level7.json");
        map.put("levels/level7.json", "levels/level8.json");
        map.put("levels/level8.json", "levels/level9.json");
        map.put("levels/level9.json", "levels/level11.json");
        map.put("levels/level11.json", "levels/level12.json");
        map.put("levels/level12.json", "levels/level13.json");
        map.put("levels/level13.json", "levels/level14.json");
        map.put("levels/level14.json", "levels/level15.json");
        map.put("levels/level15.json", "levels/level16.json");
        map.put("levels/level16.json", "levels/level17.json");
        map.put("levels/level17.json", "levels/level18.json");
        map.put("levels/level18.json", "levels/level19.json");
        map.put("levels/level19.json", "levels/level21.json");
        map.put("levels/level21.json", "levels/level22.json");
        map.put("levels/level22.json", "levels/level23.json");
        map.put("levels/level23.json", "levels/level24.json");
        map.put("levels/level24.json", "levels/level25.json");
        map.put("levels/level25.json", "levels/level26.json");
        map.put("levels/level26.json", "levels/level27.json");
        map.put("levels/level27.json", "levels/level28.json");
        map.put("levels/level28.json", "levels/level29.json");
    }

    public static String getNextLevel(String currentLevel){
        return map.get(currentLevel);}
}

