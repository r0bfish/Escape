package com.robin.escape.sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.escape;
import com.robin.escape.managers.ObjectManager;

public class GameObject {
    protected ObjectManager.TYPE type;
    protected Vector3 position;
    protected Sprite sprite;
    protected Rectangle bounds;
    protected Rectangle boxOffsetPos;

    public GameObject(Vector3 position, String texture, ObjectManager.TYPE type, Rectangle boxOffsetPos){
        this.type = type;
        this.position = position;
        if(texture != null) {
            this.sprite = new Sprite(new Texture(texture));
            bounds = new Rectangle(position.x, position.y, this.sprite.getWidth(), this.sprite.getHeight());
        }
        else
            bounds = new Rectangle();
        if(boxOffsetPos != null)
            this.boxOffsetPos = boxOffsetPos;
        else
            this.boxOffsetPos = new Rectangle(0,0,0,0);
    }
    public GameObject(Vector3 position, String texture){
        this.position = position;
        if(texture != null) {
            this.sprite = new Sprite(new Texture(texture));
            bounds = new Rectangle(position.x, position.y, this.sprite.getWidth(), this.sprite.getHeight());
        }
        else
            bounds = new Rectangle();
        boxOffsetPos = new Rectangle(0,0,0,0);
    }

    public  Vector3 getPosition(){ return position; };
    public void update(float dt){}
    public void render(SpriteBatch sb){}

    public void dispose(){
        if(sprite.getTexture() != null)
            sprite.getTexture().dispose();
    }
    public Sprite getSprite(){ return sprite;}
    public ObjectManager.TYPE getType(){return type;}

    public Rectangle getBounds() {
        updateBounds();
        return bounds;
    }
    private void updateBounds(){
        /*bounds.x = position.x;
        bounds.y = position.y;
        bounds.width = getSprite().getWidth();
        bounds.height = getSprite().getHeight();*/
        bounds.x = position.x + boxOffsetPos.x;
        bounds.y = position.y + boxOffsetPos.y;
        if(sprite != null) {
            bounds.width = getSprite().getWidth() + boxOffsetPos.width;
            bounds.height = getSprite().getHeight() + boxOffsetPos.height;
        }
        else{
            bounds.width = boxOffsetPos.width;
            bounds.height = boxOffsetPos.height;
        }
    }
}

