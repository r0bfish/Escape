package com.robin.escape.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.escape;
import com.robin.escape.sprites.Area;
import com.robin.escape.sprites.GameObject;
import com.robin.escape.sprites.Tile;

import java.util.ArrayList;

public class LogicManager {
    private TilesetManager.ATTRIBUTE selectedAttribute;
    private boolean isFocused;
    private Vector3 mousePos;
    private ArrayList<Area> logicAreas;
    private GameObject logicset;

    public LogicManager(String logicset){
        selectedAttribute = null;
        isFocused = false;
        mousePos = new Vector3();
        logicAreas = new ArrayList<Area>();
        this.logicset = new GameObject(new Vector3(160,480,0), logicset);

    }

    /**************************
     * Getters and Setters for this class
     * ************************/
    public GameObject getLogicset(){return logicset;}
    public ArrayList<Area> getLogicAreas(){return logicAreas;}
    public boolean getFocused(){
        return isFocused;
    }
    public void setSelected(Vector3 mousePos){
        this.mousePos = mousePos;
        switch ((int)(Math.abs(logicset.getPosition().x - (mousePos.x - (mousePos.x % escape.TILEWIDTH)))/ escape.TILEWIDTH)){
            case 0:
                selectedAttribute = TilesetManager.ATTRIBUTE.KILL;
                break;
            case 1:
                selectedAttribute = TilesetManager.ATTRIBUTE.SLIDE;
                break;
            case 2:
                selectedAttribute = TilesetManager.ATTRIBUTE.BLOCK;
                break;
            case 3:
                selectedAttribute = TilesetManager.ATTRIBUTE.NORMAL;
                break;
            case 4:
                selectedAttribute = TilesetManager.ATTRIBUTE.HARDSLIDE;
                break;
            case 5:
                selectedAttribute = TilesetManager.ATTRIBUTE.WIN;
                break;
            case 6:
                selectedAttribute = TilesetManager.ATTRIBUTE.JUMPBLOCK;
                break;
            case 7:
                selectedAttribute = TilesetManager.ATTRIBUTE.HILLBLOCK1;
                break;
            case 8:
                selectedAttribute = TilesetManager.ATTRIBUTE.HILLBLOCK2;
                break;
            case 9:
                selectedAttribute = TilesetManager.ATTRIBUTE.HILLPUSHLEFT;
                break;
            case 10:
                selectedAttribute = TilesetManager.ATTRIBUTE.HILLPUSHRIGHT;
                break;
            case 11:
                selectedAttribute = TilesetManager.ATTRIBUTE.HILLPUSHUP;
                break;
            case 12:
                selectedAttribute = TilesetManager.ATTRIBUTE.ENEMYSPAWN;
                break;
        }
    }
    public void setFocused(boolean b){ isFocused = b;}
    private Area logicAreaExists(Vector3 mousePos){
        for(Area a : logicAreas){
            if(a.getPosition().x == (mousePos.x - (mousePos.x % escape.TILEWIDTH)))
                if(a.getPosition().y == (mousePos.y - (mousePos.y % escape.TILEHEIGHT))){
                    return a;
                }
        }
        return null;
    }
    public TilesetManager.ATTRIBUTE getSelectedAttribute(){return selectedAttribute;}


    public void addLogicArea(Area area){
        logicAreas.add(area);
    }
    public void addLogicArea(Vector3 worldPos, Vector3 size){
        Area a = new Area(
                worldPos,
                size,
                new Vector3(
                        ((mousePos.x - (mousePos.x % escape.TILEWIDTH) - logicset.getPosition().x)),
                        (logicset.getSprite().getTexture().getHeight() - escape.TILEHEIGHT - (mousePos.y - (mousePos.y % escape.TILEHEIGHT) - logicset.getPosition().y)),
                        0),
                selectedAttribute);
        //Gdx.app.log("" + tt.getWorldPos(), "" + tt.getTilesetPos());
        logicAreas.add(a);


    }


    public void render(SpriteBatch sb){
        for(Area a : logicAreas){
            sb.draw(
                    logicset.getSprite().getTexture(),
                    a.getPosition().x,
                    a.getPosition().y,            /* reposition to draw from half way up from the original sprite position */
                    a.getBounds().width / 2,
                    a.getBounds().height / 2,
                    a.getBounds().width,
                    a.getBounds().height,                  /* draw the sprite at half height*/
                    logicset.getSprite().getScaleX(),
                    logicset.getSprite().getScaleY(),
                    0,
                    (int)a.getLogicSetPos().x,
                    (int)a.getLogicSetPos().y,
                    16,
                    16, /* only use the texture data from the top of the sprite */
                    false,
                    false);

 }
        if(isFocused) {
            sb.draw(logicset.getSprite(), logicset.getPosition().x, logicset.getPosition().y);
        }

    }
    public void dispose() {
        //for(int i=0; i<logicAreas.size(); i++)
        //    logicAreas.get(i).dispose();
        logicset.getSprite().getTexture().dispose();
    }
}
