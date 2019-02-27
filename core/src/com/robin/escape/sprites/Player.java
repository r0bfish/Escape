package com.robin.escape.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.escape;
import com.robin.escape.managers.ObjectManager;
import com.robin.escape.managers.SkidmarkManager;
import com.robin.escape.utilities.AnimationHandler;
import com.robin.escape.utilities.CameraStyles;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.Gdx.gl;


public class Player extends MovingObject {
    private AnimationHandler animationHandler;
    private SkidmarkManager skidmarkManager;
    private Vector3 startPosition;
    private Vector3 lastClickedPos;
    private Vector3 currentVelocity;
    private Texture iceShardText;
    private boolean onIce;
    private boolean hardSlide;
    private boolean iceshardExplosionTrigger;
    private boolean inAir;
    private boolean deathAnimation;
    private boolean winnableState = false;
    public boolean diedInWater;
    private float jumpSpeed;
    private float iceSpeed;
    private float elapsedTime;
    private float jumpStartTime;
    private float explosionStartTime;
    private float winStartTime;
    private float clickedTime;
    private double currentDegree;
    public Rectangle mouseRect;

    public Player(float x, float y, Texture texture, String style, Rectangle boxOffsetPos){
        super(x, y, texture, ObjectManager.TYPE.PLAYER, boxOffsetPos);

        skidmarkManager = new SkidmarkManager(400);
        animationHandler = new AnimationHandler();
        animationHandler.addAnimation("walkDownLeft", style + "/animation/player/walk/DownLeft.png", 27, 33, 7);
        animationHandler.addAnimation("walkDownRight", style + "/animation/player/walk/DownRight.png", 27, 33, 7);
        animationHandler.addAnimation("walkUpLeft", style + "/animation/player/walk/UpLeft.png", 27, 33, 8);
        animationHandler.addAnimation("walkUpRight", style + "/animation/player/walk/UpRight.png", 27, 33, 8);
        animationHandler.addAnimation("jumpUp", style + "/animation/player/jump/jumpUp.png", 27, 48, 8);
        animationHandler.addAnimation("jumpDown", style + "/animation/player/jump/jumpDown.png", 27, 48, 8);
        animationHandler.addAnimation("win2", style + "/animation/player/win/win2.png", 27, 56, 15);
        animationHandler.addAnimation("win1", style + "/animation/player/win/win1.png", 27, 34, 15);
        animationHandler.addAnimation("death1", style + "/animation/player/death/death1.png", 30, 48, 9);
        animationHandler.addAnimation("death2", style + "/animation/player/death/death2.png", 27, 48, 10);
        animationHandler.addAnimation("iceShardJump", style + "/animation/iceshard/jump.png", 27, 48, 8);
        animationHandler.addAnimation("iceShardExplosion", style + "/animation/iceshard/explosion.png", 37, 40, 4);

        iceShardText = new Texture(style + "/objects/iceShard.png");
        mouseRect = new Rectangle(0,0,0,0);
        speed = 45;
        jumpSpeed = 1;
        iceSpeed = 1;
        onIce = false;
        hardSlide = false;
        inAir = false;
        deathAnimation = false;
        iceshardExplosionTrigger = false;
        startPosition = new Vector3(x, y, 0);
        currentVelocity = new Vector3(0,0,0);
        lastClickedPos = new Vector3(x,y,0);
        velocity.set(0, -1, 0);
        currentDegree = 90;
    }

    public Vector3 getCurrentVelocity(){return currentVelocity;}
    public boolean isDead(){ return deathAnimation;}
    public void resetPlayer(boolean diedInWater){
        if(!deathAnimation) {
            this.diedInWater = diedInWater;
            deathAnimation = true;
            winStartTime = elapsedTime;
            slide(false, false);
            inAir = false;
        }
    }

