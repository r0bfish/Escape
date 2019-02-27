package com.robin.escape.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.escape;

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
        bounds.width = escape.TILEWIDTH+16;
        bounds.height = escape.TILEHEIGHT+16;
        return bounds;
    }

    private void updateBounds(){
        bounds.x = position.x;
        bounds.y = position.y;
        bounds.width = escape.TILEWIDTH;
        bounds.height = escape.TILEHEIGHT;
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(textureset.getTexture(), getWorldPos().x,getWorldPos().y,
                (((int)getTilesetPos().x + escape.TILEWIDTH)/escape.TILEWIDTH) * (escape.TILEPADDING * ((((int)getTilesetPos().x + escape.WIDTH)/escape.WIDTH)))  + (int)getTilesetPos().x,
                (((int)getTilesetPos().y + escape.TILEHEIGHT)/escape.TILEHEIGHT) * (escape.TILEPADDING * ((((int)getTilesetPos().y + escape.HEIGHT)/escape.HEIGHT)))  + (int)getTilesetPos().y,
                escape.TILEWIDTH,
                escape.TILEHEIGHT);

        /*sb.draw(textureset.getTexture(), getWorldPos().x,getWorldPos().y,
                escape.TILEPADDING + (int)getTilesetPos().x,
                escape.TILEPADDING + (int)getTilesetPos().y,
                escape.TILEWIDTH   + escape.TILEPADDING,
                escape.TILEHEIGHT  + escape.TILEPADDING);
                */
    }


}
