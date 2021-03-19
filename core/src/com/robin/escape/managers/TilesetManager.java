package com.robin.escape.managers;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.Game;
import com.robin.escape.sprites.GameObject;
import com.robin.escape.sprites.Tile;

import java.util.ArrayList;

public class TilesetManager {
    public enum ATTRIBUTE{ NORMAL, BLOCK, JUMPBLOCK, HILLBLOCK1, HILLBLOCK2,
        HILLPUSHLEFT, HILLPUSHRIGHT, HILLPUSHUP, KILL, SLIDE, HARDSLIDE, WIN, ENEMYSPAWN }
    private boolean isFocused;
    private Vector3 mousePos;
    private ArrayList<Tile> tiles;
    private GameObject tileset;

    public TilesetManager(String tileset){
        isFocused = false;
        mousePos = new Vector3();
        tiles = new ArrayList<Tile>();
        this.tileset = new GameObject(new Vector3(160,320,0),tileset);

    }

    /**************************
     * Getters and Setters for this class
     * ************************/
    public GameObject getTileset(){return tileset;}
    public ArrayList<Tile> getTiles(){return tiles;}
    public boolean getFocused(){
        return isFocused;
    }
    public static ATTRIBUTE stringToAttribute(String attr){
        if(attr.equals("BLOCK")) return ATTRIBUTE.BLOCK;
        else if(attr.equals("KILL")) return ATTRIBUTE.KILL;
        else if(attr.equals("SLIDE")) return ATTRIBUTE.SLIDE;
        else if(attr.equals("NORMAL")) return ATTRIBUTE.NORMAL;
        else if(attr.equals("HARDSLIDE")) return ATTRIBUTE.HARDSLIDE;
        else if(attr.equals("WIN")) return ATTRIBUTE.WIN;
        else if(attr.equals("JUMPBLOCK")) return ATTRIBUTE.JUMPBLOCK;
        else if(attr.equals("HILLBLOCK1")) return ATTRIBUTE.HILLBLOCK1;
        else if(attr.equals("HILLBLOCK2")) return ATTRIBUTE.HILLBLOCK2;
        else if(attr.equals("HILLPUSHLEFT")) return ATTRIBUTE.HILLPUSHLEFT;
        else if(attr.equals("HILLPUSHRIGHT")) return ATTRIBUTE.HILLPUSHRIGHT;
        else if(attr.equals("HILLPUSHUP")) return ATTRIBUTE.HILLPUSHUP;
        else if(attr.equals("ENEMYSPAWN")) return ATTRIBUTE.ENEMYSPAWN;
        return null;
    }
    public void setSelected(Vector3 mousePos){
        this.mousePos = mousePos;
    }
    public void setFocused(boolean b){ isFocused = b;}
    private Tile tileExists(Vector3 mousePos){
        for(Tile t : tiles){
            if(t.getWorldPos().x == (mousePos.x - (mousePos.x % Game.TILEWIDTH)))
                if(t.getWorldPos().y == (mousePos.y - (mousePos.y % Game.TILEHEIGHT))){
                    return t;
                }
        }
        return null;
    }


    public void addTile(Tile tile){
        Tile tile2 = tileExists(tile.getWorldPos());
        if(tile2 != null) {
            tiles.remove(tile2);
        }
        tiles.add(tile);
    }
    public void addTile(Vector3 worldPos, TilesetManager.ATTRIBUTE attribute){
        Tile tile = tileExists(worldPos);
        if(tile != null) {
            tiles.remove(tile);
        }

        Tile tt = new Tile(
                new Vector3(
                        worldPos.x - (worldPos.x % Game.TILEWIDTH),
                        worldPos.y - (worldPos.y % Game.TILEHEIGHT),
                        0),
                new Vector3(
                        ((mousePos.x - (mousePos.x % Game.TILEWIDTH) - tileset.getPosition().x)),
                        (tileset.getSprite().getTexture().getHeight() - Game.TILEHEIGHT - (mousePos.y - (mousePos.y % Game.TILEHEIGHT) - tileset.getPosition().y)),
                        0),
                tileset.getSprite(),
                attribute);
        //Gdx.app.log("" + tt.getWorldPos(), "" + tt.getTilesetPos());
        tiles.add(tt);

    }


    public void render(SpriteBatch sb){
        for(int i=0; i<tiles.size(); i++)
            sb.draw(tileset.getSprite().getTexture(), tiles.get(i).getWorldPos().x,tiles.get(i).getWorldPos().y,
                    (((int)tiles.get(i).getTilesetPos().x + Game.TILEWIDTH)/ Game.TILEWIDTH) * (Game.TILEPADDING * ((((int)tiles.get(i).getTilesetPos().x + Game.WIDTH)/ Game.WIDTH)))  + (int)tiles.get(i).getTilesetPos().x,
                    (((int)tiles.get(i).getTilesetPos().y + Game.TILEHEIGHT)/ Game.TILEHEIGHT) * (Game.TILEPADDING * ((((int)tiles.get(i).getTilesetPos().y + Game.HEIGHT)/ Game.HEIGHT)))  + (int)tiles.get(i).getTilesetPos().y,
                    Game.TILEWIDTH,
                    Game.TILEHEIGHT);
        /*for(Tile t : tiles){
            sb.draw(tileset.getSprite().getTexture(), t.getWorldPos().x,t.getWorldPos().y, (int)t.getTilesetPos().x, (int)t.getTilesetPos().y, escape.TILEWIDTH, escape.TILEHEIGHT);
        }*/
        if(isFocused) {
            sb.draw(tileset.getSprite(), tileset.getPosition().x, tileset.getPosition().y);
        }
    }
    public void dispose(){
        //for(int i=0; i<tiles.size(); i++)
        //    tiles.get(i).dispose();
        tileset.getSprite().getTexture().dispose();
    }
}