    private void deathAnimation(){
        position.set(startPosition);
        target.set(startPosition);
        deathAnimation = false;
        slide(false, false);
        /*position.x = position.x + (startPosition.x - position.x) * .075f;
        position.y = position.y + (startPosition.y - position.y) * .075f;
        Vector3 distance = new Vector3(startPosition.x - position.x, startPosition.y - position.y, 0);
        if(distance.len() < 1) {
            deathAnimation = false;
            Gdx.app.log("alive", "");
        }*/
    }
    public void updateRotation(){
        // Set the turnrate
        int turnrate = 5;
        if(!onIce)
            turnrate = 25;

        // Recalculate the direction vector
        velocity.set(target.x - (bounds.x + bounds.width/2), target.y - (bounds.y), 0);
        //velocity.set(target.x - (position.x + sprite.getTexture().getWidth()/2), target.y - (position.y), 0);
        velocity.nor();

        // Get the angles
        currentDegree = Math.atan2(currentVelocity.y, currentVelocity.x)*180/Math.PI;
        double degree = Math.atan2(velocity.y, velocity.x)*180/Math.PI;

        if(currentDegree != degree && moving && !hardSlide) {
            if (currentDegree + turnrate < degree) {
                if (Math.abs(currentDegree - degree) < 180)
                    currentDegree += turnrate;
                else currentDegree -= turnrate;
            }
            else if(currentDegree - turnrate > degree){
                if (Math.abs(currentDegree - degree) < 180)
                    currentDegree -= turnrate;
                else currentDegree += turnrate;
            }
            else
                currentDegree = degree;

            currentVelocity.x = (float) Math.cos(Math.toRadians(currentDegree));
            currentVelocity.y = (float) Math.sin(Math.toRadians(currentDegree));
        }
    }

    public double getDirectionAngle(){return currentDegree;}
    public boolean isOnIce(){return onIce;}

    public void update(float dt){
        elapsedTime += dt;
        updateRotation();
        timerHandling();

        if(deathAnimation && animationHandler.getAnimation("death1").isAnimationFinished((elapsedTime - winStartTime) * 0.75f)) {
                    inAir = true;
                    deathAnimation();
        }
        else if(moving || onIce){
            if(!isDead() || diedInWater) {
                // Check if player har reached clicked point

                distance.set(target.x - (bounds.x + bounds.width / 2), target.y - (bounds.y), 0);
                //distance.set(target.x - (position.x + sprite.getTexture().getWidth()/2), target.y - (position.y), 0);
                //Vector3 distance = new Vector3(target.x - (position.x + sprite.getTexture().getWidth()/2), target.y - (position.y), 0);
                if (distance.len() < 2) {
                    moving = false;
                    target.setZero();
                }

                position.x += currentVelocity.x * speed * dt * iceSpeed * jumpSpeed;
                position.y += currentVelocity.y * speed * dt * iceSpeed * jumpSpeed;
            }
        }
    }

    public boolean playerInAir(){ return inAir; }

    public SkidmarkManager getSkidmarkManager(){return skidmarkManager;}

