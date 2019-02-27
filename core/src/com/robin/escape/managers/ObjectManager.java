package com.robin.escape.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.escape;
import com.robin.escape.sprites.Enemy;
import com.robin.escape.sprites.GameObject;
import com.robin.escape.sprites.MovingObject;
import com.robin.escape.sprites.Platform;
import com.robin.escape.sprites.Player;

import java.util.ArrayList;

public class ObjectManager {
    public enum TYPE{ PLAYER, ENEMY, PENGUIN, PLATFORM, PLATFORM2 }
    private TYPE selectedType;
    private boolean isFocused;
    private ArrayList<GameObject> objects;
    private GameObject objectset;
    private String style;
    private Texture PLAYER;
    private Texture ENEMY;
    private Texture PENGUIN;
    private Texture PLATFORM;
    private Texture PLATFORM2;

    public ObjectManager(String style, String objectset){
        selectedType = null;
        isFocused = false;
        this.style = style;
        objects = new ArrayList<GameObject>();
        this.objectset = new GameObject(new Vector3(160,160,0),objectset);

        PLAYER = new Texture(style + "/objects/player.png");
        ENEMY = new Texture(style + "/objects/enemy.png");
        PENGUIN = new Texture(style + "/objects/penguin.png");
        PLATFORM = new Texture(style + "/objects/platform.png");
        PLATFORM2 = new Texture(style + "/objects/platform2.png");
    }

    /**************************
     * Getters and Setters for this class
     * ************************/
    public GameObject getTileset(){return objectset;}
    public ArrayList<GameObject> getObjects(){return objects;}
    public ObjectManager.TYPE getSelectedType(){return selectedType;}
    public boolean getFocused(){
        return isFocused;
    }
    public void setSeleced(Vector3 mousePos){
        switch ((int)(Math.abs(objectset.getPosition().x - (mousePos.x - (mousePos.x % escape.TILEWIDTH)))/ escape.TILEWIDTH)){
            case 0:
                selectedType = TYPE.PLAYER;
                break;
            case 1:
                selectedType = TYPE.ENEMY;
                break;
            case 2:
                selectedType = TYPE.PENGUIN;
                break;
            case 3:
                selectedType = TYPE.PLATFORM;
                break;
            case 4:
                selectedType = TYPE.PLATFORM2;
                break;
        }
    }
    public void setFocused(boolean b){ isFocused = b;}

    public Texture stringToObjectTexture(String objectType){
        if(objectType.equals("PLAYER")) return PLAYER;
        else if(objectType.equals("ENEMY")) return ENEMY;
        else if(objectType.equals("PENGUIN")) return PENGUIN;
        else if(objectType.equals("PLATFORM")) return PLATFORM;
        else if(objectType.equals("PLATFORM2")) return PLATFORM2;
        return null;
    }
    public static ObjectManager.TYPE stringToObjectType(String objectType){
        if(objectType.equals("PLAYER")) return TYPE.PLAYER;
        else if(objectType.equals("ENEMY")) return TYPE.ENEMY;
        else if(objectType.equals("PENGUIN")) return TYPE.PENGUIN;
        else if(objectType.equals("PLATFORM")) return TYPE.PLATFORM;
        else if(objectType.equals("PLATFORM2")) return TYPE.PLATFORM2;
        return null;
    }

    public void addObject(float worldX, float worldY, float patrolX, float patrolY, ObjectManager.TYPE type){
        if(type != null) {
            switch (type) {
                case PLAYER:
                    // Check if player already exists
                    GameObject player = playerExists();
                    if (player == null)
                        objects.add(new Player(worldX, worldY, PLAYER, style,
                                new Rectangle(
                                        0,
                                        4,
                                        -12,
                                        -29)));
                    else
                        player.getPosition().set(worldX,worldY,0);
                    break;
                case ENEMY:
                    objects.add(new Enemy(worldX - (worldX % (escape.TILEWIDTH)), worldY - (worldY % (escape.TILEHEIGHT)),
                            ENEMY, patrolX, patrolY, style,
                            new Rectangle(
                                    5,
                                    6,
                                    -29,
                                    -32)));
                    break;
                case PENGUIN:
                    objects.add(new MovingObject(worldX, worldY, PENGUIN, TYPE.PENGUIN, new Rectangle(
                            2,
                            4,
                            -14,
                            -29)));
                    break;
                case PLATFORM:
                    objects.add(new Platform(worldX, worldY, PLATFORM, PLATFORM2, TYPE.PLATFORM, false, new Rectangle(
                            2,
                            0,
                            -10,
                            -10)));
                    //objects.add(new MovingObject(worldX, worldY, PLATFORM, TYPE.PLATFORM, null));
                    break;
                case PLATFORM2:
                    objects.add(new Platform(worldX, worldY, PLATFORM, PLATFORM2, TYPE.PLATFORM2, true ,new Rectangle(
                            2,
                            0,
                            -10,
                            -10)));
                    //objects.add(new MovingObject(worldX, worldY, PLATFORM2, TYPE.PLATFORM2, null));
                    break;
            }
        }
    }

    private GameObject playerExists(){
        for(GameObject gb : objects){
            if(gb.getClass() == Player.class) {
                if(gb.getType() == TYPE.PLAYER)
                    return gb;
            }
        }
        return null;
    }

    public void render(SpriteBatch sb){
        for(GameObject t : objects){
            //t.render(sb);
            sb.draw(t.getSprite(), t.getPosition().x, t.getPosition().y);
        }
        if(isFocused) {
            sb.draw(objectset.getSprite(), objectset.getPosition().x, objectset.getPosition().y);
        }

    }
    public void dispose(){
        objectset.getSprite().getTexture().dispose();
        for(int i=0; i<objects.size(); i++){
            objects.get(i).dispose();
        }
    }
}
