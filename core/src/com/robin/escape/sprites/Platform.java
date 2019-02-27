package com.robin.escape.sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.robin.escape.managers.ObjectManager;
import com.robin.escape.utilities.AnimationHandler;

public class Platform extends GameObject {
    private AnimationHandler animationHandler;
    private Texture platform1;
    private Texture platform2;
    private float elapsedTime;
    private float animationTime;
    private boolean runAnimation;
    private boolean flipped;



    public Platform(float x, float y, Texture tex1, Texture tex2, ObjectManager.TYPE type, boolean flipped, Rectangle boxOffsetPos) {
        super(new Vector3(x,y,0), null, type, boxOffsetPos);
        if(flipped)
            this.sprite = new Sprite(tex1);
        else
            this.sprite = new Sprite(tex2);
        platform1 = tex1;
        platform2 = tex2;
        //super(position, "objects/platform.png", type, null);
        //platform2 = new Texture("normal/objects/platform2.png");

        animationHandler = new AnimationHandler();
        animationHandler.addAnimation("flip", "normal/animation/platform/flip.png", 54, 60, 4);
        animationHandler.addAnimation("flip2", "normal/animation/platform/flip2.png", 54, 60, 4);

        runAnimation = false;
        this.flipped = flipped;
    }

    public void flip(){
        runAnimation = true;
        animationTime = elapsedTime;
        if(flipped) {
            type = ObjectManager.TYPE.PLATFORM;
            flipped = false;
        }
        else {
            type = ObjectManager.TYPE.PLATFORM2;
            flipped = true;
        }
    }

    public void update(float dt){
        elapsedTime += dt;

        if(runAnimation)
            if(animationHandler.getAnimation("flip").isAnimationFinished((elapsedTime - animationTime) * 3.0f) ||
                    animationHandler.getAnimation("flip2").isAnimationFinished((elapsedTime - animationTime) * 3.0f))
                runAnimation = false;
        /*if(elapsedTime > animationTime + (1.0f/3.0f)){
            runAnimation = false;
        }*/
    }

    public void render(SpriteBatch sb){
        if(runAnimation) {
            if(!flipped)
                sb.draw((TextureRegion) animationHandler.getAnimation("flip").getKeyFrame((elapsedTime - animationTime) * 3.0f, true), getPosition().x, getPosition().y);
            else
                sb.draw((TextureRegion) animationHandler.getAnimation("flip2").getKeyFrame((elapsedTime - animationTime) * 3.0f, true), getPosition().x, getPosition().y);
        }
        else {
            if (!flipped)
                sb.draw(platform1, position.x, position.y);
            else{
                sb.draw(platform2, position.x, position.y);
            }
        }

    }
}