    public void render(SpriteBatch sb){
        //skidmarkManager.render(sb);
        if(!winnableState) {
            if(moving) {
                sb.draw(getSprite().getTexture(), target.x - 3, target.y - 3, 6, 6);
                sb.draw(getSprite().getTexture(), currentVelocity.x - 3 * 3, currentVelocity.y - 3 * 3, 6, 6);
            }

            if(deathAnimation){
                if(animationHandler.getAnimation("death1").isAnimationFinished((elapsedTime - winStartTime)*0.75f))
                        sb.draw(sprite, getPosition().x, getPosition().y);
                else {
                    if(diedInWater)
                        sb.draw((TextureRegion) animationHandler.getAnimation("death1").getKeyFrame((elapsedTime - winStartTime) * 0.75f, false), getPosition().x - 6, getPosition().y - 5);
                    else
                        sb.draw((TextureRegion) animationHandler.getAnimation("death2").getKeyFrame((elapsedTime - winStartTime) * 0.75f, false), getPosition().x, getPosition().y);
                }
            }
            else {
                if (inAir) {
                    if (currentVelocity.y <= 0)
                        sb.draw((TextureRegion) animationHandler.getAnimation("jumpDown").getKeyFrame(elapsedTime - jumpStartTime, true), getPosition().x, getPosition().y);
                    else if (currentVelocity.y > 0)
                        sb.draw((TextureRegion) animationHandler.getAnimation("jumpUp").getKeyFrame(elapsedTime - jumpStartTime, true), getPosition().x, getPosition().y);

                } else if (moving || onIce) {
                    float onIceVar = 1;
                    if (onIce)
                        onIceVar = 0.5f;
                    if (hardSlide && !target.isZero()) {
                        onIceVar = 0.001f;
                        Vector3 hardIceDirection = new Vector3(target.x - position.x, target.y - position.y, 0);
                        hardIceDirection.nor();

                        if (hardIceDirection.x < 0 && hardIceDirection.y <= 0)
                            sb.draw((TextureRegion) animationHandler.getAnimation("walkDownLeft").getKeyFrame(elapsedTime * 2 * onIceVar, true), getPosition().x, getPosition().y);
                        else if (hardIceDirection.x >= 0 && hardIceDirection.y <= 0)
                            sb.draw((TextureRegion) animationHandler.getAnimation("walkDownRight").getKeyFrame(elapsedTime * 2 * onIceVar, true), getPosition().x, getPosition().y);
                        else if (hardIceDirection.x < 0 && hardIceDirection.y > 0)
                            sb.draw((TextureRegion) animationHandler.getAnimation("walkUpLeft").getKeyFrame(elapsedTime * 2 * onIceVar, true), getPosition().x, getPosition().y);
                        else if (hardIceDirection.x >= 0 && hardIceDirection.y > 0)
                            sb.draw((TextureRegion) animationHandler.getAnimation("walkUpRight").getKeyFrame(elapsedTime * 2 * onIceVar, true), getPosition().x, getPosition().y);

                    } else {
                        if (currentVelocity.x < 0 && currentVelocity.y <= 0)
                            sb.draw((TextureRegion) animationHandler.getAnimation("walkDownLeft").getKeyFrame(elapsedTime * 2 * onIceVar, true), getPosition().x, getPosition().y);
                        else if (currentVelocity.x >= 0 && currentVelocity.y <= 0)
                            sb.draw((TextureRegion) animationHandler.getAnimation("walkDownRight").getKeyFrame(elapsedTime * 2 * onIceVar, true), getPosition().x, getPosition().y);
                        else if (currentVelocity.x < 0 && currentVelocity.y > 0)
                            sb.draw((TextureRegion) animationHandler.getAnimation("walkUpLeft").getKeyFrame(elapsedTime * 2 * onIceVar, true), getPosition().x, getPosition().y);
                        else if (currentVelocity.x >= 0 && currentVelocity.y > 0)
                            sb.draw((TextureRegion) animationHandler.getAnimation("walkUpRight").getKeyFrame(elapsedTime * 2 * onIceVar, true), getPosition().x, getPosition().y);
                    }
                } else
                    sb.draw(sprite, getPosition().x, getPosition().y);

                //----------------------
                // ICESHARD

                if(hardSlide){
                    iceshardExplosionTrigger = true;
                    explosionStartTime = elapsedTime;
                    if(inAir)
                        sb.draw((TextureRegion) animationHandler.getAnimation("iceShardJump").getKeyFrame(elapsedTime - jumpStartTime, true), getPosition().x-5, getPosition().y);
                    else
                        sb.draw(iceShardText, position.x - 5, position.y - 5);
                }

            }

            if(animationHandler.getAnimation("iceShardExplosion").isAnimationFinished((elapsedTime-explosionStartTime)*3))
                iceshardExplosionTrigger = false;
            if(iceshardExplosionTrigger && !hardSlide)
                sb.draw((TextureRegion) animationHandler.getAnimation("iceShardExplosion").getKeyFrame((elapsedTime - explosionStartTime)*3, false), getPosition().x - 7, getPosition().y-5);

            //-------------------------

        }
        else {
            if(Math.round(winStartTime % 2) == 1)
                sb.draw((TextureRegion) animationHandler.getAnimation("win1").getKeyFrame(elapsedTime - winStartTime, false), getPosition().x, getPosition().y);
            else
                sb.draw((TextureRegion) animationHandler.getAnimation("win2").getKeyFrame(elapsedTime - winStartTime, false), getPosition().x, getPosition().y);

        }
    }

    public void slide(boolean onIce, boolean hardSlide){
        this.onIce = onIce;
        this.hardSlide = hardSlide;

        if(onIce)
            iceSpeed = (float)2.0;
        else if(!onIce)
            iceSpeed = (float) 1;
    }

    public void jump(){
        if(!deathAnimation) {
            if (clickedTime + 0.25 > elapsedTime)
                if (!inAir) {
                    inAir = true;
                    jumpStartTime = elapsedTime;
                    jumpSpeed = (float) 1.0;
                }
            if(elapsedTime > jumpStartTime + 0.8)
                inAir = false;
            clickedTime = elapsedTime;
        }

    }

    public void setWinnableState(boolean b){
        if(b) {
            winnableState = b;
            speed = 0;
            moving = false;
            inAir = false;
            onIce = false;
            winStartTime = elapsedTime;
        }
    }

    private void timerHandling()
    {
        if(elapsedTime > jumpStartTime +0.85) {
            jumpSpeed = 1.0f;
            inAir = false;
        }
    }

}
