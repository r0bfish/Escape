package com.robin.escape.sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.managers.ObjectManager;

public class MovingObject extends GameObject {
    protected boolean moving;
    protected double speed;
    protected Vector3 velocity;
    protected Vector3 target;
    protected Vector3 distance;


    public MovingObject(float x, float y, Texture texture, ObjectManager.TYPE type, Rectangle boxOffsetPos) {
        super(new Vector3(x,y,0), null, type, boxOffsetPos);
        this.sprite = new Sprite(texture);
        moving = false;
        speed = 30;
        velocity = new Vector3(0, 0, 0);
        target = new Vector3(x,y,0);
        //bounds = new Rectangle(x, y, this.sprite.getTexture().getWidth(), this.sprite.getTexture().getHeight());
        bounds = new Rectangle(x,y,10,10);
        distance = new Vector3(0,0,0);
    }

    public void update(float dt){
        position.x += velocity.x * speed * dt;
        position.y += velocity.y * speed * dt;

    }

    public void moveTowards(Vector3 target){
        this.target.set(target);
        //velocity.set(target.x - (position.x + sprite.getTexture().getWidth()/2), target.y - (position.y), 0);
        velocity.set(target.x - (bounds.x + bounds.width/2), target.y - (bounds.y), 0);
        velocity.nor();
        moving = true;
    }

    public void render(SpriteBatch sb){
        sb.draw(sprite.getTexture(), position.x, position.y);
    }

}
