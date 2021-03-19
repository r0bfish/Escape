package com.robin.escape.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.Game;

public class Tile extends GameObject{
    private Vector3 tilesetPos;
    private Sprite textureset;
    private com.robin.escape.managers.TilesetManager.ATTRIBUTE attribute;

    public Tile(Vector3 worldPos, Vector3 tilesetPos, Sprite tileset, com.robin.escape.managers.TilesetManager.ATTRIBUTE attribute){
        super(worldPos, null);
        this.tilesetPos = tilesetPos;
        this.textureset = tileset;
        this.attribute = attribute;
    }

    public Vector3 getWorldPos(){return position;}
    public Vector3 getTilesetPos(){return tilesetPos;}
    public com.robin.escape.managers.TilesetManager.ATTRIBUTE getAttribute(){return attribute;}

    @Override
    public Vector3 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        updateBounds();
        return bounds;
    }

    public Rectangle specialEditionBounds(){
        bounds.x = position.x-8;
        bounds.y = position.y-8;
        bounds.width = Game.TILEWIDTH+16;
        bounds.height = Game.TILEHEIGHT+16;
        return bounds;
    }

    private void updateBounds(){
        bounds.x = position.x;
        bounds.y = position.y;
        bounds.width = Game.TILEWIDTH;
        bounds.height = Game.TILEHEIGHT;
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(textureset.getTexture(), getWorldPos().x,getWorldPos().y,
                (((int)getTilesetPos().x + Game.TILEWIDTH)/ Game.TILEWIDTH) * (Game.TILEPADDING * ((((int)getTilesetPos().x + Game.WIDTH)/ Game.WIDTH)))  + (int)getTilesetPos().x,
                (((int)getTilesetPos().y + Game.TILEHEIGHT)/ Game.TILEHEIGHT) * (Game.TILEPADDING * ((((int)getTilesetPos().y + Game.HEIGHT)/ Game.HEIGHT)))  + (int)getTilesetPos().y,
                Game.TILEWIDTH,
                Game.TILEHEIGHT);

        /*sb.draw(textureset.getTexture(), getWorldPos().x,getWorldPos().y,
                escape.TILEPADDING + (int)getTilesetPos().x,
                escape.TILEPADDING + (int)getTilesetPos().y,
                escape.TILEWIDTH   + escape.TILEPADDING,
                escape.TILEHEIGHT  + escape.TILEPADDING);
                */
    }


}
