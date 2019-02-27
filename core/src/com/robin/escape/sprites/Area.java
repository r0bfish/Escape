package com.robin.escape.sprites;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Area extends GameObject{
    private com.robin.escape.managers.TilesetManager.ATTRIBUTE attribute;
    private Vector3 logicSetPos;

    public Area(Vector3 position, Vector3 size, Vector3 logicSetPos, com.robin.escape.managers.TilesetManager.ATTRIBUTE attribute) {
        super(position, null, null, null);
        if(size.x < 0) {position.x += size.x; size.x *= -1;}
        if(size.y < 0) {position.y += size.y; size.y *= -1;}
        bounds = new Rectangle(position.x, position.y, size.x, size.y);
        this.logicSetPos = logicSetPos;
        this.attribute = attribute;
    }

    public com.robin.escape.managers.TilesetManager.ATTRIBUTE getAttribute(){ return attribute; }
    public Vector3 getLogicSetPos(){return logicSetPos;}
    public Rectangle getBounds() {
        updateBounds();
        return bounds;
    }
    private void updateBounds(){
        bounds.x = position.x;
        bounds.y = position.y;
    }
}
