package com.robin.escape.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.robin.escape.sprites.SkidMark;

import java.util.ArrayList;
import java.util.List;

public class SkidmarkManager {
    private int poolSize;
    private Texture text;
    private List<SkidMark> skidMarks;

    public SkidmarkManager(int poolSize){
        this.poolSize = poolSize;
        text = new Texture("normal/objects/skidmark.png");
        skidMarks = new ArrayList<>();
    }

    public List<SkidMark> getSkidMarks(){return skidMarks;}

    public void newSkid(float x, float y, float angle, boolean leftSkid){
        if(skidMarks.size() >= poolSize) {
            SkidMark tmpSkid = skidMarks.get(0);
            if(leftSkid)
                tmpSkid.setPos(x+2, y, angle + 90);
            else
                tmpSkid.setPos(x+10, y, angle + 90);
            skidMarks.remove(tmpSkid);
            skidMarks.add(tmpSkid);
            //skidMarks.remove(skidMarks.get(0));
            //skidMarks.remove(skidMarks.get(0));
        }
        else {
            if(leftSkid)
            //if (skidMarks.size() % 2 == 0)
                skidMarks.add(new SkidMark(text, x+2, y, angle + 90));
            else
                skidMarks.add(new SkidMark(text, x + 10, y, angle + 90));
        }

    }

    public void render(SpriteBatch sb){
        //Gdx.app.log("size "+skidMarks.size(), "" );
        for(int i=0; i<skidMarks.size(); i++) {
            skidMarks.get(i).render(sb);
        }
    }

    public void dispose(){
        text.dispose();
        skidMarks.clear();
    }
}
