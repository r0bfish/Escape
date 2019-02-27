package com.robin.escape.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class SkidMark {
    private Sprite sprite;

    public SkidMark(Texture text, float x, float y, float angle){
        sprite = new Sprite(text);
        sprite.setPosition(x, y);
        sprite.setRotation(angle);
    }

    public void setPos(float x, float y, float angle){
        sprite.setPosition(x, y);
        sprite.setRotation(angle);
    }

    public Sprite getSprite(){return sprite;}
    public void update(float dt){}
    public void render(SpriteBatch sb){
        sprite.draw(sb);
    }
}
