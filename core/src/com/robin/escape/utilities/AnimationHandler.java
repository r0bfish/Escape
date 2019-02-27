package com.robin.escape.utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationHandler {
    private TextureRegion animationFrames[];
    private Map<String, Animation> animationMap;

    public AnimationHandler(){
        animationMap = new HashMap<>();
    }

    public Animation getAnimation(String key){
        return animationMap.get(key);
    }

    public void addAnimation(String key,String texture, int frameWidth, int frameHeight, int frameCount){
        Texture animationTexture = new Texture(texture);
        TextureRegion tmpFrames[][] = TextureRegion.split(animationTexture, frameWidth, frameHeight);
        animationFrames = new TextureRegion[frameCount];
        for(int i=0; i<frameCount; i++){
            animationFrames[i] = tmpFrames[0][i];
        }

        Animation animation = new Animation(1/(float)frameCount, animationFrames);
        animationMap.put(key, animation);
    }
}
