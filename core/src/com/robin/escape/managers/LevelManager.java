package com.robin.escape.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robin.escape.sprites.Enemy;
import com.robin.escape.sprites.Platform;
import com.robin.escape.utilities.Collision;
import com.robin.escape.sprites.Area;
import com.robin.escape.sprites.GameObject;
import com.robin.escape.sprites.MovingObject;
import com.robin.escape.sprites.Player;
import com.robin.escape.sprites.Props;
import com.robin.escape.states.EditorState;
import com.robin.escape.states.GameStateManager;
import com.robin.escape.states.MenuState;
import com.robin.escape.utilities.Textlistener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LevelManager {
    public enum MANAGER{ TERRAINMANAGER, LOGICMANAGER, PROPSMANAGER, OBJECTMANAGER }
    private StyleManager styleManager;
    private MANAGER selectedManager;
    private String levelName;
    private String levelNameNext;
    private TilesetManager tilesetManager;
    private PropsManager propsManager;
    private LogicManager logicManager;
    private ObjectManager objectManager;
    private ArrayList<Integer> starTimes;
    private ArrayList<JSONTile> JSONtiles;
    private ArrayList<JSONProp> JSONprops;
    private ArrayList<JSONArea> JSONlogic;
    private ArrayList<Object> JSONobjects;
    private ArrayList<JSONSpawn> JSONSpawn;
    private ArrayList<EnemyManager> enemySpawns;
    private Area selectedSpawn;
    private Textlistener textlistener;

    public LevelManager(){
        styleManager = new StyleManager();
        tilesetManager = new TilesetManager(styleManager.getStyle() + "/tileset.png");
        propsManager = new PropsManager(styleManager.getStyle(), "propsset.png");
        logicManager = new LogicManager("logicset.png");
        objectManager = new ObjectManager(styleManager.getStyle(), "objectset.png");
        starTimes = new ArrayList<Integer>();
        JSONtiles = new ArrayList<JSONTile>();
        JSONprops = new ArrayList<JSONProp>();
        JSONlogic = new ArrayList<JSONArea>();
        JSONobjects = new ArrayList<Object>();
        JSONSpawn = new ArrayList<JSONSpawn>();
        enemySpawns = new ArrayList<>();
        selectedManager = null;
        textlistener = new Textlistener();
    }

    /**************************
     * EditorState functions below
     * ************************/
    public void textHandler(){
        if (textlistener.getInputText() != null)
        {
            if(textlistener.getHeader().equals("amountOfMonsters")) {
                float f = Float.parseFloat(textlistener.getInputText());
                enemySpawns.add(new EnemyManager(
                        selectedSpawn.getBounds(),
                        (int)Math.floor((double)f),
                        1-(f - (int)Math.floor((double)f)),
                        styleManager.getStyle(),
                        new Texture(styleManager.getStyle() + "/objects/enemy.png")));
                //Gdx.app.log("SIze", ""+enemySpawns.size());
            }
            textlistener.resetInputText();
        }
    }
    public MANAGER getSelectedManager(){
        return selectedManager;
    }
    public void setPosOnSet(Vector3 mousePos){
        if(selectedManager != null) {
            switch (selectedManager) {
                case TERRAINMANAGER:
                    if (tilesetManager.getTileset().getBounds().contains(mousePos.x, mousePos.y)) {
                        tilesetManager.setSelected(mousePos);
                    }
                    break;
                case LOGICMANAGER:
                    if (logicManager.getLogicset().getBounds().contains(mousePos.x, mousePos.y)) {
                        logicManager.setSelected(mousePos);
                    }
                    else {
                        for (int i = 0; i < logicManager.getLogicAreas().size(); i++) {
                            if (logicManager.getLogicAreas().get(i).getBounds().contains(mousePos.x, mousePos.y)) {
                                if (logicManager.getLogicAreas().get(i).getAttribute() == TilesetManager.ATTRIBUTE.ENEMYSPAWN) {
                                    selectedSpawn = logicManager.getLogicAreas().get(i);
                                    Gdx.input.getTextInput(textlistener, "Amount of monsters, Frequency", null, "");
                                    textlistener.setHeader("amountOfMonsters");
                                    break;
                                    //Gdx.input.getTextInput(textlistener, "Frequency of monsters", null, "");
                                    //textlistener.setHeader("frequency");
                                }
                            }
                        }
                    }

                    break;
                case PROPSMANAGER:
                    if (propsManager.getPropset().getBounds().contains(mousePos.x, mousePos.y)) {
                        propsManager.setSelected(mousePos);
                    }
                    break;
                case OBJECTMANAGER:
                    if (objectManager.getTileset().getBounds().contains(mousePos.x, mousePos.y)) {
                        objectManager.setSeleced(mousePos);
                    }
                    break;
            }
        }
    }
    public void setFocus(MANAGER manager, Vector3 mousePos){
        switch (manager) {
            case TERRAINMANAGER:
                if(tilesetManager.getFocused())
                    tilesetManager.setFocused(false);
                else if(!tilesetManager.getFocused()) {
                    selectedManager = MANAGER.TERRAINMANAGER;
                    objectManager.setFocused(false);
                    tilesetManager.setFocused(true);
                    logicManager.setFocused(false);
                    propsManager.setFocused(false);
                }
                break;
            case LOGICMANAGER:
                if(logicManager.getFocused())
                    logicManager.setFocused(false);
                else if(!logicManager.getFocused()) {
                    selectedManager = MANAGER.LOGICMANAGER;
                    objectManager.setFocused(false);
                    tilesetManager.setFocused(false);
                    logicManager.setFocused(true);
                    propsManager.setFocused(false);
                }
                break;
            case PROPSMANAGER:
                if(propsManager.getFocused())
                    propsManager.setFocused(false);
                else if(!propsManager.getFocused()) {
                    selectedManager = MANAGER.PROPSMANAGER;
                    logicManager.setFocused(false);
                    objectManager.setFocused(false);
                    tilesetManager.setFocused(false);
                    propsManager.setFocused(true);
                }
                break;
            case OBJECTMANAGER:
                if(objectManager.getFocused())
                    objectManager.setFocused(false);
                else if(!objectManager.getFocused()) {
                    selectedManager = MANAGER.OBJECTMANAGER;
                    objectManager.setFocused(true);
                    tilesetManager.setFocused(false);
                    logicManager.setFocused(false);
                    propsManager.setFocused(false);
                }
                break;
        }
    }
    public void addOnLevel(Vector3 mouseStart, Vector3 mouseEnd){
        if(selectedManager != null) {
            switch (selectedManager) {
                case TERRAINMANAGER:
                    tilesetManager.addTile(mouseStart, TilesetManager.ATTRIBUTE.NORMAL);
                    break;
                case LOGICMANAGER:
                    if(mouseEnd != null) {
                        Vector3 logicSize = new Vector3(mouseEnd.x - mouseStart.x, mouseEnd.y - mouseStart.y, 0);
                        logicManager.addLogicArea(mouseStart, logicSize);
                    }
                    break;
                case PROPSMANAGER:
                    propsManager.addProps(mouseStart, propsManager.getSelectedPropType());
                    break;
                case OBJECTMANAGER:
                    objectManager.addObject(mouseStart.x, mouseStart.y, 0,0, objectManager.getSelectedType());
                    break;
            }
        }
    }
    public void exportLevel(String stringLvl){
        // Create a temporary Level-Manager for future json.
        LevelManager level = new LevelManager();

        // Add all the tiles to the tile array.
        for(com.robin.escape.sprites.Tile tile : tilesetManager.getTiles()){
            level.addTile(
                    (int)tile.getWorldPos().x, (int)tile.getWorldPos().y,
                    (int)tile.getTilesetPos().x, (int)tile.getTilesetPos().y,
                    tile.getAttribute());
        }

        // Add all the logic to the logic array.
        for(Area logObj : logicManager.getLogicAreas()){
            //Gdx.app.log("" + logObj.getAttribute(), "");
            level.addLogicArea(
                    (int) logObj.getPosition().x, (int)logObj.getPosition().y,
                    (int)logObj.getBounds().width, (int)logObj.getBounds().height,
                    (int)logObj.getLogicSetPos().x, (int)logObj.getLogicSetPos().y,
                    logObj.getAttribute());
        }

        // Add all the objects to the objects array.
        for(GameObject obj : objectManager.getObjects()){
            if(obj.getType() != null) {
                switch (obj.getType()) {
                case ENEMY:
                    level.addObject(
                            ((Enemy) obj).getPatrolPos1(),
                            ((Enemy) obj).getPatrolPos2(),
                            obj.getType());
                    break;
                    default:
                        level.addObject(
                                obj.getPosition(),
                                null,
                                obj.getType());
                }
            }
        }

        // Add all the props to the props array.
        for(com.robin.escape.sprites.Props prop : propsManager.getProps()){
            level.addProps(
                    (int)prop.getPosition().x, (int)prop.getPosition().y,
                    prop.getPropType());
        }

        for(int i=0; i<enemySpawns.size(); i++){
            EnemyManager em = enemySpawns.get(i);
            Rectangle region = em.getSpawnRegion();
            level.addSpawn(
                    Math.round(region.x), Math.round(region.y), Math.round(region.width), Math.round(region.height),
                    0, 0, TilesetManager.ATTRIBUTE.ENEMYSPAWN, em.getSpawnAmount(), em.getSpawnFreq());
        }

        // Null all the managers because we don't want them in the JSON.
        level.starTimes = null;
        level.styleManager = null;
        level.tilesetManager = null;
        level.logicManager = null;
        level.propsManager = null;
        level.objectManager = null;
        level.enemySpawns = null;
        level.textlistener = null;
        ArrayList<LevelManager> lvl = new ArrayList<LevelManager>();
        lvl.add(level);

        // Create a json-string of the level with pretty-printing.
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonLevel = gson.toJson(lvl, ArrayList.class);

        // Create a file and add the json-string into the file.
        FileHandle file = Gdx.files.local(stringLvl);
        file.writeString("", false);
        file.writeString("[\n" +
                "  {\n" +
                "    \"INFO\": [\n" +
                "      {\n" +
                "        \"2star\": 20,\n" +
                "        \"3star\": 10\n" +
                "\t  }\n" +
                "\t  ]\n" +
                "  }", true);

        file.writeString(jsonLevel.substring(1), true);
    }












    /**************************
     * PlayState functions below
     * ************************/
    public void importLevel(String stringLvl){
        levelName = stringLvl;
        FileHandle file = Gdx.files.internal(stringLvl);
        String lvlJSON = file.readString();
        Json json = new Json();
        ArrayList<JsonValue> lvlInfo = json.fromJson(ArrayList.class, lvlJSON);

        tilesetManager.getTiles().clear();
        propsManager.getProps().clear();
        logicManager.getLogicAreas().clear();
        objectManager.getObjects().clear();
        for(JsonValue t : lvlInfo.get(0).child()) {
            starTimes.add(t.getInt("2star"));
            starTimes.add(t.getInt("3star"));
        }


        // Add the tiles
        for(JsonValue t : lvlInfo.get(1).child()){
            int worldX = t.getInt("worldX");
            int worldY = t.getInt("worldY");
            int textureX = t.getInt("textureX");
            int textureY = t.getInt("textureY");
            String attribute = t.getString("attr");

            tilesetManager.addTile(new com.robin.escape.sprites.Tile(
                    new Vector3(worldX, worldY, 0),
                    new Vector3(textureX,textureY, 0),
                    tilesetManager.getTileset().getSprite(),
                    TilesetManager.ATTRIBUTE.NORMAL));
        }
        // Add the propstiles
        for(JsonValue t : lvlInfo.get(1).child().next()){
            int worldX = t.getInt("worldX");
            int worldY = t.getInt("worldY");
            String propType = t.getString("propType");
            propsManager.addProps(new Vector3(worldX, worldY,0), propsManager.stringToPropType(propType));
        }

        // Add the logictiles
        for(JsonValue t : lvlInfo.get(1).child().next().next()){
            int worldX = t.getInt("worldX");
            int worldY = t.getInt("worldY");
            int width = t.getInt("width");
            int height = t.getInt("height");
            int textureX = t.getInt("textureX");
            int textureY = t.getInt("textureY");
            String attribute = t.getString("attr");

            logicManager.addLogicArea(new Area(
                    new Vector3(worldX, worldY, 0),
                    new Vector3(width, height, 0),
                    new Vector3(textureX,textureY, 0),
                    tilesetManager.stringToAttribute(attribute)));
        }

        // Add the objects to the world
        for(JsonValue t : lvlInfo.get(1).child().next().next().next()){
            float worldX = t.getFloat("worldX");
            float worldY = t.getFloat("worldY");
            float patrolX = t.getFloat("patrolX");
            float patrolY = t.getFloat("patrolY");
            String type = t.getString("type");

            objectManager.addObject(worldX, worldY, patrolX, patrolY, objectManager.stringToObjectType(type));
        }

        // Add the spawnRegions
        for(JsonValue t : lvlInfo.get(1).child().next().next().next().next()){
            int worldX = t.getInt("worldX");
            int worldY = t.getInt("worldY");
            int width = t.getInt("width");
            int height = t.getInt("height");
            //int textureX = t.getInt("textureX");
            //int textureY = t.getInt("textureY");
            //String attribute = t.getString("attr");
            int amountEnemies = t.getInt("amountEnemies");
            double spawnFreq = t.getDouble("spawnFreq");
            enemySpawns.add(new EnemyManager(
                    new Rectangle(worldX, worldY, width, height),
                    amountEnemies,
                    spawnFreq,
                    styleManager.getStyle(),
                    new Texture(styleManager.getStyle() + "/objects/enemy.png")));
        }
    }
    public ArrayList<EnemyManager> getEnemySpawns(){return enemySpawns;}
    public ArrayList<Integer> getStarTimes(){ return starTimes; }
    public int getStarsFromTime(double time){
        if(Math.round(time) <= starTimes.get(1))
            return 3;
        else if(Math.round(time) <= starTimes.get(0))
            return 2;
        else
            return 1;
    }
    public Player getPlayer(){
        for(GameObject gb : objectManager.getObjects()){
            if(gb.getClass() == Player.class) {
                if(((MovingObject)gb).getType() == ObjectManager.TYPE.PLAYER) {
                    return (Player) gb;
                }
            }
        }
        return null;
    }
    public String getLevelName(){ return levelName; }

    public TilesetManager getTilesetManager(){return tilesetManager;}
    public PropsManager getPropsManager(){return propsManager;}
    public LogicManager getLogicManager(){return logicManager;}
    public ObjectManager getObjectManager(){return objectManager;}

    /**************************
     * General functions to update, render and dispose shit
     * ************************/

    public void update(float dt){
        textHandler();
        for(int i=0; i<enemySpawns.size(); i++)
            enemySpawns.get(i).update(dt);
    }
    public void render(SpriteBatch sb){
        for(int i=0; i<enemySpawns.size(); i++)
            enemySpawns.get(i).render(sb);
    }
    public void dispose(){
        tilesetManager.dispose();
        logicManager.dispose();
        propsManager.dispose();
        objectManager.dispose();
    }

    /**************************
     * Private functions to properly load and save json begins below
     * ************************/
    private void addTile(int worldX, int worldY, int textureX, int textureY, TilesetManager.ATTRIBUTE attr){
        JSONtiles.add(new JSONTile(worldX, worldY, textureX, textureY, attr));
    }
    private void addProps(int worldX, int worldY, PropsManager.PROPS propType){
        JSONprops.add(new JSONProp(worldX, worldY, propType));
    }
    private void addLogicArea(int worldX, int worldY, int width, int height, int textureX, int textureY, TilesetManager.ATTRIBUTE attr){
        //Gdx.app.log("" + attr, "");
        JSONlogic.add(new JSONArea(worldX, worldY, width, height, textureX, textureY, attr));
    }
    private void addSpawn(int worldX, int worldY, int width, int height, int textureX, int textureY, TilesetManager.ATTRIBUTE attr, int amountEnemies, double spawnFreq){
        JSONSpawn.add(new JSONSpawn(worldX, worldY, width, height, textureX, textureY, attr, amountEnemies, spawnFreq));
    }
    private void addObject(Vector3 position, Vector3 target, ObjectManager.TYPE type){
        JSONobjects.add(new JSONObject(position, type));
        if(target != null)
            ((JSONObject)JSONobjects.get(JSONobjects.size()-1)).addPatrol(target);
    }
    private static class JSONTile{
        private TilesetManager.ATTRIBUTE attr;
        private int worldX;
        private int worldY;
        private int textureX;
        private int textureY;
        public JSONTile(int worldX, int worldY, int textureX, int textureY, TilesetManager.ATTRIBUTE attr){
            this.worldX = worldX; this.worldY = worldY; this.textureX = textureX; this.textureY = textureY; this.attr = attr;
        }
    }
    private static class JSONArea{
        private TilesetManager.ATTRIBUTE attr;
        private int worldX;
        private int worldY;
        private int width;
        private int height;
        private int textureX;
        private int textureY;
        public JSONArea(int worldX, int worldY, int width, int height, int textureX, int textureY, TilesetManager.ATTRIBUTE attr){
            this.worldX = worldX; this.worldY = worldY; this.width = width; this.height = height; this.textureX = textureX; this.textureY = textureY; this.attr = attr;
        }
    }
    private static class JSONObject{
        private ObjectManager.TYPE type;
        private float worldX;
        private float worldY;
        private float patrolX;
        private float patrolY;
        public JSONObject(Vector3 position, ObjectManager.TYPE type){
            this.type = type;
            this.worldX = position.x;
            this.worldY = position.y;
        }
        public void addPatrol(Vector3 patrol){
            patrolX = patrol.x;
            patrolY = patrol.y;
        }
    }
    private static class JSONProp{
        private PropsManager.PROPS propType;
        private int worldX;
        private int worldY;
        public JSONProp(int worldX, int worldY, PropsManager.PROPS propType){
            this.propType = propType;
            this.worldX = worldX;
            this.worldY = worldY;
        }
    }
    private static class JSONSpawn{
        private TilesetManager.ATTRIBUTE attr;
        private int worldX;
        private int worldY;
        private int width;
        private int height;
        private int textureX;
        private int textureY;
        private int amountEnemies;
        private double spawnFreq;
        public JSONSpawn(int worldX, int worldY, int width, int height, int textureX, int textureY, TilesetManager.ATTRIBUTE attr, int amountEnemies, double spawnFreq){
            this.worldX = worldX; this.worldY = worldY; this.width = width; this.height = height; this.textureX = textureX; this.textureY = textureY; this.attr = attr; this.amountEnemies = amountEnemies; this.spawnFreq = spawnFreq;
        }
    }
    // _



}
