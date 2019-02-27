package com.robin.escape.sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.robin.escape.managers.ObjectManager;
import com.robin.escape.utilities.AnimationHandler;

public class Enemy extends MovingObject {
    private AnimationHandler animationHandler;
    private Vector3 patrolPos1;
    private Vector3 patrolPos2;
    private boolean swapDirection;
    private float elapsedTime;

    public Enemy(float x, float y, Texture texture, float patrolX, float patrolY, String style, Rectangle boxOffsetPos){
        super(x, y, texture, ObjectManager.TYPE.ENEMY, boxOffsetPos);
        animationHandler = new AnimationHandler();
        animationHandler.addAnimation("Down", style + "/animation/enemy/walk/Down2.png", 44, 40, 7);
        animationHandler.addAnimation("Up", style + "/animation/enemy/walk/Up2.png", 44, 40, 7);
        animationHandler.addAnimation("Left", style + "/animation/enemy/walk/Left2.png", 44, 40, 7);
        animationHandler.addAnimation("Right", style + "/animation/enemy/walk/Right2.png", 44, 40, 7);
        speed = 30.0;
        swapDirection = true;
        patrolPos1 = new Vector3(x,y,0);
        if(patrolX != 0 && patrolY != 0)
            patrolPos2 = new Vector3(patrolX, patrolY, 0);
    }

    public void update(float dt){
        elapsedTime += dt;
        // This is what makes the patrol work
        if(patrolPos2 != null) {
            changeDirection();

            position.x += velocity.x * speed * dt;
            position.y += velocity.y * speed * dt;
        }
    }

    public void render(SpriteBatch sb){
        if(velocity.y < 0 && Math.abs(velocity.y) > Math.abs(velocity.x))
            sb.draw((TextureRegion) animationHandler.getAnimation("Down").getKeyFrame(elapsedTime * 1.5f, true), getPosition().x, getPosition().y);
        else if(velocity.y > 0 && Math.abs(velocity.y) > Math.abs(velocity.x))
            sb.draw((TextureRegion) animationHandler.getAnimation("Up").getKeyFrame(elapsedTime * 1.5f, true), getPosition().x, getPosition().y);
        else if(velocity.x > 0 && Math.abs(velocity.x) > Math.abs(velocity.y))
            sb.draw((TextureRegion) animationHandler.getAnimation("Right").getKeyFrame(elapsedTime * 2.0f, true), getPosition().x, getPosition().y);
        else if(velocity.x < 0 && Math.abs(velocity.x) > Math.abs(velocity.y))
            sb.draw((TextureRegion) animationHandler.getAnimation("Left").getKeyFrame(elapsedTime * 2.0f, true), getPosition().x, getPosition().y);
        else
            sb.draw(sprite, getPosition().x, getPosition().y);

    }
    public Vector3 getPatrolPos1(){ return patrolPos1;}
    public Vector3 getPatrolPos2(){ return patrolPos2;}

    public void changeDirection(){
        if(patrolPos1 != null && patrolPos2 != null){
            if(swapDirection){
                Vector3 distance = new Vector3((patrolPos1.x - position.x),(patrolPos1.y - position.y), 0);
                if(distance.len() < 0.6)
                {
                    swapDirection = false;
                    velocity.set(patrolPos2.x - position.x, patrolPos2.y - position.y, 0);
                    velocity.nor();
                }
            }
            if(!swapDirection){
                Vector3 distance = new Vector3((patrolPos2.x - position.x),(patrolPos2.y - position.y), 0);
                if(distance.len() < 0.6)
                {
                    swapDirection = true;
                    velocity.set(patrolPos1.x - position.x, patrolPos1.y - position.y, 0);
                    velocity.nor();
                }
            }
        }

    }

    public void patrol(Vector3 from, Vector3 to){
        patrolPos1 = new Vector3(from);
        patrolPos2 = new Vector3(to);
        velocity.set(to.x - from.x, to.y - from.y, 0);
        velocity.nor();
    }

}
