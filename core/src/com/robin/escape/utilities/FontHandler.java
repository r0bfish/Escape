package com.robin.escape.utilities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class FontHandler {
    private ArrayList<TextWithPos> textList;
    private BitmapFont font;

    public FontHandler(){
        //textList = new ArrayList<>();
        font = new BitmapFont(Gdx.files.internal("font/GillSansSM.fnt"), Gdx.files.internal("font/GillSansSM.png"), false);
        font.setColor(0.392f, 0.675f, 0.808f, 1.0f);
    }

    public void addTextPos(String text, Vector3 position){ textList.add(new TextWithPos(position, text)); }
    public void render(SpriteBatch sb){
        for(TextWithPos item : textList){
            font.draw(sb, item.layout, item.position.x - (item.layout.width/2), item.position.y);
        }
    }
    public void dispose(){
        font.dispose();
    }


    private class TextWithPos{
        Vector3 position;
        GlyphLayout layout;

        private TextWithPos(Vector3 position, String text){
            this.position = position;
            layout = new GlyphLayout(font, text);
        }
    }
}

